package com.jing.app.jjgallery.viewsystem.sub.book;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jing.app.jjgallery.service.image.ImageValue;

import java.util.List;

public class BookPageAdapter extends BaseAdapter {

	private static final String TAG = "BookPageAdapter";
	private List<List<ImageValue>> imageList;
	private Context mContext;
	private OnClickListener imageListener;

	public BookPageAdapter(Context context, List<List<ImageValue>> list) {
		mContext = context;
		imageList = list;
	}

	public void setOnImageClickListener(OnClickListener listener) {
		imageListener = listener;
	}

	@Override
	public int getCount() {
		return imageList == null ? 0:imageList.size();
	}

	@Override
	public Object getItem(int position) {
		return imageList == null ? 0:imageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d(TAG, "getView " + position);
		ViewHolder holder = null;
		convertView = null;
		if (convertView == null) {
			holder = new ViewHolder();
			holder.bookPage = new BookPage(mContext);
			convertView = holder.bookPage.getConvertView(imageList.get(position));
			convertView.setTag(holder);
		}
		else  {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.bookPage.setImageListener(imageListener);
		holder.bookPage.bindDatas(imageList.get(position));
		return convertView;
	}

	private class ViewHolder {
		BookPage bookPage;
	}
}
