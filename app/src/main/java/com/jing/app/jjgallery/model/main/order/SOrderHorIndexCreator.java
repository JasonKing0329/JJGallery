package com.jing.app.jjgallery.model.main.order;

import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.viewsystem.main.order.HorizontalIndexView;

import java.util.ArrayList;
import java.util.List;

public class SOrderHorIndexCreator {

	private int PAGE_MAX_ITEM;
	private int totalPages;
	private int defaultMode;

	private SOrderManager sOrderManager;
	
	public SOrderHorIndexCreator(SOrderManager manager) {
		this.sOrderManager = manager;
		PAGE_MAX_ITEM = 16;
	}

	public void setPageMaxItem(int num) {
		PAGE_MAX_ITEM = num;
	}

	public List<HorizontalIndexView.IndexItem> createIndex(List<SOrder> orderList) {
		return createIndex(orderList, defaultMode);
	}

	public List<HorizontalIndexView.IndexItem> createIndex(List<SOrder> orderList, int mode) {

		if (orderList == null) {
			return null;
		}

		defaultMode = mode;
		List<HorizontalIndexView.IndexItem> list = null;
		totalPages = (orderList.size() - 1) / PAGE_MAX_ITEM + 1;
		if (totalPages > 1) {
			list = new ArrayList<>();
			HorizontalIndexView.IndexItem indexItem = null;
			SOrder order = null;

			if (mode == PreferenceValue.ORDERBY_NAME) {
				for (int i = 1; i <= totalPages; i ++) {
					indexItem = new HorizontalIndexView.IndexItem();
					order = orderList.get((i - 1) * PAGE_MAX_ITEM);
					if (order.getName().length() < 2) {
						indexItem.index = "" + order.getName().charAt(0);
					}
					else {
						indexItem.index = order.getName().substring(0, 2).toLowerCase();
					}
					list.add(indexItem);
				}
			}
			else {
				for (int i = 1; i <= totalPages; i ++) {
					indexItem = new HorizontalIndexView.IndexItem();
					indexItem.index = "" + i;
					list.add(indexItem);
				}
			}
		}
		return list;
	}

	public List<SOrder> getPageItem(List<SOrder> totalList, int index) throws HorizontalIndexView.PageIndexOutOfBoundsException {
		if (index < 1 || index > totalPages) {
			throw new HorizontalIndexView.PageIndexOutOfBoundsException();
		}
		List<SOrder> list = new ArrayList<SOrder>();
		int start = (index - 1) * PAGE_MAX_ITEM, end = 0;
		if (index == totalPages) {
			end = totalList.size();
		}
		else {
			end = index * PAGE_MAX_ITEM;
		}
		for (int i = start; i < end; i ++) {
			list.add(totalList.get(i));
		}
		return list;
	}
}
