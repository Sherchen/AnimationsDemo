package com.sherchen.wanjiedemo.home;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sherchen.wanjiedemo.R;
import com.sherchen.wanjiedemo.detail.DetailActivity;
import com.sherchen.wanjiedemo.detail.TransitionsEntity;
import com.sherchen.wanjiedemo.utils.GlideUtils;

import java.util.List;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/25
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class HomeAdapter extends BaseQuickAdapter<HomeEntity, BaseViewHolder>{
    public HomeAdapter(@Nullable List<HomeEntity> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HomeEntity item) {
        helper.setText(R.id.tv_item_title, item.getTitle());
        helper.setText(R.id.tv_item_subtitle, item.getSubtitle());

        ImageView ivProfile = helper.getView(R.id.iv_item_profile);
        GlideUtils.displayImage(mContext, ivProfile, item.getUrl(), R.drawable.def_placeholder);
    }
}
