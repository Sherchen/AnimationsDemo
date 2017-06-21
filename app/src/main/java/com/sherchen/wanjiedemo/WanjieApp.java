package com.sherchen.wanjiedemo;

import android.app.Application;

import com.sherchen.common.util.Utils;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/27
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class WanjieApp extends Application{
    private static WanjieApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        sInstance = this;
    }

    public static WanjieApp getInstance(){
        return sInstance;
    }
}
