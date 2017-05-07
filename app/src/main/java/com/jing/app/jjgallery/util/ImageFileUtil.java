package com.jing.app.jjgallery.util;

import android.graphics.BitmapFactory;

import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;

import java.io.File;

public class ImageFileUtil {

	public static void getWidthHeight(FileBean bean, File file) {
		byte datas[] = EncryptUtil.getEncrypter().decipherToByteArray(file);
		if (datas != null) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;// 对bitmap不分配空间，只是用于计算文件options的各种属性(本程序需要计算width,height)
			BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
			bean.setWidth(opts.outWidth);
			bean.setHeight(opts.outHeight);
		}
	}
}
