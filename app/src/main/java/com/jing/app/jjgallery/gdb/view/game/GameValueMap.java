package com.jing.app.jjgallery.gdb.view.game;

/**
 * Created by Administrator on 2017/1/15 0015.
 * 数据库固定值与UI之间的显示映射
 */

public class GameValueMap {

    /**
     * 将数据库里的字段转化为UI显示的字段
     * @param type
     * @return
     */
    public static String fromCoachType(String type) {
        if ("1".equals(type)) {
            return "Top";
        }
        else if ("0".equals(type)) {
            return "Bottom";
        }
        else {
            return "Half";
        }
    }

    /**
     * 将UI显示的字段转化为数据库里的字段
     * @param type
     * @return
     */
    public static String toCoachType(String type) {
        if ("Top".equals(type)) {
            return "1";
        }
        else if ("Bottom".equals(type)) {
            return "0";
        }
        else {
            return "0.5";
        }
    }
}
