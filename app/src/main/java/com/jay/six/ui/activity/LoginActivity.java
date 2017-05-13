package com.jay.six.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jay.six.R;
import com.jay.six.bean.Account;
import com.jay.six.common.BaseActivity;
import com.jay.six.common.Constants;
import com.jay.six.common.manager.ActivityManager;
import com.jay.six.common.manager.PreferencesManager;
import com.jay.six.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jayli on 2017/5/11 0011.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.login_sina)
    ImageView loginSina;
    @BindView(R.id.login_wechat)
    ImageView loginWechat;
    @BindView(R.id.login_qq)
    ImageView loginQq;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.tv_phone, R.id.tv_forget_pwd, R.id.login_sina, R.id.login_wechat, R.id.login_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.tv_phone:
                break;
            case R.id.tv_forget_pwd:
                break;
            case R.id.login_sina:
                break;
            case R.id.login_wechat:
                break;
            case R.id.login_qq:
                break;
        }
    }


    private void register() {
        String uname = etName.getText().toString();
        String upwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(upwd)) {
            ToastUtils.shortToast(this, "账户或密码不能为空！");
            return;
        }
        //使用BmobSDK提供的注册功能
        Account user = new Account();
        user.setUsername(uname);
        user.setPassword(upwd);
        user.signUp(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtils.shortToast(LoginActivity.this, "注册成功！");
                //TODO 完善个人信息：上传头像，性别，年龄，地址
                startActivity(MoreInfoActivity.class);
                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.shortToast(LoginActivity.this, s);
            }
        });
    }

    private void login() {
        final String uname = etName.getText().toString();
        final String upwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(upwd)) {
            ToastUtils.shortToast(LoginActivity.this, "账户或密码不能为空！");
            return;
        }

        //使用BmobSDK提供的登录功能
        Account user = new Account();
        user.setUsername(uname);
        user.setPassword(upwd);
        user.login(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtils.shortToast(LoginActivity.this, "登录成功！");
                /*
                * TODO 把用户的登录信息保存到本地：sp\sqlite：（登录状态，登录类别，登录账户信息）
                * 注意:为了保证数据安全，一般对数据进行加密
                * 通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
                * 如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息
                * */
                Account user = BmobUser.getCurrentUser(LoginActivity.this,Account.class);
                PreferencesManager preferences = PreferencesManager.getInstance(LoginActivity.this);
                preferences.put(Constants.IS_LOGIN, true);
                preferences.put(Constants.USER_NAME, user.getUsername());
                preferences.put(Constants.USER_PHOTO, user.getPhoto());
                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.shortToast(LoginActivity.this, s);
                clearInput();
            }
        });
    }

    private void clearInput() {
        etName.setText("");
        etPwd.setText("");
    }

}
