package com.sherchen.wanjiedemo.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.sherchen.wanjiedemo.ButterknifeMvpActivity;
import com.sherchen.wanjiedemo.R;
import com.sherchen.wanjiedemo.detail.DetailActivity;
import com.sherchen.wanjiedemo.detail.TransitionsEntity;
import com.sherchen.wanjiedemo.utils.GlideUtils;
import com.sherchen.wanjiedemo.utils.TransitionHelper;
import com.transitionseverywhere.ArcMotion;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.extra.TranslationTransition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/25
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class HomeActivity extends ButterknifeMvpActivity<HomeView, HomePresent> implements HomeView {
    private static final int REQUEST_CODE_DEF = 1;

    @BindView(R.id.rv_home)
    RecyclerView rvHome;

    @BindView(R.id.iv_home_transition)
    ImageView ivTrans;

    @BindView(R.id.fl_scene_root)
    FrameLayout vSceneRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        presenter.uiShowList();
        setupWindowTransitions();

//        Log.v("HomeActivity", "channelId:" + MCPTool.getChannelId(this, "12345678", "null"));
//
//        final String market = PackerNg.getMarket(this);
//        Log.v("HomeActivity", "market:" + market);

//        final String market = ChannelUtil.getChannel(this, "null");
//        Log.v("HomeActivity", "channelId:" + market);

//        String channel = ChannelUtils.getChannel(this);
//        Log.v("HomeActivity", "channel:" + channel);

        readChannel();
    }

    private void readChannel() {
        final long startTime = System.currentTimeMillis();
        final ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(this.getApplicationContext());
        if (channelInfo != null) {
            Log.v("channel:", channelInfo.getChannel() + "\n" + channelInfo.getExtraInfo());
        }
    }

    private void setupWindowTransitions(){
        if(TransitionHelper.isSysTransitionEnable()) {
            Fade fade = new Fade();
            fade.setDuration(500);
            getWindow().setExitTransition(fade);
            getWindow().setReenterTransition(fade);
        }
    }

    @NonNull
    @Override
    public HomePresent createPresenter() {
        return new HomePresent(this);
    }

    private ImageView clickImage;
    private TransitionsEntity clickOrigin;

    @Override
    public void showLists(List<HomeEntity> list) {
        rvHome.setLayoutManager(new LinearLayoutManager(this));
        HomeAdapter adapter = new HomeAdapter(list);
        rvHome.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                playTransition(view, position);
            }

            private void playTransition(View view, int position) {
                if(TransitionHelper.isSysTransitionEnable()) {
                    final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(
                        HomeActivity.this, false,
                            new Pair<>((ImageView) view.findViewById(R.id.iv_item_profile)
                                    , getString(R.string.transition_image))
                    );
                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    intent.putExtra(DetailActivity.KEY_USE_SYS_TRANSITION, true);
                    TransitionsEntity entity = new TransitionsEntity();
                    entity.setUrl(adapter.getItem(position).getUrl());
                    intent.putExtra(DetailActivity.KEY_ENTITY, entity);
                    ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this, pairs);
                    startActivity(intent, transitionActivityOptions.toBundle());
                }else{
                    int[] locs = new int[2];
                    clickImage = (ImageView) view.findViewById(R.id.iv_item_profile);
                    clickImage.getLocationOnScreen(locs);
                    clickOrigin = new TransitionsEntity();
                    clickOrigin.setLeft(locs[0]);
                    clickOrigin.setTop(locs[1]);
                    clickOrigin.setWidth(clickImage.getWidth());
                    clickOrigin.setHeight(clickImage.getHeight());
                    HomeEntity item = (HomeEntity) adapter.getItem(position);
                    clickOrigin.setUrl(item.getUrl());
                    GlideUtils.displayImage(getApplicationContext(), ivTrans, item.getUrl(), R.drawable.def_placeholder);
                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra(DetailActivity.KEY_ENTITY, false);
                    intent.putExtra(DetailActivity.KEY_ENTITY, clickOrigin);
                    startActivityForResult(intent, REQUEST_CODE_DEF);
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_DEF && resultCode == RESULT_OK) {
            TransitionsEntity outEntity = (TransitionsEntity) data.getSerializableExtra(DetailActivity.KEY_ENTITY);
            if(outEntity == null) return;
            clickImage.setVisibility(View.GONE);
            ivTrans.setVisibility(View.VISIBLE);
            ivTrans.setTranslationX(outEntity.getLeft());
            ivTrans.setTranslationY(outEntity.getTop());

            vSceneRoot.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TransitionManager.beginDelayedTransition(vSceneRoot, new TranslationTransition().setPathMotion(new ArcMotion()).setDuration(200).addListener(new Transition.TransitionListenerAdapter(){
                        @Override
                        public void onTransitionEnd(Transition transition) {
                            super.onTransitionEnd(transition);
                            ivTrans.setVisibility(View.GONE);
                            clickImage.setVisibility(View.VISIBLE);
                        }
                    }));
                    ivTrans.setTranslationX(outEntity.getOldLeft());
                    ivTrans.setTranslationY(outEntity.getOldTop());
                }
            }, 20);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

interface HomeView extends MvpView {
    void showLists(List<HomeEntity> list);
}

class HomePresent extends MvpBasePresenter<HomeView> {

    public HomePresent(Activity activity) {
        super(activity);
    }

    void uiShowList() {
        List<HomeEntity> list = new ArrayList();
        list.add(new HomeEntity("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3343858452,3932805447&fm=23&gp=0.jpg", "魔琴", "颜值编译钢琴曲"));
        list.add(new HomeEntity("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=523087010,536628020&fm=23&gp=0.jpg", "发呆音乐", "智能生成我的背景音"));
        list.add(new HomeEntity("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2984099645,837622091&fm=23&gp=0.jpg", "录音相机", "拍一张会说话的照片"));
        if (isViewAttached()) {
            getView().showLists(list);
        }
    }
}

