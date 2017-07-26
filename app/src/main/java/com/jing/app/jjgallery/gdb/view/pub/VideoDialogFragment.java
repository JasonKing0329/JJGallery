package com.jing.app.jjgallery.gdb.view.pub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseActivity;
import com.jing.app.jjgallery.gdb.bean.VideoData;
import com.jing.app.jjgallery.gdb.model.VideoModel;
import com.jing.app.jjgallery.gdb.model.VideoThumbCallback;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.util.FormatUtil;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DraggableDialogFragmentV4;
import com.jing.app.jjgallery.viewsystem.sub.dialog.VideoImageDialogFragment;
import com.king.lib.saveas.ScreenUtils;
import com.king.service.gdb.bean.Record;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/20 15:23
 */
public class VideoDialogFragment extends DraggableDialogFragmentV4 implements IVideoContentHolder {

    private VideoContentFragment ftContent;

    private String videoPath;
    private Record record;

    public void setVideoPath(String path) {
        this.videoPath = path;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public String getVideoPath() {
        return videoPath;
    }

    @Override
    public Record getRecord() {
        return record;
    }

    @Override
    public void setDialogWidth(int width) {
        setWidth(width);
    }

    protected int getMaxHeight() {
        return ScreenUtils.getScreenHeight(getActivity()) * 2 / 3;
    }

    @Override
    protected View getToolbarView(ViewGroup groupToolbar) {
        requestPlayAction();
        requestCloseAction();
        setTitle("Video");
        return null;
    }

    @Override
    protected Fragment getContentViewFragment() {
        ftContent = new VideoContentFragment();
        return ftContent;
    }

    @Override
    protected void onClickPlay() {
        ftContent.playVideo(videoPath);
        dismiss();
    }

    public static class VideoContentFragment extends ContentFragmentV4 {

        private final int NUM_LOAD_THUMB_PUBLISH = 4;
        private final int NUM_THUMBNAIL = 16;

        private IVideoContentHolder holder;

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_path_server)
        TextView tvPathServer;
        @BindView(R.id.tv_path_local)
        TextView tvPathLocal;
        @BindView(R.id.rv_images)
        RecyclerView rvImages;
        @BindView(R.id.cb_show_time)
        CheckBox cbShowTime;

        private List<Bitmap> bitmapList;
        private int itemWidth, itemHeight;
        private List<Integer> timeList;

        private VideoData videoData;
        
        private ImageAdapter imageAdapter;
        private VideoModel videoModel;

        @Override
        protected void bindChildFragmentHolder(IFragmentHolder holder) {
            if (holder instanceof IVideoContentHolder) {
                this.holder = (IVideoContentHolder) holder;
            }
        }

        @Override
        protected int getLayoutRes() {
            return R.layout.dlg_gdb_video_detail;
        }

        @Override
        protected void initView(View view) {

            // 嵌套在fragment，fragment实现IVideoContentHolder，没有走onAttach
            if (getParentFragment() instanceof IVideoContentHolder) {
                this.holder = (IVideoContentHolder) getParentFragment();
            }

            ButterKnife.bind(this, view);
            GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
            rvImages.setLayoutManager(manager);

            cbShowTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    imageAdapter.notifyDataSetChanged();
                }
            });

            itemWidth = getContext().getResources().getDimensionPixelSize(R.dimen.video_detail_thumb_item_width);
            itemHeight = getContext().getResources().getDimensionPixelSize(R.dimen.video_detail_thumb_item_height);
            initData();
        }

        private void initData() {
            bitmapList = new ArrayList<>();
            videoModel = new VideoModel();

            videoData = videoModel.queryVideoDataByPath(getActivity(), holder.getVideoPath());
            tvName.setText(holder.getRecord().getName());
            tvPathLocal.setText(videoData.getPath());
            tvPathServer.setText(holder.getRecord().getDirectory());

            timeList = videoModel.createVideoFrames(videoData, NUM_THUMBNAIL);
            videoModel.createThumbnails(videoData, timeList, NUM_LOAD_THUMB_PUBLISH
                    , itemWidth, itemHeight, false, new VideoThumbCallback() {
                        @Override
                        public void onThumbnailCreated(List<Bitmap> list) {
                            bitmapList.addAll(list);
                            imageAdapter.notifyDataSetChanged();
                        }
                    });
            imageAdapter = new ImageAdapter();
            rvImages.setAdapter(imageAdapter);

            tvSize.setText(videoData.getSize());
            tvTime.setText(videoData.getDuration());

            updateDialogWidth();
        }

        private void updateDialogWidth() {
            int paddingHor = getContext().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
            int width = itemWidth * 4 + paddingHor * 2;
//            ViewGroup.LayoutParams params = rvImages.getLayoutParams();
//            params.width = width;
//            rvImages.setLayoutParams(params);
            // 必须设置dialog的width
            holder.setDialogWidth(width);
        }

        @OnClick(R.id.tv_save_image)
        public void onViewClicked() {
        }

        public void playVideo(String path) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setPackage("com.king.app.video");
                String type = "video/*";
                Uri uri = Uri.parse("file://" + path);
                intent.setDataAndType(uri, type);
                getActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                if (getContext() instanceof GBaseActivity) {
                    ((GBaseActivity) getActivity()).showToastLong("Can't play this video: " + path);
                }

            }
        }

        private void showImageDialog(int frame) {
            int width = ScreenUtils.getScreenWidth(getContext());
            int height = width * 9 / 16;
            final Bitmap bitmap = videoModel.getVideoFrame(videoData.getPath(), frame, width, height, true);
            VideoImageDialogFragment dialog = new VideoImageDialogFragment();
            dialog.setBitmap(bitmap);
            dialog.setOnShowImageListener(new VideoImageDialogFragment.OnShowImageListener() {
                @Override
                public void onSetAsCover() {
                    videoModel.saveBitmap(bitmap, holder.getRecord().getName());
                }
            });
            dialog.show(getChildFragmentManager(), "VideoImageDialogFragment");
        }

        private class ItemHolder extends RecyclerView.ViewHolder {

            ImageView ivImage;

            TextView tvTime;

            LinearLayout groupItem;

            public ItemHolder(View itemView) {
                super(itemView);
                ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                groupItem = (LinearLayout) itemView.findViewById(R.id.group_item);
            }
        }

        private class ImageAdapter extends RecyclerView.Adapter<ItemHolder> implements View.OnClickListener {

            @Override
            public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dlg_video_item, parent, false));
            }

            @Override
            public void onBindViewHolder(ItemHolder holder, int position) {
                if (position >= bitmapList.size() || bitmapList.get(position) == null) {
                    holder.ivImage.setImageResource(R.drawable.icon_loading);
                }
                else {
                    holder.ivImage.setImageBitmap(bitmapList.get(position));
                }

                if (cbShowTime.isChecked()) {
                    holder.tvTime.setText(FormatUtil.formatTime(timeList.get(position)));
                    holder.tvTime.setVisibility(View.VISIBLE);
                }
                else {
                    holder.tvTime.setVisibility(View.GONE);
                }

                holder.groupItem.setTag(position);
                holder.groupItem.setOnClickListener(this);
            }

            @Override
            public int getItemCount() {
                return timeList == null ? 0:timeList.size();
            }

            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                showImageDialog(timeList.get(position));
            }
        }

    }
}
