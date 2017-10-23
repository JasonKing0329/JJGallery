package com.jing.app.jjgallery.gdb.view.record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allure.lbanners.LMBanners;
import com.allure.lbanners.adapter.LBaseAdapter;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseActivity;
import com.jing.app.jjgallery.gdb.bean.Star3W;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.model.GdbImageProvider;
import com.jing.app.jjgallery.gdb.model.VideoModel;
import com.jing.app.jjgallery.gdb.presenter.record.RecordPresenter;
import com.jing.app.jjgallery.gdb.utils.LMBannerViewUtil;
import com.jing.app.jjgallery.gdb.view.pub.BannerAnimDialogFragment;
import com.jing.app.jjgallery.gdb.view.pub.VideoDialogFragment;
import com.jing.app.jjgallery.gdb.view.recommend.RecordFilterDialog;
import com.jing.app.jjgallery.model.pub.ObjectCache;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.publicview.PointDescLayout;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.RecordSingleScene;
import com.king.service.gdb.bean.RecordThree;
import com.king.service.gdb.bean.Star;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JingYang on 2016/8/17 0017.
 * Description:
 */
public class RecordActivity extends GBaseActivity implements IRecordView {

    @BindView(R.id.iv_record)
    ImageView ivRecord;
    @BindView(R.id.lmbanner)
    LMBanners lmBanners;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_star1)
    CircularImageView ivStar1;
    @BindView(R.id.tv_star1)
    TextView tvStar1;
    @BindView(R.id.group_star1)
    LinearLayout groupStar1;
    @BindView(R.id.iv_star2)
    CircularImageView ivStar2;
    @BindView(R.id.tv_star2)
    TextView tvStar2;
    @BindView(R.id.group_star2)
    LinearLayout groupStar2;
    @BindView(R.id.tv_scene)
    TextView tvScene;
    @BindView(R.id.tv_scene_score)
    TextView tvSceneScore;
    @BindView(R.id.tv_score_total)
    TextView tvScoreTotal;
    @BindView(R.id.tv_deprecated)
    TextView tvDeprecated;
    @BindView(R.id.group_scene)
    RelativeLayout groupScene;
    @BindView(R.id.group_bareback)
    RelativeLayout groupBareback;
    @BindView(R.id.tv_path)
    TextView tvPath;
    @BindView(R.id.tv_cum)
    TextView tvCum;
    @BindView(R.id.tv_star)
    TextView tvStar;
    @BindView(R.id.tv_starc)
    TextView tvStarC;
    @BindView(R.id.tv_special)
    TextView tvSpecial;
    @BindView(R.id.tv_special_content)
    TextView tvSpecialContent;
    @BindView(R.id.group_special)
    RelativeLayout groupSpecial;
    @BindView(R.id.tv_fk)
    TextView tvFk;
    @BindView(R.id.tv_hd)
    TextView tvHd;
    @BindView(R.id.tv_basic)
    TextView tvBasic;
    @BindView(R.id.tv_feel)
    TextView tvFeel;
    @BindView(R.id.tv_bjob)
    TextView tvBjob;
    @BindView(R.id.tv_rhythm)
    TextView tvRhythm;
    @BindView(R.id.tv_foreplay)
    TextView tvForeplay;
    @BindView(R.id.tv_story)
    TextView tvStory;
    @BindView(R.id.tv_extra)
    TextView tvExtra;
    @BindView(R.id.tv_rim)
    TextView tvRim;
    @BindView(R.id.tv_cshow)
    TextView tvCshow;
    @BindView(R.id.group_fk)
    PointDescLayout groupFk;
    @BindView(R.id.group_record)
    RelativeLayout groupRecord;

    @BindView(R.id.group_star_1v1)
    LinearLayout groupStar1v1;
    @BindView(R.id.iv_3w_star1)
    CircularImageView iv3wStar1;
    @BindView(R.id.tv_3w_star1)
    TextView tv3wStar1;
    @BindView(R.id.group_3w_star1)
    LinearLayout group3wStar1;
    @BindView(R.id.iv_3w_star2)
    CircularImageView iv3wStar2;
    @BindView(R.id.tv_3w_star2)
    TextView tv3wStar2;
    @BindView(R.id.group_3w_star2)
    LinearLayout group3wStar2;
    @BindView(R.id.iv_3w_star3)
    CircularImageView iv3wStar3;
    @BindView(R.id.tv_3w_star3)
    TextView tv3wStar3;
    @BindView(R.id.group_3w_star3)
    LinearLayout group3wStar3;
    @BindView(R.id.group_star_3w)
    LinearLayout groupStar3w;
    @BindView(R.id.tv_3w_flag1)
    TextView tv3wFlag1;
    @BindView(R.id.tv_3w_flag2)
    TextView tv3wFlag2;
    @BindView(R.id.tv_3w_flag3)
    TextView tv3wFlag3;

    private Record record;
    private RecordPresenter mPresenter;

    private String videoPath;

    private List<String> headPathList;
    private BannerAnimDialogFragment bannerSettingDialog;

    private Map<Integer, ImageView> starImageViewMap;

    @Override
    public int getContentView() {
        return R.layout.gdb_record_1v1;
    }

    @Override
    public void initController() {
        starImageViewMap = new HashMap<>();
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.gdb_record_default);
        mPresenter = new RecordPresenter(this);
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("Record");

        initValue();
    }

    @Override
    public void initBackgroundWork() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        // don't use finish(), transition animation will be executed only by onBackPressed()
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initValue();
        initBackgroundWork();
    }

    private void initValue() {
        Object obj = ObjectCache.getData();
        if (obj instanceof RecordOneVOne) {
            record = (RecordOneVOne) ObjectCache.getData();
        } else if (obj instanceof RecordThree) {
            record = (RecordThree) ObjectCache.getData();
        }

        initHeadPart();

        // RecordOneVOne和RecordThree都是继承于RecordSingleScene
        if (record instanceof RecordSingleScene) {
            initRecordSingleScene((RecordSingleScene) record);

            if (record instanceof RecordOneVOne) {
                groupStar3w.setVisibility(View.GONE);
                groupStar1v1.setVisibility(View.VISIBLE);
                initRecordOneVOne((RecordOneVOne) record);
            } else if (record instanceof RecordThree) {
                groupStar3w.setVisibility(View.VISIBLE);
                groupStar1v1.setVisibility(View.GONE);
                initRecordThree((RecordThree) record);
            }
        }

        // Record公共部分
        tvPath.setText(record.getDirectory() + "/" + record.getName());
        tvHd.setText("" + record.getHDLevel());
        tvScoreTotal.setText("" + record.getScore());
        tvFeel.setText("" + record.getScoreFeel());
        tvStory.setText("" + record.getScoreStory());
        tvBasic.setText("" + record.getScoreBasic());
        tvExtra.setText("" + record.getScoreExtra());

        tvDeprecated.setVisibility(record.getDeprecated() == 1 ? View.VISIBLE : View.GONE);

        videoPath = VideoModel.getVideoPath(record.getName());
//        videoPath = "/storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1489199749192.mp4";
        if (videoPath == null) {
            ivPlay.setVisibility(View.GONE);
        } else {
            ivPlay.setVisibility(View.VISIBLE);
        }
    }

    private void initRecordSingleScene(RecordSingleScene record) {
        tvScene.setText(record.getSceneName());
        tvSceneScore.setText("" + record.getScoreScene());
        if (record.getScoreNoCond() > 0) {
            groupBareback.setVisibility(View.VISIBLE);
        } else {
            groupBareback.setVisibility(View.GONE);
        }
        tvBjob.setText("" + record.getScoreBJob());
        tvCum.setText("" + record.getScoreCum());
        tvRhythm.setText("" + record.getScoreRhythm());
        tvForeplay.setText("" + record.getScoreForePlay());
        tvSpecial.setText("" + record.getScoreSpeicial());
        if (TextUtils.isEmpty(record.getSpecialDesc())) {
            groupSpecial.setVisibility(View.GONE);
        } else {
            groupSpecial.setVisibility(View.VISIBLE);
            tvSpecialContent.setText(record.getSpecialDesc());
        }
        tvRim.setText("" + record.getScoreRim());
        tvCshow.setText("" + record.getScoreCShow());
        tvFk.setText("FK(" + record.getScoreFk() + ")");
    }

    private void initRecordOneVOne(RecordOneVOne record) {
        Star star1 = record.getStar1();
        if (star1 == null) {
            tvStar1.setText(GDBProperites.STAR_UNKNOWN + "(" + record.getScoreStar1() + "/" + record.getScoreStarC1() + ")");
        } else {
            starImageViewMap.put(star1.getId(), ivStar1);
            tvStar1.setText(record.getStar1().getName() + "(" + record.getScoreStar1() + "/" + record.getScoreStarC1() + ")");
        }
        Star star2 = record.getStar2();
        if (star2 == null) {
            tvStar2.setText(GDBProperites.STAR_UNKNOWN + "(" + record.getScoreStar2() + "/" + record.getScoreStarC2() + ")");
        } else {
            starImageViewMap.put(star2.getId(), ivStar2);
            tvStar2.setText(record.getStar2().getName() + "(" + record.getScoreStar2() + "/" + record.getScoreStarC2() + ")");
        }
        tvStar.setText("" + record.getScoreStar());
        tvStarC.setText("" + record.getScoreStarC());

        mPresenter.loadStar(record.getStar1().getId());
        mPresenter.loadStar(record.getStar2().getId());
        
        initFkDetails(record);
    }

    private void initRecordThree(RecordThree record) {
        List<Star3W> starList = mPresenter.getStar3WList(record);

        // load star
        if (starList.size() > 0) {
            Star3W star = starList.get(0);
            tv3wFlag1.setText(star.getFlag());
            tv3wStar1.setText(star.getStar().getName() + "(" + star.getScore() + "/" + star.getScoreC() + ")");

            starImageViewMap.put(star.getStar().getId(), iv3wStar1);
            mPresenter.loadStar(star.getStar().getId());
        } else {
            tv3wFlag1.setText(GDBProperites.STAR_UNKNOWN);
            tv3wStar1.setText(GDBProperites.STAR_UNKNOWN);
        }
        if (starList.size() > 1) {
            Star3W star = starList.get(1);
            tv3wFlag2.setText(star.getFlag());
            tv3wStar2.setText(star.getStar().getName() + "(" + star.getScore() + "/" + star.getScoreC() + ")");

            starImageViewMap.put(star.getStar().getId(), iv3wStar2);
            mPresenter.loadStar(star.getStar().getId());
        } else {
            tv3wFlag2.setText(GDBProperites.STAR_UNKNOWN);
            tv3wStar2.setText(GDBProperites.STAR_UNKNOWN);
        }
        if (starList.size() > 2) {
            Star3W star = starList.get(2);
            tv3wFlag3.setText(star.getFlag());
            tv3wStar3.setText(star.getStar().getName() + "(" + star.getScore() + "/" + star.getScoreC() + ")");

            starImageViewMap.put(star.getStar().getId(), iv3wStar3);
            mPresenter.loadStar(star.getStar().getId());
        } else {
            tv3wFlag3.setText(GDBProperites.STAR_UNKNOWN);
            tv3wStar3.setText(GDBProperites.STAR_UNKNOWN);
        }

        tvStar.setText("" + record.getScoreStar());
        tvStarC.setText("" + record.getScoreStarC());

        initFkDetails(record);
    }

    private void initHeadPart() {
        boolean showImage;
        if (GdbImageProvider.hasRecordFolder(record.getName())) {
            headPathList = GdbImageProvider.getRecordPathList(record.getName());
            if (headPathList.size() <= 1) {
                showImage = true;
            } else {
                showImage = false;
                lmBanners.setVisibility(View.VISIBLE);
                ivSetting.setVisibility(View.VISIBLE);
                ivRecord.setVisibility(View.GONE);
                initBanner(headPathList);
            }
        } else {
            showImage = true;
        }

        if (showImage) {
            lmBanners.setVisibility(View.GONE);
            ivSetting.setVisibility(View.GONE);
            ivRecord.setVisibility(View.VISIBLE);
            String path = GdbImageProvider.getRecordRandomPath(record.getName(), null);
            SImageLoader.getInstance().displayImage(path, ivRecord);
        }
    }

    private void initBanner(List<String> pathList) {
        // 禁用btnStart(只在onPageScroll触发后有效)
        lmBanners.isGuide(false);
        // 显示引导圆点
//        lmBanners.hideIndicatorLayout();
        lmBanners.setIndicatorPosition(LMBanners.IndicaTorPosition.BOTTOM_MID);
        // 可以不写，因为文件名直接覆用的LMBanners-1.0.8里的res
        lmBanners.setSelectIndicatorRes(R.drawable.page_indicator_select);
        lmBanners.setUnSelectUnIndicatorRes(R.drawable.page_indicator_unselect);
        // 轮播切换时间
        lmBanners.setDurtion(SettingProperties.getGdbRecordNavAnimTime(this));
        if (SettingProperties.isGdbRecordNavAnimRandom(this)) {
            Random random = new Random();
            int type = Math.abs(random.nextInt()) % RecordFilterDialog.ANIM_TYPES.length;
            LMBannerViewUtil.setScrollAnim(lmBanners, type);
        } else {
            LMBannerViewUtil.setScrollAnim(lmBanners, SettingProperties.getGdbRecordNavAnimType(this));
        }

        HeadBannerAdapter adapter = new HeadBannerAdapter();
        lmBanners.setAdapter(adapter, pathList);
    }

    private void initFkDetails(RecordOneVOne record) {
        List<String> keyList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        if (record.getScoreFkType1() > 0) {
            keyList.add("For Sit");
            contentList.add(record.getScoreFkType1() + "");
        }
        if (record.getScoreFkType2() > 0) {
            keyList.add("Back Sit");
            contentList.add(record.getScoreFkType2() + " ");
        }
        if (record.getScoreFkType3() > 0) {
            keyList.add("For Stand");
            contentList.add(record.getScoreFkType3() + " ");
        }
        if (record.getScoreFkType4() > 0) {
            keyList.add("Back Stand");
            contentList.add(record.getScoreFkType4() + " ");
        }
        if (record.getScoreFkType5() > 0) {
            keyList.add("Side");
            contentList.add(record.getScoreFkType5() + " ");
        }
        if (record.getScoreFkType6() > 0) {
            keyList.add("Special");
            contentList.add(record.getScoreFkType6() + " ");
        }
        groupFk.addPoint(keyList, contentList);
    }

    private void initFkDetails(RecordThree record) {
        List<String> keyList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        if (record.getScoreFkType1() > 0) {
            keyList.add("For Sit");
            contentList.add(record.getScoreFkType1() + "");
        }
        if (record.getScoreFkType2() > 0) {
            keyList.add("Back Sit");
            contentList.add(record.getScoreFkType2() + " ");
        }
        if (record.getScoreFkType3() > 0) {
            keyList.add("For");
            contentList.add(record.getScoreFkType3() + " ");
        }
        if (record.getScoreFkType4() > 0) {
            keyList.add("Back");
            contentList.add(record.getScoreFkType4() + " ");
        }
        if (record.getScoreFkType5() > 0) {
            keyList.add("Side");
            contentList.add(record.getScoreFkType5() + " ");
        }
        if (record.getScoreFkType6() > 0) {
            keyList.add("Double");
            contentList.add(record.getScoreFkType6() + " ");
        }
        if (record.getScoreFkType7() > 0) {
            keyList.add("Sequence");
            contentList.add(record.getScoreFkType6() + " ");
        }
        if (record.getScoreFkType8() > 0) {
            keyList.add("Special");
            contentList.add(record.getScoreFkType6() + " ");
        }
        groupFk.addPoint(keyList, contentList);
    }

    @Override
    public void onStarLoaded(StarProxy star) {
        if (starImageViewMap.get(star.getStar().getId()) != null) {
            SImageLoader.getInstance().displayImage(star.getImagePath(), starImageViewMap.get(star.getStar().getId()));
        }
    }

    @OnClick({R.id.group_star1, R.id.group_star2, R.id.group_scene
        , R.id.group_3w_star1, R.id.group_3w_star2, R.id.group_3w_star3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_star1:
                ActivityManager.startStarActivity(RecordActivity.this, ((RecordOneVOne) record).getStar1());
                break;
            case R.id.group_star2:
                ActivityManager.startStarActivity(RecordActivity.this, ((RecordOneVOne) record).getStar2());
                break;
            case R.id.group_scene:
                break;
            case R.id.group_3w_star1:
                List<Star3W> starList = mPresenter.getStar3WList((RecordThree) record);
                if (starList != null) {
                    if (starList.size() > 0) {
                        ActivityManager.startStarActivity(RecordActivity.this, starList.get(0).getStar());
                    }
                }
                break;
            case R.id.group_3w_star2:
                starList = mPresenter.getStar3WList((RecordThree) record);
                if (starList != null) {
                    if (starList.size() > 1) {
                        ActivityManager.startStarActivity(RecordActivity.this, starList.get(1).getStar());
                    }
                }
                break;
            case R.id.group_3w_star3:
                starList = mPresenter.getStar3WList((RecordThree) record);
                if (starList != null) {
                    if (starList.size() > 2) {
                        ActivityManager.startStarActivity(RecordActivity.this, starList.get(2).getStar());
                    }
                }
                break;
        }
    }

    @OnClick(R.id.iv_play)
    public void onClickPlay() {
        VideoDialogFragment dialog = new VideoDialogFragment();
        dialog.setRecord(record);
        dialog.setVideoPath(videoPath);
        dialog.show(getSupportFragmentManager(), "VideoDialogFragment");
    }

    @OnClick(R.id.iv_setting)
    public void onClickSetting() {
        showSettingDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lmBanners != null) {
            lmBanners.stopImageTimerTask();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lmBanners != null) {
            lmBanners.startImageTimerTask();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lmBanners != null) {
            lmBanners.clearImageTimerTask();
        }
    }

    private void showSettingDialog() {
        if (bannerSettingDialog == null) {
            bannerSettingDialog = new BannerAnimDialogFragment();
            bannerSettingDialog.setOnAnimSettingListener(new BannerAnimDialogFragment.OnAnimSettingListener() {
                @Override
                public void onRandomAnim(boolean random) {
                    SettingProperties.setGdbRecordNavAnimRandom(RecordActivity.this, random);
                }

                @Override
                public boolean isRandomAnim() {
                    return SettingProperties.isGdbRecordNavAnimRandom(RecordActivity.this);
                }

                @Override
                public int getAnimType() {
                    return SettingProperties.getGdbRecordNavAnimType(RecordActivity.this);
                }

                @Override
                public void onSaveAnimType(int type) {
                    SettingProperties.setGdbRecordNavAnimType(RecordActivity.this, type);
                }

                @Override
                public int getAnimTime() {
                    return SettingProperties.getGdbRecordNavAnimTime(RecordActivity.this);
                }

                @Override
                public void onSaveAnimTime(int time) {
                    SettingProperties.setGdbRecordNavAnimTime(RecordActivity.this, time);
                }

                @Override
                public void onParamsSaved() {
                    initBanner(headPathList);
                }
            });
        }
        bannerSettingDialog.show(getSupportFragmentManager(), "BannerAnimDialogFragment");
    }

    private class HeadBannerAdapter implements LBaseAdapter<String> {

        @Override
        public View getView(LMBanners lBanners, Context context, int position, String path) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_gdb_star_list_banner, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_star);
            SImageLoader.getInstance().displayImage(path, imageView);
            return view;
        }
    }
}
