package com.jing.app.jjgallery.gdb.view.adapter;

import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.presenter.GdbGuidePresenter;
import com.jing.app.jjgallery.gdb.view.AutoScrollAdapter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * Created by 景阳 on 2016/12/27.
 */

public class GuideScrollAdapter extends AutoScrollAdapter {

    private List<Record> mList;
    private GdbGuidePresenter presenter;

    public GuideScrollAdapter(List<Record> mList) {
        this.mList = mList;
    }

    public void setPresenter(GdbGuidePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public int getCount() {
        return mList == null ? 0:mList.size();
    }

    @Override
    public void loadImage(int position, ImageView imageView) {
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.gdb_record_default);
        SImageLoader.getInstance().displayImage(presenter.getRecordPath(mList.get(position).getName()), imageView);
    }
}
