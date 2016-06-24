package com.jing.app.jjgallery.model.main.login;

import android.content.Context;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class LoginModel implements ILoginModel {

    private LoginCallback mCallback;
    private FingerPrintController fingerPrintController;

    public LoginModel(Context context, LoginCallback callback) {
        mCallback = callback;
        fingerPrintController = new FingerPrintController(context);
    }

    @Override
    public void signBasic(String user, String password) {
        if ("jyjyjyjyjyjyjyjy".equals(password)) {
            mCallback.onLoginSuccess();
        }
        else {
            mCallback.onLoginFailed(LoginParams.TYPE_ERROR_WRONG_PWD, "密码错误");
        }
    }

    @Override
    public void signWithFingerPrint() {
        startFingerPrintDialog();
    }

    @Override
    public boolean isFingerPrintEnable() {
        return fingerPrintController.isSupported();
    }

    private void startFingerPrintDialog() {
        if (fingerPrintController.hasRegistered()) {
            boolean withPW = false;
            fingerPrintController.showIdentifyDialog(withPW, new FingerPrintController.SimpleIdentifyListener() {

                @Override
                public void onSuccess() {
                    mCallback.onLoginSuccess();
                }

                @Override
                public void onFail() {
                    mCallback.onLoginFailed(LoginParams.TYPE_ERROR_WRONG_FINGERPRINT, "指纹验证失败");
                }

                @Override
                public void onCancel() {
                    mCallback.onLoginFailed(LoginParams.TYPE_ERROR_CANCEL_FINGERPRINT, "取消指纹验证");
                }
            });
        }
        else {
            mCallback.onLoginFailed(LoginParams.TYPE_ERROR_UNREGIST_FINGERPRINT, "未注册指纹");
        }
    }

}
