package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.Application;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.bean.order.STag;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.presenter.main.order.SOrderPresenter;
import com.jing.app.jjgallery.service.image.SImageLoader;

import java.util.HashMap;
import java.util.List;

public class SOrderExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<List<SOrder>> list;
	private List<STag> tagList;
	private Controller uiController;
	private int COLOR_BK1, COLOR_BK2;
	private HashMap<Integer, ChildViewHolder> childMap;

	private SOrderPresenter mPresenter;

	public SOrderExpandableListAdapter(Context context, List<List<SOrder>> list, List<STag> tList) {
		this.context = context;
		this.list = list;
		tagList = tList;

		int themeColor = ThemeManager.getInstance().getBasicColor(context);
		COLOR_BK1 = themeColor;
		COLOR_BK2 = themeColor;
		uiController = new Controller();
		childMap = new HashMap<Integer, ChildViewHolder>();
		SImageLoader.getInstance().setDefaultImgRes(R.drawable.icon_loading);
	}
	public void setTagList(List<STag> tagList) {
		this.tagList = tagList;
	}
	public void setListInExpand(List<List<SOrder>> list) {
		this.list = list;
	}
	public void setPresenter(SOrderPresenter mPresenter) {
		this.mPresenter = mPresenter;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		return list.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return list.get(groupPosition).get(childPosition).getId();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.sorder_list_child, null);
			holder = new ChildViewHolder();
			holder.cover = (ImageView) convertView.findViewById(R.id.sorder_list_child_cover);
			holder.name = (TextView) convertView.findViewById(R.id.sorder_list_child_name);
			holder.previewLayout = (LinearLayout) convertView.findViewById(R.id.sorder_list_child_preview);
			holder.imageView1 = (ImageView) convertView.findViewById(R.id.sorder_list_child_img1);
			holder.imageView2 = (ImageView) convertView.findViewById(R.id.sorder_list_child_img2);
			holder.imageView3 = (ImageView) convertView.findViewById(R.id.sorder_list_child_img3);
			holder.imageView4 = (ImageView) convertView.findViewById(R.id.sorder_list_child_img4);
			holder.imageView5 = (ImageView) convertView.findViewById(R.id.sorder_list_child_img5);
			holder.showPreview = (ImageView) convertView.findViewById(R.id.sorder_list_child_showpreview);
			convertView.setTag(holder);
			holder.showPreview.setTag(holder);
			holder.showPreview.setOnClickListener(uiController);
			if (Application.isLollipop()) {
				holder.showPreview.setBackgroundResource(R.drawable.selector_custom_dialog_icon_bk_l);
			}
		}
		else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		SOrder order = list.get(groupPosition).get(childPosition);
		SImageLoader.getInstance().displayImage(order.getCoverPath(), holder.cover);
		holder.name.setText(order.getName() + "(" + order.getItemNumber() + ")");
		holder.sOrder = order;
		holder.id = order.getId();
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return list.get(groupPosition) == null ? 0:list.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return list.get(groupPosition);
	}

	@Override
	public int getGroupCount() {

		return list == null ? 0:list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {

		TitleViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.sorder_list_header, null);
			holder = new TitleViewHolder();
			holder.arrow = (ImageView) convertView.findViewById(R.id.sorder_list_head_indicator);
			holder.tag = (TextView) convertView.findViewById(R.id.sorder_list_head_tag);
			convertView.setTag(holder);
		}
		else {
			holder = (TitleViewHolder) convertView.getTag();
		}
		if (position % 2 == 0) {
			convertView.setBackgroundColor(COLOR_BK1);
		}
		else {
			convertView.setBackgroundColor(COLOR_BK2);
		}
		if (isExpanded) {
			holder.arrow.setImageResource(R.drawable.group_expand_up);
		}
		else {
			holder.arrow.setImageResource(R.drawable.group_expand_down);
		}
		holder.tag.setText(tagList.get(position).getName() + " (" + list.get(position).size() + ")");
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}

	private class TitleViewHolder {
		ImageView arrow;
		TextView tag;
	}
	private class ChildViewHolder {
		ImageView cover;
		TextView name;
		LinearLayout previewLayout;
		ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
		ImageView showPreview;
		SOrder sOrder;
		int id;
	}

	@Override
	public void notifyDataSetChanged() {
		closeAllPreview();
		super.notifyDataSetChanged();
	}
	private void closeAllPreview() {
		for (ChildViewHolder holder:childMap.values()) {
			uiController.hideOrderPreview(holder);
		}
		childMap.clear();
	}

	private class Controller implements OnClickListener {

		public void hideOrderPreview(final ChildViewHolder holder) {
			holder.previewLayout.setVisibility(View.GONE);
			holder.showPreview.setImageResource(R.drawable.ic_apps_white_36dp);

			Animation animation = AnimationUtils.loadAnimation(context, R.anim.sorder_list_shrink);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					//这些一定要写在动画结束，否则都为null了，previewLayout就已经是空内容，就看不到动画效果了！！
					holder.imageView1.setImageBitmap(null);
					holder.imageView2.setImageBitmap(null);
					holder.imageView3.setImageBitmap(null);
					holder.imageView4.setImageBitmap(null);
					holder.imageView5.setImageBitmap(null);
				}
			});
			holder.previewLayout.startAnimation(animation);
		}

		public void showOrderPreview(ChildViewHolder holder) {
			List<String> paths = mPresenter.loadSOrderPreviews(holder.sOrder, context.getResources().getInteger(R.integer.sorder_expan_preview));
			if (paths != null) {
				int realNumber = paths.size();
				ImageView imageView = null;
				for (int i = 0; i < realNumber; i ++) {
					switch (i) {
						case 1:
							imageView = holder.imageView2;
							break;
						case 2:
							imageView = holder.imageView3;
							break;
						case 3:
							imageView = holder.imageView4;
							break;
						case 4:
							imageView = holder.imageView5;
							break;
						case 0:
						default:
							imageView = holder.imageView1;
							break;
					}
					imageView.setVisibility(View.VISIBLE);
					SImageLoader.getInstance().displayImage(paths.get(i), imageView);
				}
			}
			Animation animation = AnimationUtils.loadAnimation(context, R.anim.sorder_list_expand);
			holder.previewLayout.startAnimation(animation);
			holder.previewLayout.setVisibility(View.VISIBLE);
			holder.showPreview.setImageResource(R.drawable.ic_close_white_36dp);
		}

		@Override
		public void onClick(View v) {
			ChildViewHolder holder = (ChildViewHolder) v.getTag();
			if (holder.previewLayout.getVisibility() == View.GONE) {
				showOrderPreview(holder);
				childMap.put(holder.id, holder);
			}
			else {
				hideOrderPreview(holder);
				childMap.remove(holder.id);
			}
		}

	}

}
