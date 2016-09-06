package com.jing.app.jjgallery.config;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.file.FileIO;
import com.king.lib.resmanager.JPrefManager;
import com.king.lib.resmanager.action.JPrefAction;
import com.king.lib.resmanager.exception.JResParseException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JingYang on 2016/7/19 0019.
 * Description:
 */
public class ConfManager {

    private static final String TAG = "ConfManager";
    private static String DATABASE_NAME = "fileencryption.db";
    private static String PREF_NAME="com.jing.app.jjgallery_preferences";
    public static String DB_PATH;
    public static String GDB_DB_PATH = Configuration.APP_DIR_CONF + "/gdata.db";
    // 采用自动更新替代gdata.db的方法，因为jornal的存在，会使重新使用这个db出现问题
    public static String GDB_DB_JOURNAL = Configuration.APP_DIR_CONF + "/gdata.db-journal";

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
        File[] files = new File(Configuration.APP_DIR_CONF_PREF_DEF).listFiles();

        if (files.length > 0) {
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
        }
        return false;
    }

    public static void replaceExtendPref(Context context) {
        File src = new File(DISK_PREF_DEFAULT_PATH);

        // 直接copy到data目录下替换的方法不可行，猜想是程序加载preference的机制所致
//        File target = new File(context.getFilesDir().getParent() + "/shared_prefs/" + PREF_NAME + ".xml");
//        if (target.exists()) {
//            target.delete();
//        }
//        Log.e(TAG, "replaceExtendPref src:" + src.getPath() + ", target:" + target.getPath());
//        try {
//            copyFile(src, target);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 采用解析外部xml文件，以key value的方式重新put进preference中的方法
        try {
            new JPrefManager().loadExtendPreference(DISK_PREF_DEFAULT_PATH, PreferenceManager.getDefaultSharedPreferences(context));
        } catch (JResParseException e) {
            e.printStackTrace();
        }
    }

    public static void saveDefaultPref(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String date = sdf.format(new Date());
        String version = String.valueOf(System.currentTimeMillis());
        SettingProperties.setPrefVersion(context, version);

        File src = new File(context.getFilesDir().getParent() + "/shared_prefs/" + PREF_NAME + ".xml");
        File[] files = new File(Configuration.APP_DIR_CONF_PREF_DEF).listFiles();
        if (files.length > 0) {
            for (File file:files) {
                file.delete();
            }
        }
        File target = new File(Configuration.APP_DIR_CONF_PREF_DEF + "/" + PREF_NAME + "_" + date + "__" + version + ".xml");
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