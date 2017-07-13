package com.jing.app.jjgallery.gdb.view.record;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.record.RecordPresenter;
import com.jing.app.jjgallery.model.pub.ObjectCache;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.publicview.PointDescLayout;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.Star;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JingYang on 2016/8/17 0017.
 * Description:
 */
public class RecordActivity extends BaseActivity implements IRecordView {

    @BindView(R.id.iv_record)
    ImageView ivRecord;
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

    private RecordOneVOne record;
    private RecordPresenter mPresenter;

    @Override
    public boolean isActionBarNeed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.gdb_record_1v1;
    }

    @Override
    public void initController() {
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.gdb_record_default);
        mPresenter = new RecordPresenter(this);
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        initValue();
    }

    @Override
    public void initBackgroundWork() {
        mPresenter.loadStar(record.getStar1().getId());
        mPresenter.loadStar(record.getStar2().getId());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initValue();
        initBackgroundWork();
    }

    private void initValue() {
        record = (RecordOneVOne) ObjectCache.getData();

        String path = Configuration.GDB_IMG_RECORD + "/" + record.getName() + EncryptUtil.getFileExtra();
        SImageLoader.getInstance().displayImage(path, ivRecord);

        Star star1 = record.getStar1();
        if (star1 == null) {
            tvStar1.setText(GDBProperites.STAR_UNKNOWN + "(" + record.getScoreStar1() + "/" + record.getScoreStarC1() + ")");
        } else {
            tvStar1.setText(record.getStar1().getName() + "(" + record.getScoreStar1() + "/" + record.getScoreStarC1() + ")");
        }
        Star star2 = record.getStar2();
        if (star2 == null) {
            tvStar2.setText(GDBProperites.STAR_UNKNOWN + "(" + record.getScoreStar2() + "/" + record.getScoreStarC2() + ")");
        } else {
            tvStar2.setText(record.getStar2().getName() + "(" + record.getScoreStar2() + "/" + record.getScoreStarC2() + ")");
        }
        tvPath.setText(record.getDirectory() + "/" + record.getName());
        tvScene.setText(record.getSceneName());
        tvSceneScore.setText("" + record.getScoreScene());
        if (record.getScoreNoCond() > 0) {
            groupBareback.setVisibility(View.VISIBLE);
        }
        else {
            groupBareback.setVisibility(View.GONE);
        }
        tvHd.setText("" + record.getHDLevel());
        tvScoreTotal.setText("" + record.getScore());
        tvFeel.setText("" + record.getScoreFeel());
        tvBjob.setText("" + record.getScoreBJob());
        tvCum.setText("" + record.getScoreCum());
        tvRhythm.setText("" + record.getScoreRhythm());
        tvForeplay.setText("" + record.getScoreForePlay());
        tvStory.setText("" + record.getScoreStory());
        tvSpecial.setText("" + record.getScoreSpeicial());
        if (TextUtils.isEmpty(record.getSpecialDesc())) {
            groupSpecial.setVisibility(View.GONE);
        }
        else {
            groupSpecial.setVisibility(View.VISIBLE);
            tvSpecialContent.setText(record.getSpecialDesc());
        }
        tvRim.setText("" + record.getScoreRim());
        tvCshow.setText("" + record.getScoreCShow());
        tvFk.setText("FK(" + record.getScoreFk() + ")");
        tvStar.setText("" + record.getScoreStar());
        tvStarC.setText("" + record.getScoreStarC());
        tvBasic.setText("" + record.getScoreBasic());
        tvExtra.setText("" + record.getScoreExtra());

        tvDeprecated.setVisibility(record.getDeprecated() == 1 ? View.VISIBLE:View.GONE);

        initFkDetails();
    }

    private void initFkDetails() {
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

    @Override
    public void onStarLoaded(StarProxy star) {
        if (record.getStar1() != null) {
            if (star.getStar().getId() == record.getStar1().getId()) {
                SImageLoader.getInstance().displayImage(star.getImagePath(), ivStar1);
                return;
            }
        }
        if (record.getStar2() != null) {
            if (star.getStar().getId() == record.getStar2().getId()) {
                SImageLoader.getInstance().displayImage(star.getImagePath(), ivStar2);
            }
        }
    }

    @OnClick({R.id.group_star1, R.id.group_star2, R.id.group_scene})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_star1:
                ActivityManager.startStarActivity(RecordActivity.this, record.getStar1());
                break;
            case R.id.group_star2:
                ActivityManager.startStarActivity(RecordActivity.this, record.getStar2());
                break;
            case R.id.group_scene:
                break;
        }
    }
}
