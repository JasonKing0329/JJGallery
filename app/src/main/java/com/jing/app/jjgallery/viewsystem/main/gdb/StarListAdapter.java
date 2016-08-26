package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.StarProxy;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.king.service.gdb.bean.Star;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

/**
 * Created by Jing Yang on 2016/7/30 0030.
 * @author Jing Yang
 * this open source use Item object to present Head object
 * so for Star object, make following rules:
 * if id is -1, then it stands for header, and the name stands for header text
 */
public class StarListAdapter extends BaseTurboAdapter<Star, BaseViewHolder> implements View.OnClickListener {

    private List<Star> originList;
    private GdbPresenter mPresenter;

    public interface OnStarClickListener {
        void onStarClick(StarProxy star);
    }

    private int colors[] = new int[] {
            R.color.actionbar_bk_blue, R.color.actionbar_bk_green
            , R.color.actionbar_bk_orange, R.color.actionbar_bk_purple
            , R.color.actionbar_bk_deepblue, R.color.actionbar_bk_lightgreen
    };

    private OnStarClickListener onStarClickListener;

    public StarListAdapter(Context context) {
        super(context);
    }

    public StarListAdapter(Context context, List<Star> data) {
        super(context, data);
        originList = data;
    }

    public void setPresenter(GdbPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    public void setOnStarClickListener(OnStarClickListener listener) {
        onStarClickListener = listener;
    }

    private boolean isHeader(int position) {
        return getItem(position).getId() == -1;
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
            return new NameHolder(inflateItemView(R.layout.adapter_gdb_starlist_item, parent));
        }
        else {
            return new IndexHeaderHolder(inflateItemView(R.layout.adapter_gdb_starlist_header, parent));
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, Star item) {
        if (holder instanceof IndexHeaderHolder) {
            ((IndexHeaderHolder) holder).header.setText(item.getName());
            if (holder instanceof IndexHeaderHolder) {
                int index = item.getName().charAt(0) % colors.length;
                ((IndexHeaderHolder) holder).container.setBackgroundColor(mContext.getResources().getColor(colors[index]));
            }
        }
        else if (holder instanceof NameHolder) {
            if (item.getRecordNumber() == 0) {
                mPresenter.loadStarRecordNumber(item);
            }
            NameHolder nHolder = (NameHolder) holder;
            nHolder.name.setText(item.getName() + " (" + item.getRecordNumber() + ")");
            StarProxy proxy = new StarProxy();
            proxy.setStar(item);
            String headPath = mPresenter.getStarImage(item.getName());
            if (headPath == null) {
                nHolder.imageView.setVisibility(View.GONE);
            }
            else {
                nHolder.imageView.setVisibility(View.VISIBLE);
                SImageLoader.getInstance().displayImage(headPath, nHolder.imageView);
            }
            proxy.setImagePath(headPath);
            nHolder.name.setTag(proxy);
            nHolder.name.setOnClickListener(this);
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(isHeader(i) && getData().get(i).getName().equals(letter)){
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

    public class NameHolder extends BaseViewHolder {

        TextView name;
        CircularImageView imageView;
        public NameHolder(View view) {
            super(view);
            name = findViewById(R.id.gdb_star_name);
            imageView = findViewById(R.id.gdb_star_headimg);
        }
    }

    @Override
    public void onClick(View v) {
        if (onStarClickListener != null) {
            StarProxy star = (StarProxy) v.getTag();
            onStarClickListener.onStarClick(star);
        }
    }

    public void onStarFilter(String name) {
        mData.clear();
        if (name.trim().length() == 0) {
            for (Star star:originList) {
                mData.add(star);
            }
        }
        else {
            for (int i = 0; i < originList.size(); i ++) {
                if (originList.get(i).getId() == -1) {
                    mData.add(originList.get(i));
                }
                else {
                    if (originList.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                        mData.add(originList.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
