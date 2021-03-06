package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

/**
 * Created by JingYang on 2016/8/4 0004.
 * Description:
 */
public class SOrderCardAdapter extends StackAdapter<SOrder> implements CardExpandMenu.OnExpandMenuClickListener {

    public interface OnExpandActionListener {
        void onSurfView(SOrder order, View view);
        void onWallView(SOrder order, View view);
        void onBookView(SOrder order, View view);
        void onPreView(SOrder order, View view);
        void onWaterfallView(SOrder order, View view);
    }

    public enum HitMode {
        Total, Year, Month, Week, Day
    }
    private HitMode hitMode;

    private OnExpandActionListener onExpandActionListener;

    public SOrderCardAdapter(Context context, HitMode mode) {
        super(context);
        hitMode = mode;
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.wall_bk5);
    }

    public void setOnExpandActionListener(OnExpandActionListener onExpandActionListener) {
        this.onExpandActionListener = onExpandActionListener;
    }

    @Override
    public void bindView(SOrder data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemLargeHeaderViewHolder) {
            ((ColorItemLargeHeaderViewHolder) holder).onBind(data, position);
        }
        else {
            ((ColorItemViewHolder) holder).onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.adapter_sorder_card_item_large:
                View view = getLayoutInflater().inflate(viewType, parent, false);
                return new ColorItemLargeHeaderViewHolder(view);
            default:
                view = getLayoutInflater().inflate(viewType, parent, false);
                return new ColorItemViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.adapter_sorder_card_item_large;
        }
        else {
            return R.layout.adapter_sorder_card_item;
        }
    }

    @Override
    public void onExpandMenuItemClick(View parent, View view, int position) {

        if (onExpandActionListener != null) {
            SOrder order = (SOrder) parent.getTag();
            switch (position) {
                case 0://surf
                    onExpandActionListener.onSurfView(order, view);
                    break;
                case 1://wall
                    onExpandActionListener.onWallView(order, view);
                    break;
                case 2://book
                    onExpandActionListener.onBookView(order, view);
                    break;
                case 3://preview
                    onExpandActionListener.onPreView(order, view);
                    break;
                case 4://waterfall
                    onExpandActionListener.onWaterfallView(order, view);
                    break;
            }
        }
    }

    class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextContent;
        TextView mTextTitle;
        RoundedImageView imageView;
        CardExpandMenu expandMenu;
        private CardExpandAdapter menuAdapter;

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            expandMenu = (CardExpandMenu) view.findViewById(R.id.card_expand_menu);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            imageView = (RoundedImageView) view.findViewById(R.id.text_list_card_image);
            mTextContent = (TextView) view.findViewById(R.id.text_list_card_content);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
            if (menuAdapter == null) {
                menuAdapter = new CardExpandAdapter(getContext());
                expandMenu.setAdapter(menuAdapter);
            }
            else {
                menuAdapter.notifyDataSetChanged();
            }
        }

        public void onBind(SOrder data, int position) {
            expandMenu.setTag(data);
            expandMenu.setOnExpandMenuClickListener(SOrderCardAdapter.this);
            bindImageView(data, position, imageView);
            bindTitle(data, position, mTextTitle);
            bindContent(data, position, mTextContent);
        }

    }

    class ColorItemLargeHeaderViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextContent;
        TextView mTextTitle;
        RoundedImageView imageView;
        CardExpandMenu expandMenu;
        private CardExpandAdapter menuAdapter;

        public ColorItemLargeHeaderViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            expandMenu = (CardExpandMenu) view.findViewById(R.id.card_expand_menu);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            imageView = (RoundedImageView) view.findViewById(R.id.text_list_card_image);
            mTextContent = (TextView) view.findViewById(R.id.text_list_card_content);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
            if (menuAdapter == null) {
                menuAdapter = new CardExpandAdapter(getContext());
                expandMenu.setAdapter(menuAdapter);
            }
            else {
                menuAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }

        public void onBind(SOrder data, int position) {
            expandMenu.setTag(data);
            expandMenu.setOnExpandMenuClickListener(SOrderCardAdapter.this);
            bindImageView(data, position, imageView);
            bindTitle(data, position, mTextTitle);
            bindContent(data, position, mTextContent);
            itemView.findViewById(R.id.text_list_card_content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CardStackView)itemView.getParent()).performItemClick(ColorItemLargeHeaderViewHolder.this);
                }
            });
        }

    }

    private void bindImageView(SOrder data, int position, RoundedImageView imageView) {
        SImageLoader.getInstance().displayImage(data.getCoverPath(), imageView);
    }

    private void bindTitle(SOrder data, int position, TextView title) {
        title.setText(String.valueOf(position + 1) + "   " + data.getName());
    }

    private void bindContent(SOrder data, int position, TextView content) {
        int count;
        if (hitMode == HitMode.Total) {
            count = data.getOrderCount().countAll;
        }
        else if (hitMode == HitMode.Year) {
            count = data.getOrderCount().countYear;
        }
        else if (hitMode == HitMode.Month) {
            count = data.getOrderCount().countMonth;
        }
        else if (hitMode == HitMode.Week) {
            count = data.getOrderCount().countWeek;
        }
        else {
            count = data.getOrderCount().countDay;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(count).append("次访问").append("\n").append("上次访问时间:  ")
                .append(data.getOrderCount().lastYear).append("-").append(data.getOrderCount().lastMonth).append("-").append(data.getOrderCount().lastDay);
        content.setText(buffer.toString());
    }

}
