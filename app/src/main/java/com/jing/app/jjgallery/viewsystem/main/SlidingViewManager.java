package com.jing.app.jjgallery.viewsystem.main;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.image.lru.ImageLoader;
import com.jing.app.jjgallery.viewsystem.HomeBean;
import com.jing.app.jjgallery.viewsystem.HomeProvider;
import com.jing.app.jjgallery.viewsystem.HomeSelecter;
import com.jing.app.jjgallery.viewsystem.main.bg.SlidingSubscriber;
import com.jing.app.jjgallery.viewsystem.publicview.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class SlidingViewManager implements SlidingSubscriber, SlidingSelectorAdapter.OnPageSelectListener {

    public interface SlidingCallback {
        void onSwitchHome();
    }

    private Context mContext;

    private View slidingLeftView;
    private View slidingRightView;
    private View.OnClickListener leftListener;
    private View.OnClickListener rightListener;

    private ImageView leftBkView, rightBkView;
    private CircleImageView circleView;
    private ViewPager viewPager;

    private List<HomeBean> homeList;
    private HomeProvider homeProvider;

    public SlidingViewManager(Context context, int leftLayoutRes, int rightLayoutRes){
        mContext = context;
        slidingLeftView = LayoutInflater.from(context).inflate(leftLayoutRes, null);
        slidingRightView = LayoutInflater.from(context).inflate(rightLayoutRes, null);
        initLeftView();
        initRightView();
    }

    public void setLeftOnClickListener(View.OnClickListener listener) {
        leftListener = listener;
    }

    public void setRightOnClickListener(View.OnClickListener listener) {
        rightListener = listener;
    }

    public View getSlidingLeftView() {
        return slidingLeftView;
    }

    public View getSlidingRightView() {
        return slidingRightView;
    }

    private void initLeftView() {
        slidingLeftView.findViewById(R.id.sliding_menu_switch).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_checkall).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_export).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_import).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_theme).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_exit).setOnClickListener(leftListener);

        leftBkView = (ImageView) slidingLeftView.findViewById(R.id.sliding_left_bk);
        circleView = (CircleImageView) slidingLeftView.findViewById(R.id.sliding_left_circle);
        viewPager = (ViewPager) slidingLeftView.findViewById(R.id.sliding_home_select);

        homeProvider = new HomeSelecter(mContext);
        homeList = homeProvider.getHomeList();
        viewPager.setAdapter(new SlidingSelectorAdapter(mContext, homeList, this));

        String bkPath = getLeftBkPath(mContext.getResources().getConfiguration().orientation);
        String circlePath = SettingProperties.getPreference(mContext, PreferenceKey.PREF_SLIDING_CIRCLE);

        if (bkPath != null) {
            ImageLoader.getInstance().loadImage(bkPath, leftBkView);
        }
        if (circlePath != null) {
            ImageLoader.getInstance().loadImage(circlePath, circleView);
        }
    }

    private void initRightView() {
        rightBkView = (ImageView) slidingRightView.findViewById(R.id.sliding_right_bk);
        String bkPath = getRightBkPath(mContext.getResources().getConfiguration().orientation);
        if (bkPath != null) {
            ImageLoader.getInstance().loadImage(bkPath, rightBkView);
        }
    }

    private boolean isLandscape() {
        return mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public String getLeftBkPath(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return SettingProperties.getPreference(mContext, PreferenceKey.PREF_SLIDING_BK_LEFT_LAND);
        }
        else {
            return SettingProperties.getPreference(mContext, PreferenceKey.PREF_SLIDING_BK_LEFT);
        }
    }

    public String getRightBkPath(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return SettingProperties.getPreference(mContext, PreferenceKey.PREF_SLIDING_BK_RIGHT_LAND);
        }
        else {
            return SettingProperties.getPreference(mContext, PreferenceKey.PREF_SLIDING_BK_RIGHT);
        }
    }

    public void onConfigurationChanged(int newOrientation) {
        ImageLoader.getInstance().loadImage(getLeftBkPath(newOrientation), leftBkView);
        ImageLoader.getInstance().loadImage(getRightBkPath(newOrientation), rightBkView);
    }

    @Override
    public void onSlidingLeftBgChanged(String path) {
        if (path != null && !isLandscape()) {
            ImageLoader.getInstance().loadImage(path, leftBkView);
        }
    }

    @Override
    public void onSlidingLeftLandBgChanged(String path) {
        if (path != null && isLandscape()) {
            ImageLoader.getInstance().loadImage(path, leftBkView);
        }
    }

    @Override
    public void onSlidingRightBgChanged(String path) {
        if (path != null && !isLandscape()) {
            ImageLoader.getInstance().loadImage(path, rightBkView);
        }
    }

    @Override
    public void onSlidingRightLandBgChanged(String path) {
        if (path != null && isLandscape()) {
            ImageLoader.getInstance().loadImage(path, rightBkView);
        }
    }

    @Override
    public void onSlidingCircleChanged(String path) {
        if (path != null) {
            ImageLoader.getInstance().loadImage(path, circleView);
        }
    }

    @Override
    public void onSelectPage(int index) {
        homeProvider.startHome((Activity) mContext, homeList.get(index).getPreferenceKey(), null);
        ((Activity) mContext).finish();
    }

    public void onOrentaionChanged(int orientation) {
        String path = getLeftBkPath(orientation);
        if (path != null) {
            ImageLoader.getInstance().loadImage(path, leftBkView);
        }
        path = getRightBkPath(orientation);
        if (path != null) {
            ImageLoader.getInstance().loadImage(path, rightBkView);
        }
    }

}
