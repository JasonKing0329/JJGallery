package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jing.app.jjgallery.Application;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.order.SOrderPresenter;
import com.jing.app.jjgallery.service.image.SImageLoader;

import java.util.List;

public class SOrderGridAdapter extends BaseAdapter {

	public enum CoverMode {
		SINGLE, CASCADE, CASCADE_ROTATE, GRID
	}
	
	private Context context;
	private List<SOrder> sorderList;
	private int coverWidth;
	private CoverMode mCoverMode;
	private int multiNum_cascade;

	private SOrderPresenter mPresenter;

	public SOrderGridAdapter(Context context, List<SOrder> orders) {
		this.context = context;
		sorderList = orders;
		coverWidth = context.getResources().getDimensionPixelSize(R.dimen.sorder_grid_cover_width);
		setCoverMode(SettingProperties.getSOrderCoverMode(context));
		multiNum_cascade = SettingProperties.getCascadeCoverNumber(context);
		SImageLoader.getInstance().setDefaultImgRes(R.drawable.ic_folder_sub);
	}

	public void setSorderList(List<SOrder> sorderList) {
		this.sorderList = sorderList;
	}

	public List<SOrder> getSorderList() {
		return sorderList;
	}

	public void setPresenter(SOrderPresenter mPresenter) {
		this.mPresenter = mPresenter;
	}

	public void setCoverMode(int prefValue) {
		switch (prefValue) {
			case PreferenceValue.SORDER_COVER_CASCADE:
				mCoverMode = CoverMode.CASCADE;
				break;
			case PreferenceValue.SORDER_COVER_CASCADE_ROTATE:
				mCoverMode = CoverMode.CASCADE_ROTATE;
				break;
			case PreferenceValue.SORDER_COVER_GRID:
				mCoverMode = CoverMode.GRID;
				break;
			default:
				mCoverMode = CoverMode.SINGLE;
				break;
		}
	}
	
	public CoverMode getCoverMode() {
		return mCoverMode;
	}

	@Override
	public int getCount() {
		
		return sorderList == null ? 0 : sorderList.size();
	}

	@Override
	public Object getItem(int position) {

		return sorderList == null ? null : sorderList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		SOrder order = sorderList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			Log.d("SOrderGridAdapter", "getView " + position + " convertView == null");
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_sorder_grid, null);
			holder = new ViewHolder();
			holder.cover = (MultiCoverView) convertView.findViewById(R.id.sorder_grid_multicover);
			holder.name = (TextView) convertView.findViewById(R.id.sorder_grid_name);
			if (Application.isLollipop()) {
				convertView.findViewById(R.id.sorder_grid_item_layout).setBackgroundResource(R.drawable.selector_order_background_l);
			}
			holder.cover.setSize(coverWidth, coverWidth);
			// MultiCoverView内部要创建view
			createCover(holder.cover, position);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(order.getName() + "(" + order.getItemNumber() + ")");
		setCoverValues(holder.cover, position);
		//new LoadCoverTask(order, holder.cover).execute();
		return convertView;
	}

	private class ViewHolder {
		public MultiCoverView cover;
		public TextView name;
	}

	private void createCover(MultiCoverView coverView, int position) {

		List<String> covers = sorderList.get(position).getCoverList();
		if (covers == null || covers.size() == 0) {
			coverView.addSingleCover(null);
		}
		else if (covers.size() == 1) {
			coverView.addSingleCover(covers.get(0));
		}
		else {
			if (mCoverMode == CoverMode.CASCADE) {
				for (String path:covers) {
					coverView.addCover(path);
				}

				int spaceX = 0, spaceY = 0;
				if (multiNum_cascade == 2) {
					spaceX = 80;
					spaceY = 110;
				}
				else if (multiNum_cascade == 3) {
					spaceX = 60;
					spaceY = 80;
				}
				else if (multiNum_cascade == 4) {
					spaceX = 40;
					spaceY = 50;
				}
				coverView.applyStyleCascade(spaceX, spaceY, multiNum_cascade);
			}
			else if (mCoverMode == CoverMode.CASCADE_ROTATE) {
				for (String path:covers) {
					coverView.addCover(path);
				}

				coverView.applyStyleCascadeRotate();
			}
			else if (mCoverMode == CoverMode.GRID) {
				for (String path:covers) {
					coverView.addCoverPath(path);
				}

				coverView.applyStyleGrid();
			}
		}
	}

	private void setCoverValues(MultiCoverView coverView, int position) {
		List<String> covers = sorderList.get(position).getCoverList();
		if (covers == null || covers.size() == 0) {
			coverView.setSingleCover(null);
		}
		else if (covers.size() == 1) {
			coverView.setSingleCover(covers.get(0));
		}
		else {
			if (mCoverMode == CoverMode.CASCADE || mCoverMode == CoverMode.CASCADE_ROTATE) {
				coverView.setCascadeCovers(covers);
			}
			else if (mCoverMode == CoverMode.GRID) {
				coverView.setGridCovers(covers);
			}
		}
	}

}
