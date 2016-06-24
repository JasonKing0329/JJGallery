package com.jing.app.jjgallery.viewsystem.main.login;

import android.view.View;
import android.widget.Toast;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.model.main.login.LoginParams;
import com.jing.app.jjgallery.presenter.main.LoginPresenter;
import com.jing.app.jjgallery.viewsystem.HomeSelecter;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginView {

    private LoginPresenter loginPresenter;

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
        if (loginPresenter.isFingerPrintEnabled()) {
            loginPresenter.signFingerPrint();
        }
        else {
            showLoginForm();
        }
    }

    private void showLoginForm() {
        findViewById(R.id.login_form).setVisibility(View.VISIBLE);
    }

    @Override
    protected void initBackgroundWork() {

    }

    @Override
    public void onSignSuccess() {
        new HomeSelecter().startHomeActivity(null);
    }

    @Override
    public void onSignFailed(int type, String msg) {
        switch (type) {
            case LoginParams.TYPE_ERROR_WRONG_PWD:
                break;
            case LoginParams.TYPE_ERROR_CANCEL_FINGERPRINT:
                break;
            case LoginParams.TYPE_ERROR_WRONG_FINGERPRINT:
                break;
            case LoginParams.TYPE_ERROR_UNREGIST_FINGERPRINT:
                showLoginForm();
                break;
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}

