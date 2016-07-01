package com.jing.app.jjgallery.service.image;

import com.jing.app.jjgallery.bean.filesystem.FilePageItem;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.data.SqlConnection;
import com.jing.app.jjgallery.service.data.dao.ImageValueDao;
import com.jing.app.jjgallery.service.data.impl.ImageValueDaoImpl;

import java.util.ArrayList;
import java.util.List;


public class ImageValueController implements ImageValueListener {

	@Override
	public void onCreate(String path, int width, int height) {
		
		String name = ImageValue.generateName(path);
		
		ImageValue value = queryImagePixel(name);
		if (value == null) {
			value = new ImageValue();
			value.setPath(path);
			value.setName(name);
			value.setWidth(width);
			value.setHeight(height);
			addImagePixel(value);
		}
	}

	public ImageValue queryImagePixel(String key) {
		ImageValue value = null;
		SqlConnection.getInstance().connect(DBInfor.DB_PATH);
		ImageValueDao dao = new ImageValueDaoImpl();

		value = dao.queryImageValue(key, SqlConnection.getInstance().getConnection());

		SqlConnection.getInstance().close();
		return value;
	}

	public void addImagePixel(ImageValue value) {
		SqlConnection.getInstance().connect(DBInfor.DB_PATH);
		ImageValueDao dao = new ImageValueDaoImpl();

		dao.insertImagePixel(value, SqlConnection.getInstance().getConnection());

		SqlConnection.getInstance().close();
	}

	/**
	 * 
	 * @param pathList
	 * @param values should be not null but size is 0.
	 */
	public void queryImagePixelFrom(List<String> pathList, List<ImageValue> values) {
		if (pathList != null && values != null) {

			List<String> keyList = new ArrayList<String>();
			for (String path:pathList) {
				values.add(null);
				keyList.add(ImageValue.generateName(path));
			}
			
			queryImagePixel(keyList, values);
			
			for (int i = 0; i < values.size(); i ++) {
				if (values.get(i) == null) {
					ImageValue value = new ImageValue();
					value.setPath(pathList.get(i));
					values.set(i, value);
				}
				else {
					values.get(i).setPath(pathList.get(i));
				}
			}
		}
	}
	
	private void queryImagePixel(List<String> keyList, List<ImageValue> values) {
		if (keyList != null && values != null) {
			SqlConnection.getInstance().connect(DBInfor.DB_PATH);
			ImageValueDao dao = new ImageValueDaoImpl();
			
			dao.queryImageValues(keyList, values, SqlConnection.getInstance().getConnection());

			SqlConnection.getInstance().close();

		}
	}
	
	public void queryImagePixelFrom(List<FilePageItem> list) {

		if (list != null) {
			List<ImageValue> values = new ArrayList<ImageValue>();
			List<String> keyList = new ArrayList<String>();
			for (FilePageItem item:list) {
				values.add(null);
				keyList.add(ImageValue.generateName(item.getFile().getPath()));
			}
			
			queryImagePixel(keyList, values);
			
			for (int i = 0; i < list.size(); i ++) {
				if (values.get(i) != null) {
					values.get(i).setPath(list.get(i).getFile().getPath());
					list.get(i).setImageValue(values.get(i));
				}
			}
		}
	}

}
