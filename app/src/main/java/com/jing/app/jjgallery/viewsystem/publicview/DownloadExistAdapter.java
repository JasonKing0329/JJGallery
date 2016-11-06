package com.jing.app.jjgallery.viewsystem.publicview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.util.FileSizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class DownloadExistAdapter extends RecyclerView.Adapter<DownloadExistAdapter.ItemHolder> {

    private List<DownloadItem> itemList;
    private SparseBooleanArray checkMap;

    public DownloadExistAdapter(List<DownloadItem> list) {
        itemList = list;
        checkMap = new SparseBooleanArray();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_download_exist_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0:itemList.size();
    }

    public List<DownloadItem> getCheckedItems() {
        List<DownloadItem> list = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i ++) {
            if (checkMap.get(i)) {
                list.add(itemList.get(i));
            }
        }
        return list;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewGroup group;
        private TextView name;
        private TextView size;
        private CheckBox check;
        private int position;

        public ItemHolder(View view) {
            super(view);
            group = (ViewGroup) view.findViewById(R.id.download_exist_layout);
            name = (TextView) view.findViewById(R.id.download_exist_name);
            size = (TextView) view.findViewById(R.id.download_exist_size);
            check = (CheckBox) view.findViewById(R.id.download_exist_check);
        }

        public void bind(int position) {
            this.position = position;
            group.setOnClickListener(this);
            name.setText(itemList.get(position).getName());
            size.setText(FileSizeUtil.convertFileSize(itemList.get(position).getSize()));
        }

        @Override
        public void onClick(View v) {
            if (checkMap.get(position)) {
                checkMap.put(position, false);
                check.setChecked(false);
            }
            else {
                checkMap.put(position, true);
                check.setChecked(true);
            }
        }
    }
}
