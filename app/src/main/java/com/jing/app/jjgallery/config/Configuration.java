package com.jing.app.jjgallery.config;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.service.data.SqlConnection;
import com.jing.app.jjgallery.service.data.dao.VersionDao;
import com.jing.app.jjgallery.service.data.impl.VersionDaoImpl;
import com.jing.app.jjgallery.service.file.FileIO;
import com.jing.app.jjgallery.util.ScreenUtils;

import java.io.File;
import java.sql.Connection;

public class Configuration {

	public static final boolean DEBUG = true;
	public static final String TAG_AUTO_VIEW = "fe_autoview";
	public static final String TAG_CONFIG = "fe_Configuration";
	
	public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
	public static final String EXTERNAL_SDCARD_HTC = "/storage/ext_sd";
	public static final String EXTERNAL_SDCARD_SAMSUNG = "/storage/extSdCard";
	
	public static final String APP_ROOT = SDCARD + "/fileencrypt";
	public static final String APP_DIR_IMG = APP_ROOT + "/img";
	public static final String APP_DIR_CROP_IMG = APP_DIR_IMG + "/crop";
	public static final String DOWNLOAD_IMAGE_DEFAULT = APP_DIR_IMG + "/download";
	public static final String APP_DIR_IMG_SAVEAS = APP_ROOT + "/saveas";
	public static final String APP_DIR_DB_HISTORY = APP_ROOT + "/history";
	public static final String APP_DIR_GAME = APP_ROOT + "/game";

	public static final String DATABASE = "fileencrypt";
	public static final String EXTEND_RES_DIR = APP_ROOT + "/res";
	public static final String EXTEND_RES_COLOR = EXTEND_RES_DIR + "/color.xml";
	public static final String EXTEND_RES_DIMEN = EXTEND_RES_DIR + "/dimens.xml";
	public static final String ASSETS_RES_COLOR = "res/color.xml";
	public static final String ASSETS_RES_DIMEN = "res/dimens.xml";

	public static final String APP_DIR_CONF = APP_ROOT + "/conf";
	public static final String APP_DIR_CONF_PREF = APP_DIR_CONF + "/shared_prefs";

	private static int screenWidth;
	private static int screenHeight;
	
	private static int sorderCoverMaxPixel;
	private static int sorderMultiCoverCascadeSize;
	private static int expandSorderCoverMaxPixel;
	private static int chooserItemWidth;
	private static int sorderCoverPreviewSize;

	public static boolean init() {
		File file = new File(APP_ROOT);
		try {
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(APP_DIR_IMG);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(APP_DIR_CROP_IMG);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(DOWNLOAD_IMAGE_DEFAULT);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(APP_DIR_IMG_SAVEAS);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(APP_DIR_DB_HISTORY);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(APP_DIR_GAME);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(EXTEND_RES_DIR);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(APP_DIR_CONF);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(APP_DIR_CONF_PREF);
			if (!file.exists()) {
				file.mkdir();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void initParams(Context context) {

		int width = context.getResources().getDimensionPixelSize(R.dimen.sorder_grid_cover_width);
		int height = context.getResources().getDimensionPixelSize(R.dimen.sorder_grid_cover_height);
		sorderCoverMaxPixel = width * height;

		width = context.getResources().getDimensionPixelSize(R.dimen.sorder_expand_cover_width);
		expandSorderCoverMaxPixel = width * width;
		
		width = context.getResources().getDimensionPixelSize(R.dimen.sorder_multicover_cascade_width);
		height = context.getResources().getDimensionPixelSize(R.dimen.sorder_multicover_cascade_height);
		sorderMultiCoverCascadeSize = width * height;
		
		chooserItemWidth = context.getResources().getDimensionPixelSize(R.dimen.spicture_chooser_item_width);
		sorderCoverPreviewSize = context.getResources().getDimensionPixelSize(R.dimen.sorder_expan_preview_size);

		reloadScreenWH(context);
		
//		PictureManagerUpdate.getInstance().getUnavailableItemImage(context);
//		PictureManagerUpdate.getInstance().getDefaultOrderCover(context);
		
		ThemeManager.init(context);
		
		Log.d(TAG_CONFIG, "screen[" + ScreenUtils.getScreenWidth(context) + ","
				+ ScreenUtils.getScreenHeight(context) + "]");
	}
	
	public static int getSorderCoverMaxPixel() {
		return sorderCoverMaxPixel;
	}
	public static int getSorderCoverPreviewSize() {
		return sorderCoverPreviewSize;
	}
	public static int getChooserItemWidth() {
		return chooserItemWidth;
	}
	public static int getSorderCascadeCoverSize() {
		return sorderMultiCoverCascadeSize;
	}
	public static int getExpandSorderCoverMaxPixel() {
		return expandSorderCoverMaxPixel;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static void reloadScreenWH(Context context) {
		screenWidth = ScreenUtils.getScreenWidth(context);
		screenHeight = ScreenUtils.getScreenHeight(context);
	}

	public static void initVersionChange() {
		SqlConnection.getInstance().connect(DBInfor.DB_PATH);
		try {
			VersionDao dao = new VersionDaoImpl();
			initV6_2Change(SqlConnection.getInstance().getConnection(), dao);
			initV7_0Change(SqlConnection.getInstance().getConnection(), dao);
		} catch (Exception e) {
			SqlConnection.getInstance().close();
			e.printStackTrace();
		}
	}

	private static void initV6_2Change(Connection connection, VersionDao dao) {
		if (dao.isBelowVersion6_2(connection)) {
			if (Constants.DEBUG) {
				Log.e(Constants.LOG_TAG_INIT, "initV6_2Change");
			}
			dao.addImagePixelTable(connection);
		}
	}

	private static void initV7_0Change(Connection connection, VersionDao dao) {
		if (dao.isBelowVersion7_0(connection)) {
			if (Constants.DEBUG) {
				Log.e(Constants.LOG_TAG_INIT, "initV7_0Change");
			}
			dao.addFilesTable(connection);
		}
	}
}
