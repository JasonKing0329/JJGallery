package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.RecordProxy;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.RecordSingleScene;
import com.king.service.gdb.bean.Star;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 景阳 on 2016/8/6 0006.
 * 为了实现RecordList/StarRecords/RecordScene里面holderUI和数据的共享，把view的初始化和赋值抽出来作为共用体
 * RecordList/StarRecords直接是适配于RecyclerView的adapter，直接使用RecyclerView.Holder(RecordHolder)
 * RecordScene所使用的adapter被其框架封装了一层，不能直接使用RecyclerView.Holder，所以要抽出能共用的部分
 */
public class RecordViewHolder {
    private View container;
    private ImageView imageView;
    private TextView seqView;
    private TextView nameView;
    private TextView dirView;
    private TextView scoreView;
    private TextView sortScoreView;
    private TextView sceneView;
    private TextView fkView;
    private TextView cumView;
    private TextView bjobView;
    private TextView fkSubView;
    private TextView star1View;
    private TextView star2View;
    private TextView scoreBasicView;
    private TextView scoreExtraView;
    private TextView actorView;

    private int nameColorNormal, nameColorBareback;
    private View.OnClickListener onClickListener;

    public void setParameters(int nameColorNormal, int nameColorBareback, View.OnClickListener onClickListener) {
        this.nameColorNormal = nameColorNormal;
        this.nameColorBareback = nameColorBareback;
        this.onClickListener = onClickListener;

        sortScoreView.setTextColor(nameColorBareback);
    }

    public void initView(View view) {
        container = view.findViewById(R.id.record_container);
        imageView = (ImageView) view.findViewById(R.id.record_thumb);
        seqView = (TextView) view.findViewById(R.id.record_seq);
        nameView = (TextView) view.findViewById(R.id.record_name);
        dirView = (TextView) view.findViewById(R.id.record_dir);
        scoreView = (TextView) view.findViewById(R.id.record_score);
        sortScoreView = (TextView) view.findViewById(R.id.record_score_sort);
        sceneView = (TextView) view.findViewById(R.id.record_scene);
        fkView = (TextView) view.findViewById(R.id.record_score_fk);
        cumView = (TextView) view.findViewById(R.id.record_score_cum);
        bjobView = (TextView) view.findViewById(R.id.record_score_bjob);
        fkSubView = (TextView) view.findViewById(R.id.record_score_fk_sub);
        star1View = (TextView) view.findViewById(R.id.record_star1);
        star2View = (TextView) view.findViewById(R.id.record_star2);
        scoreBasicView = (TextView) view.findViewById(R.id.record_score_basic);
        scoreExtraView = (TextView) view.findViewById(R.id.record_score_extra);
        actorView = (TextView) view.findViewById(R.id.record_score_actor);

        if (!DisplayHelper.isTabModel(view.getContext())) {
            view.findViewById(R.id.record_show_tablet).setVisibility(View.GONE);
        }
    }

    public void bind(RecordProxy item, int sortMode) {
        bind(item.getRecord(), item.getPositionInHeader(), sortMode);
    }

    public void bind(Record item, int position, int sortMode) {
        container.setTag(item);
        container.setOnClickListener(onClickListener);

        // image
        String path = GdbPresenter.getRecordPath(item.getName());
        if (path == null) {
            imageView.setVisibility(View.GONE);
        }
        else {
            imageView.setVisibility(View.VISIBLE);
            SImageLoader.getInstance().displayImage(path, imageView);
        }

        seqView.setText("" + "" + (position + 1));
        nameView.setText("" + item.getName());
        dirView.setText("" + item.getDirectory());
        scoreView.setText("" + "" + item.getScore());
        scoreBasicView.setText("basic(" + item.getScoreBasic() + ")");
        scoreExtraView.setText("extra(" + item.getScoreExtra() + ")");
        if (item instanceof RecordSingleScene) {
            RecordSingleScene record = (RecordSingleScene) item;
            sceneView.setText("" + record.getSceneName() + "(" + record.getScoreScene() + ")");
            fkView.setText("" + "fk(" + record.getScoreFk() + ")");
            cumView.setText("" + "cum(" + record.getScoreCum() + ")");
            bjobView.setText("" + "bjob(" + record.getScoreBJob() + ")");

            if (record.getScoreNoCond() == GDBProperites.BAREBACK) {
                nameView.setTextColor(nameColorBareback);
            }
            else {
                nameView.setTextColor(nameColorNormal);
            }
        }
        else {
            nameView.setTextColor(nameColorNormal);
        }
        if (item instanceof RecordOneVOne) {
            RecordOneVOne record = (RecordOneVOne) item;

            actorView.setText("star(" + record.getScoreStar() + "/" + record.getScoreStarC() + ")");

            Star star1 = record.getStar1();
            if (star1 == null) {
                star1View.setText("" + GDBProperites.STAR_UNKNOWN);
            }
            else {
                star1View.setText("" + star1.getName() + "(" + record.getScoreStar1() + "/" + record.getScoreStarC1() + ")");
            }
            Star star2 = record.getStar2();
            if (star2 == null) {
                star2View.setText("" + GDBProperites.STAR_UNKNOWN);
            }
            else {
                star2View.setText("" + star2.getName() + "(" + record.getScoreStar2() + "/" + record.getScoreStarC2() + ")");
            }

            StringBuffer buffer = new StringBuffer();
            if (record.getScoreFkType1() != 0) {
                buffer.append("坐面(").append(record.getScoreFkType1()).append("/").append(record.getRateFkType1()).append("%)   ");
            }
            if (record.getScoreFkType2() != 0) {
                buffer.append("坐背(").append(record.getScoreFkType2()).append("/").append(record.getRateFkType2()).append("%)   ");
            }
            if (record.getScoreFkType3() != 0) {
                buffer.append("立面(").append(record.getScoreFkType3()).append("/").append(record.getRateFkType3()).append("%)   ");
            }
            if (record.getScoreFkType4() != 0) {
                buffer.append("立背(").append(record.getScoreFkType4()).append("/").append(record.getRateFkType4()).append("%)   ");
            }
            if (record.getScoreFkType5() != 0) {
                buffer.append("侧(").append(record.getScoreFkType5()).append("/").append(record.getRateFkType5()).append("%)   ");
            }
            if (record.getScoreFkType6() != 0) {
                buffer.append("特殊(").append(record.getScoreFkType6()).append("/").append(record.getRateFkType6()).append("%)   ");
            }
            String text = buffer.toString();
            if (text.length() > 0) {
                fkSubView.setText("" + text.substring(0, text.length() - 3));
            }
        }

        showSortScore(item, sortMode);
    }

    private void showSortScore(Record item, int sortMode) {
        switch (sortMode) {
            case PreferenceValue.GDB_SR_ORDERBY_DATE:
                sortScoreView.setVisibility(View.VISIBLE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(new Date(item.getLastModifyTime()));
                sortScoreView.setText(date);
                break;
            case PreferenceValue.GDB_SR_ORDERBY_BAREBACK:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreNoCond());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_BJOB:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreBJob());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_CSHOW:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreCShow());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_CUM:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreCum());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreFk());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK1:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText(((RecordOneVOne) item).getRateFkType1() + "%");
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK2:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText(((RecordOneVOne) item).getRateFkType2() + "%");
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK3:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText(((RecordOneVOne) item).getRateFkType3() + "%");
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK4:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText(((RecordOneVOne) item).getRateFkType4() + "%");
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK5:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText(((RecordOneVOne) item).getRateFkType5() + "%");
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FK6:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText(((RecordOneVOne) item).getRateFkType6() + "%");
                break;
            case PreferenceValue.GDB_SR_ORDERBY_FOREPLAY:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreForePlay());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_HD:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + item.getHDLevel());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_RHYTHM:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreRhythm());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_RIM:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreRim());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SCENE:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreScene());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SCOREFEEL:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + item.getScoreFeel());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SPECIAL:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordSingleScene) item).getScoreSpeicial());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STAR1:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordOneVOne) item).getScoreStar1());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STAR2:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordOneVOne) item).getScoreStar2());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STARCC1:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordOneVOne) item).getScoreStarC1());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STARCC2:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordOneVOne) item).getScoreStarC2());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STORY:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + item.getScoreStory());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SCORE_BASIC:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + item.getScoreBasic());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_SCORE_EXTRA:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + item.getScoreExtra());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STAR:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordOneVOne) item).getScoreStar());
                break;
            case PreferenceValue.GDB_SR_ORDERBY_STARC:
                sortScoreView.setVisibility(View.VISIBLE);
                sortScoreView.setText("" + ((RecordOneVOne) item).getScoreStarC());
                break;
            default:
                sortScoreView.setVisibility(View.GONE);
                break;
        }

        if (sortMode == PreferenceValue.GDB_SR_ORDERBY_SCORE) {
            scoreView.setTextColor(nameColorBareback);
        }
        else {
            scoreView.setTextColor(nameColorNormal);
        }
    }
}
