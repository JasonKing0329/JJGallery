package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.RecordProxy;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.RecordSingleScene;
import com.king.service.gdb.bean.Star;

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
    private TextView scoreView;
    private TextView fkView;
    private TextView cumView;
    private TextView bjobView;
    private TextView star1View;
    private TextView star2View;

    private int nameColorNormal, nameColorBareback;
    private View.OnClickListener onClickListener;

    public void setParameters(int nameColorNormal, int nameColorBareback, View.OnClickListener onClickListener) {
        this.nameColorNormal = nameColorNormal;
        this.nameColorBareback = nameColorBareback;
        this.onClickListener = onClickListener;
    }

    public void initView(View view) {
        container = view.findViewById(R.id.record_container);
        imageView = (ImageView) view.findViewById(R.id.record_thumb);
        seqView = (TextView) view.findViewById(R.id.record_seq);
        nameView = (TextView) view.findViewById(R.id.record_name);
        scoreView = (TextView) view.findViewById(R.id.record_score);
        fkView = (TextView) view.findViewById(R.id.record_score_fk);
        cumView = (TextView) view.findViewById(R.id.record_score_cum);
        bjobView = (TextView) view.findViewById(R.id.record_score_bjob);
        star1View = (TextView) view.findViewById(R.id.record_star1);
        star2View = (TextView) view.findViewById(R.id.record_star2);
    }

    public void bind(RecordProxy item) {
        bind(item.getRecord(), item.getPositionInHeader());
    }

    public void bind(Record item, int position) {
        container.setTag(item);
        container.setOnClickListener(onClickListener);

        String path = GdbPresenter.getRecordPath(item.getName());
        if (path == null) {
            imageView.setVisibility(View.GONE);
        }
        else {
            imageView.setVisibility(View.VISIBLE);
            SImageLoader.getInstance().displayImage(path, imageView);
        }

        seqView.setText("" + (position + 1));
        nameView.setText(item.getName());
        scoreView.setText("" + item.getScore());
        if (item instanceof RecordSingleScene) {
            RecordSingleScene record = (RecordSingleScene) item;
            fkView.setText("fk(" + record.getScoreFk() + ")");
            cumView.setText("cum(" + record.getScoreCum() + ")");
            bjobView.setText("bjob(" + record.getScoreBJob() + ")");

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
            Star star1 = record.getStar1();
            if (star1 == null) {
                star1View.setText(GDBProperites.STAR_UNKNOWN);
            }
            else {
                star1View.setText(star1.getName() + "(" + record.getScoreStar1() + "/" + record.getScoreStarC1() + ")");
            }
            Star star2 = record.getStar2();
            if (star2 == null) {
                star2View.setText(GDBProperites.STAR_UNKNOWN);
            }
            else {
                star2View.setText(star2.getName() + "(" + record.getScoreStar2() + "/" + record.getScoreStarC2() + ")");
            }
        }
    }
}
