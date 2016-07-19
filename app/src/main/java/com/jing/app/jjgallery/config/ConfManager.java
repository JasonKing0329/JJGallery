package com.jing.app.jjgallery.config;

import android.content.Context;
import android.util.Log;

import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.file.FileIO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by JingYang on 2016/7/19 0019.
 * Description:
 */
public class ConfManager {

    private static final String TAG = "ConfManager";
    private static String DATABASE_NAME = "fileencryption.db";
    private static String PREF_NAME="com.jing.app.jjgallery_preferences";
    public static String DB_PATH;

    private static String DISK_PREF_DEFAULT_PATH;

    public static boolean prepareDatabase(Context context) {
        DB_PATH = context.getFilesDir().getPath() + "/" + DATABASE_NAME;
        if (!new File(DB_PATH).exists()) {
            try {
                InputStream in = context.getAssets().open(DATABASE_NAME);
                FileOutputStream out = new FileOutputStream(DB_PATH);
                byte[] buffer = new byte[1024];
                int byteread = 0;
                while ((byteread = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteread);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean exportDatabase(Context context) {
        DB_PATH = context.getFilesDir().getPath() + "/" + DATABASE_NAME;
        if (new File(DB_PATH).exists()) {
            try {
                Calendar calendar = Calendar.getInstance();
                StringBuffer targetPath = new StringBuffer(Configuration.APP_DIR_DB_HISTORY);
                targetPath.append("/");
                targetPath.append(calendar.get(Calendar.YEAR)).append("_");
                targetPath.append(calendar.get(Calendar.MONTH) + 1).append("_");
                targetPath.append(calendar.get(Calendar.DAY_OF_MONTH)).append("_");
                targetPath.append(calendar.get(Calendar.HOUR)).append("_");
                targetPath.append(calendar.get(Calendar.MINUTE)).append("_");
                targetPath.append(calendar.get(Calendar.SECOND)).append(".db");

                InputStream in = new FileInputStream(DB_PATH);
                FileOutputStream out = new FileOutputStream(targetPath.toString());
                byte[] buffer = new byte[1024];
                int byteread = 0;
                while ((byteread = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteread);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * @param context
     */
    public static boolean replaceDatabase(Context context, String target) {

        if (target == null || !new File(target).exists()) {
            return false;
        }
        //先检查是否存在，存在则删除
        DB_PATH = context.getFilesDir().getPath() + "/" + DATABASE_NAME;
        File defaultDb = new File(DB_PATH);
        if (defaultDb.exists()) {
            defaultDb.delete();
        }
        try {
            InputStream in = new FileInputStream(target);
            File file = new File(DB_PATH);
            OutputStream fileOut = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                fileOut.write(buffer, 0, length);
            }

            fileOut.flush();
            fileOut.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void initParams(Context context) {
        FileIO.copyResFromAssets(context, Configuration.ASSETS_RES_COLOR, Configuration.EXTEND_RES_COLOR);
        FileIO.copyResFromAssets(context, Configuration.ASSETS_RES_DIMEN, Configuration.EXTEND_RES_DIMEN);
    }

    /**
     * 检查配置目录是否存在默认配置文件
     * @param context
     * @return
     */
    public static boolean checkExtendConf(Context context) {
        File[] files = new File(Configuration.APP_DIR_CONF_PREF).listFiles();

        for (File file:files) {
            if (file.getName().startsWith(PREF_NAME)) {
                try {
                    String[] arr = file.getName().split("__");
                    String version = arr[1].split("\\.")[0];

                    String curVersion = SettingProperties.getPrefVersion(context);
                    Log.e(TAG, "checkExtendConf version:" + version + " curVersion:" + curVersion);
                    if (!version.equals(curVersion)) {
                        DISK_PREF_DEFAULT_PATH = file.getPath();
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return false;
    }

    public static void replaceExtendPref(Context context) {
        File src = new File(DISK_PREF_DEFAULT_PATH);
        File target = new File(context.getFilesDir().getParent() + "/shared_prefs/" + PREF_NAME + ".xml");
        if (target.exists()) {
            target.delete();
        }
        Log.e(TAG, "replaceExtendPref src:" + src.getPath() + ", target:" + target.getPath());
        try {
            copyFile(src, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDefaultPref(Context context) {
        String version = String.valueOf(System.currentTimeMillis());
        SettingProperties.setPrefVersion(context, version);

        File src = new File(context.getFilesDir().getParent() + "/shared_prefs/" + PREF_NAME + ".xml");
        File target = null;
        if (DISK_PREF_DEFAULT_PATH != null) {
            target = new File(DISK_PREF_DEFAULT_PATH);
            target.delete();
        }
        target = new File(Configuration.APP_DIR_CONF_PREF + "/" + PREF_NAME + "__" + version + ".xml");
        Log.e(TAG, "saveDefaultPref src:" + src.getPath() + ", target:" + target.getPath());
        try {
            copyFile(src, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourcefile, File targetFile)
            throws IOException {

        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourcefile);
        BufferedInputStream inbuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream out = new FileOutputStream(targetFile);
        BufferedOutputStream outbuff = new BufferedOutputStream(out);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len = 0;
        while ((len = inbuff.read(b)) != -1) {
            outbuff.write(b, 0, len);
        }

        // 刷新此缓冲的输出流
        outbuff.flush();

        // 关闭流
        inbuff.close();
        outbuff.close();
        out.close();
        input.close();

    }

}