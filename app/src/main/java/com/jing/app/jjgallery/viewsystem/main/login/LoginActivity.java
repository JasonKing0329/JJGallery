package com.jing.app.jjgallery.viewsystem.main.login;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.AppCheckBean;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.config.ConfManager;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.model.main.login.LoginParams;
import com.jing.app.jjgallery.presenter.main.LoginPresenter;
import com.jing.app.jjgallery.res.ColorRes;
import com.jing.app.jjgallery.res.JResource;
import com.jing.app.jjgallery.service.file.FileDBService;
import com.jing.app.jjgallery.service.file.OnServiceProgressListener;
import com.jing.app.jjgallery.service.http.BaseUrl;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.HomeSelecter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;
import com.jing.app.jjgallery.viewsystem.publicview.DownloadDialog;
import com.jing.app.jjgallery.viewsystem.publicview.ProgressButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener
    , OnServiceProgressListener{

    private LoginPresenter loginPresenter;
    private AutoCompleteTextView mUserEdit;
    private EditText mPwdEdit;
    private View progressView;
    private ProgressButton progressButton;

    private TextView progressTextView;

    private boolean executeInsertProcess;
    private boolean isServiceBound;

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initController() {
        // 设置base url
        BaseUrl.getInstance().setBaseUrl(SettingProperties.getGdbServerBaseUrl(this));

        loginPresenter = new LoginPresenter(this, this);
    }

    @Override
    public void initView() {

        mActionBar.addBackIcon();

        progressView = findViewById(R.id.login_init);
        progressButton = (ProgressButton) findViewById(R.id.progressButton);
        progressButton.setAnimationStripWidth(15);

    }

    @Override
    public void initBackgroundWork() {
        showInitProgress();
        // 执行初始化操作
        new InitTask().execute();

        if (!TextUtils.isEmpty(SettingProperties.getGdbServerBaseUrl(this))) {
            // 检测App更新，必须在配置过服务器以后
            try {
                loginPresenter.checkAppUpdate(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showInitProgress() {
        progressView.setVisibility(View.VISIBLE);
        progressButton.startAnimating();
    }

    private void hideInitProgress() {
        progressView.setVisibility(View.GONE);
        progressButton.stopAnimating();
    }

    /**
     * 初始化操作包括初始化文件目录、数据库、参数、扩展resource、配置文件
     */
    private class InitTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPostExecute(Boolean hasPref) {
            if (hasPref) {
                new DefaultDialogManager().showWarningActionDialog(LoginActivity.this
                        , getResources().getString(R.string.login_extend_pref_exist)
                        , getResources().getString(R.string.yes)
                        , null
                        , getResources().getString(R.string.no)
                        , new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    new PrefTask().execute();
                                }
                                else {
                                    afterPrefCheck();
                                    hideInitProgress();
                                }
                            }
                        });
            }
            else {
                afterPrefCheck();
                hideInitProgress();
            }
            super.onPostExecute(hasPref);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // 文件目录
            if (!Configuration.init()) {
                showToastLong(getString(R.string.error_app_root_create_fail), ProgressProvider.TOAST_ERROR);
            }
            // 数据库
            if (!DBInfor.prepareDatabase(LoginActivity.this)) {
                showToastLong(getString(R.string.error_database_create_fail), ProgressProvider.TOAST_ERROR);
            }
            // 已有数据库升级
            Configuration.initVersionChange();
            // 初始化参数
            Configuration.initParams(LoginActivity.this);
            // 创建扩展resource资源
            ConfManager.initParams(LoginActivity.this);
            // 加载扩展resource资源
            JResource.initializeColors();

            // 检查扩展配置
            return ConfManager.checkExtendConf(LoginActivity.this);
        }
    }

    private class PrefTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            afterPrefCheck();
            hideInitProgress();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ConfManager.replaceExtendPref(LoginActivity.this);
            return null;
        }
    }

    private void afterPrefCheck() {
        applyExtendColors();
        // Open SettingActivity when application is started for the first time.
        // Application will be considered as initialized only after sign in successfully.
        if (TextUtils.isEmpty(SettingProperties.getGdbServerBaseUrl(this))) {
            showToastLong(getString(R.string.server_not_conf), ProgressProvider.TOAST_WARNING);
            Intent intent = new Intent().setClass(this, SettingsActivity.class);
            startActivity(intent);
        }
        else {
            if (SettingProperties.isAppInited(this)) {
                showPage();
            }
            else {
                Intent intent = new Intent().setClass(this, SettingsActivity.class);
                startActivity(intent);
            }
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
    public void onAppUpdateFound(final AppCheckBean bean) {
        String msg = String.format(getString(R.string.app_update_found), bean.getAppVersion());
        new DefaultDialogManager().showOptionDialog(LoginActivity.this, null, msg
                , getResources().getString(R.string.yes)
                , null
                , getResources().getString(R.string.no)
                , new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            startDownloadNewApp(bean);
                        }
                    }
                });
    }

    private void startDownloadNewApp(final AppCheckBean bean) {
        final DownloadDialog dialog = new DownloadDialog(this, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                DownloadItem item = new DownloadItem();
                item.setKey(bean.getAppName());
                item.setFlag("app");
                item.setSize(bean.getAppSize());
                item.setName(bean.getAppName());
                List<DownloadItem> list = new ArrayList<>();
                list.add(item);

                data.put("items", list);
                data.put("savePath", Configuration.APP_DIR_CONF);
                data.put("noOption", true);
            }
        });
        dialog.setOnDownloadListener(new DownloadDialog.OnDownloadListener() {
            @Override
            public void onDownloadFinish(String path) {
                loginPresenter.installApp(LoginActivity.this, path);
                dialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 100);
            }
        });
        dialog.show();
    }

    @Override
    public void onSignSuccess() {
        // Application will be considered as initialized only after sign in successfully.
        if (!SettingProperties.isAppInited(this)) {
            SettingProperties.setAppInited(this);
        }

        superUser();
    }

    @Override
    public void onSignFailed(int type, String msg) {
        switch (type) {
            case LoginParams.TYPE_ERROR_WRONG_PWD:
                showToastLong(msg, ProgressProvider.TOAST_ERROR);
                break;
            case LoginParams.TYPE_ERROR_CANCEL_FINGERPRINT:
                finish();
                break;
            case LoginParams.TYPE_ERROR_WRONG_FINGERPRINT:
                break;
            case LoginParams.TYPE_ERROR_UNREGIST_FINGERPRINT:
                showLoginForm();
                showToastLong(msg, ProgressProvider.TOAST_ERROR);
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
                                isServiceBound = bindService(new Intent().setClass(LoginActivity.this, FileDBService.class)
                                        , connection, BIND_AUTO_CREATE);
                            }
                        }
                        else if (which == DialogInterface.BUTTON_NEGATIVE) {

                            showLoading();
                            if (!isServiceRunning()) {
                                isServiceBound = bindService(new Intent().setClass(LoginActivity.this, FileDBService.class)
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
            if (isServiceBound) {
                unbindService(connection);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void applyExtendColors() {
        mActionBar.updateBackground(JResource.getColor(this, ColorRes.ACTIONBAR_BK, ThemeManager.getInstance().getBasicColorResId(this)));
    }

}

