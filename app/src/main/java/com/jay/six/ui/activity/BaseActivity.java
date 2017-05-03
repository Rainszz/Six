package com.jay.six.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jay.six.R;
import com.jay.six.common.manager.ActivityManager;
import com.jay.six.utils.DensityUtils;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    private ViewFlipper mContentView;
    protected RelativeLayout layout_head;
    protected Button btn_left;
    protected Button btn_right;
    protected TextView tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.layout_base);

        initView();
    }

    private void initView() {
        mContext = this;
        //初始化公共头部
        mContentView = (ViewFlipper) super.findViewById(R.id.layout_container);
        layout_head = (RelativeLayout) super.findViewById(R.id.layout_head);
        btn_left = (Button) super.findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_right = (Button) super.findViewById(R.id.btn_right);
        tv_title = (TextView) super.findViewById(R.id.tv_title);
        //Activity管理
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        mContentView.addView(view, lp);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.unbindReferences(mContentView);
        ActivityManager.getInstance().removeActivity(this);
        mContentView = null;
        mContext = null;
    }

    /**
     * 设置头部是否可见
     *
     * @param visibility
     */
    public void setHeadVisibility(int visibility) {
        layout_head.setVisibility(visibility);
    }

    /**
     * 设置标题
     */
    public void setTitle(int titleId) {
        tv_title.setText(getString(titleId));
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置左边按钮是否显示
     *
     * @param visibility
     */
    public void setLeftVisibility(int visibility) {
        btn_left.setVisibility(visibility);
    }

    /**
     * 设置右边按钮是否显示
     *
     * @param visibility
     */
    public void setRightVisibility(int visibility) {
        btn_right.setVisibility(visibility);
    }

    /**
     * 设置右边按钮的文字
     *
     * @param titleId
     */
    public void setRightText(int titleId) {
        btn_right.setText(getString(titleId));
    }

    /**
     * 设置右边按钮的文字
     *
     * @param text
     */
    public void setRightText(String text) {
        btn_right.setText(text);
    }

    /**
     * 设置右边按钮的图标
     *
     * @param resId
     */
    public void setRightDrawable(int resId) {
        btn_right.setWidth(DensityUtils.dp2px(this, 36));
        btn_right.setHeight(DensityUtils.dp2px(this, 36));
        btn_right.setBackgroundResource(resId);
    }

    /**
     * 设置右边按钮的监听器
     *
     * @param listenr
     */
    public void setRightOnClickListener(View.OnClickListener listenr) {
        btn_right.setOnClickListener(listenr);
    }

}
