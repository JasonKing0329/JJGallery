package com.jing.app.jjgallery.viewsystem.sub.waterfall;

/**
 * Created by JingYang on 2016/7/26 0026.
 * Description:
 */
public interface OnWaterfallItemListener {
    void onItemClick(int position);
    void onItemLongClick(int position);

    void onEmptyChecked();

    void onFullChecked();
}
