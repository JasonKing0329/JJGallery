package com.jing.app.jjgallery.viewsystem.publicview.download;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.DownloadItemProxy;
import com.jing.app.jjgallery.util.FileSizeUtil;
import com.jing.app.jjgallery.viewsystem.publicview.NumberProgressBar;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ItemHolder> {

    private List<DownloadItemProxy> itemList;

    public DownloadAdapter(List<DownloadItemProxy> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_download_list, parent, false);
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

    class ItemHolder extends RecyclerView.ViewHolder implements Handler.Callback
    {

        private TextView name;
        private TextView size;
        private NumberProgressBar progressBar;

        public ItemHolder(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.download_item_name);
            size = (TextView) view.findViewById(R.id.download_item_size);
            progressBar = (NumberProgressBar) view.findViewById(R.id.download_item_progressbar);
        }

        public void bind(int position) {
            if (itemList.get(position).getItem().getKey() != null) {
                name.setText(itemList.get(position).getItem().getKey() + "/" + itemList.get(position).getItem().getName());
            }
            else {
                name.setText(itemList.get(position).getItem().getName());
            }
            size.setText(FileSizeUtil.convertFileSize(itemList.get(position).getItem().getSize()));
            progressBar.setProgress(itemList.get(position).getProgress());
        }

        @Override
        public boolean handleMessage(Message msg) {
            progressBar.setProgress(msg.what);
            return false;
        }
    }


}
