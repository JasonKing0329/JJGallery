package com.jing.app.jjgallery.gdb.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.GdbPresenter;
import com.jing.app.jjgallery.gdb.view.star.OnStarClickListener;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;
import com.king.service.gdb.bean.FavorBean;

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
public class StarListAdapter extends BaseTurboAdapter<StarProxy, BaseViewHolder> implements View.OnClickListener {

    private List<StarProxy> originList;
    private GdbPresenter mPresenter;

    private int colors[] = new int[] {
            R.color.actionbar_bk_blue, R.color.actionbar_bk_green
            , R.color.actionbar_bk_orange, R.color.actionbar_bk_purple
            , R.color.actionbar_bk_deepblue, R.color.actionbar_bk_lightgreen
    };

    private OnStarClickListener onStarClickListener;

    public StarListAdapter(Context context) {
        super(context);
    }

    public StarListAdapter(Context context, List<StarProxy> data) {
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
        return getItem(position).getStar().getId() == -1;
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
    protected void convert(BaseViewHolder holder, StarProxy item) {
        if (holder instanceof IndexHeaderHolder) {
            ((IndexHeaderHolder) holder).header.setText(item.getStar().getName());
            if (holder instanceof IndexHeaderHolder) {
                int index = item.getStar().getName().charAt(0) % colors.length;
                ((IndexHeaderHolder) holder).container.setBackgroundColor(mContext.getResources().getColor(colors[index]));
            }
        }
        else if (holder instanceof NameHolder) {
            NameHolder nHolder = (NameHolder) holder;
            nHolder.name.setText(item.getStar().getName() + " (" + item.getStar().getRecordNumber() + ")");
            String headPath = item.getImagePath();
            if (headPath == null) {
                nHolder.imageView.setVisibility(View.GONE);
            }
            else {
                nHolder.imageView.setVisibility(View.VISIBLE);
                SImageLoader.getInstance().displayImage(headPath, nHolder.imageView);
            }
            nHolder.name.setTag(item);
            nHolder.name.setOnClickListener(this);

            nHolder.starProxy = item;
            nHolder.favorView.setTag(nHolder);
            nHolder.favorView.setOnClickListener(this);

            if (item.getFavor() > 0) {
                nHolder.favorView.setSelected(true);
                nHolder.favorScore.setVisibility(View.VISIBLE);
                nHolder.favorScore.setText(String.valueOf(item.getFavor()));
            }
            else {
                nHolder.favorView.setSelected(false);
                nHolder.favorScore.setVisibility(View.INVISIBLE);
            }
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(isHeader(i) && getData().get(i).getStar().getName().equals(letter)){
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
        ImageView favorView;
        StarProxy starProxy;
        TextView favorScore;
        public NameHolder(View view) {
            super(view);
            name = findViewById(R.id.gdb_star_name);
            imageView = findViewById(R.id.gdb_star_headimg);
            favorView = (ImageView) itemView.findViewById(R.id.gdb_star_favor);
            favorScore = (TextView) itemView.findViewById(R.id.gdb_star_favor_score);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            if (onStarClickListener != null) {
                StarProxy star = (StarProxy) v.getTag();
                onStarClickListener.onStarClick(star);
            }
        }
        else if (v instanceof ImageView) {
            final NameHolder holder = (NameHolder) v.getTag();
            if (holder.starProxy.getFavor() > 0) {
                holder.starProxy.setFavor(0);
                saveFavor(holder.starProxy, 0);
                notifyItemChanged(holder.getAdapterPosition());
            }
            else {
                new DefaultDialogManager().openInputDialog(v.getContext(), new DefaultDialogManager.OnDialogActionListener() {
                    @Override
                    public void onOk(String name) {
                        try {
                            int favor = Integer.parseInt(name);
                            holder.starProxy.setFavor(Integer.parseInt(name));
                            saveFavor(holder.starProxy, favor);
                            notifyItemChanged(holder.getAdapterPosition());
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void saveFavor(StarProxy starProxy, int favor) {
        FavorBean bean = new FavorBean();
        bean.setStarId(starProxy.getStar().getId());
        bean.setStarName(starProxy.getStar().getName());
        bean.setFavor(favor);
        mPresenter.saveFavor(bean);
    }

    public void onStarFilter(String name) {
        mData.clear();
        if (name.trim().length() == 0) {
            for (StarProxy star:originList) {
                mData.add(star);
            }
        }
        else {
            for (int i = 0; i < originList.size(); i ++) {
                if (originList.get(i).getStar().getId() == -1) {
                    mData.add(originList.get(i));
                }
                else {
                    if (originList.get(i).getStar().getName().toLowerCase().contains(name.toLowerCase())) {
                        mData.add(originList.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
