package com.jing.app.jjgallery.gdb.view.pub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import com.jing.app.jjgallery.util.FormatUtil;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.king.service.gdb.bean.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/19 15:17
 */
public class VideoDialog extends CustomDialog {

    public static final String KEY_PATH = "path";
    public static final String KEY_RECORD = "record";

    private final int NUM_LOAD_THUMB_PUBLISH = 4;
    private final int NUM_THUMBNAIL = 16;

    private TextView tvName;
    private TextView tvPathServer;
    private TextView tvPathLocal;
    private TextView tvTime;
    private TextView tvSize;
    private RecyclerView rvImages;
    private CheckBox cbShowTime;
    private TextView tvSaveImage;

    private List<Bitmap> bitmapList;
    private int itemWidth, itemHeight;
    private List<Integer> timeList;

    private VideoData videoData;
    private Record record;

    private ImageAdapter imageAdapter;
    private VideoModel videoModel;

    public VideoDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        bitmapList = new ArrayList<>();
        videoModel = new VideoModel();

        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        String path = (String) map.get(KEY_PATH);
        record = (Record) map.get(KEY_RECORD);
        tvName.setText(record.getName());
        tvPathLocal.setText(path);
        tvPathServer.setText(record.getDirectory());

        videoData = videoModel.queryVideoDataByPath(getContext(), path);
        timeList = videoModel.createVideoFrames(videoData, NUM_THUMBNAIL);
        videoModel.createThumbnails(videoData, timeList, NUM_LOAD_THUMB_PUBLISH
                , itemWidth, itemHeight, new VideoThumbCallback() {
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
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_gdb_video_detail, null);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvPathServer = (TextView) view.findViewById(R.id.tv_path_server);
        tvPathLocal = (TextView) view.findViewById(R.id.tv_path_local);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        rvImages = (RecyclerView) view.findViewById(R.id.rv_images);
        cbShowTime = (CheckBox) view.findViewById(R.id.cb_show_time);
        tvSaveImage = (TextView) view.findViewById(R.id.tv_save_image);
        tvSaveImage.setOnClickListener(this);

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
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        requestPlayAction(true);
        requestCancelAction(true);
        setTitle("Video");
        return null;
    }

    @Override
    protected void onClickPlay() {
        playVideo(videoData.getPath());
        dismiss();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == tvSaveImage) {

        }
    }

    private void playVideo(String path) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setPackage("com.king.app.video");
            String type = "video/*";
            Uri uri = Uri.parse("file://" + path);
            intent.setDataAndType(uri, type);
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            if (getContext() instanceof GBaseActivity) {
                ((GBaseActivity) getContext()).showToastLong("Can't play this video: " + path);
            }

        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        TextView textView;

        public ItemHolder(View itemView) {
            super(itemView);
            LinearLayout layout = (LinearLayout) itemView;
            layout.setOrientation(LinearLayout.VERTICAL);

            imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    itemWidth, itemHeight);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);

            textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(textView);
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemHolder(new LinearLayout(getContext()));
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            if (position >= bitmapList.size() || bitmapList.get(position) == null) {
                holder.imageView.setImageResource(R.drawable.icon_loading);
            }
            else {
                holder.imageView.setImageBitmap(bitmapList.get(position));
            }

            if (cbShowTime.isChecked()) {
                holder.textView.setText(FormatUtil.formatTime(timeList.get(position)));
                holder.textView.setVisibility(View.VISIBLE);
            }
            else {
                holder.textView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return timeList == null ? 0:timeList.size();
        }
    }
}
