package com.jing.app.jjgallery.viewsystem.publicview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.main.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/10 10:58
 */
public class CrashDialogActivity extends Activity {
    public static final String KEY_MSG = "key_msg";

    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.cancel)
    TextView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_common);
        ButterKnife.bind(this);
        info.setText(getIntent().getStringExtra(KEY_MSG));
    }

    @OnClick({R.id.confirm, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
