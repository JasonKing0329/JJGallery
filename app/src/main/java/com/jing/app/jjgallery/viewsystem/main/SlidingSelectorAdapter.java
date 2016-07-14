package com.jing.app.jjgallery.viewsystem.main;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

		TextView textView = new TextView(context);
		textView.setBackgroundResource(R.drawable.ripple_rect_white);
		textView.setTextColor(context.getResources().getColor(R.color.actionbar_bk_orange));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, context.getResources().getInteger(R.integer.sliding_left_bottom_text_size));
		textView.setText(homeList.get(position).getName());
		textView.setGravity(Gravity.CENTER);
		textView.setTag(position);
		textView.setOnClickListener(this);
		container.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		return textView;
		
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		if (onPageSelectListener != null) {
			onPageSelectListener.onSelectPage(position);
		}
	}

}
