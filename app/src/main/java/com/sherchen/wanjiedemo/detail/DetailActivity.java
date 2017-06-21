package com.sherchen.wanjiedemo.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hitomi.tilibrary.transfer.Transferee;
import com.sherchen.wanjiedemo.R;
import com.sherchen.wanjiedemo.camera.CameraActivity;
import com.sherchen.wanjiedemo.utils.CommonUtils;
import com.sherchen.wanjiedemo.utils.FragmentNavigator;
import com.sherchen.wanjiedemo.utils.FragmentNavigatorAdapter;
import com.sherchen.wanjiedemo.utils.GlideUtils;
import com.sherchen.wanjiedemo.utils.KLog;
import com.transitionseverywhere.ArcMotion;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.extra.TranslationTransition;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : Sherchen
 *     e-mail : ncuboy_045wsq@qq.com
 *     time   : 2017/5/25
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class DetailActivity extends AppCompatActivity implements ListenImageView.OnSizeChanged {
    public static final String KEY_USE_SYS_TRANSITION = "key_sys_transition";
    private boolean useSysTransition;

    private static final int REQUEST_CAMERA = 0;

    public static final String KEY_ENTITY = "key_entity";

    @BindView(R.id.iv_detail_action)
    ListenImageView ivDetail;
    @OnClick(R.id.iv_detail_action) void gotoCamera(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @BindView(R.id.rl_detail_root)
    RelativeLayout rlDetailRoot;
    @BindView(R.id.fl_detail_container)
    FrameLayout flDetailContainer;
    @OnClick(R.id.iv_detail_events) void showEvents(){
        mFragNavigator.showFragment(0);
    }
    @OnClick(R.id.iv_detail_help) void showHelp(){
        mFragNavigator.showFragment(1);
    }
    private Unbinder unbinder;

    private TransitionsEntity entity;
    private TransitionsEntity outEntity;

    private FragmentNavigator mFragNavigator;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        unbinder = ButterKnife.bind(this);
        handleTransitions();
        mFragNavigator = new FragmentNavigator(getSupportFragmentManager(), new DetailFragmentAdapter(), R.id.fl_detail_container);
        mFragNavigator.setDefaultPosition(0);
        mFragNavigator.onCreate(savedInstanceState);
        mFragNavigator.showFragment(mFragNavigator.getCurrentPosition());
        ivDetail.setOnSizeChanged(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragNavigator.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void handleTransitions() {
        useSysTransition = getIntent().getBooleanExtra(KEY_USE_SYS_TRANSITION, false);
        if(useSysTransition) {
            ivDetail.setVisibility(View.VISIBLE);
            Fade fade = new Fade();
            fade.setDuration(500);
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
            ChangeBounds changeBounds = new ChangeBounds();
            getWindow().setSharedElementEnterTransition(changeBounds);
        }
        entity = (TransitionsEntity) getIntent().getSerializableExtra(KEY_ENTITY);
        KLog.d(entity);
        GlideUtils.displayImage(this, ivDetail, entity.getUrl(), R.drawable.def_placeholder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        Transferee.getDefault(this).destroy();
    }

    private float lastTranX;
    private float lastTranY;

    @Override
    public void onSizeChanged() {
        if(!useSysTransition) {
            int toTop = entity.getTop();
            int toLeft = entity.getLeft();

            int[] orgins = new int[2];
            ivDetail.getLocationOnScreen(orgins);
            KLog.d("originX:" + orgins[0] + ",originY:" + orgins[1]);

            if (outEntity == null) {
                outEntity = new TransitionsEntity();
            }
            outEntity.setOldLeft(toLeft);
            outEntity.setOldTop(toTop);
            outEntity.setLeft(orgins[0]);
            outEntity.setTop(orgins[1]);
            outEntity.setWidth(ivDetail.getWidth());
            outEntity.setHeight(ivDetail.getHeight());

            lastTranX = toLeft - orgins[0];
            lastTranY = toTop - orgins[1];

            ivDetail.setTranslationX(lastTranX);
            ivDetail.setTranslationY(lastTranY);
            ivDetail.setVisibility(View.VISIBLE);
            rlDetailRoot.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TransitionManager.beginDelayedTransition(rlDetailRoot, new TranslationTransition().setPathMotion(new ArcMotion()).setDuration(200));
                    ivDetail.setTranslationX(0);
                    ivDetail.setTranslationY(0);
                }
            }, 20);
        }
    }

    @Override
    public void onBackPressed() {
        if(useSysTransition) {
            super.onBackPressed();
        }else{
            Intent intent = new Intent();
            intent.putExtra(KEY_ENTITY, outEntity);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(0, 0);
        }
//        rlDetailRoot.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Transition transition = new TranslationTransition().
//                        setPathMotion(new ArcMotion()).setDuration(400);
//                transition.addListener(new Transition.TransitionListenerAdapter() {
//                    @Override
//                    public void onTransitionEnd(Transition transition) {
//                        super.onTransitionEnd(transition);
//                        finish();
//                        overridePendingTransition(0, 0);
//                    }
//                });
//                TransitionManager.beginDelayedTransition(rlDetailRoot, transition);
//                ivDetail.setTranslationX(lastTranX);
//                ivDetail.setTranslationY(lastTranY);
//            }
//        }, 20);
    }

    private void startDownJump() {

    }

    private class DetailFragmentAdapter implements FragmentNavigatorAdapter {

        @Override
        public Fragment onCreateFragment(int position) {
            if(position == 0) {
                return new DetailEventsFragment();
            }else{
                return new DetailHelpFragment();
            }
        }

        @Override
        public String getTag(int position) {
            if(position == 0) {
                return DetailEventsFragment.TAG;
            }else{
                return DetailHelpFragment.TAG;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK && data != null){
            String filename = data.getStringExtra(CameraActivity.KEY_FILENAME);
            String time = CommonUtils.getDisplayTime(new Date().getTime());
            DetailEvent event = new DetailEvent(filename, time);
            DetailEventsFragment fragment = (DetailEventsFragment) mFragNavigator.getFragment(0);
            fragment.addNewEntity(event);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
