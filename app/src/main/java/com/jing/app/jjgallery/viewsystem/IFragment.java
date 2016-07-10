package com.jing.app.jjgallery.viewsystem;

import android.support.v4.app.Fragment;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public interface IFragment {
    void setActionbar(ActionBar actionbar);
    Fragment getFragment();
    IPage getPage();
    IColorPage getColorPage();
    void setPresenter(BasePresenter presenter);
}
