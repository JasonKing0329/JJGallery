package com.jing.app.jjgallery.service.image;

import com.jing.app.jjgallery.bean.filesystem.FilePageItem;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.data.SqlConnection;
import com.jing.app.jjgallery.service.data.dao.ImageValueDao;
import com.jing.app.jjgallery.service.data.impl.ImageValueDaoImpl;

import java.util.ArrayList;
import java.util.List;


public class ImageValueController {

	public ImageValue queryImagePixel(String path) {
		ImageValue value;
		SqlConnection.getInstance().connect(DBInfor.DB_PATH);
		ImageValueDao dao = new ImageValueDaoImpl();

		value = dao.queryImageValue(path, SqlConnection.getInstance().getConnection());

		SqlConnection.getInstance().close();
		return value;
	}

	/**
	 * 
	 * @param pathList
	 * @param values should be not null but size is 0.
	 */
	public void queryImagePixelFrom(List<String> pathList, List<ImageValue> values) {
		if (pathList != null && values != null) {

			queryImagePixel(pathList, values);
			
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

	/**
	 *
	 * @param pathList
	 * @param values should be not null but size is 0.
     */
	private void queryImagePixel(List<String> pathList, List<ImageValue> values) {
		if (pathList != null && values != null) {
			SqlConnection.getInstance().connect(DBInfor.DB_PATH);
			ImageValueDao dao = new ImageValueDaoImpl();
			
			dao.queryImageValues(pathList, values, SqlConnection.getInstance().getConnection());

			SqlConnection.getInstance().close();

		}
	}
	
	public void queryImagePixelFrom(List<FilePageItem> list) {

		if (list != null) {
			List<ImageValue> values = new ArrayList<ImageValue>();
			List<String> pathList = new ArrayList<String>();
			for (FilePageItem item:list) {
				values.add(null);
				pathList.add(item.getFile().getPath());
			}
			
			queryImagePixel(pathList, values);
			
			for (int i = 0; i < list.size(); i ++) {
				if (values.get(i) != null) {
					values.get(i).setPath(list.get(i).getFile().getPath());
					list.get(i).setImageValue(values.get(i));
				}
			}
		}
	}

}
