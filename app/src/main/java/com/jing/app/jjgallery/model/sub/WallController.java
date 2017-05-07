package com.jing.app.jjgallery.model.sub;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.model.main.order.SOrderManager;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.encrypt.impl.SimpleEncrypter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class WallController {

	private final String WALL_DEFAULT_KEY = "wall_default_key";
	private Context context;
	private int wallIndex;
	private int[] wallRes = new int[] {
		R.drawable.wall_bk1, R.drawable.wall_bk2, R.drawable.wall_bk3
		, R.drawable.wall_bk4, R.drawable.wall_bk5
	};
	
	public WallController(Context context) {
		this.context = context;
	}
	
	public int getDefaultWallRes() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int res = preferences.getInt(WALL_DEFAULT_KEY, R.drawable.wall_bk1);
		return res;
	}

	public void saveDefaultWallRes() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(WALL_DEFAULT_KEY, wallRes[wallIndex]);
		editor.commit();
	}
	
	public int changeWallRes() {
		if (wallIndex == wallRes.length - 1) {
			wallIndex = 0;
		}
		else {
			wallIndex ++;
		}
		return wallRes[wallIndex];
	}

	public void deleteFile(String path) {
		File file = new File(path);
		if (EncryptUtil.isEncrypted(file)) {
			file.delete();
			path = path.replace(EncryptUtil.getFileExtra(), EncryptUtil.getNameExtra());
			file = new File(path);
			file.delete();
			Log.i("FileEncryption", "delete file " + path);
		}
		else {
			if (file.exists()) {
				file.delete();
				Log.i("FileEncryption", "delete file " + path);
			}
		}
	}

	public void deleteFile(SOrder currentOrder, int index) {
		new SOrderManager(null).deleteItemFromOrder(index, currentOrder);
	}

	public List<String> loadFolderItems(String path) {
		List<String> list = new ArrayList<>();
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String filename) {

					return filename.endsWith(SimpleEncrypter.FILE_EXTRA);
				}
			});
			if (files != null && files.length > 0) {
				for (File f:files) {
					list.add(f.getPath());
				}
			}
		}
		return list;
	}

	public SOrder loadOrder(int orderId) {

		SOrderManager manager = new SOrderManager(null);
		SOrder order = manager.queryOrder(orderId);
		manager.loadOrderItems(order);
		return order;
	}
}
