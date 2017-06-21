package com.sherchen.wanjiedemo;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/25
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

import android.os.Bundle;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class ButterknifeMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
    extends MvpActivity<V, P>
{
    private Unbinder bind;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        bind = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bind != null) {
            bind.unbind();
        }
    }
}
