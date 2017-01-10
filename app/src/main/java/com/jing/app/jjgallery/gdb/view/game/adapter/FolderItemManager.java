package com.jing.app.jjgallery.gdb.view.game.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.king.lib.tool.ui.RippleFactory;
import com.king.service.gdb.game.bean.SeasonBean;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class FolderItemManager extends AbstractFolderManager {

    public interface FolderItemListener {
        void onBattle(SeasonBean bean);
        void onCross(SeasonBean bean);
        void onFinal(SeasonBean bean);
        void onGroup(SeasonBean bean);
        void onFolderSetting(SeasonBean seasonBean);
    }

    private TextView titleView;
    private ImageView bkView;
    private ImageView settingView;

    private LinearLayout groupButton;
    private TextView backButton, battleButton, crossButton, finalButton;

    private SeasonBean seasonBean;
    private FolderItemListener folderItemListener;

    public void setFolderItemListener(FolderItemListener folderItemListener) {
        this.folderItemListener = folderItemListener;
    }

    @Override
    protected void setupViews() {
        ViewGroup coverGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.adapter_gdb_season_item_cover, null);
        ViewGroup detailGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.adapter_gdb_season_item_detail, null);

        titleView = (TextView) coverGroup.findViewById(R.id.season_cover_title);
        bkView = (ImageView) coverGroup.findViewById(R.id.season_cover_bk);
        settingView = (ImageView) coverGroup.findViewById(R.id.season_cover_setting);
        groupButton = (LinearLayout) detailGroup.findViewById(R.id.season_item_group);
        backButton = (TextView) detailGroup.findViewById(R.id.season_item_back);
        battleButton = (TextView) detailGroup.findViewById(R.id.season_item_battle);
        crossButton = (TextView) detailGroup.findViewById(R.id.season_item_cross);
        finalButton = (TextView) detailGroup.findViewById(R.id.season_item_final);
        groupButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        battleButton.setOnClickListener(this);
        crossButton.setOnClickListener(this);
        finalButton.setOnClickListener(this);
        settingView.setOnClickListener(this);

        mFoldableLayout.setupViews(coverGroup, detailGroup, coverGroup.getContext().getResources().getDimensionPixelSize(R.dimen.gdb_season_item_height));

        titleView.setBackground(RippleFactory.getBorderlessRippleBackground(Color.rgb(0xbb, 0xbb, 0xbb)));
        settingView.setBackground(RippleFactory.getBorderlessRippleBackground(Color.rgb(0xbb, 0xbb, 0xbb)));
        groupButton.setBackground(RippleFactory.getRippleBackground(
                mContext.getResources().getColor(R.color.actionbar_bk_blue),
                mContext.getResources().getColor(R.color.ripple_material_light)));
        battleButton.setBackground(RippleFactory.getRippleBackground(
                mContext.getResources().getColor(R.color.actionbar_bk_orange),
                mContext.getResources().getColor(R.color.ripple_material_light)));
        crossButton.setBackground(RippleFactory.getRippleBackground(
                mContext.getResources().getColor(R.color.actionbar_bk_lightgreen),
                mContext.getResources().getColor(R.color.ripple_material_light)));
        finalButton.setBackground(RippleFactory.getRippleBackground(
                mContext.getResources().getColor(R.color.colorPrimary),
                mContext.getResources().getColor(R.color.ripple_material_light)));
        backButton.setBackground(RippleFactory.getRippleBackground(
                Color.rgb(0xf7, 0x44, 0x61),
                mContext.getResources().getColor(R.color.ripple_material_light)));
    }

    protected void bindViewData(SeasonBean seasonBean) {
        this.seasonBean = seasonBean;
        titleView.setText(seasonBean.getName());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.season_item_back:
                mFoldableLayout.foldWithAnimation();
                break;
            case R.id.season_cover_setting:
                if (folderItemListener != null) {
                    folderItemListener.onFolderSetting(seasonBean);
                }
                break;
            case R.id.season_item_group:
                if (folderItemListener != null) {
                    folderItemListener.onGroup(seasonBean);
                }
                break;
            case R.id.season_item_battle:
                if (folderItemListener != null) {
                    folderItemListener.onBattle(seasonBean);
                }
                break;
            case R.id.season_item_cross:
                if (folderItemListener != null) {
                    folderItemListener.onCross(seasonBean);
                }
                break;
            case R.id.season_item_final:
                if (folderItemListener != null) {
                    folderItemListener.onFinal(seasonBean);
                }
                break;
        }
    }
}
