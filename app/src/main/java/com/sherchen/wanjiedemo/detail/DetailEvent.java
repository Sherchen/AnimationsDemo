package com.sherchen.wanjiedemo.detail;

import android.net.Uri;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/26
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class DetailEvent {

    private String url;
    private Uri uri;

    private String time;

    public DetailEvent(String url, String time) {
        this.url = url;
        this.time = time;
    }

    public DetailEvent(Uri uri, String time) {
        this.uri = uri;
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public Uri getUri() {
        return uri;
    }

    public String getTime() {
        return time;
    }
}
