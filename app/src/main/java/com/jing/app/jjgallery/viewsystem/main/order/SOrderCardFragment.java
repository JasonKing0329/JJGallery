package com.jing.app.jjgallery.viewsystem.main.order;

import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.main.BaseFragment;

/**
 * Created by JingYang on 2016/8/4 0004.
 * Description:
 */
public class SOrderCardFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.page_sorder_rank;
    }

    @Override
    protected IPage createPage(View contentView) {
        return new SOrderCardPage(getActivity(), contentView);
    }
}
