package com.jay.six.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.jay.six.BuildConfig;
import com.jay.six.utils.AndroidLogAdapter;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;


/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class BaseApplication extends Application {
    private static final String TAG = "Six";

    @Override
    public void onCreate() {
        super.onCreate();
        initLooger();
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
