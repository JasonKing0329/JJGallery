package com.jing.app.jjgallery.gdb.model;

import android.content.Context;

import com.jing.app.jjgallery.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: loadVideos in GHomeActivity, clear int GHomeActivity.onDestroy
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/18 13:53
 */
public class VideoModel {

    private static Map<String, String> map;

    public static void loadVideos(Context context) {
        map = new HashMap<>();
        File file = new File(Configuration.getGdbVideoDir(context));
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f:files) {
                try {
                    String name = f.getName().substring(0, f.getName().lastIndexOf('.'));
                    map.put(name, f.getPath());
                } catch (Exception e) {

                }
            }
        }
    }

    public static void clear() {
        map = null;
    }

    public static String getVideoPath(String name) {
        if (map == null) {
            return null;
        }
        return map.get(name);
    }
}
