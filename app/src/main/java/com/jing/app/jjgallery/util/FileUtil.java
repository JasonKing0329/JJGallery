package com.jing.app.jjgallery.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jing.app.jjgallery.service.encrypt.EncryptUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.R.attr.value;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/24 11:20
 */
public class FileUtil {

    /**
     * move file from src path to target path, src will be deleted
     * @param src
     * @param target
     */
    public static void moveFile(String src, String target) {
        File srcfFile = new File(src);
        copyFile(srcfFile, new File(target));
        srcfFile.delete();
        DebugLog.e("src[" + src + "], target[" + target + "]");
    }

    /**
     * copy file from src to target
     * @param src
     * @param target
     */
    public static void copyFile(File src, File target) {
        try {
            if (!target.exists()) {
                target.createNewFile();
            }
            InputStream fileIn = new FileInputStream(src);
            OutputStream fileOut = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileIn.read(buffer))>0){
                fileOut.write(buffer, 0, length);
            }

            fileOut.flush();
            fileOut.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the width and height of image file
     * @param filePath
     * @param isEncrypted
     * @return value[0]: width, value[1]: height
     */
    public static int[] getImageFileSize(String filePath, boolean isEncrypted) {
        int[] values = null;
        if (isEncrypted) {
            byte datas[] = EncryptUtil.getEncrypter().decipherToByteArray(new File(filePath));
            if (datas != null) {
                values = new int[2];
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;// 对bitmap不分配空间，只是用于计算文件options的各种属性
                BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
                values[0] = opts.outWidth;
                values[0] = opts.outHeight;
            }
        }
        else {
            values = new int[2];
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;// 对bitmap不分配空间，只是用于计算文件options的各种属性
            BitmapFactory.decodeFile(filePath, opts);
            values[0] = opts.outWidth;
            values[0] = opts.outHeight;
        }
        return values;
    }
}
