package com.sherchen.wanjiedemo.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.sherchen.wanjiedemo.R;
import com.sherchen.wanjiedemo.camera.CameraActivity;
import com.sherchen.wanjiedemo.utils.KLog;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.blurry.Blurry;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/26
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class DetailEventsFragment extends MvpFragment<DetailEventsView, DetailEventsPresent> implements DetailEventsView {
    public static final String TAG = "DetailEventsFragment";

    @BindView(R.id.rv_detail_events)
    ObservableRecyclerView rvDetailEvents;
    @BindView(R.id.iv_detail_header)
    ImageView ivDetailHeader;
    DetailEventAdapter mAdapter;
    Unbinder unbinder;

    private boolean mLayoutFinished;

    int headerHeight;

    private Bitmap srcHeaderBg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_events, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        headerHeight = getResources().getDimensionPixelSize(R.dimen.header_bg_height);
        presenter.uiShowDetailEvents();
        presenter.parseHeaderBg();
        initSlide();
    }

    private void initSlide() {
        ScrollUtils.addOnGlobalLayoutListener(rvDetailEvents, new Runnable() {
            @Override
            public void run() {
                mLayoutFinished = true;
                updateScroll(rvDetailEvents.getCurrentScrollY());
            }
        });
        rvDetailEvents.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                if (!mLayoutFinished) return;
                updateScroll(scrollY);
            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
            }
        });
    }

    private void updateScroll(int currentScrollY) {
        KLog.d("currentScrollY:" + currentScrollY);
        int newScrollY = currentScrollY;
        if(newScrollY > headerHeight) {
            newScrollY = headerHeight;
        }
        float alpha = 1f - (float) Math.abs(newScrollY) / headerHeight;
//        KLog.d(alpha);
        ViewCompat.setAlpha(ivDetailHeader, alpha > .6f ? alpha : .6f);
        if(currentScrollY <= headerHeight) {
            blurImage(alpha);
        }
    }

    private float lastAlpha;

    private void blurImage(float alpha){
        if(srcHeaderBg == null) return;
        if(alpha > 0) {
            if(alpha >= 1) {
                lastAlpha = 1;
                KLog.d("lastAlpha:" + lastAlpha);
                ivDetailHeader.setImageBitmap(srcHeaderBg);
            }else {
                float delta = alpha - lastAlpha;
                if(Math.abs(delta) >= .1f) {
                    int samping = (int) (8 * (1 - alpha));
                    int radius = (int) (25 * (1 - alpha));
                    KLog.d("sampling:" + samping);
                    tryBlur(samping, radius);
                    lastAlpha = alpha;
                    KLog.d("lastAlpha:" + lastAlpha);
                }
            }
        }
    }

    private void tryBlur(int sampling, int radius){
        int newSampling = sampling;
        if(newSampling < 2) {
            newSampling = 2;
        }
        Blurry.with(getActivity())
                .radius(10)
                .sampling(newSampling)
                .async(new Blurry.ImageComposer.ImageComposerListener() {
                    @Override
                    public void onImageReady(BitmapDrawable drawable) {
                          if(lastAlpha == 1f){
                              ivDetailHeader.setImageBitmap(srcHeaderBg);
                          }  else {
                              ivDetailHeader.setImageDrawable(drawable);
                          }
                    }
                })
                .from(srcHeaderBg)
                .into(ivDetailHeader)
        ;
    }


    @Override
    public DetailEventsPresent createPresenter() {
        return new DetailEventsPresent(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showEvents(List<DetailEvent> events) {
        int headerHeight = getResources().getDimensionPixelSize(R.dimen.header_bg_height);
        rvDetailEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DetailEventAdapter(events, headerHeight);
        View view = new View(getActivity());
        view.setLayoutParams(new LinearLayoutCompat.LayoutParams(-1, headerHeight));
        mAdapter.addHeaderView(view);
        rvDetailEvents.setAdapter(mAdapter);
//        final StickyRecyclerHeadersDecoration header = new StickyRecyclerHeadersDecoration(mAdapter);
//        rvDetailEvents.addItemDecoration(header);
        rvDetailEvents.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvDetailEvents.setItemAnimator(new SlideInLeftAnimator());
        rvDetailEvents.getItemAnimator().setAddDuration(500);
        rvDetailEvents.getItemAnimator().setRemoveDuration(500);
    }

    @Override
    public void showHeaderBg(final Bitmap bitmap) {
        srcHeaderBg = bitmap;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivDetailHeader.setImageBitmap(bitmap);
            }
        });
    }

    public void addNewEntity(DetailEvent event){
        mAdapter.addData(0, event);
    }
}

interface DetailEventsView extends MvpView {
    void showEvents(List<DetailEvent> events);

    void showHeaderBg(Bitmap bitmap);
}

class DetailEventsPresent extends MvpBasePresenter<DetailEventsView> {
    public DetailEventsPresent(Activity activity) {
        super(activity);
    }

    void uiShowDetailEvents() {
        List<DetailEvent> list = new ArrayList<>();
        list.add(new DetailEvent("http://images.china.cn/attachement/jpg/site1000/20121114/001aa0ba5c77120d2cd450.jpg", "5月25日 17:36"));
        list.add(new DetailEvent("http://images.china.cn/attachement/jpg/site1000/20121114/001aa0ba5c77120d2cd450.jpg", "5月24日 16:36"));
        list.add(new DetailEvent("http://images.china.cn/attachement/jpg/site1000/20121114/001aa0ba5c77120d2cd450.jpg", "5月23日 15:36"));
        list.add(new DetailEvent("http://images.china.cn/attachement/jpg/site1000/20121114/001aa0ba5c77120d2cd450.jpg", "5月22日 14:36"));
        list.add(new DetailEvent("http://images.china.cn/attachement/jpg/site1000/20121114/001aa0ba5c77120d2cd450.jpg", "5月21日 13:36"));
        if (isViewAttached()) {
            getView().showEvents(list);
        }
    }

    void parseHeaderBg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(m_Activity.getResources(), R.drawable.header_bg);
                if(isViewAttached()) {
                    getView().showHeaderBg(bitmap);
                }
            }
        }).start();
    }
}