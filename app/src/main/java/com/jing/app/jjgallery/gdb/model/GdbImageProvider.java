package com.jing.app.jjgallery.gdb.model;

import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/21 10:51
 */
public class GdbImageProvider {

    /**
     *
     * @param name
     * @param indexPackage save the real index, can be null
     * @return
     */
    public static String getRecordRandomPath(String name, IndexPackage indexPackage) {
        return getImagePath(Configuration.GDB_IMG_RECORD, name, -1, indexPackage);
    }

    public static String getRecordPath(String name, int index) {
        return getImagePath(Configuration.GDB_IMG_RECORD, name, index, null);
    }

    public static List<String> getRecordPathList(String name) {
        return getImagePathList(Configuration.GDB_IMG_RECORD, name);
    }

    public static boolean hasRecordFolder(String name) {
        return hasFolder(Configuration.GDB_IMG_RECORD, name);
    }

    /**
     *
     * @param name
     * @param indexPackage save the real index, can be null
     * @return
     */
    public static String getStarRandomPath(String name, IndexPackage indexPackage) {
        return getImagePath(Configuration.GDB_IMG_STAR, name, -1, indexPackage);
    }

    public static String getStarPath(String name, int index) {
        return getImagePath(Configuration.GDB_IMG_STAR, name, index, null);
    }

    public static List<String> getStarPathList(String name) {
        return getImagePathList(Configuration.GDB_IMG_STAR, name);
    }

    public static boolean hasStarFolder(String name) {
        return hasFolder(Configuration.GDB_IMG_STAR, name);
    }

    private static boolean hasFolder(String parent, String name) {
        File file = new File(parent + "/" + name);
        return file.exists() && file.isDirectory();
    }

    /**
     *
     * @param parent
     * @param name
     * @param index if random, then -1
     * @param indexPackage save the true index, can be null
     * @return
     */
    private static String getImagePath(String parent, String name, int index, IndexPackage indexPackage) {
        String path;
        if (hasFolder(parent, name)) {
            File file = new File(parent + "/" + name);
            File[] files = file.listFiles();
            if (files.length == 0) {
                path = parent + "/" + name + EncryptUtil.getFileExtra();
            }
            else {
                if (index == -1 || index >= files.length) {
                    int pos = Math.abs(new Random().nextInt()) % files.length;
                    if (indexPackage != null) {
                        indexPackage.index = pos;
                    }
                    return files[pos].getPath();
                }
                else {
                    return files[index].getPath();
                }
            }
        }
        else {
            path = parent + "/" + name + EncryptUtil.getFileExtra();
        }
        return path;
    }

    private static List<String> getImagePathList(String parent, String name) {
        List<String> list = new ArrayList<>();
        File file = new File(parent + "/" + name);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f:files) {
                list.add(f.getPath());
            }
            Collections.shuffle(list);
        }
        return list;
    }

    public static class IndexPackage {
        public int index;
    }
}
