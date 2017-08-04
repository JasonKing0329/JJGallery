package com.jing.app.jjgallery.gdb.presenter.star;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.model.GdbImageProvider;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.king.service.gdb.bean.Record;

import java.util.List;
import java.util.Random;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/4 14:04
 */
public class SwipeAdapter extends BaseAdapter {

    private List<StarProxy> list;
    private Random random;

    public SwipeAdapter() {
        random = new Random();
    }

    public void setList(List<StarProxy> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_star_card, parent, false);
            holder = new ViewHolder();
            holder.ivRecord = (ImageView) convertView.findViewById(R.id.iv_record);
            holder.ivStar = (ImageView) convertView.findViewById(R.id.iv_star);
            holder.tvStarName = (TextView) convertView.findViewById(R.id.tv_star_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        StarProxy star = list.get(position);
        holder.tvStarName.setText(star.getStar().getName());
        SImageLoader.getInstance().displayImage(star.getImagePath(), holder.ivStar);
        
        List<Record> list = star.getStar().getRecordList();
        if (list != null && list.size() > 0) {
            int index = Math.abs(random.nextInt()) % list.size();
            String path = GdbImageProvider.getRecordRandomPath(list.get(index).getName(), null);
            SImageLoader.getInstance().displayImage(path, holder.ivRecord);
        }
        
        return convertView;
    }

    private class ViewHolder {
        ImageView ivRecord;
        ImageView ivStar;
        TextView tvStarName;
    }
}
