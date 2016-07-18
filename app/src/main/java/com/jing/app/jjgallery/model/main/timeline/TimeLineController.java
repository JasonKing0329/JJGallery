package com.jing.app.jjgallery.model.main.timeline;

import android.os.AsyncTask;

import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.data.SqlConnection;
import com.jing.app.jjgallery.service.data.dao.SOrderDao;
import com.jing.app.jjgallery.service.data.dao.TimeLineDao;
import com.jing.app.jjgallery.service.data.impl.SOrderDaoImpl;
import com.jing.app.jjgallery.service.data.impl.TimeLineDaoImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeLineController {

	private TimeLineCallback mCallback;

	private List<HashMap<String, String>> totalGroupList;
	private List<HashMap<String, String>> groupList;
	private HashMap<String, List<FileBean>> contentMap;
	private List<String> headerList;
	private List<FileBean> fileBeanList;
	private List<String> bkList;

	// Sticky grid view的bug，第一项一开始滑动后总是显示空白
	private boolean enableFakeData = true;

	public TimeLineController(TimeLineCallback callback) {
		mCallback = callback;
		totalGroupList = new ArrayList<HashMap<String,String>>();
		groupList = new ArrayList<HashMap<String,String>>();
		contentMap = new HashMap<String, List<FileBean>>();
		headerList = new ArrayList<String>();
	}

	public HashMap<String, List<FileBean>> getContentMap() {
		return contentMap;
	}

	public List<FileBean> getFileBeanList() {
		return fileBeanList;
	}

	public List<String> getHeaderList() {
		return headerList;
	}

	/**
	 * 异步方法
	 * 查询时间线上所有的文件，并按照日期分组
	 * 适用于v7.2及以后
	 */
	public boolean loadTimeLineItems() {
		new LoadTask().execute();
		return true;
	}

	private class LoadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void aVoid) {
			mCallback.onTimeLineItemsReady();
			super.onPostExecute(aVoid);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				SqlConnection.getInstance().connect(DBInfor.DB_PATH);
				TimeLineDao dao = new TimeLineDaoImpl();

				fileBeanList = dao.queryAllFileBeans(SqlConnection.getInstance().getConnection(), DBInfor.TF_COL_TIME + " DESC");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SqlConnection.getInstance().close();
			}

			if (fileBeanList == null || fileBeanList.size() == 0) {
				return null;
			}

			if (Constants.FEATURE_TIMELINE_ENABLE_BK) {
				// Sticky grid view的bug，第一项一开始滑动后总是显示空白
				// 用假数据占据第一个header
				if (enableFakeData) {
					contentMap.put("fakeData", new ArrayList<FileBean>());
					headerList.add("fakeData");
				}
			}

			List<FileBean> subList = null;
			for (int i = 0; i < fileBeanList.size(); i ++) {
				String timeTag = fileBeanList.get(i).getTimeTag();
				if (contentMap.get(timeTag) == null) {
					subList = new ArrayList<FileBean>();
					contentMap.put(timeTag, subList);
					headerList.add(timeTag);
				}
				subList.add(fileBeanList.get(i));
			}
			return null;
		}
	}

	/**
	 * 同步方法（耗时较短）
	 * @return
     */
	public List<String> getIndicatorBkList() {
		if (bkList == null) {
			try {
				SqlConnection.getInstance().connect(DBInfor.DB_PATH);
				SOrderDao dao = new SOrderDaoImpl();

				bkList = dao.queryOrderItemsByName(Constants.TIMELINE_INDICATOR_DEFAULT_ORDER, SqlConnection.getInstance().getConnection());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SqlConnection.getInstance().close();
			}
		}
		return bkList;
	}
}
