package com.sherchen.wanjiedemo;

import android.graphics.Bitmap;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/25
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class SystemInfo {

    private static final SystemInfo sInstance = new SystemInfo();

    public static SystemInfo getInstance(){
        return sInstance;
    }

    private Bitmap bitmap;


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
