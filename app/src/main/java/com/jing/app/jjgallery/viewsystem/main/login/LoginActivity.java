package com.jing.app.jjgallery.viewsystem.main.login;

import android.content.Intent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.model.main.login.LoginParams;
import com.jing.app.jjgallery.presenter.main.LoginPresenter;
import com.jing.app.jjgallery.viewsystem.HomeSelecter;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {

    private LoginPresenter loginPresenter;
    private AutoCompleteTextView mUserEdit;
    private EditText mPwdEdit;

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initController() {
        loginPresenter = new LoginPresenter(this, this);
    }

    @Override
    protected void initView() {

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
        new HomeSelecter().startHomeActivity(this, null);
        finish();
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
}

