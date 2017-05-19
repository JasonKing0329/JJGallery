package com.jing.app.jjgallery.gdb.view.recommend;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseFragment;
import com.jing.app.jjgallery.gdb.bean.recommend.FilterModel;
import com.jing.app.jjgallery.gdb.presenter.recommend.FilterPresenter;
import com.jing.app.jjgallery.gdb.presenter.GdbGuidePresenter;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26 0026.
 */

public class RecommendFragment extends GBaseFragment implements IRecommend, View.OnClickListener {

    private ImageView imageView;
    private TextView nameView;
    private TextView starView;
    private TextView scoreView;
    private ProgressBar progressBar;

    private GdbGuidePresenter gdbGuidePresenter;
    private IRecommendHolder recommendHolder;
    private FilterPresenter filterPresenter;

    /**
     * 过滤器对话框
     */
    private RecordFilterDialog filterDialog;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        recommendHolder = (IRecommendHolder) holder;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_gdb_recommand;
    }

    @Override
    protected void initView(View view) {
        imageView = (ImageView) view.findViewById(R.id.gdb_recommend_image);
        nameView = (TextView) view.findViewById(R.id.gdb_recommend_name);
        starView = (TextView) view.findViewById(R.id.gdb_recommend_star);
        scoreView = (TextView) view.findViewById(R.id.gdb_recommend_score);
        progressBar = (ProgressBar) view.findViewById(R.id.gdb_recommend_progress);
        if (!DisplayHelper.isTabModel(getActivity())) {
            nameView.setMaxLines(1);
            nameView.setEllipsize(TextUtils.TruncateAt.END);
        }
        view.findViewById(R.id.gdb_recommend_previous).setOnClickListener(this);
        view.findViewById(R.id.gdb_recommend_next).setOnClickListener(this);
        view.findViewById(R.id.gdb_recommend_setting).setOnClickListener(this);
        view.findViewById(R.id.gdb_recommend_click_group).setOnClickListener(this);

        gdbGuidePresenter = recommendHolder.getPresenter();
        gdbGuidePresenter.setRecommendView(this);

        filterPresenter = new FilterPresenter();
        // 设置过滤器
        gdbGuidePresenter.setFilterModel(filterPresenter.getFilters(getContext()));
        // 加载所有记录，通过onRecordRecommand回调
        gdbGuidePresenter.initialize();
    }

    @Override
    public void onRecordsLoaded(List<Record> list) {
        recommendHolder.onRecommendRecordsLoaded();
        gdbGuidePresenter.recommendNext();
    }

    @Override
    public void onRecordRecommand(Record record) {
        progressBar.setVisibility(View.GONE);

        if (record == null) {
            Toast.makeText(getContext(), R.string.gdb_rec_no_match, Toast.LENGTH_LONG).show();
            return;
        }
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
}
