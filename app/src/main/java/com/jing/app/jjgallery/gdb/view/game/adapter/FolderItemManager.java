package com.jing.app.jjgallery.gdb.view.game.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class FolderItemManager extends AbstractFolderManager {

    private TextView titleView;
    private ImageView bkView;

    private LinearLayout groupButton;
    private TextView backButton, battleButton, crossButton, finalButton;

    @Override
    protected void setupViews() {
        ViewGroup coverGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.adapter_gdb_season_item_cover, null);
        ViewGroup detailGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.adapter_gdb_season_item_detail, null);

        titleView = (TextView) coverGroup.findViewById(R.id.season_cover_title);
        bkView = (ImageView) coverGroup.findViewById(R.id.season_cover_bk);
        groupButton = (LinearLayout) detailGroup.findViewById(R.id.season_item_group);
        backButton = (TextView) detailGroup.findViewById(R.id.season_item_back);
        battleButton = (TextView) detailGroup.findViewById(R.id.season_item_battle);
        crossButton = (TextView) detailGroup.findViewById(R.id.season_item_cross);
        finalButton = (TextView) detailGroup.findViewById(R.id.season_item_final);
    }
}
