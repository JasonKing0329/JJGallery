package com.jing.app.jjgallery.gdb.view.surf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.view.adapter.RecordHolder;
import com.jing.app.jjgallery.http.bean.data.FileBean;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/27 11:26
 */
public class SurfAdapter extends RecyclerView.Adapter {

    private final int TYPE_FOLDER = 1;

    private final int TYPE_FILE = 2;

    private List<FileBean> list;

    private OnSurfItemActionListener onSurfItemActionListener;

    public SurfAdapter(List<FileBean> list) {
        this.list = list;
    }

    public void setList(List<FileBean> list) {
        this.list = list;
    }

    public void setOnSurfItemActionListener(OnSurfItemActionListener onSurfItemActionListener) {
        this.onSurfItemActionListener = onSurfItemActionListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isFolder()) {
            return TYPE_FOLDER;
        }
        else {
            return TYPE_FILE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOLDER) {
            return new FolderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_surf_folder, parent, false));
        }
        else {
            RecordHolder holder = new RecordHolder(parent);
            holder.setParameters(parent.getContext().getResources().getColor(R.color.gdb_record_text_normal_light)
                    , parent.getContext().getResources().getColor(R.color.gdb_record_text_bareback_light), fileListener);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FolderHolder) {
            FolderHolder fHolder = (FolderHolder) holder;
            fHolder.tvName.setText(list.get(position).getName());
            fHolder.groupFolder.setTag(list.get(position));
            fHolder.groupFolder.setOnClickListener(folderListener);
        }
        else {
            RecordHolder rHolder = (RecordHolder) holder;
            Record record = new RecordOneVOne();
            record.setName(list.get(position).getName());
            rHolder.bind(record, position);
        }
    }

    View.OnClickListener fileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onSurfItemActionListener != null) {
                Record record = (Record) v.getTag();
                onSurfItemActionListener.onClickSurfRecord(record);
            }
        }
    };

    View.OnClickListener folderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onSurfItemActionListener != null) {
                FileBean bean = (FileBean) v.getTag();
                onSurfItemActionListener.onClickSurfFolder(bean);
            }
        }
    };

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public static class FolderHolder extends RecyclerView.ViewHolder {

        LinearLayout groupFolder;
        TextView tvName;

        public FolderHolder(View itemView) {
            super(itemView);

            groupFolder = (LinearLayout) itemView.findViewById(R.id.group_folder);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public interface OnSurfItemActionListener {
        void onClickSurfFolder(FileBean fileBean);
        void onClickSurfRecord(Record record);
    }
}
