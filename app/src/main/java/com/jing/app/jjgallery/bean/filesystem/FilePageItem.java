package com.jing.app.jjgallery.bean.filesystem;

import android.graphics.drawable.Drawable;

import com.jing.app.jjgallery.service.image.ImageValue;

import java.io.File;

public class FilePageItem {

	private File file;
	private long date;
	private String strDate;
	private String originName;
	private String displayName;
	private Drawable icon;
	private ImageValue imageValue;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getStrDate() {
		return strDate;
	}
	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}
	public String getOriginName() {
		return originName;
	}
	public void setOriginName(String originName) {
		this.originName = originName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public ImageValue getImageValue() {
		return imageValue;
	}
	public void setImageValue(ImageValue imageValue) {
		this.imageValue = imageValue;
	}
}
