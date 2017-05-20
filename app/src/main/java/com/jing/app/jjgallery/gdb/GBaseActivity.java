package com.jing.app.jjgallery.gdb;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.jing.app.jjgallery.JJApplication;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.main.bg.BackgroundManager;
import com.jing.app.jjgallery.viewsystem.publicview.ProgressManager;
import com.jing.app.jjgallery.viewsystem.publicview.toast.TastyToast;

/**
 * Created by Administrator on 2017/5/20 0020.
 */

public abstract class GBaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ProgressManager progressManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayHelper.enableFullScreen();
        DisplayHelper.disableScreenshot(this);
        
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);

        setContentView(getContentView());
        initController();
        initView();
        initBackgroundWork();
    }

    protected abstract int getContentView();

    protected abstract void initController();

    protected abstract void initView();

    protected abstract void initBackgroundWork();

    public void showProgress(String text) {
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    public boolean dismissProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            return true;
        }
        return  false;
    }

    public void showProgressCycler() {
        if (progressManager == null) {
            progressManager = new ProgressManager(this);
            BackgroundManager.getInstance().addProgressSubscriber(progressManager);
        }
        progressManager.showProgressCycler();
    }

    public boolean dismissProgressCycler() {
        if (progressManager.isShowing()) {
            progressManager.dismissProgressCycler();
            return true;
        }
        return false;
    }

    public void showToastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void showToastShort(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String text, int type) {
        showToastLib(text, type, TastyToast.LENGTH_LONG);
    }

    public void showToastShort(String text, int type) {
        showToastLib(text, type, TastyToast.LENGTH_SHORT);
    }

    public void showToastLib(String text, int type, int time) {
        switch (type) {
            case ProgressProvider.TOAST_SUCCESS:
                TastyToast.makeText(this, text, time, TastyToast.SUCCESS);
                break;
            case ProgressProvider.TOAST_ERROR:
                TastyToast.makeText(this, text, time, TastyToast.ERROR);
                break;
            case ProgressProvider.TOAST_WARNING:
                TastyToast.makeText(this, text, time, TastyToast.WARNING);
                break;
            case ProgressProvider.TOAST_INFOR:
                TastyToast.makeText(this, text, time, TastyToast.INFO);
                break;
            case ProgressProvider.TOAST_DEFAULT:
                TastyToast.makeText(this, text, time, TastyToast.DEFAULT);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((JJApplication) getApplication()).removeActivity(this);
        BackgroundManager.getInstance().removeProgressSubscriber(progressManager);
    }

}
