package com.jing.app.jjgallery.viewsystem.publicview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.Application;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.model.pub.DrawableUtils;
import com.jing.app.jjgallery.util.ScreenUtils;

public class IndexView extends LinearLayout implements OnClickListener {

	public interface OnIndexSelectListener {
		public void onSelect(String index);
	}
	
	private OnIndexSelectListener listener;

	private int mNormalColor;
	private int mTextColor;
	private int mPressColor;

	public IndexView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mNormalColor = getResources().getColor(R.color.actionbar_bk_blue);
		mPressColor = getResources().getColor(R.color.toolbar_item_pressed_bk);
		mTextColor = getResources().getColor(R.color.white);
	}


	public void clearAllIndex() {
		removeAllViews();
	}
	
	public void addIndex(String index) {
		TextView tv = new TextView(getContext());
		tv.setGravity(Gravity.CENTER);
		tv.setText(index);
		tv.setTag(index);
		tv.setOnClickListener(this);
		addView(tv);
	}
	
	public void build() {
		int size = getChildCount();
		int minIndex = getResources().getDimensionPixelSize(R.dimen.thumbfolder_index_min_height);
		int normalHeight = getResources().getDimensionPixelSize(R.dimen.thumbfolder_index_height);
		int totalHeight = ScreenUtils.getScreenHeight(getContext())
				- getResources().getDimensionPixelSize(R.dimen.actionbar_height);
		int height = totalHeight / size;
		if (height < minIndex) {
			height = minIndex;
		}
		if (height > normalHeight) {
			height = normalHeight;
		}
		int margin = getResources().getDimensionPixelSize(R.dimen.thumbfolder_index_margin);
		TextView child = null;
		LayoutParams params = null;
		MarginLayoutParams mParams = null;

		for (int i = 0; i < size; i ++) {
			child = (TextView) getChildAt(i);
			child.setTextColor(mTextColor);
			child.setBackground(DrawableUtils.getColorStateDrawable(getContext(), mNormalColor, mPressColor));
			params = (LayoutParams) child.getLayoutParams();
			params.height = height;
			mParams = params;
			mParams.topMargin = margin;
			mParams.bottomMargin = margin;
		}
	}

	public void updateNormalColor(int color) {
		mNormalColor = color;
	}

	public void updateTextColor(int color) {
		mTextColor = color;
	}

	public void refresh() {
		TextView child = null;
		for (int i = 0; i < getChildCount(); i ++) {
			child = (TextView) getChildAt(i);
			child.setTextColor(mTextColor);
			child.setBackground(DrawableUtils.getColorStateDrawable(getContext(), mNormalColor, mPressColor));
		}
	}

	public void setOnIndexSelectListener(OnIndexSelectListener listener) {
		this.listener = listener;
	}
	@Override
	public void onClick(View view) {
		if (listener != null) {
			String index = (String) view.getTag();
			listener.onSelect(index);
		}
	}
	
	public int getLeftPosition() {
		if (getChildAt(0) != null) {
			return getChildAt(0).getLeft();
		}
		return 0;
	}
	
	public int getTopPosition() {
		if (getChildAt(0) != null) {
			return getChildAt(0).getTop();
		}
		return 0;
	}
}
