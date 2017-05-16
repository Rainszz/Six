package com.jay.six.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.jay.six.BuildConfig;
import com.jay.six.R;
import com.jay.six.bean.Account;
import com.jay.six.common.manager.PreferencesManager;
import com.jay.six.ui.activity.LoginActivity;
import com.jay.six.utils.AndroidLogAdapter;
import com.jay.six.utils.LogUtils;
import com.jay.six.utils.LoginUtils;
import com.jay.six.utils.ToastUtils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class BaseApplication extends Application {
    private static final String TAG = "Six";
    private static BaseApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = (BaseApplication) getApplicationContext();
        initLooger();
        initOkhttpUtils();
        initShareSDK();
        initBmobSDK();
        initData();
        chengeTheme();
    }

    private void initData() {
        //TODO 初始化数据
        LoginUtils.checkLogin(false);
    }

    private void chengeTheme() {
        //默认是false
        if(!PreferencesManager.getInstance(this).get(Constants.APP_THEME,false)){
            PreferencesManager.getInstance(this).put(Constants.APP_THEME,true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            PreferencesManager.getInstance(this).put(Constants.APP_THEME,false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void initBmobSDK() {
        Bmob.initialize(this, "464085faa39f50f12a26d82c81d12228");
    }

    private void initShareSDK() {
        ShareSDK.initSDK(this);
    }

    private void initOkhttpUtils() {
        //允许使用cookie
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("HTTP"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static synchronized BaseApplication getInstance(){
        return instance;
    }

    /**
     * 初始化Looger配置
     */
    private void initLooger() {
        Logger.init(TAG)                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)        // default LogLevel.FULL
                .methodOffset(2)                // default 0
                .logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base); MultiDex.install(this);
    }
}
