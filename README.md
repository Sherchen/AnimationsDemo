## Gif demo


## Transition Animation
if api >  21, use activity transition animation, otherwise use Transitions-Everywhere


```
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
```

## blur animation
use Blurry

```
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
```

## recycler animation
use recyclerview-animations

```
rvDetailEvents.setItemAnimator(new SlideInLeftAnimator());
        rvDetailEvents.getItemAnimator().setAddDuration(500);
        rvDetailEvents.getItemAnimator().setRemoveDuration(500);
```

## Thanks

1. https://github.com/Hitomis/transferee
2. https://github.com/Skykai521/StickerCamera
3. https://github.com/gogopop/CameraKit-Android
4. https://github.com/ksoichiro/Android-ObservableScrollView
5. https://github.com/wasabeef/recyclerview-animators
6. https://github.com/wasabeef/Blurry
