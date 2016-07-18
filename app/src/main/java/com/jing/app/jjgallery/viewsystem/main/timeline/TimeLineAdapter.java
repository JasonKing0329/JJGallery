package com.jing.app.jjgallery.viewsystem.main.timeline;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.filesystem.FileBean;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.model.main.timeline.IndicatorController;
import com.jing.app.jjgallery.presenter.main.timeline.TimeLinePresenter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;

import java.util.List;
import java.util.Random;

public class TimeLineAdapter extends BaseAdapter implements
		StickyGridHeadersBaseAdapter, OnClickListener {

	private Context mContext;
	private TimeLinePresenter mPresenter;
	private List<FileBean> fileBeanList;
	private List<String> bkList;

	private IndicatorController indicatorController;

	private Random random;

	private OnHeadImageClickListener onHeadImageClickListener;

	public TimeLineAdapter(Context context, OnHeadImageClickListener listener
			, TimeLinePresenter presenter) {
		mContext = context;
		onHeadImageClickListener = listener;
		this.mPresenter = presenter;

		fileBeanList = mPresenter.getFileBeanList();
		if (Constants.FEATURE_TIMELINE_ENABLE_BK) {
			bkList = mPresenter.getIndicatorBkList();
		}
		random = new Random();
		indicatorController = new IndicatorController(context);

		SImageLoader.getInstance().setDefaultImgRes(R.drawable.icon_loading);
	}

	@Override
	public int getCount() {

		return fileBeanList == null ? 0:fileBeanList.size();
	}

	@Override
	public Object getItem(int position) {
		return fileBeanList == null ? 0:fileBeanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_timeline_grid_item, null);
			holder = new ItemViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.timeline_grid_item_image);
			convertView.setTag(holder);
		}
		else {
			holder = (ItemViewHolder) convertView.getTag();
		}

		String path = fileBeanList.get(position).getPath();
		SImageLoader.getInstance().displayImage(path, holder.image);
		return convertView;
	}

	@Override
	public int getCountForHeader(int header) {
		String tag = mPresenter.getHeaderList().get(header);
		int count = mPresenter.getContentMap().get(tag).size();
		return count;
	}

	@Override
	public int getNumHeaders() {
		return mPresenter.getHeaderList().size();
	}

	@Override
	/**
	 * 经调试发现，非衔接情况和普通的listView模式相同
	 * 衔接情况较特殊，举例第index=2个衔接第index=1个时，当第2个把第1个推至刚好看不见时，这时候会调用一次position=2,
	 * 在此之后，一直到第3个应该出现和接替第2个之前，会无数次调用position=0，也就是在header的固定阶段都有调用position等于0的操作
	 * 不知这是什么原因，但是固定的确实时正确的index，只是不知道滑到过程为何会一直调用position=0
	 */
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		if (Constants.FEATURE_TIMELINE_ENABLE_BK) {
			if (position == 0) {
				if (convertView == null) {
					// Sticky grid view的bug，第一项一开始滑动后总是显示空白
					// 用假数据占据第一个header
					TextView textView = new TextView(mContext);
					textView.setVisibility(View.GONE);
					return textView;
				}
				return convertView;
			}
		}

		if (Constants.DEBUG) {
			Log.d(Constants.LOG_ADAPTER, "getHeaderView " + position);
		}
		HeaderViewHolder holder = null;
		//调试中发现position=1时执行了setTag，但是紧接着下一次执行position=1，getTag还是null，所以这里再加个判断
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_timeline_grid_header, null);
			holder = new HeaderViewHolder();
			holder.bkView = (ImageView) convertView.findViewById(R.id.timeline_indicator_bk);
			holder.imageView = (ImageView) convertView.findViewById(R.id.timeline_indicator_image);
			holder.time = (TextView) convertView.findViewById(R.id.timeline_indicator_text);
			holder.number = (TextView) convertView.findViewById(R.id.timeline_indicator_num);
			convertView.setTag(holder);
		}
		else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		List<FileBean> list = mPresenter.getContentMap()
				.get(mPresenter.getHeaderList().get(position));

		String time = mPresenter.getHeaderList().get(position);
		holder.timeTag = time;
		holder.time.setText(time);
		holder.number.setText(list.size() + "张");

		int index = Math.abs(random.nextInt()) % list.size();

		holder.imageView.setTag(convertView);
		holder.imageView.setOnClickListener(this);

		if (Constants.FEATURE_TIMELINE_ENABLE_BK) {
			int bkIndex = Math.abs(random.nextInt()) % bkList.size();

			indicatorController.loadIndicator(time, holder.imageView, list.get(index).getPath()
					, holder.bkView, bkList.get(bkIndex), true);
		}
		else {
			if (position == 0) {
				//缓存第1个图片，否则因为异步原因和sticky gridview的bug，第1个header总是显示不出来图片
				Bitmap bitmap = indicatorController.getFirstHeadBitmap(list.get(index).getPath());
				holder.imageView.setImageBitmap(bitmap);
			}
			else {
				indicatorController.loadIndicator(time, holder.imageView, list.get(index).getPath(), true);
			}
		}
		return convertView;
	}

	private class HeaderViewHolder {

		ImageView bkView;
		ImageView imageView;
		TextView time;
		TextView number;
		String timeTag;
	}

	private class ItemViewHolder {
		ImageView image;
	}

	@Override
	public void onClick(View v) {
		View view = (View) v.getTag();
		if (onHeadImageClickListener != null) {
			onHeadImageClickListener.onHeadImageClicked(view, v, 0);
		}
	}

}
