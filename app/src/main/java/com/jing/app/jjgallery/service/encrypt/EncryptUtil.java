package com.jing.app.jjgallery.service.encrypt;

import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.service.encrypt.action.Generater;

import java.io.File;

/**
 * Created by Administrator on 2016/9/6.
 */
public class EncryptUtil {

    private static Encrypter encrypter = EncrypterFactory.create();
    private static Generater generater = EncrypterFactory.generater();

    public static String encryptFile(File file) {
        String generateName = generater.generateName();
        if (!encrypter.isEncrypted(file)) {
            encrypter.encrypt(file, generateName);
            return file.getParent() + "/" + generateName + encrypter.getFileExtra();
        }
        return file.getPath();
    }
}
