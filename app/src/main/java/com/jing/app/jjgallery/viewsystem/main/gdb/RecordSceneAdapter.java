package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.RecordProxy;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.RecordSingleScene;
import com.king.service.gdb.bean.Star;

import java.util.List;
import java.util.Random;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

/**
 * Created by Jing Yang on 2016/7/30 0030.
 * @author Jing Yang
 * this open source use Item object to present Head object
 * so for Star object, make following rules:
 * if id is -1, then it stands for header, and the name stands for header text
 */
public class RecordSceneAdapter extends BaseTurboAdapter<RecordProxy, BaseViewHolder> implements View.OnClickListener {

    private List<RecordProxy> originList;
    private int nameColorNormal, nameColorBareback;
    private Random random;

    public interface OnRecordClickListener {
        void onRecordClick(Star star);
    }

    private int colors[] = new int[] {
            R.color.actionbar_bk_blue, R.color.actionbar_bk_green
            , R.color.actionbar_bk_orange, R.color.actionbar_bk_purple
            , R.color.actionbar_bk_deepblue, R.color.actionbar_bk_lightgreen
    };

    private OnRecordClickListener onRecordClickListener;

    public RecordSceneAdapter(Context context) {
        super(context);
    }

    public RecordSceneAdapter(Context context, List<RecordProxy> data) {
        super(context, data);
        originList = data;
        nameColorNormal = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, false));
        nameColorBareback = context.getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(context, true));
        random = new Random();
    }

    public void setOnStarClickListener(OnRecordClickListener listener) {
        onRecordClickListener = listener;
    }

    public List<RecordProxy> getRecordList() {
        return mData;
    }

    private boolean isHeader(int position) {
        return getItem(position).isHeader();
    }

    @Override
    protected int getDefItemViewType(int position) {
        /**
         * normal item must be 0
         * open source design this way
         */
        if (isHeader(position)) {
            return 1;
        }
        return 0;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        /**
         * normal item must be 0
         * open source design this way
         */
        if (viewType == 0) {
            return new RecordHolderProxy(parent);
        }
        else {
            return new IndexHeaderHolder(inflateItemView(R.layout.adapter_gdb_starlist_header, parent));
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, RecordProxy item) {
        if (holder instanceof IndexHeaderHolder) {
            ((IndexHeaderHolder) holder).header.setText(item.getHeaderName() + "(" + item.getItemNumber() + ")");
            if (holder instanceof IndexHeaderHolder) {
                int index = item.getHeaderName().charAt(0) % colors.length;
                ((IndexHeaderHolder) holder).container.setBackgroundColor(mContext.getResources().getColor(colors[index]));
            }
        }
        else if (holder instanceof RecordHolderProxy) {
            ((RecordHolderProxy) holder).bind(item);
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(isHeader(i) && getData().get(i).getRecord().getName().equals(letter)){
                return i;
            }
        }
        return -1;
    }

    public class IndexHeaderHolder extends BaseViewHolder {

        View container;
        TextView header;
        public IndexHeaderHolder(View view) {
            super(view);
            header = findViewById(R.id.gdb_star_head);
            container = findViewById(R.id.gdb_star_head_container);
        }
    }

    public class RecordHolderProxy extends BaseViewHolder {

        private RecordViewHolder viewHolder;

        public RecordHolderProxy(View view) {
            super(view);
            viewHolder = new RecordViewHolder();
            viewHolder.initView(view);
            viewHolder.setParameters(nameColorNormal, nameColorBareback, RecordSceneAdapter.this);
        }

        public RecordHolderProxy(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_record_item, parent, false));
        }

        public void bind(RecordProxy item) {
            viewHolder.bind(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (onRecordClickListener != null) {
            Star star = (Star) v.getTag();
            onRecordClickListener.onRecordClick(star);
        }
    }

    public void onRecordFilter(String name) {
        mData.clear();
        if (name.trim().length() == 0) {
            for (RecordProxy record:originList) {
                mData.add(record);
            }
        }
        else {
            for (int i = 0; i < originList.size(); i ++) {
                if (originList.get(i).isHeader()) {
                    mData.add(originList.get(i));
                }
                else {
                    if (originList.get(i).getRecord().getName().toLowerCase().contains(name.toLowerCase())) {
                        mData.add(originList.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
