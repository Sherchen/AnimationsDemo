package com.sherchen.wanjiedemo.detail;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hitomi.glideloader.GlideImageLoader;
import com.hitomi.tilibrary.style.progress.ProgressPieIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.sherchen.wanjiedemo.R;
import com.sherchen.wanjiedemo.utils.GlideUtils;
import com.sherchen.wanjiedemo.utils.KLog;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/26
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class DetailEventAdapter extends BaseQuickAdapter<DetailEvent, BaseViewHolder>
    implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
{
    private int headerHeight;
    private ArrayList<String> sourceImageList;
    private List<ImageView> sourceImageViewList;

    public DetailEventAdapter(@Nullable List<DetailEvent> data, int headerHeight) {
        super(R.layout.item_detail_events, data);
        this.headerHeight = headerHeight;
        sourceImageList = new ArrayList<>();
        sourceImageViewList = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DetailEvent item) {
        helper.setText(R.id.tv_event_time, item.getTime());
        final ImageView ivPic = helper.getView(R.id.iv_event_pic);
        if(item.getUrl().startsWith("/")) {
            GlideUtils.displayImage(mContext, ivPic, new File(item.getUrl()), R.drawable.def_placeholder);
        }else{
            GlideUtils.displayImage(mContext, ivPic, item.getUrl(), R.drawable.def_placeholder);
        }

        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceImageList.clear();
                sourceImageList.add(item.getUrl());
                sourceImageViewList.clear();
                sourceImageViewList.add(ivPic);
                TransferConfig config = TransferConfig.build()
                        .setNowThumbnailIndex(0)
                        .setSourceImageList(sourceImageList)
                        .setMissPlaceHolder(R.drawable.def_placeholder)
                        .setJustLoadHitImage(true)
                        .setOriginImageList(sourceImageViewList)
                        .setProgressIndicator(new ProgressPieIndicator())
                        .setImageLoader(GlideImageLoader.with(mContext.getApplicationContext()))
                        .create();
                Transferee.getDefault(mContext).apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
                    @Override
                    public void onShow() {
                        Glide.with(mContext).pauseRequests();
                    }

                    @Override
                    public void onDismiss() {
                        Glide.with(mContext).resumeRequests();
                    }
                });
            }
        });

        final ViewGroup bottom = (ViewGroup) helper.getView(R.id.ll_detail_bottom);
        final TextView update = (TextView) helper.getView(R.id.tv_detail_update);
        final TextView delete = (TextView) helper.getView(R.id.tv_detail_delete);
        TextView more = (TextView) helper.getView(R.id.tv_detail_more);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d("position1:" + helper.getAdapterPosition());
                KLog.d("position2:" + helper.getLayoutPosition());

                TransitionManager.beginDelayedTransition((ViewGroup) bottom,
                        new ChangeBounds().setDuration(300)
                        );
                if(update.getVisibility() == View.GONE) {
                    update.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }else{
                    update.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(helper.getAdapterPosition());
            }
        });
    }

    @Override
    public long getHeaderId(int position) {
//        if (position == 0) {
//            return -1;
//        } else {
//            return getItem(position).getUrl().charAt(0);
//        }
        return 1000L;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = new View(parent.getContext());
        view.setLayoutParams(new LinearLayoutCompat.LayoutParams(-1, headerHeight));
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
