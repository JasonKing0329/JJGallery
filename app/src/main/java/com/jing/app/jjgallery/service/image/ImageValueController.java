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

			// pathList中如果有已经不存在的路径，values将不会包含，所以查询后values的size和pathList可能不对等
			queryImagePixel(pathList, values);
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

			// pathList中如果有已经不存在的路径，values将不会包含，所以查询后values的size和pathList可能不对等
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
		}
	}

}
