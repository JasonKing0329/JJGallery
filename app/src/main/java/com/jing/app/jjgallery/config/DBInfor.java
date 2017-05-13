package com.jing.app.jjgallery.config;

import android.content.Context;

import com.jing.app.jjgallery.bean.order.STag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class DBInfor {

	public static String DB_PATH;
	public static final String TABLE_ORDER = "fe_orders";
	public static final String TABLE_ORDER_LIST = "fe_order_list";
	public static final String TABLE_IMAGE_PIXEL = "fe_image_pixel";
	public static final String TABLE_FILES = "fe_files";
	public static final String TABLE_TAG = "fe_tag";
	public static final String TABLE_SEQUENCE = "sqlite_sequence";
	public static final String TABLE_ORDER_COUNT = "fe_orders_count";
	public static final String TO_COL_ID = "o_id";
	public static final String TO_COL_NAME = "o_name";
	public static final String TO_COL_COVER = "o_cover";
	public static final String TO_COL_TAGID = "o_tag_id";
	public static final int NUM_TO_COL_ID = 1;
	public static final int NUM_TO_COL_NAME = 2;
	public static final int NUM_TO_COL_COVER = 3;
	public static final int NUM_TO_COL_TAGID = 4;
	public static final String TOL_COL_ID = "ol_id";
	public static final String TOL_COL_OID = "ol_order_id";
	public static final String TOL_COL_PATH = "ol_item_path";
	public static final int NUM_TOL_COL_ID = 1;
	public static final int NUM_TOL_COL_OID = 2;
	public static final int NUM_TOL_COL_PATH = 3;
	public static final String[] TOC_COL = new String[]{"o_id","o_all","o_year","o_month","o_week","o_day","o_lastyear","o_lastmonth","o_lastweek","o_lastday"};
	public static final String TOC_ALL = "o_all";
	public static final String TOC_YEAR = "o_year";
	public static final String TOC_MONTH= "o_month";
	public static final String TOC_WEEK = "o_week";
	public static final String TOC_DAY = "o_day";
	public static final int NUM_TOC_COL_ID = 1;
	public static final String TIP_COL_ID = "fip_id";
	public static final String TIP_COL_NAME = "fip_name";
	public static final String TIP_COL_WIDTH = "fip_width";
	public static final String TIP_COL_HEIGHT = "fip_height";
	public static final String TIP_COL_OTHER = "fip_other";
	public static final String TF_COL_ID = "f_id";
	public static final String TF_COL_PATH = "f_path";
	public static final String TF_COL_TIME = "f_time";
	public static final String TF_COL_TIMETAG = "f_time_tag";
	public static final String TF_COL_WIDTH = "f_width";
	public static final String TF_COL_HEIGHT = "f_height";
	public static final String TF_COL_OTHER = "f_other";
	public static final int NUM_TIP_COL_ID = 1;
	public static final int NUM_TIP_COL_NAME = 2;
	public static final int NUM_TIP_COL_WIDTH = 3;
	public static final int NUM_TIP_COL_HEIGHT = 4;
	public static final int NUM_TIP_COL_OTHER = 5;
	public static final String TT_COL_ID = "t_id";
	public static final String TT_COL_TAG = "t_tag";
	public static final STag DEFAULT_TAG = new STag(6, "other");
	public static final int NUM_TT_COL_ID = 1;
	public static final int NUM_TT_COL_TAG = 2;
	public static final String TS_COL_NAME = "name";
	public static final String TS_COL_SEQ = "seq";
	public static final int NUM_TS_COL_NAME = 1;
	public static final int NUM_TS_COL_SEQ = 2;

	// gdata.db只支持外部扩展目录，不内置到程序中
	public static String GDB_DB_PATH = ConfManager.GDB_DB_PATH;
	public static String GDB_GAME_DB_PATH = ConfManager.GDB_GAME_DB_PATH;
	public static String GDB_FAVOR_DB_PATH = ConfManager.GDB_FAVOR_DB_PATH;

	public static boolean prepareDatabase(Context context) {
		boolean result = ConfManager.prepareDatabase(context);
		DB_PATH = ConfManager.DB_PATH;
		return  result;
	}
}
