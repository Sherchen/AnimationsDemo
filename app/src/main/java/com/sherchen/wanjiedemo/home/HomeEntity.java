package com.sherchen.wanjiedemo.home;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/25
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */
class HomeEntity {

    public HomeEntity(String url, String title, String subtitle) {
        this.url = url;
        this.title = title;
        this.subtitle = subtitle;
    }

    private String url;
    private String title;
    private String subtitle;

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
