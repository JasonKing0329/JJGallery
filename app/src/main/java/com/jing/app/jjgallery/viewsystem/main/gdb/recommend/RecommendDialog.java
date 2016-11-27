package com.jing.app.jjgallery.viewsystem.main.gdb.recommend;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.ScreenUtils;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;

import java.util.List;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public class RecommendDialog extends Dialog implements IRecommend, View.OnClickListener {

    private WindowManager.LayoutParams windowParams;
    private ImageView imageView;
    private TextView nameView;
    private TextView starView;
    private TextView scoreView;
    private ProgressBar progressBar;

    private RecommendPresenter recommendPresenter;
    private Point startPoint, touchPoint;

    private class Point {
        float x;
        float y;
    }

    public RecommendDialog(Context context) {
        super(context, R.style.GdbRecommendDialog);
        setContentView(R.layout.dialog_gdb_recommand);

        windowParams = getWindow().getAttributes();
        int width = ScreenUtils.getScreenWidth(context);
        width = width * 9 / 10;
        setWidth(width);
        setHeight(width * 9 / 16);
        touchPoint = new Point();
        startPoint = new Point();

        imageView = (ImageView) findViewById(R.id.gdb_recommend_image);
        nameView = (TextView) findViewById(R.id.gdb_recommend_name);
        starView = (TextView) findViewById(R.id.gdb_recommend_star);
        scoreView = (TextView) findViewById(R.id.gdb_recommend_score);
        progressBar = (ProgressBar) findViewById(R.id.gdb_recommend_progress);
        findViewById(R.id.gdb_recommend_previous).setOnClickListener(this);
        findViewById(R.id.gdb_recommend_next).setOnClickListener(this);
        findViewById(R.id.gdb_recommend_setting).setOnClickListener(this);
        findViewById(R.id.gdb_recommend_click_group).setOnClickListener(this);

        recommendPresenter = new RecommendPresenter(this);
        recommendPresenter.initialize();
    }

    public void setWidth(int w) {
        windowParams.width = w;
        getWindow().setAttributes(windowParams);
    }

    public void setHeight(int h) {
        windowParams.height = h;
        getWindow().setAttributes(windowParams);
    }

    private void move(int x, int y) {

        windowParams.x += x;
        windowParams.y += y;
        getWindow().setAttributes(windowParams);//must have
    }

    @Override
    public void onRecordsLoaded(List<Record> list) {
        recommendPresenter.recommendNext();
    }

    @Override
    public void onRecordRecommand(Record record) {
        progressBar.setVisibility(View.GONE);
        nameView.setText(record.getDirectory() + "/" + record.getName());
        StringBuffer buffer = new StringBuffer();
        if (record instanceof RecordOneVOne) {
            RecordOneVOne oRecord = (RecordOneVOne) record;
            if (oRecord.getStar1() == null) {
                buffer.append(GDBProperites.STAR_UNKNOWN);
            }
            else {
                buffer.append(oRecord.getStar1().getName());
            }
            buffer.append(" & ");
            if (oRecord.getStar2() == null) {
                buffer.append(GDBProperites.STAR_UNKNOWN);
            }
            else {
                buffer.append(oRecord.getStar2().getName());
            }
        }
        starView.setText(buffer.toString());
        scoreView.setText("" + record.getScore());
        SImageLoader.getInstance().displayImage(recommendPresenter.getRecordPath(record.getName()), imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gdb_recommend_next:
                recommendPresenter.recommendNext();
                break;
            case R.id.gdb_recommend_previous:
                recommendPresenter.recommendPrevious();
                break;
            case R.id.gdb_recommend_setting:
                break;
            case R.id.gdb_recommend_click_group:
                ActivityManager.startGdbRecordActivity(getContext(), recommendPresenter.getCurrentRecord());
                break;
        }
    }

    @Override
    /**
     * notice: getRawX/Y是相对屏幕的坐标，getX/Y是相对控件的
     * 要实现拖动效果，只能用getRawX/Y，用getX/Y会出现拖动不流畅并且抖动的效果
     * (from internet: getX getY获取的是相对于child 左上角点的 x y 当第一次获取的时候通过layout设置了child一个新的位置 马上 再次获取x y时就会变了 变成了 新的x y
     * 然后马上layout 然后又获取了新的x y又。。。。所以会看到 一个view不断地在屏幕上闪来闪去)
     */
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                float x = event.getRawX();//
                float y = event.getRawY();
                startPoint.x = x;
                startPoint.y = y;
                Log.d("RecommendDialog", "ACTION_DOWN x=" + x + ", y=" + y);
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getRawX();
                y = event.getRawY();
                touchPoint.x = x;
                touchPoint.y = y;
                float dx = touchPoint.x - startPoint.x;
                float dy = touchPoint.y - startPoint.y;

                move((int)dx, (int)dy);

                startPoint.x = x;
                startPoint.y = y;
                break;
            case MotionEvent.ACTION_UP:
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

}
