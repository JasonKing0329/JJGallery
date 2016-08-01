package com.jing.app.jjgallery.viewsystem.main.gdb;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;

public class StarActivity extends BaseActivity {

    public static final String KEY_STAR_ID = "key_star_id";
    private StarFragment starFragment;

    @Override
    protected boolean isActionBarNeed() {
        return false;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pull_zoom_header;
    }

    @Override
    protected void initController() {
    }

    @Override
    protected void initView() {
        starFragment = new StarFragment(getIntent().getIntExtra(KEY_STAR_ID, -1));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, starFragment)
                .commit();
    }

    @Override
    protected void initBackgroundWork() {

    }
}
