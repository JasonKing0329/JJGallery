package com.jing.app.jjgallery.viewsystem.main;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.HomeBean;

/**
 * left sliding menu of home
 * the bottom bar to select file manager or sorder
 */
public class SlidingSelectorAdapter extends PagerAdapter implements View.OnClickListener {

	public interface OnPageSelectListener {
		void onSelectPage(int index);
		void onNextPage(int next);
		void onPreviousPage(int previous);
	}

	private List<HomeBean> homeList;
	private Context context;
	public int index;

	private OnPageSelectListener onPageSelectListener;
	
	public SlidingSelectorAdapter(Context context, List<HomeBean> list, OnPageSelectListener listener) {
		homeList = list;
		this.context = context;
		onPageSelectListener = listener;
	}

	@Override
	public int getCount() {
		return homeList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView(mTextViews.get(position));
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		ViewHolder holder = null;
		holder = new ViewHolder();
		holder.view = LayoutInflater.from(context).inflate(R.layout.adapter_viewpager_home, null);
		holder.textView = (TextView) holder.view.findViewById(R.id.viewpager_home_text);
		holder.btnPrevious = (ImageView) holder.view.findViewById(R.id.viewpager_home_previous);
		holder.btnNext = (ImageView) holder.view.findViewById(R.id.viewpager_home_next);
		//没用
//		if (container.getTag() == null) {
//			holder = new ViewHolder();
//			holder.view = LayoutInflater.from(context).inflate(R.layout.adapter_viewpager_home, null);
//			holder.textView = (TextView) holder.view.findViewById(R.id.viewpager_home_text);
//			holder.btnPrevious = (ImageView) holder.view.findViewById(R.id.viewpager_home_previous);
//			holder.btnNext = (ImageView) holder.view.findViewById(R.id.viewpager_home_next);
//			container.addView(holder.view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//			container.setTag(holder);
//		}
//		else {
//			holder = (ViewHolder) container.getTag();
//		}

		if (position == 0) {
			holder.btnPrevious.setVisibility(View.INVISIBLE);
			holder.btnNext.setVisibility(View.VISIBLE);
			holder.btnNext.startAnimation(getAnimation());
			holder.btnPrevious.clearAnimation();
		}
		else if (position == 3) {
			holder.btnPrevious.setVisibility(View.VISIBLE);
			holder.btnNext.setVisibility(View.INVISIBLE);
			holder.btnPrevious.startAnimation(getAnimation());
			holder.btnNext.clearAnimation();
		}
		else {
			holder.btnPrevious.setVisibility(View.VISIBLE);
			holder.btnNext.setVisibility(View.VISIBLE);
			holder.btnPrevious.startAnimation(getAnimation());
			holder.btnNext.startAnimation(getAnimation());
		}
		holder.btnNext.setTag(position);
		holder.btnNext.setOnClickListener(nextListener);
		holder.btnPrevious.setTag(position);
		holder.btnPrevious.setOnClickListener(previousListener);
		holder.textView.setTag(position);
		holder.textView.setText(homeList.get(position).getName());
		holder.textView.setOnClickListener(this);
		container.addView(holder.view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		return holder.view;
		
	}

	private Animation getAnimation() {
		Animation animation = new AlphaAnimation(0, 1);
		animation.setRepeatMode(Animation.REVERSE);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setDuration(500);
		return animation;
	}

	private class ViewHolder {
		View view;
		TextView textView;
		ImageView btnPrevious;
		ImageView btnNext;
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		if (onPageSelectListener != null) {
			onPageSelectListener.onSelectPage(position);
		}
	}

	private View.OnClickListener nextListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag() + 1;
			if (onPageSelectListener != null) {
				onPageSelectListener.onNextPage(position);
			}
		}
	};

	private View.OnClickListener previousListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag() - 1;
			if (onPageSelectListener != null) {
				onPageSelectListener.onPreviousPage(position);
			}
		}
	};
}
