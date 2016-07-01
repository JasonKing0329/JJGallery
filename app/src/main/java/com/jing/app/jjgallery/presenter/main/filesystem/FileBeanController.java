package com.jing.app.jjgallery.presenter.main.filesystem;

import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.util.FileSizeUtil;
import com.jing.app.jjgallery.util.ImageFileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileBeanController {

	private SimpleDateFormat dateFormat;
	private Encrypter encrypter;
	
	public FileBeanController() {

		dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		encrypter = EncrypterFactory.create();
	}
	
	public FileBean createFileBean(File file) {
		FileBean bean = new FileBean();
		bean.setPath(file.getPath());
		bean.setTime(file.lastModified());
		bean.setTimeTag(dateFormat.format(new Date(file.lastModified())));
		try {
			bean.setSize(FileSizeUtil.getFileSize(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		ImageFileUtil.getWidthHeight(bean, file, encrypter);
		return bean;
	}
}
