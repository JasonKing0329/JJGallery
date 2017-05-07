package com.jing.app.jjgallery.gdb.view.record;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.GdbPresenter;
import com.jing.app.jjgallery.gdb.view.star.IStarView;
import com.jing.app.jjgallery.model.pub.ObjectCache;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.Star;

/**
 * Created by JingYang on 2016/8/17 0017.
 * Description:
 */
public class RecordActivity extends BaseActivity implements IStarView {

    private RecordOneVOne record;
    private GdbPresenter mPresenter;

    private ImageView recordImageView;
    private ImageView star1ImageView, star2ImageView;
    private TextView star1View, star2View;
    private TextView pathView, sceneView, sceneScoreView, hdView, barebackView, bjobView, cumView, scoreView, scoreFeelView
            , rhythmView, foreplayView, storyView, specialView, rimView, cshowView, fkView, starView
            , starCView, scoreBasicView, scoreExtraView, specialContentView
            , fktype1View, fktype1RateView, fktype2View, fktype2RateView, fktype3View, fktype3RateView
            , fktype4View, fktype4RateView, fktype5View, fktype5RateView, fktype6View, fktype6RateView;
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
        record = (RecordOneVOne) ObjectCache.getData();
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.wall_bk5);
        mPresenter = new GdbPresenter();
        mPresenter.setViewCallback(this);
    }

    @Override
    public void initView() {
        recordImageView = (ImageView) findViewById(R.id.gdb_record_img);
        star1ImageView = (ImageView) findViewById(R.id.gdb_record_star1_img);
        star2ImageView = (ImageView) findViewById(R.id.gdb_record_star2_img);
        star1View = (TextView) findViewById(R.id.gdb_record_star1_content);
        star2View = (TextView) findViewById(R.id.gdb_record_star2_content);
        pathView = (TextView) findViewById(R.id.gdb_record_path);
        sceneView = (TextView) findViewById(R.id.gdb_record_scene);
        sceneScoreView = (TextView) findViewById(R.id.gdb_record_score_scene);
        bjobView = (TextView) findViewById(R.id.gdb_record_score_bjob);
        cumView = (TextView) findViewById(R.id.gdb_record_score_cum);
        hdView = (TextView) findViewById(R.id.gdb_record_hd);
        barebackView = (TextView) findViewById(R.id.gdb_record_bareback);
        rhythmView = (TextView) findViewById(R.id.gdb_record_score_rhythm);
        scoreView = (TextView) findViewById(R.id.gdb_record_score);
        scoreFeelView = (TextView) findViewById(R.id.gdb_record_score_feel);
        foreplayView = (TextView) findViewById(R.id.gdb_record_score_foreplay);
        storyView = (TextView) findViewById(R.id.gdb_record_score_story);
        rimView = (TextView) findViewById(R.id.gdb_record_score_rim);
        cshowView = (TextView) findViewById(R.id.gdb_record_score_cshow);
        specialView = (TextView) findViewById(R.id.gdb_record_score_special);
        specialContentView = (TextView) findViewById(R.id.gdb_record_score_specia_desc);
        fkView = (TextView) findViewById(R.id.gdb_record_score_fk);
        starView = (TextView) findViewById(R.id.gdb_record_score_star);
        starCView = (TextView) findViewById(R.id.gdb_record_score_starc);
        scoreBasicView = (TextView) findViewById(R.id.gdb_record_score_basic);
        scoreExtraView = (TextView) findViewById(R.id.gdb_record_score_extra);
        fktype1View = (TextView) findViewById(R.id.gdb_record_score_fk_type1);
        fktype1RateView = (TextView) findViewById(R.id.gdb_record_score_fk_type1_rate);
        fktype2View = (TextView) findViewById(R.id.gdb_record_score_fk_type2);
        fktype2RateView = (TextView) findViewById(R.id.gdb_record_score_fk_type2_rate);
        fktype3View = (TextView) findViewById(R.id.gdb_record_score_fk_type3);
        fktype3RateView = (TextView) findViewById(R.id.gdb_record_score_fk_type3_rate);
        fktype4View = (TextView) findViewById(R.id.gdb_record_score_fk_type4);
        fktype4RateView = (TextView) findViewById(R.id.gdb_record_score_fk_type4_rate);
        fktype5View = (TextView) findViewById(R.id.gdb_record_score_fk_type5);
        fktype5RateView = (TextView) findViewById(R.id.gdb_record_score_fk_type5_rate);
        fktype6View = (TextView) findViewById(R.id.gdb_record_score_fk_type6);
        fktype6RateView = (TextView) findViewById(R.id.gdb_record_score_fk_type6_rate);

        star1ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startStarActivity(RecordActivity.this, record.getStar1());
                finish();
            }
        });
        star2ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startStarActivity(RecordActivity.this, record.getStar2());
                finish();
            }
        });
    }

    @Override
    public void initBackgroundWork() {
        initValue();
        mPresenter.loadStar(record.getStar1().getId());
        mPresenter.loadStar(record.getStar2().getId());
    }

    private void initValue() {

        String path = Configuration.GDB_IMG_RECORD + "/" + record.getName() + EncryptUtil.getFileExtra();
        SImageLoader.getInstance().displayImage(path, recordImageView);

        Star star1 = record.getStar1();
        if (star1 == null) {
            star1View.setText(GDBProperites.STAR_UNKNOWN + "(" + record.getScoreStar1() + "/" + record.getScoreStarC1() + ")");
        } else {
            star1View.setText(record.getStar1().getName() + "(" + record.getScoreStar1() + "/" + record.getScoreStarC1() + ")");
        }
        Star star2 = record.getStar2();
        if (star2 == null) {
            star2View.setText(GDBProperites.STAR_UNKNOWN + "(" + record.getScoreStar2() + "/" + record.getScoreStarC2() + ")");
        }
        else {
            star2View.setText(record.getStar2().getName() + "(" + record.getScoreStar2() + "/" + record.getScoreStarC2() + ")");
        }
        pathView.setText(record.getDirectory() + "/" + record.getName());
        sceneView.setText(record.getSceneName());
        sceneScoreView.setText("" + record.getScoreScene());
        barebackView.setText("" + record.getScoreNoCond());
        hdView.setText("" + record.getHDLevel());
        scoreView.setText("" + record.getScore());
        scoreFeelView.setText("" + record.getScoreFeel());
        bjobView.setText("" + record.getScoreBJob());
        cumView.setText("" + record.getScoreCum());
        rhythmView.setText("" + record.getScoreRhythm());
        foreplayView.setText("" + record.getScoreForePlay());
        storyView.setText("" + record.getScoreStory());
        specialView.setText("" + record.getScoreSpeicial());
        specialContentView.setText(record.getSpecialDesc());
        rimView.setText("" + record.getScoreRim());
        cshowView.setText("" + record.getScoreCShow());
        fkView.setText("" + record.getScoreFk());
        starView.setText("" + record.getScoreStar());
        starCView.setText("" + record.getScoreStarC());
        scoreBasicView.setText("" + record.getScoreBasic());
        scoreExtraView.setText("" + record.getScoreExtra());
        fktype1View.setText("" + record.getScoreFkType1());
        fktype1RateView.setText("" + record.getRateFkType1());
        fktype2View.setText("" + record.getScoreFkType2());
        fktype2RateView.setText("" + record.getRateFkType2());
        fktype3View.setText("" + record.getScoreFkType3());
        fktype3RateView.setText("" + record.getRateFkType3());
        fktype4View.setText("" + record.getScoreFkType4());
        fktype4RateView.setText("" + record.getRateFkType4());
        fktype5View.setText("" + record.getScoreFkType5());
        fktype5RateView.setText("" + record.getRateFkType5());
        fktype6View.setText("" + record.getScoreFkType6());
        fktype6RateView.setText("" + record.getRateFkType6());
    }

    @Override
    public void onStarLoaded(StarProxy star) {
        if (record.getStar1() != null) {
            if (star.getStar().getId() == record.getStar1().getId()) {
                SImageLoader.getInstance().displayImage(star.getImagePath(), star1ImageView);
                return;
            }
        }
        if (record.getStar2() != null) {
            if (star.getStar().getId() == record.getStar2().getId()) {
                SImageLoader.getInstance().displayImage(star.getImagePath(), star2ImageView);
            }
        }
    }
}
