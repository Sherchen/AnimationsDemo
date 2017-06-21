package com.sherchen.wanjiedemo.camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sherchen.common.util.ScreenUtils;
import com.sherchen.common.util.StringUtils;
import com.sherchen.common.util.TimeUtils;
import com.sherchen.common.util.ToastUtils;
import com.sherchen.wanjiedemo.R;
import com.sherchen.wanjiedemo.SystemInfo;
import com.sherchen.wanjiedemo.camera.model.Addon;

import java.lang.ref.WeakReference;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.widget.HListView;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 图片处理界面
 * Created by sky on 2015/7/8.
 * Weibo: http://weibo.com/2030683111
 * Email: 1132234509@qq.com
 */
public class PhotoProcessActivity extends AppCompatActivity {
    public static final String KEY_BITMAP = "key_bitmap";

    @BindView(R.id.title_layout)
    CommonTitleBar titleBar;

    //滤镜图片
    @BindView(R.id.gpuimage)
    GPUImageView mGPUImageView;
    //绘图区域
    @BindView(R.id.drawing_view_container)
    ViewGroup drawArea;
    //工具区
    @BindView(R.id.list_tools)
    HListView bottomToolBar;
    private MyImageViewDrawableOverlay mImageView;

    //当前图片
    private Bitmap currentBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);
        ButterKnife.bind(this);
        EffectUtil.clear();
        initView();
        initEvent();
        initStickerToolBar();
        currentBitmap = SystemInfo.getInstance().getBitmap();
        mGPUImageView.setImage(currentBitmap);

    }
    private void initView() {
        //添加贴纸水印的画布
        View overlay = LayoutInflater.from(PhotoProcessActivity.this).inflate(
                R.layout.view_drawable_overlay, null);
        mImageView = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        int screenWidth = ScreenUtils.getScreenWidth();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth,
                ScreenUtils.getScreenWidth());
        mImageView.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay);

        RelativeLayout.LayoutParams rparams = new RelativeLayout.LayoutParams(screenWidth, screenWidth);

        //初始化滤镜图片
        mGPUImageView.setLayoutParams(rparams);


        EffectUtil.addLabelEditable(mImageView, drawArea, null,
                mImageView.getWidth() / 2, mImageView.getWidth() / 2);
    }

    private void initEvent() {
        titleBar.setRightBtnOnclickListener(v -> {
            savePicture();
        });
    }

    private ProgressDialog progressDialog;

    private void showProgressDialog(String text){
        progressDialog = ProgressDialog.show(
                this, "", text
        );
    }

    private void dismissProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //保存图片
    private void savePicture(){
        showProgressDialog("图片处理中...");
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mImageView.getWidth(), mImageView.getHeight());
        try {
            cv.drawBitmap(mGPUImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cv.drawBitmap(currentBitmap, null, dst, null);
        }
        //加贴纸水印
        EffectUtil.applyOnSave(cv, mImageView);

        new SavePicToFileTask(this).execute(newBitmap);
    }


    private class SavePicToFileTask extends AsyncTask<Bitmap,Void,String>{
        private WeakReference<Activity> context;

        public SavePicToFileTask(Activity context) {
            this.context = new WeakReference<>(context);
        }

        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String fileName = null;
            try {
                bitmap = params[0];

                String picName = TimeUtils.date2String(new Date(), "yyyyMMddHHmmss");
                 fileName = ImageUtils.saveToFile(
                         FileUtils.getInst().getPhotoSavedPath() + "/"+ picName, false, bitmap);

            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showLongToast("图片处理错误，请退出相机并重试");
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            dismissProgressDialog();
            if (StringUtils.isEmpty(fileName)) {
                return;
            }

            Activity activity = context.get();
            if(activity == null) return;
            if(activity.isFinishing()) return;
            Intent intent = new Intent();
            intent.putExtra(CameraActivity.KEY_FILENAME, fileName);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }


    //初始化贴图
    private void initStickerToolBar(){

        bottomToolBar.setAdapter(new StickerToolAdapter(PhotoProcessActivity.this, EffectUtil.addonList));
        bottomToolBar.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> arg0,
                                    View arg1, int arg2, long arg3) {
                Addon sticker = EffectUtil.addonList.get(arg2);
                EffectUtil.addStickerImage(mImageView, PhotoProcessActivity.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(currentBitmap != null && !currentBitmap.isRecycled()) {
            currentBitmap.recycle();
            currentBitmap = null;
        }
    }
}
