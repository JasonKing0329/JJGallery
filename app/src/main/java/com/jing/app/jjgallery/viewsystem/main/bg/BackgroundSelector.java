package com.jing.app.jjgallery.viewsystem.main.bg;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class BackgroundSelector extends CustomDialog {

    private RecyclerView recyclerView;
    private BackgroundManager manager;

    private BackgroundAdapter mAdapter;
    private String imagePath;

    public BackgroundSelector(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        requestCancelAction(true);
        requestOkAction(true);
        applyThemeStyle();
        setTitle(R.string.bg_selector_title);

        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        imagePath = (String) map.get("imagePath");
    }

    @Override
    protected View getCustomView() {
        manager = BackgroundManager.getInstance();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bg_selector, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.dlg_bg_selector_recyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new BackgroundAdapter(getContext(), manager.getItemList());
        recyclerView.setAdapter(mAdapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelSize(R.dimen.dlg_loadfrom_list_height)
        );
        view.setLayoutParams(params);

        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == saveIcon) {
            manager.setBackground(getContext(), mAdapter.getSelectedKey(), imagePath);
        }
        super.onClick(view);
    }
}
