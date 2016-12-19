package com.jing.app.jjgallery.gdb.view;

import android.content.Intent;
import android.view.View;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;

/**
 * 通过star page的 record list打开record设置为打开后保留StarActivity，为了防止在star page>record page(finish)>star page>record page(finish)...过程中
 * ，star page(StarActivity)被无限打开，将StarActivity的启动模式设置为singleTask
 * singleTask模式下，重新启动一个StarActivity的时候不会执行onCreate，这时候就需要在StarActivity的onNewIntent设置完成后在onResume里重新获取starId进行重新加载了
 * 把onResume的操作直接写在StarFragment里
 */
public class StarActivity extends BaseActivity {

    public static final String KEY_STAR_ID = "key_star_id";
    private StarFragment starFragment;

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_pull_zoom_header;
    }

    @Override
    public void initController() {
    }

    @Override
    public void initView() {
        starFragment = new StarFragment();
        starFragment.setStarId(getIntent().getIntExtra(KEY_STAR_ID, -1));
        starFragment.setActionbar(mActionBar);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, starFragment)
                .commit();
    }

    @Override
    public void initBackgroundWork() {

    }

    @Override
    public void onIconClick(View view) {
        super.onIconClick(view);
        starFragment.onIconClick(view);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 通过调试发现必须先super.onNewIntent(intent)然后setIntent(intent)，否则直接从这个intent里读还是读取到了之前的starId
        setIntent(intent);
    }

    @Override
    public void onBack() {
        super.onBack();
        finish();
    }
}
