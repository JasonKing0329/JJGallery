package com.jing.app.jjgallery.gdb.view.recommend;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.recommend.FilterModel;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class RecordFilterDialog extends CustomDialog {

    private RecyclerView recyclerView;
    private FilterModel mFilterModel;
    private FilterAdapter mAdapter;

    public RecordFilterDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        requestSaveAction(true);
        requestCancelAction(true);
        applyLightThemeStyle();
        setTitle(getContext().getString(R.string.gdb_rec_filter_title));

        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        mFilterModel = (FilterModel) map.get("model");
        mAdapter = new FilterAdapter(mFilterModel.getList());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected View getCustomView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.gdb_recommend_filter, null);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recommend_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        return contentView;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == saveIcon) {
            actionListener.onSave(mFilterModel);
        }
        super.onClick(view);
    }
}
