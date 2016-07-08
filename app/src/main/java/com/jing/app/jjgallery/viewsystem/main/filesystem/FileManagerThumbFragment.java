package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.view.View;

import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbFragment;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class FileManagerThumbFragment extends ThumbFragment {
    @Override
    protected IPage createThumbPage(View contentView) {
        return new FileManagerThumbPage(getActivity(), contentView
                , getActivity().getIntent().getBooleanExtra(Constants.KEY_THUMBFOLDER_CHOOSER_MODE, false));
    }
}
