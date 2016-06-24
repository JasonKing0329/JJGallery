package com.jing.app.jjgallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(getContentView());

        initController();
        initView();

        initBackgroundWork();
    }

    protected abstract int getContentView();

    protected abstract void initController();

    protected abstract void initView();

    protected abstract void initBackgroundWork();

}
