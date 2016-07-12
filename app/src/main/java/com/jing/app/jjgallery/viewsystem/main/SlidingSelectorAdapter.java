package com.jing.app.jjgallery.viewsystem.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;

/**
 * left sliding menu of home
 * the bottom bar to select file manager or sorder
 */
public class SlidingSelectorAdapter extends PagerAdapter {

	private String[] texts;
	private Context context;
	private List<TextView> mTextViews ;
	public int index;
	
	public SlidingSelectorAdapter(Context context, String[] texts) {
		this.texts = texts;
		mTextViews= new ArrayList<>();
		this.context = context;
	}

	@Override
	public int getCount() {
		return texts.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mTextViews.get(position));
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		TextView textView = new TextView(context);
		textView.setBackgroundResource(R.drawable.ripple_rect_white);
		textView.setTextColor(context.getResources().getColor(R.color.actionbar_bk_orange));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
		textView.setText(texts[position]);
		textView.setGravity(Gravity.CENTER);
		container.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		return textView;
		
	}

}
