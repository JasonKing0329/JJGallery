package com.jing.app.jjgallery.config;

/**
 * Created by JingYang on 2016/7/19 0019.
 * Description:
 */
public class PreferenceValue {
    public static final String VALUE_FM_VIEW_LIST = "List";
    public static final String VALUE_FM_VIEW_THUMB = "Thumbfolder";
    public static final String VALUE_FM_VIEW_INDEX = "Index";

    public static final String VALUE_SORDER_VIEW_GRID = "Grid";
    public static final String VALUE_SORDER_VIEW_THUMB = "Thumbfolder";
    public static final String VALUE_SORDER_VIEW_INDEX = "Index";

    public static final String VALUE_TIMELINE_VIEW_TIMELINE = "Timeline";
    public static final String VALUE_TIMELINE_VIEW_WATERFALL = "Waterfall";

    public static final int START_VIEW_FILEMANAGER = 0;
    public static final int START_VIEW_SORDER = 1;
    public static final int START_VIEW_TIMELINE = 2;
    public static final int START_VIEW_GUIDE = 3;
    // has value, but don't be saved in preference
    public static final int START_GDB = 4;

    public static final int AUTOPLAY_MODE_SEQUENCE = 0;
    public static final int AUTOPLAY_MODE_RANDOM = 1;
    public static final int AUTOPLAY_MODE_REPEATABLE = 2;

    public static final int ORDERBY_NONE = 0;
    public static final int ORDERBY_NAME = 1;
    public static final int ORDERBY_DATE = 2;
    public static final int ORDERBY_ITEMNUMBER =3;

    public static final int SORDER_COVER_SINGLE =0;
    public static final int SORDER_COVER_CASCADE =1;
    public static final int SORDER_COVER_CASCADE_ROTATE =2;
    public static final int SORDER_COVER_GRID =3;

    public static final int SORDER_CASECADE_NUM_DEFAULT = 3;
    public static final int SORDER_PAGE_NUM_DEFAULT = 16;

    public static final String EXPLORE_OPEN_SURF = "Surf";
    public static final String EXPLORE_OPEN_WALL = "Wall";
    public static final String EXPLORE_OPEN_BOOK = "Book";

    public static final int GDB_SR_ORDERBY_NONE = 0;
    public static final int GDB_SR_ORDERBY_NAME = 1;
    public static final int GDB_SR_ORDERBY_DATE = 2;
    public static final int GDB_SR_ORDERBY_SCORE =3;
    public static final int GDB_SR_ORDERBY_FK =4;
    public static final int GDB_SR_ORDERBY_CUM =5;

}
