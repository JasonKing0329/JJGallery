package com.jing.app.jjgallery.viewsystem.main.order;

import android.view.View;

import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.viewsystem.IColorPage;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbFragment;

/**
 * Created by JingYang on 2016/7/11 0011.
 * Description:
 */
public class SOrderThumbFragment extends ThumbFragment {

    private IPage thumbPage;

    @Override
    protected IPage createThumbPage(View contentView) {
        thumbPage = new SOrderThumbPage(getContext(), contentView
                , getActivity().getIntent().getBooleanExtra(Constants.KEY_THUMBFOLDER_CHOOSER_MODE, false));
        return thumbPage;
    }

    @Override
    public IColorPage getColorPage() {
        if (thumbPage instanceof IColorPage) {
            return (IColorPage) thumbPage;
        }
        return null;
    }
}
