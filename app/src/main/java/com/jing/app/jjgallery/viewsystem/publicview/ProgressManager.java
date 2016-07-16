package com.jing.app.jjgallery.viewsystem.publicview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.image.PictureManagerUpdate;
import com.jing.app.jjgallery.viewsystem.main.bg.ProgressSubscriber;

/**
 * Created by Administrator on 2016/7/16 0016.
 * manager of ProgressButton
 */
public class ProgressManager implements ProgressSubscriber {

    private Context context;
    private ViewGroup progressView;
    private ProgressButton progressButton;

    public ProgressManager(Activity activity) {
        context = activity;
        progressView = (ViewGroup) activity.findViewById(R.id.progress);
        progressButton = (ProgressButton) activity.findViewById(R.id.progressButton);
        String path = SettingProperties.getPreference(activity, PreferenceKey.PREF_BG_PROGRESS);
        if (path != null) {
            progressButton.setImageBitmap(PictureManagerUpdate.getInstance().createCircleBitmap(
                    path, activity));
        }
        progressButton.setAnimationStripWidth(activity.getResources().getInteger(R.integer.progress_arc_sweep));

    }

    /**
     * show progress with auto-run style
     */
    public void showProgressCycler() {
        progressView.setVisibility(View.VISIBLE);
        progressButton.startAnimating();
    }

    public void dismissProgressCycler() {
        progressView.setVisibility(View.GONE);
        progressButton.stopAnimating();
    }

    @Override
    public void onProgressSrcChanged(@NonNull String path) {
        progressButton.setImageBitmap(PictureManagerUpdate.getInstance().createCircleBitmap(
                path, context));
    }
}
