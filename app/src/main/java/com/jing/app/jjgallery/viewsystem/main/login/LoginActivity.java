package com.jing.app.jjgallery.viewsystem.main.login;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.ConfManager;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.model.main.login.LoginParams;
import com.jing.app.jjgallery.presenter.main.LoginPresenter;
import com.jing.app.jjgallery.res.ColorRes;
import com.jing.app.jjgallery.res.JResource;
import com.jing.app.jjgallery.service.file.FileDBService;
import com.jing.app.jjgallery.service.file.OnServiceProgressListener;
import com.jing.app.jjgallery.util.DBExportor;
import com.jing.app.jjgallery.viewsystem.HomeSelecter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener
    , OnServiceProgressListener{

    private LoginPresenter loginPresenter;
    private AutoCompleteTextView mUserEdit;
    private EditText mPwdEdit;

    private TextView progressTextView;

    private boolean executeInsertProcess;

    @Override
    protected boolean isActionBarNeed() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initController() {

        if (!Configuration.init()) {
            Toast.makeText(this, R.string.error_app_root_create_fail, Toast.LENGTH_LONG).show();
        }
        if (!DBInfor.prepareDatabase(this)) {
            Toast.makeText(this, R.string.error_database_create_fail, Toast.LENGTH_LONG).show();
        }
        Configuration.initVersionChange();
        Configuration.initParams(this);
        ConfManager.initParams(this);

        JResource.initializeColors();

        loginPresenter = new LoginPresenter(this, this);
    }

    protected void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    protected void initView() {

        applyExtendColors();

        if (ConfManager.checkExtendConf(this)) {
            new DefaultDialogManager().showWarningActionDialog(this
                    , getResources().getString(R.string.login_extend_pref_exist)
                    , getResources().getString(R.string.yes)
                    , null
                    , getResources().getString(R.string.no)
                    , new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                ConfManager.replaceExtendPref(LoginActivity.this);
                                finish();
//                                afterPrefCheck();
                            }
                            else {
                                afterPrefCheck();
                            }
                        }
                    });
        }
        else {
            afterPrefCheck();
        }
    }

    private void afterPrefCheck() {
        // Open SettingActivity when application is started for the first time.
        // Application will be considered as initialized only after sign in successfully.
        if (SettingProperties.isAppInited(this)) {
            showPage();
        }
        else {
            Intent intent = new Intent().setClass(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    private void showPage() {
        if (SettingProperties.isFingerPrintEnable(this) && loginPresenter.isFingerPrintEnabled()) {
            loginPresenter.signFingerPrint();
        }
        else {
            showLoginForm();
        }
    }

    private void showLoginForm() {
        findViewById(R.id.login_form).setVisibility(View.VISIBLE);
        findViewById(R.id.login_signin).setOnClickListener(this);
        mUserEdit = (AutoCompleteTextView) findViewById(R.id.login_username);
        mPwdEdit = (EditText) findViewById(R.id.login_pwd);
    }

    @Override
    protected void initBackgroundWork() {

    }

    @Override
    public void onSignSuccess() {
        // Application will be considered as initialized only after sign in successfully.
        SettingProperties.setAppInited(this);

        superUser();
    }

    @Override
    public void onSignFailed(int type, String msg) {
        switch (type) {
            case LoginParams.TYPE_ERROR_WRONG_PWD:
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                break;
            case LoginParams.TYPE_ERROR_CANCEL_FINGERPRINT:
                finish();
                break;
            case LoginParams.TYPE_ERROR_WRONG_FINGERPRINT:
                break;
            case LoginParams.TYPE_ERROR_UNREGIST_FINGERPRINT:
                showLoginForm();
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_signin:
                loginPresenter.sign(mUserEdit.getText().toString(), mPwdEdit.getText().toString());
                break;
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        showPage();
    }

    protected void superUser() {
//		startService(new Intent().setClass(this, FileDBService.class));
        new DefaultDialogManager().showWarningActionDialog(this
                , getResources().getString(R.string.login_start_service_insert)
                , getResources().getString(R.string.yes)
                , getResources().getString(R.string.allno)
                , getResources().getString(R.string.no)
                , new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            executeInsertProcess = true;
                            showLoading();
                            if (!isServiceRunning()) {
                                bindService(new Intent().setClass(LoginActivity.this, FileDBService.class)
                                        , connection, BIND_AUTO_CREATE);
                            }
                        }
                        else if (which == DialogInterface.BUTTON_NEGATIVE) {

                            showLoading();
                            if (!isServiceRunning()) {
                                bindService(new Intent().setClass(LoginActivity.this, FileDBService.class)
                                        , connection, BIND_AUTO_CREATE);
                            }
                        }
                        else {//netrual, all no
                            onServiceDone();
                        }
                    }
                });
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.king.app.fileencryption.service.FileDBService"
                    .equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void showLoading() {
        setContentView(R.layout.activity_login_loading);
        progressTextView = (TextView) findViewById(R.id.login_loading_progress_num);
//        loadingBkView = (ImageView) findViewById(R.id.login_loading_bk);
//		loadingBkView.startAnimation(getLoadingBkAnimation());
    }

    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FileDBService fileDBService = ((FileDBService.FileDbBinder) service).getService();
            fileDBService.setOnProgressListener(LoginActivity.this);
            fileDBService.startWork(executeInsertProcess);
        }
    };

    @Override
    public void onServiceProgress(int progress) {
        progressTextView.setText(progress + "%");
    }

    @Override
    public void onServiceDone() {
        new HomeSelecter(this).startDefaultHome(this, null);
        finish();
        applyAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            // if select all no, then FC occurs in xiaomi phone(Caused by: java.lang.IllegalArgumentException: Service not registered)
            unbindService(connection);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void applyExtendColors() {
        mActionBar.updateBackground(JResource.getColor(this, ColorRes.ACTIONBAR_BK, R.color.actionbar_bk_blue));
    }

}

