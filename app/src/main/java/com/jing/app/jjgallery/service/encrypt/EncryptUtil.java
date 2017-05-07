package com.jing.app.jjgallery.service.encrypt;

import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.service.encrypt.action.Generater;

import java.io.File;

/**
 * Created by Administrator on 2016/9/6.
 * encrypt util only for GDB module
 */
public class EncryptUtil {

    private static Encrypter encrypter = EncrypterFactory.create();
    private static Generater generater = EncrypterFactory.generater();

    public static String encryptFile(File file) {
        String path = file.getPath();
        if (!encrypter.isEncrypted(file)) {
            // encrypt files under gdb directory with original name
            if (path.startsWith(Configuration.GDB_IMG)) {
                path = encrypter.encrypt(file);
            }
            else {
                path = encrypter.encrypt(file, generater.generateName());
            }
        }
        return path;
    }

    public static boolean decipherFile(File file, String target) {
        boolean result = false;
        // only '.jfe' file can be deciphered
        if (file.getName().endsWith(encrypter.getFileExtra())) {
            String path = file.getPath();
            // encrypt files under gdb directory with original name
            if (path.startsWith(Configuration.GDB_IMG)) {
                result = encrypter.restore(file, target, true);
            }
            else {
                result = encrypter.restore(file, target);
            }
        }
        return result;
    }

    public static boolean isEncrypted(File file) {
        return encrypter.isEncrypted(file);
    }

    public static String getOriginName(File file) {
        if (encrypter.isEncrypted(file)) {
            String path = file.getPath();
            // encrypt files under gdb directory with original name
            if (path.startsWith(Configuration.GDB_IMG)) {
                return file.getName();
            }
            else {
                return encrypter.decipherOriginName(file);
            }
        }
        else {
            return file.getPath();
        }
    }

    public static String getNameExtra() {
        return encrypter.getNameExtra();
    }

    public static String getFileExtra() {
        return encrypter.getFileExtra();
    }

    public static void deleteFile(File file) {
        encrypter.deleteFile(file);
    }

    public static Encrypter getEncrypter() {
        return encrypter;
    }
}
