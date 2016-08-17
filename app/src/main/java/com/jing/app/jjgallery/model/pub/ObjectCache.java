package com.jing.app.jjgallery.model.pub;

/**
 * Created by JingYang on 2016/8/17 0017.
 * Description:
 */
public class ObjectCache {
    private static Object data;

    public static void putData(Object data) {
        ObjectCache.data = data;
    }

    public static Object getData() {
        return data;
    }

}
