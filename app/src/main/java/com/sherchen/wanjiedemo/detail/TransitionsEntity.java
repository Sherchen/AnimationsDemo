package com.sherchen.wanjiedemo.detail;

import java.io.Serializable;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/25
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class TransitionsEntity implements Serializable{

    int top;
    int left;
    int width;
    int height;
    String url;

    int oldTop;
    int oldLeft;



    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TransitionsEntity{" +
                "top=" + top +
                ", left=" + left +
                ", width=" + width +
                ", height=" + height +
                ", url='" + url + '\'' +
                '}';
    }

    public int getOldTop() {
        return oldTop;
    }

    public void setOldTop(int oldTop) {
        this.oldTop = oldTop;
    }

    public int getOldLeft() {
        return oldLeft;
    }

    public void setOldLeft(int oldLeft) {
        this.oldLeft = oldLeft;
    }
}
