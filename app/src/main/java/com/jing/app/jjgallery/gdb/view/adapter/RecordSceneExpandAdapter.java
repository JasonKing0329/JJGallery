package com.jing.app.jjgallery.gdb.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.util.ColorUtils;
import com.king.service.gdb.bean.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3 0003.
 */

public class RecordSceneExpandAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    public interface OnRecordClickListener {
        void onRecordClick(Record record);
    }

    private Map<String, List<Record>> mOriginRecordMap;
    private List<String> mHeaderList;
    private Map<String, List<Record>> mRecordMap;
    private int nameColorNormal, nameColorBareback;
    private int sortMode;
    private List<Integer> colorList;

    private OnRecordClickListener onRecordClickListener;

    public RecordSceneExpandAdapter(Context context, Map<String, List<Record>> recordMap) {
        this.mOriginRecordMap = recordMap;
        createHeaders();
        nameColorNormal = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, false));
        nameColorBareback = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, true));
    }

    private void createHeaders() {
        mHeaderList = new ArrayList<>();
        colorList = new ArrayList<>();
        if (mOriginRecordMap != null) {
            // 将scene按名称升序排序
            Iterator<String> it = mOriginRecordMap.keySet().iterator();
            while (it.hasNext()) {
                mHeaderList.add(it.next());
                colorList.add(0);
            }
            Collections.sort(mHeaderList, new NameComparator());
        }
        mRecordMap = mOriginRecordMap;
    }

    public void setSortMode(int sortMode) {
        this.sortMode = sortMode;
    }

    public void setOnRecordClickListener(OnRecordClickListener listener) {
        onRecordClickListener = listener;
    }

    public Map<String, List<Record>> getRecordMap() {
        return mOriginRecordMap;
    }

    @Override
    public int getGroupCount() {
        return mHeaderList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mRecordMap.get(mHeaderList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mRecordMap.get(mHeaderList.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mRecordMap.get(mHeaderList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        IndexHeaderHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_starlist_header, parent, false);
            holder = new IndexHeaderHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (IndexHeaderHolder) convertView.getTag();
        }
        holder.header.setText(mHeaderList.get(groupPosition) + "(" + mRecordMap.get(mHeaderList.get(groupPosition)).size() + ")");

        // 避免每次产生新颜色
        if (colorList.get(groupPosition) == 0) {
            colorList.set(groupPosition, ColorUtils.randomWhiteTextBgColor());
        }
        holder.container.setBackgroundColor(colorList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        RecordHolderProxy holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_record_item, parent, false);
            holder = new RecordHolderProxy(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (RecordHolderProxy) convertView.getTag();
        }
        holder.bind(mRecordMap.get(mHeaderList.get(groupPosition)).get(childPosition), childPosition, sortMode);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (onRecordClickListener != null) {
            Record record = (Record) v.getTag();
            onRecordClickListener.onRecordClick(record);
        }
    }

    public class IndexHeaderHolder {

        View container;
        TextView header;
        public IndexHeaderHolder(View view) {
            header = (TextView) view.findViewById(R.id.gdb_star_head);
            container = view.findViewById(R.id.gdb_star_head_container);
        }
    }

    public class NameComparator implements Comparator<String> {

        @Override
        public int compare(String l, String r) {
            if (l == null || r == null) {
                return 0;
            }

            return l.toLowerCase().compareTo(r.toLowerCase());
        }
    }

    public class RecordHolderProxy {

        private RecordViewHolder viewHolder;

        public RecordHolderProxy(View view) {
            viewHolder = new RecordViewHolder();
            viewHolder.initView(view);
            viewHolder.setParameters(nameColorNormal, nameColorBareback, RecordSceneExpandAdapter.this);
        }

        public void bind(Record record, int position, int sortMode) {
            viewHolder.bind(record, position, sortMode);
        }
    }

    public void onRecordFilter(String name) {
        mRecordMap.clear();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(name.trim())) {
            mRecordMap = mOriginRecordMap;
        }
        else {
            for (int i = 0; i < mHeaderList.size(); i ++) {
                List<Record> list = new ArrayList<>();
                mRecordMap.put(mHeaderList.get(i), list);
                List<Record> originList = mOriginRecordMap.get(mHeaderList.get(i));
                for (int j = 0; j < originList.size(); j ++) {
                    if (originList.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                        list.add(originList.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
