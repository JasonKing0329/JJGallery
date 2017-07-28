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
    public static final String VALUE_SORDER_VIEW_ACCESS = "Access";

    public static final String VALUE_TIMELINE_VIEW_TIMELINE = "Timeline";
    public static final String VALUE_TIMELINE_VIEW_WATERFALL = "Waterfall";

    public static final String VALUE_BOOK_SWITCH_VER = "Vertical";
    public static final String VALUE_BOOK_SWITCH_LAND = "Horizontal";

    public static final int START_VIEW_FILEMANAGER = 0;
    public static final int START_VIEW_SORDER = 1;
    public static final int START_VIEW_TIMELINE = 2;
//    public static final int START_VIEW_GUIDE = 3;
    // has value, but don't be saved in preference
    public static final int START_GDB = 3;

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

    public static final int SORDER_CARD_TOP_NUMBER = 10;

    public static final String EXPLORE_OPEN_SURF = "Surf";
    public static final String EXPLORE_OPEN_WALL = "Wall";
    public static final String EXPLORE_OPEN_BOOK = "Book";

    public static final int HTTP_MAX_DOWNLOAD = 3;
    public static final int GDB_LATEST_NUM = 30;
    /**
     * 最高同时下载7个
     */
    public static final int HTTP_MAX_DOWNLOAD_UPLIMIT = 7;

    public static final int GDB_SR_ORDERBY_NONE = 0;
    public static final int GDB_SR_ORDERBY_NAME = 1;
    public static final int GDB_SR_ORDERBY_DATE = 2;
    public static final int GDB_SR_ORDERBY_SCORE =3;
    public static final int GDB_SR_ORDERBY_FK =4;
    public static final int GDB_SR_ORDERBY_CUM =5;
    public static final int GDB_SR_ORDERBY_STAR1 =6;
    public static final int GDB_SR_ORDERBY_STARCC1 =7;
    public static final int GDB_SR_ORDERBY_BJOB =8;
    public static final int GDB_SR_ORDERBY_STAR2 =9;
    public static final int GDB_SR_ORDERBY_STARCC2 =10;
    public static final int GDB_SR_ORDERBY_BAREBACK =11;
    public static final int GDB_SR_ORDERBY_SCOREFEEL =12;
    public static final int GDB_SR_ORDERBY_STORY =13;
    public static final int GDB_SR_ORDERBY_FOREPLAY =14;
    public static final int GDB_SR_ORDERBY_RIM =15;
    public static final int GDB_SR_ORDERBY_RHYTHM =16;
    public static final int GDB_SR_ORDERBY_SCENE =17;
    public static final int GDB_SR_ORDERBY_CSHOW =18;
    public static final int GDB_SR_ORDERBY_SPECIAL =19;
    public static final int GDB_SR_ORDERBY_HD =20;
    public static final int GDB_SR_ORDERBY_FK1 =21;
    public static final int GDB_SR_ORDERBY_FK2 =22;
    public static final int GDB_SR_ORDERBY_FK3 =23;
    public static final int GDB_SR_ORDERBY_FK4 =24;
    public static final int GDB_SR_ORDERBY_FK5 =25;
    public static final int GDB_SR_ORDERBY_FK6 =26;
    public static final int GDB_SR_ORDERBY_SCORE_BASIC =27;
    public static final int GDB_SR_ORDERBY_SCORE_EXTRA =28;
    public static final int GDB_SR_ORDERBY_STAR =29;
    public static final int GDB_SR_ORDERBY_STARC =30;

    public static final int GDB_SR_ORDERBY_TIME =31;
    public static final int GDB_SR_ORDERBY_SIZE =32;
}
