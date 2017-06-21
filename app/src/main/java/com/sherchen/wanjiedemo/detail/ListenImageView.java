package com.sherchen.wanjiedemo.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/25
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class ListenImageView extends ImageView{
    public ListenImageView(Context context) {
        super(context);
    }

    public ListenImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListenImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(listener != null) {
            listener.onSizeChanged();
        }
    }

    private OnSizeChanged listener;

    public void setOnSizeChanged(OnSizeChanged l){
        this.listener = l;
    }

    public interface OnSizeChanged{
        void onSizeChanged();
    }

}
