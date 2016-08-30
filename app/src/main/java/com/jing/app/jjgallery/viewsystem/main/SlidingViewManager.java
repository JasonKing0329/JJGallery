package com.jing.app.jjgallery.viewsystem.main;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.HomeBean;
import com.jing.app.jjgallery.viewsystem.HomeProvider;
import com.jing.app.jjgallery.viewsystem.HomeSelecter;
import com.jing.app.jjgallery.viewsystem.main.bg.SlidingSubscriber;
import com.jing.app.jjgallery.viewsystem.publicview.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class SlidingViewManager implements SlidingSubscriber, SlidingSelectorAdapter.OnPageSelectListener
    , View.OnClickListener{

    public interface SlidingLeftCallback {
        void onCheckAllService();
        void onExport();
        void onImport();
        void onSetting();
        void onExit();
        void onChangeTheme();
    }

    public interface SlidingRightCallback {
        View onSetupRightSlidingMenu();
    }

    private Context mContext;

    // left menu defines common function of home
    private View slidingLeftView;
    // right menu apply custom function
    private View slidingRightView;

    private SlidingLeftCallback slidingLeftCallback;
    private SlidingRightCallback slidingRightCallback;
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
    }

    public void setup() {
        initLeftView();
        initRightView();
    }

    public void setSlidingLeftCallback(SlidingLeftCallback callback) {
        slidingLeftCallback = callback;
    }

    public void setSlidingRightCallback(SlidingRightCallback callback) {
        slidingRightCallback = callback;
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
        slidingLeftView.findViewById(R.id.sliding_menu_checkall).setOnClickListener(this);
        slidingLeftView.findViewById(R.id.sliding_menu_export).setOnClickListener(this);
        slidingLeftView.findViewById(R.id.sliding_menu_import).setOnClickListener(this);
        slidingLeftView.findViewById(R.id.sliding_menu_theme).setOnClickListener(this);
        slidingLeftView.findViewById(R.id.sliding_menu_setting).setOnClickListener(this);
        slidingLeftView.findViewById(R.id.sliding_menu_exit).setOnClickListener(this);

        leftBkView = (ImageView) slidingLeftView.findViewById(R.id.sliding_left_bk);
        circleView = (CircleImageView) slidingLeftView.findViewById(R.id.sliding_left_circle);
        viewPager = (ViewPager) slidingLeftView.findViewById(R.id.sliding_home_select);

        homeProvider = new HomeSelecter(mContext);
        homeList = homeProvider.getHomeList();
        viewPager.setAdapter(new SlidingSelectorAdapter(mContext, homeList, this));

        String bkPath = getLeftBkPath(mContext.getResources().getConfiguration().orientation);
        String circlePath = SettingProperties.getPreference(mContext, PreferenceKey.PREF_SLIDING_CIRCLE);

        if (bkPath != null) {
            SImageLoader.getInstance().displayImage(bkPath, leftBkView);
        }
        if (circlePath != null) {
            SImageLoader.getInstance().displayImage(circlePath, circleView);
        }
    }

    private void initRightView() {
        rightBkView = (ImageView) slidingRightView.findViewById(R.id.sliding_right_bk);
        String bkPath = getRightBkPath(mContext.getResources().getConfiguration().orientation);
        if (bkPath != null) {
            SImageLoader.getInstance().displayImage(bkPath, rightBkView);
        }

        ViewGroup group = (ViewGroup) slidingRightView.findViewById(R.id.sliding_right_content);
        if (slidingRightCallback != null) {
            View content = slidingRightCallback.onSetupRightSlidingMenu();
            if (content != null) {
                group.addView(content);
            }
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
        SImageLoader.getInstance().displayImage(getLeftBkPath(newOrientation), leftBkView);
        SImageLoader.getInstance().displayImage(getRightBkPath(newOrientation), rightBkView);
    }

    @Override
    public void onClick(View v) {
        if (slidingLeftCallback != null) {
            switch (v.getId()) {
                case R.id.sliding_menu_checkall:
                    slidingLeftCallback.onCheckAllService();
                    break;
                case R.id.sliding_menu_import:
                    slidingLeftCallback.onImport();
                    break;
                case R.id.sliding_menu_export:
                    slidingLeftCallback.onExport();
                    break;
                case R.id.sliding_menu_setting:
                    slidingLeftCallback.onSetting();
                    break;
                case R.id.sliding_menu_theme:
                    slidingLeftCallback.onChangeTheme();
                    break;
                case R.id.sliding_menu_exit:
                    slidingLeftCallback.onExit();
                    break;
            }
        }
    }

    @Override
    public void onSlidingLeftBgChanged(String path) {
        if (path != null && !isLandscape()) {
            SImageLoader.getInstance().displayImage(path, leftBkView);
        }
    }

    @Override
    public void onSlidingLeftLandBgChanged(String path) {
        if (path != null && isLandscape()) {
            SImageLoader.getInstance().displayImage(path, leftBkView);
        }
    }

    @Override
    public void onSlidingRightBgChanged(String path) {
        if (path != null && !isLandscape()) {
            SImageLoader.getInstance().displayImage(path, rightBkView);
        }
    }

    @Override
    public void onSlidingRightLandBgChanged(String path) {
        if (path != null && isLandscape()) {
            SImageLoader.getInstance().displayImage(path, rightBkView);
        }
    }

    @Override
    public void onSlidingCircleChanged(String path) {
        if (path != null) {
            SImageLoader.getInstance().displayImage(path, circleView);
        }
    }

    @Override
    public void onSelectPage(int index) {

        if (homeProvider.startHome((Activity) mContext, homeList.get(index).getPreferenceKey(), null)) {
            ((Activity) mContext).finish();
        }
    }

    @Override
    public void onNextPage(int next) {
        viewPager.setCurrentItem(next, true);
    }

    @Override
    public void onPreviousPage(int previous) {
        viewPager.setCurrentItem(previous, true);
    }

    public void setViewPagerPage(int index) {
        viewPager.setCurrentItem(index);
    }

    public void onOrentaionChanged(int orientation) {
        String path = getLeftBkPath(orientation);
        if (path != null) {
            SImageLoader.getInstance().displayImage(path, leftBkView);
        }
        path = getRightBkPath(orientation);
        if (path != null) {
            SImageLoader.getInstance().displayImage(path, rightBkView);
        }
    }

}
