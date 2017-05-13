package com.jay.six.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jay.six.R;
import com.jay.six.bean.ProvinceBean;
import com.jay.six.common.BaseActivity;
import com.jay.six.ui.widget.CircleImageView;
import com.jay.six.utils.CityDataUtil;
import com.jay.six.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jayli on 2017/5/13 0013.
 * 提交头像、性别，年龄，地址
 */

public class MoreInfoActivity extends BaseActivity {

    @BindView(R.id.cimg_photo)
    CircleImageView cimgPhoto;
    @BindView(R.id.layout_photo)
    RelativeLayout layoutPhoto;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.layout_sex)
    LinearLayout layoutSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.layout_age)
    LinearLayout layoutAge;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.layout_address)
    LinearLayout layoutAddress;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private ArrayList<String> ageItems = new ArrayList<>();
    private ArrayList<String> sexItems = new ArrayList<>();

    private String photoUrl,sex,age,address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);
        ButterKnife.bind(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        options1Items = CityDataUtil.getProvinceData();
        options2Items = CityDataUtil.getCityData();
        options3Items = CityDataUtil.getAreData();
    }

    @OnClick({R.id.layout_photo, R.id.layout_sex, R.id.layout_age, R.id.layout_address, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_photo:

                break;
            case R.id.layout_sex:
                showSexPickView();
                break;
            case R.id.layout_age:
                showAgePickView();
                break;
            case R.id.layout_address:
                showCityPickView();
                break;
            case R.id.btn_submit:
                break;
        }
    }

    private void showSexPickView() {
        sexItems.add("男");
        sexItems.add("女");
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvSex.setText(sexItems.get(options1));
            }
        })
                .setTitleText("选择性别")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        pvOptions.setPicker(sexItems);//一级选择器
        pvOptions.show();
    }

    private void showAgePickView() {
        for (int i=1;i<100;i++){
            ageItems.add(String.valueOf(i));
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvAge.setText(ageItems.get(options1));
            }
        })
                .setTitleText("选择年龄")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        pvOptions.setPicker(ageItems);//一级选择器
        pvOptions.show();
    }

    private void showCityPickView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                tvAddress.setText(tx);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
}
