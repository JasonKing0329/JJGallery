package com.jing.app.jjgallery.gdb.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allure.lbanners.LMBanners;
import com.allure.lbanners.adapter.LBaseAdapter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.gdb.model.GdbImageProvider;
import com.jing.app.jjgallery.gdb.utils.LMBannerViewUtil;
import com.jing.app.jjgallery.gdb.view.recommend.RecordFilterDialog;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.FormatUtil;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;
import com.jing.app.jjgallery.viewsystem.publicview.PullZoomRecyclerView;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarRecordsAdapter extends RecyclerListAdapter implements View.OnClickListener {

    public interface OnRecordItemClickListener {
        void onClickRecordItem(Record record);
        void onFavorStar(Star star, int score);
        void showAnimSetting();
    }

    private PullZoomHeaderHolder headerHolder;

    private PullZoomRecyclerView recyclerView;
    protected List<Record> listData;
    private StarProxy star;

    private int nameColorNormal, nameColorBareback;
    private OnRecordItemClickListener itemClickListener;

    private int sortMode;

    private boolean isStarFavor;

    private List<String> headPathList;

    public StarRecordsAdapter(StarProxy star, PullZoomRecyclerView recyclerView) {
        this.star = star;
        this.recyclerView = recyclerView;
        listData = star.getStar().getRecordList();
        nameColorNormal = recyclerView.getContext().getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(recyclerView.getContext(), false));
        nameColorBareback = recyclerView.getContext().getResources().getColor(ThemeManager.getInstance().getGdbSRTextColorId(recyclerView.getContext(), true));
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.wall_bk1);

        addViewType(Record.class, new ViewHolderFactory<RecordHolder>() {
            @Override
            public RecordHolder onCreateViewHolder(ViewGroup parent) {
                RecordHolder holder = new RecordHolder(parent);
                holder.setParameters(nameColorNormal, nameColorBareback, StarRecordsAdapter.this);
                return holder;
            }
        });
        addViewType(TYPE_HEADER, new ViewHolderFactory<PullZoomHeaderHolder>() {
            @Override
            public PullZoomHeaderHolder onCreateViewHolder(ViewGroup parent) {
                headerHolder = new PullZoomHeaderHolder(parent);
                return headerHolder;
            }
        });

    }

    public void setStarFavor(boolean starFavor) {
        this.isStarFavor = starFavor;
    }

    public void setItemClickListener(OnRecordItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setSortMode(int sortMode) {
        this.sortMode = sortMode;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return ITEM_HEADER;
        }
        return listData.get(--position);
    }

    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    /**
     * 覆盖父类的方法，这个PullZoom open source是把第recyclerView第0个当成pull image，
     * 但是现在RecordHolder是跟record list界面共用的一个holder，那里是正常的用第0个index的
     * 所以覆盖父类的实现方法，当holder的类型是RecordHolder，将position - 1。父类的getItem其实就是listData.get(position - 1)
     * 但是这里要把正确的position传给RecordHolder
     *
     * PullZoomHeaderHolder还是实现父类的方法
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof PullZoomHeaderHolder) {
            headerHolder = (PullZoomHeaderHolder) holder;
            super.onBindViewHolder(holder, position);
        }
        else {
            RecordHolder vh = (RecordHolder) holder;
            vh.setSortMode(sortMode);
            vh.bind(listData.get(position - 1), position - 1);
        }
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            Record record = (Record) v.getTag();
            itemClickListener.onClickRecordItem(record);
        }
    }

    public void refreshBanner() {
        headerHolder.initBanner(headPathList);
    }

    private class PullZoomHeaderHolder extends RecyclerListAdapter.ViewHolder<Object> {
        private ImageView zoomView;
        private ImageView ivFavor;
        private ImageView ivSetting;
        private ViewGroup zoomHeaderContainer;
        private TextView nameView;
        private TextView numberView;
        private TextView typeView;
        private TextView scoreView;
        private TextView cscoreView;
        private LMBanners lmBanners;

        public PullZoomHeaderHolder(ViewGroup parent) {
            this(LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.adapter_gdb_star_header, parent, false));
        }

        public PullZoomHeaderHolder(View view) {
            super(view);
            zoomView = (ImageView) view.findViewById(R.id.gdb_star_header_image);
            ivSetting = (ImageView) view.findViewById(R.id.iv_setting);
            lmBanners = (LMBanners) view.findViewById(R.id.lmbanner);
            ivFavor = (ImageView) view.findViewById(R.id.iv_favor);
            zoomHeaderContainer = (ViewGroup) view.findViewById(R.id.gdb_star_header_container);
            nameView = (TextView) view.findViewById(R.id.gdb_star_header_name);
            numberView = (TextView) view.findViewById(R.id.gdb_star_header_number);
            typeView = (TextView) view.findViewById(R.id.gdb_star_header_type);
            scoreView = (TextView) view.findViewById(R.id.gdb_star_header_score);
            cscoreView = (TextView) view.findViewById(R.id.gdb_star_header_cscore);
            initHeadPart();
        }

        private void initHeadPart() {
            boolean showImage;
            if (GdbImageProvider.hasStarFolder(star.getStar().getName())) {
                headPathList = GdbImageProvider.getStarPathList(star.getStar().getName());
                if (headPathList.size() <= 1) {
                    showImage = true;
                }
                else {
                    showImage = false;
                    lmBanners.setVisibility(View.VISIBLE);
                    ivSetting.setVisibility(View.VISIBLE);
                    zoomView.setVisibility(View.GONE);
                    recyclerView.setZoomView(lmBanners);
                    initBanner(headPathList);
                }
            }
            else {
                showImage = true;
            }

            if (showImage) {
                lmBanners.setVisibility(View.GONE);
                ivSetting.setVisibility(View.GONE);
                zoomView.setVisibility(View.VISIBLE);
                recyclerView.setZoomView(zoomView);
                String path = GdbImageProvider.getStarRandomPath(star.getStar().getName(), null);
                SImageLoader.getInstance().displayImage(path, zoomView);
            }
        }

        public void initBanner(List<String> pathList) {
            // 禁用btnStart(只在onPageScroll触发后有效)
            lmBanners.isGuide(false);
            // 显示引导圆点
//            lmBanners.hideIndicatorLayout();
            lmBanners.setIndicatorPosition(LMBanners.IndicaTorPosition.BOTTOM_MID);
            // 可以不写，因为文件名直接覆用的LMBanners-1.0.8里的res
            lmBanners.setSelectIndicatorRes(R.drawable.page_indicator_select);
            lmBanners.setUnSelectUnIndicatorRes(R.drawable.page_indicator_unselect);
            // 轮播切换时间
            lmBanners.setDurtion(SettingProperties.getGdbStarNavAnimTime(lmBanners.getContext()));
            if (SettingProperties.isGdbStarNavAnimRandom(lmBanners.getContext())) {
                Random random = new Random();
                int type = Math.abs(random.nextInt()) % RecordFilterDialog.ANIM_TYPES.length;
                LMBannerViewUtil.setScrollAnim(lmBanners, type);
            }
            else {
                LMBannerViewUtil.setScrollAnim(lmBanners, SettingProperties.getGdbStarNavAnimType(lmBanners.getContext()));
            }

            HeadBannerAdapter adapter = new HeadBannerAdapter();
            lmBanners.setAdapter(adapter, pathList);
        }

        @Override
        public void bind(Object item, int position) {
            recyclerView.setHeaderContainer(zoomHeaderContainer);
            SImageLoader.getInstance().displayImage(star.getImagePath(), zoomView);
            nameView.setText(star.getStar().getName());
            numberView.setText(String.format(recyclerView.getContext().getString(R.string.gdb_star_file_numbers), listData.size()));

            StringBuffer buffer = new StringBuffer();
            if (star.getStar().getBeTop() > 0) {
                buffer.append("top(").append(star.getStar().getBeTop()).append(")");
                buffer.append("   ");
            }
            if (star.getStar().getBeBottom() > 0) {
                buffer.append("bottom(").append(star.getStar().getBeBottom()).append(")");
            }
            typeView.setText(buffer.toString());

            buffer = new StringBuffer();
            buffer.append("avg(").append(FormatUtil.formatScore(star.getStar().getAverage(), 1)).append(")")
                    .append("  ").append("max(").append(star.getStar().getMax()).append(")")
                    .append("  ").append("min(").append(star.getStar().getMin()).append(")");
            scoreView.setText(buffer.toString());

            buffer = new StringBuffer();
            buffer.append("cock avg(").append(FormatUtil.formatScore(star.getStar().getcAverage(), 1)).append(")")
                    .append("  ").append("max(").append(star.getStar().getcMax()).append(")")
                    .append("  ").append("min(").append(star.getStar().getcMin()).append(")");
            cscoreView.setText(buffer.toString());

            ivFavor.setSelected(isStarFavor);

            ivFavor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        // cancel favor
                        itemClickListener.onFavorStar(star.getStar(), 0);
                        ivFavor.setSelected(false);
                    }
                    else {
                        new DefaultDialogManager().openInputDialog(v.getContext(), new DefaultDialogManager.OnDialogActionListener() {
                            @Override
                            public void onOk(String name) {
                                try {
                                    int favor = Integer.parseInt(name);
                                    itemClickListener.onFavorStar(star.getStar(), favor);
                                    ivFavor.setSelected(true);
                                } catch (Exception e) {

                                }
                            }
                        });
                    }
                }
            });

            ivSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.showAnimSetting();
                }
            });
        }
    }

    private class HeadBannerAdapter implements LBaseAdapter<String> {

        @Override
        public View getView(LMBanners lBanners, Context context, int position, String path) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_gdb_star_list_banner, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_star);
            SImageLoader.getInstance().displayImage(path, imageView);
            return view;
        }
    }
}
