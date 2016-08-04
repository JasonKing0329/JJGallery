package com.jing.app.jjgallery.viewsystem.main.order;

import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.main.BaseFragment;

/**
 * Created by JingYang on 2016/7/14 0014.
 * Description:
 */
public class SOrderIndexFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.page_file_manager_index;
    }

    @Override
    protected IPage createPage(View contentView) {
        return new SOrderIndexPage(getActivity(), contentView);
    }
}
