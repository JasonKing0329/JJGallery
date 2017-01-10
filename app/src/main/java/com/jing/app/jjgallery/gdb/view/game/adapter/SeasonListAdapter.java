package com.jing.app.jjgallery.gdb.view.game.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.pubview.FoldableLayout;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class SeasonListAdapter extends RecyclerView.Adapter<SeasonListAdapter.SeasonListHolder> {

    private List<SeasonBean> seasonList;
    private FolderItemManager.FolderItemListener folderItemListener;


    public SeasonListAdapter(List<SeasonBean> seasonList) {
        this.seasonList = seasonList;
    }

    public void setFolderItemListener(FolderItemManager.FolderItemListener folderItemListener) {
        this.folderItemListener = folderItemListener;
    }

    public void setSeasonList(List<SeasonBean> seasonList) {
        this.seasonList = seasonList;
    }

    @Override
    public SeasonListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SeasonListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_season_item, parent, false)
                        , folderItemListener);
    }

    @Override
    public void onBindViewHolder(SeasonListHolder holder, int position) {
        holder.itemManager.bindViewData(seasonList.get(position));
    }

    @Override
    public int getItemCount() {
        return seasonList == null ? 0:seasonList.size();
    }

    public static class SeasonListHolder extends RecyclerView.ViewHolder {

        FoldableLayout foldableLayout;
        FolderItemManager itemManager;

        public SeasonListHolder(View itemView, FolderItemManager.FolderItemListener folderItemListener) {
            super(itemView);
            itemManager = new FolderItemManager();
            itemManager.setFolderItemListener(folderItemListener);
            foldableLayout = (FoldableLayout) itemView.findViewById(R.id.season_item_layout);
            itemManager.initFolderView(itemView.getContext(), foldableLayout);
        }
    }
}
