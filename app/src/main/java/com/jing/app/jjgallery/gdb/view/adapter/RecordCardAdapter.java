package com.jing.app.jjgallery.gdb.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述: record list presented by card item view
 * codes are implemented by RecyclerView.Adapter
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/7 11:44
 */
public class RecordCardAdapter extends RecyclerView.Adapter<RecordCardAdapter.CardHolder> {

    private List<Record> list;
    private Star currentStar;
    private OnCardActionListener onCardActionListener;

    public void setCurrentStar(Star currentStar) {
        this.currentStar = currentStar;
    }

    public void setOnCardActionListener(OnCardActionListener onCardActionListener) {
        this.onCardActionListener = onCardActionListener;
    }

    public void setRecordList(List<Record> recordList) {
        this.list = recordList;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gdb_record_card, parent, false));
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        Record record = list.get(position);
        holder.tvDate.setText(FormatUtil.formatDate(record.getLastModifyTime()));
        holder.tvName.setText(record.getName());
        holder.tvPath.setText(record.getDirectory());
        holder.tvScore.setText(String.valueOf(record.getScore()));
        if (record instanceof RecordOneVOne) {
            RecordOneVOne rRecord = (RecordOneVOne) record;
            holder.tvScene.setText(rRecord.getSceneName());

            // show star different with current star
            String name = rRecord.getStar1().getName();
            if (!currentStar.getName().equals(name)) {
                name = rRecord.getStar2().getName();
            }
            SImageLoader.getInstance().displayImage(GdbImageProvider.getStarRandomPath(name, null), holder.ivStar);
        }
        SImageLoader.getInstance().displayImage(GdbImageProvider.getRecordRandomPath(record.getName(), null), holder.ivRecord);

        // record's images shown on the bottom
        List<String> pathList = GdbImageProvider.getRecordPathList(record.getName());
        if (pathList.size() > 1) {
            holder.groupFrames.setVisibility(View.VISIBLE);
            if (pathList.size() > 2) {
                SImageLoader.getInstance().displayImage(pathList.get(2), holder.ivFrame3);
            }
            SImageLoader.getInstance().displayImage(pathList.get(1), holder.ivFrame2);
            SImageLoader.getInstance().displayImage(pathList.get(0), holder.ivFrame1);
        }
        else {
            holder.groupFrames.setVisibility(View.GONE);
        }

        // to keep UI better, hide tvDate if the visibility of groupFrames is VISIBLE
        holder.tvDate.setVisibility(holder.groupFrames.getVisibility() == View.VISIBLE ? View.GONE:View.VISIBLE);

        // the last item should has margin to right
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.groupCard.getLayoutParams();
        if (position == list.size() - 1) {
            params.rightMargin = holder.groupCard.getContext().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        }
        else {
            params.rightMargin = 0;
        }

        // set group listener
        holder.groupCard.setTag(record);
        holder.groupCard.setOnClickListener(cardListener);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onCardActionListener != null) {
                onCardActionListener.onClickCardItem((Record) v.getTag());
            }
        }
    };

    public interface OnCardActionListener {
        void onClickCardItem(Record record);
    }

    public static class CardHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_record)
        ImageView ivRecord;
        @BindView(R.id.iv_star)
        CircleImageView ivStar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_score)
        TextView tvScore;
        @BindView(R.id.tv_path)
        TextView tvPath;
        @BindView(R.id.tv_scene)
        TextView tvScene;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.iv_frame1)
        RoundedImageView ivFrame1;
        @BindView(R.id.iv_frame2)
        RoundedImageView ivFrame2;
        @BindView(R.id.iv_frame3)
        RoundedImageView ivFrame3;
        @BindView(R.id.group_frames)
        LinearLayout groupFrames;
        @BindView(R.id.group_card)
        CardView groupCard;
        
        public CardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
