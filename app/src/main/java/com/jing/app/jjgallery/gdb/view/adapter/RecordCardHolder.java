package com.jing.app.jjgallery.gdb.view.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.model.GdbImageProvider;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.FormatUtil;
import com.jing.app.jjgallery.viewsystem.publicview.CircleImageView;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.Star;

import java.util.List;

/**
 * 描述: record的卡片布局
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/9 11:53
 */
public class RecordCardHolder {
    private CardView groupCard;
    private ImageView ivRecord;
    private CircleImageView ivStar;
    private TextView tvName;
    private TextView tvScore;
    private TextView tvPath;
    private TextView tvScene;
    private TextView tvDate;
    private LinearLayout groupFrames;
    private RoundedImageView ivFrame1;
    private RoundedImageView ivFrame2;
    private RoundedImageView ivFrame3;

    private Star currentStar;

    public void init(View itemView) {
        groupCard = (CardView) itemView.findViewById(R.id.group_card);
        ivRecord = (ImageView) itemView.findViewById(R.id.iv_record);
        ivStar = (CircleImageView) itemView.findViewById(R.id.iv_star);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvScore = (TextView) itemView.findViewById(R.id.tv_score);
        tvPath = (TextView) itemView.findViewById(R.id.tv_path);
        tvScene = (TextView) itemView.findViewById(R.id.tv_scene);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        groupFrames = (LinearLayout) itemView.findViewById(R.id.group_frames);
        ivFrame1 = (RoundedImageView) itemView.findViewById(R.id.iv_frame1);
        ivFrame2 = (RoundedImageView) itemView.findViewById(R.id.iv_frame2);
        ivFrame3 = (RoundedImageView) itemView.findViewById(R.id.iv_frame3);

    }

    public void setCurrentStar(Star currentStar) {
        this.currentStar = currentStar;
    }

    public void bindView(Record record, int position, int endPosition, View.OnClickListener cardListener) {
        tvDate.setText(FormatUtil.formatDate(record.getLastModifyTime()));
        tvName.setText(record.getName());
        tvPath.setText(record.getDirectory());
        tvScore.setText(String.valueOf(record.getScore()));
        if (record instanceof RecordOneVOne) {
            RecordOneVOne rRecord = (RecordOneVOne) record;
            tvScene.setText(rRecord.getSceneName());

            // show star different with current star
            String name = rRecord.getStar1().getName();
            int id = rRecord.getStar1().getId();
            if (id == currentStar.getId()) {
                name = rRecord.getStar2().getName();
            }
            SImageLoader.getInstance().displayImage(GdbImageProvider.getStarRandomPath(name, null), ivStar);
        }
        SImageLoader.getInstance().displayImage(GdbImageProvider.getRecordRandomPath(record.getName(), null), ivRecord);

        // record's images shown on the bottom
        List<String> pathList = GdbImageProvider.getRecordPathList(record.getName());
        if (pathList.size() > 1) {
            groupFrames.setVisibility(View.VISIBLE);
            if (pathList.size() > 2) {
                SImageLoader.getInstance().displayImage(pathList.get(2), ivFrame3);
            }
            SImageLoader.getInstance().displayImage(pathList.get(1), ivFrame2);
            SImageLoader.getInstance().displayImage(pathList.get(0), ivFrame1);
        }
        else {
            groupFrames.setVisibility(View.GONE);
        }

        // to keep UI better, hide tvDate if the visibility of groupFrames is VISIBLE
        tvDate.setVisibility(groupFrames.getVisibility() == View.VISIBLE ? View.GONE:View.VISIBLE);

        // the last item should has margin to right
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) groupCard.getLayoutParams();
        if (position == endPosition) {
            params.rightMargin = groupCard.getContext().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        }
        else {
            params.rightMargin = 0;
        }

        // set group listener
        groupCard.setTag(record);
        groupCard.setOnClickListener(cardListener);

    }
}