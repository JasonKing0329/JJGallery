package com.jing.app.jjgallery.model.main.login;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public interface ILoginModel {

    public void signBasic(String user, String password);

    public void signWithFingerPrint();

    public void isFingerPrintEnable();
}
