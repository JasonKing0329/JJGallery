package com.jing.app.jjgallery.gdb.view.pub;

import android.view.ViewGroup;

public abstract class AutoScrollAdapter<T extends AutoScrollView.ViewHolder> {

	private AutoScrollView autoScrollView;
	
	public void setAutoScrollView(AutoScrollView view) {
		autoScrollView = view;
	}
	
	public void notifyDataSetChanged() {
		autoScrollView.notifyDataSetChanged();
	}

	public abstract int getCount();

	public abstract T onCreateViewHolder(ViewGroup parent);

	public abstract void onBindView(int position, T holder);

	public abstract int getItemWidth();
}
