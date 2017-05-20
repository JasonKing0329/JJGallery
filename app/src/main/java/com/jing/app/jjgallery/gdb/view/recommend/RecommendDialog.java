package com.jing.app.jjgallery.gdb.view.recommend;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.recommend.FilterModel;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.ScreenUtils;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.gdb.presenter.recommend.FilterPresenter;
import com.jing.app.jjgallery.gdb.presenter.GdbGuidePresenter;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public class RecommendDialog extends Dialog implements IRecommend, View.OnClickListener {

    private WindowManager.LayoutParams windowParams;
    private ImageView imageView;
    private TextView starView;
    private ProgressBar progressBar;

    private GdbGuidePresenter gdbGuidePresenter;
    private FilterPresenter filterPresenter;

    /**
     * 过滤器对话框
     */
    private RecordFilterDialog filterDialog;

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
        starView = (TextView) findViewById(R.id.gdb_recommend_star);
        progressBar = (ProgressBar) findViewById(R.id.gdb_recommend_progress);
        findViewById(R.id.gdb_recommend_previous).setOnClickListener(this);
        findViewById(R.id.gdb_recommend_next).setOnClickListener(this);
        findViewById(R.id.gdb_recommend_setting).setOnClickListener(this);
        findViewById(R.id.gdb_recommend_click_group).setOnClickListener(this);

        gdbGuidePresenter = new GdbGuidePresenter(this);
        filterPresenter = new FilterPresenter();
        // 设置过滤器
        gdbGuidePresenter.setFilterModel(filterPresenter.getFilters(getContext()));
        // 加载所有记录，通过onRecordRecommand回调
        gdbGuidePresenter.initialize();
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
        gdbGuidePresenter.recommendNext();
    }

    @Override
    public void onRecordRecommand(Record record) {
        progressBar.setVisibility(View.GONE);

        if (record == null) {
            Toast.makeText(getContext(), R.string.gdb_rec_no_match, Toast.LENGTH_LONG).show();
            return;
        }
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
        SImageLoader.getInstance().displayImage(gdbGuidePresenter.getRecordPath(record.getName()), imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gdb_recommend_next:
                gdbGuidePresenter.recommendNext();
                break;
            case R.id.gdb_recommend_previous:
                gdbGuidePresenter.recommendPrevious();
                break;
            case R.id.gdb_recommend_setting:
                if (filterDialog == null) {
                    filterDialog = new RecordFilterDialog(getContext(), new CustomDialog.OnCustomDialogActionListener() {
                        @Override
                        public boolean onSave(Object object) {
                            FilterModel model = (FilterModel) object;
                            filterPresenter.saveFilters(getContext(), model);
                            gdbGuidePresenter.setFilterModel(model);
                            gdbGuidePresenter.recommendNext();
                            return true;
                        }

                        @Override
                        public boolean onCancel() {
                            return false;
                        }

                        @Override
                        public void onLoadData(HashMap<String, Object> data) {
                            data.put("model", filterPresenter.getFilters(getContext()));
                        }
                    });
                }
                filterDialog.show();
                break;
            case R.id.gdb_recommend_click_group:
                ActivityManager.startGdbRecordActivity(getContext(), gdbGuidePresenter.getCurrentRecord());
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
