package com.jing.app.jjgallery.viewsystem.publicview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.jing.app.jjgallery.R;

import java.util.ArrayList;
import java.util.List;

public class ActionBar implements OnClickListener, TextWatcher, OnMenuItemClickListener {

	public interface ActionSelectAllListener {
		void onSelectAll();
		void onDeselectall();
	}

	public interface ActionIconListener {
		void onBack();
		void onIconClick(View view);
	}

	public interface ActionMenuListener {
		void createMenu(MenuInflater menuInflater, Menu menu);
		void onPrepareMenu(MenuInflater menuInflater, Menu menu);
		boolean onMenuItemClick(MenuItem item);
	}

	public interface ActionSearchListener {
		void onTextChanged(String text, int start, int before, int count);
	}

	private Context context;

	/**
	 * 整个actionbar根view
	 */
	private LinearLayout layout;


	/**
	 * normal container包含的所有图标
	 */
	private ImageView backButton, menuLeftButton, addButton
		, galleryButton, sortButton, searchButton, closeButton, refreshButton
		, changeButton, colorButton, fullScreenButton, thumbButton;

	/**
	 * 最右侧菜单图标，固定位置
	 */
	private ImageView menuButton;

	/**
	 * select mode container包含的所有图标
	 */
	private ImageView addToButton, deleteButton;

	/**
	 * 标题
	 */
	private TextView titleView;

	/**
	 * 搜索框
	 */
	private EditText searchEdit;

	/**
	 * 全选/取消全选
	 */
	private CheckBox selectCheckBox;

	/**
	 * 当前添加的所有normal container包含的图标
	 */
	private List<View> currentButtons;

	/**
	 * 右侧按钮下拉菜单
	 */
	private PopupMenu popupMenu;


	/**
	 * 全选/取消全选事件
	 */
	private ActionSelectAllListener actionSelectAllListener;

	/**
	 * 图标点击事件
	 */
	private ActionIconListener actionIconListener;

	/**
	 * 右侧下拉菜单事件
	 */
	private ActionMenuListener actionMenuListener;

	/**
	 * 输入框文字变化事件
	 */
	private ActionSearchListener actionSearchListener;


	/**
	 * 由返回/标题/全选/取消全选组成的左侧布局
	 */
	private ViewGroup leftLayout;

	/**
	 * 由返回/标题组成的布局
	 */
	private ViewGroup leftBackTitleLayout;

	/**
	 * 输入框布局
	 */
	private ViewGroup searchLayout;

	/**
	 * 右侧全部图标布局（除menu图标以外）
	 */
	private ViewGroup iconContainer;

	/**
	 * normal图标布局
	 */
	private ViewGroup normalIconContainer;

	/**
	 * selection mode图标布局
	 */
	private ViewGroup selectionIconContainer;


	public ActionBar(final Context context, View view) {
		this.context = context;

		// 初始化各个view
		layout = (LinearLayout) view.findViewById(R.id.actionbar_content);

		backButton = (ImageView) view.findViewById(R.id.actionbar_back);
		menuButton = (ImageView) view.findViewById(R.id.actionbar_menu);
		menuLeftButton = (ImageView) view.findViewById(R.id.actionbar_menu_left);
		addButton = (ImageView) view.findViewById(R.id.actionbar_add);
		galleryButton = (ImageView) view.findViewById(R.id.actionbar_gallery);
		refreshButton = (ImageView) view.findViewById(R.id.actionbar_refresh);
		searchButton = (ImageView) view.findViewById(R.id.actionbar_search);
		sortButton = (ImageView) view.findViewById(R.id.actionbar_sort);
		changeButton = (ImageView) view.findViewById(R.id.actionbar_change);
		fullScreenButton = (ImageView) view.findViewById(R.id.actionbar_fullscreen);
		closeButton = (ImageView) view.findViewById(R.id.actionbar_search_close);
		colorButton = (ImageView) view.findViewById(R.id.actionbar_color);
		thumbButton = (ImageView) view.findViewById(R.id.actionbar_thumb);

		addToButton = (ImageView) view.findViewById(R.id.actionbar_addto);
		deleteButton = (ImageView) view.findViewById(R.id.actionbar_delete);

		searchEdit = (EditText) view.findViewById(R.id.actionbar_search_edittext);
		titleView = (TextView) view.findViewById(R.id.actionbar_title);
		selectCheckBox = (CheckBox) view.findViewById(R.id.actionbar_select_check);

		leftLayout = (ViewGroup) view.findViewById(R.id.actionbar_left);
		leftBackTitleLayout = (ViewGroup) view.findViewById(R.id.actionbar_left_backtitle);
		iconContainer = (ViewGroup) view.findViewById(R.id.actionbar_icon_container);
		searchLayout = (ViewGroup) view.findViewById(R.id.actionbar_search_layout);
		normalIconContainer = (ViewGroup) view.findViewById(R.id.actionbar_icons_normal);
		selectionIconContainer = (ViewGroup) view.findViewById(R.id.actionbar_icons_selection);

		// 设置图标监听事件--normal icons
		backButton.setOnClickListener(this);
		menuButton.setOnClickListener(this);
		menuLeftButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		refreshButton.setOnClickListener(this);
		sortButton.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		fullScreenButton.setOnClickListener(this);
		closeButton.setOnClickListener(this);
		changeButton.setOnClickListener(this);
		galleryButton.setOnClickListener(this);
		colorButton.setOnClickListener(this);
		thumbButton.setOnClickListener(this);

		// 设置图标监听事件--selection icons
		addToButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);

		// 设置输入框文字变化监听事件
		searchEdit.addTextChangedListener(this);

		// 设置全选/取消全选监听事件
		// 不用OnCheckChangedListener，因为外部一个个勾选完全部或者取消勾选完全部，要引起setChecked事件，会触发该listener
		// 只监听主动按键的check变化
		selectCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (actionSelectAllListener != null) {
					CheckBox box = (CheckBox) v;
					if (box.isChecked()) {
						actionSelectAllListener.onSelectAll();
						selectCheckBox.setText(context.getString(R.string.menu_thumb_deselectall));
					}
					else {
						actionSelectAllListener.onDeselectall();
						selectCheckBox.setText(context.getString(R.string.menu_thumb_selectall));
					}
				}
			}
		});

		// 初始化normal icons集合
		currentButtons = new ArrayList<>();
	}

	/**
	 * 更改select all选中状态
	 * @param selectall
     */
	public void showSelectAllStatus(boolean selectall) {
		selectCheckBox.setChecked(selectall);
		if (selectall) {
			selectCheckBox.setText(context.getString(R.string.menu_thumb_deselectall));
		}
		else {
			selectCheckBox.setText(context.getString(R.string.menu_thumb_selectall));
		}
	}

	/**
	 * 禁止使用全选按钮
	 */
	public void disableSelectAll() {
		selectCheckBox.setEnabled(false);
	}

	/**
	 * 更改背景
	 * @param newColor
	 */
	public void updateBackground(int newColor) {
		layout.setBackgroundColor(newColor);
	}

	/**
	 * 设置全选/取消全选事件监听
	 * @param listener
	 */
	public void setActionSelectAllListener(ActionSelectAllListener listener) {
		actionSelectAllListener = listener;
	}

	/**
	 * 设置图标点击事件监听
	 * @param listener
	 */
	public void setActionIconListener(ActionIconListener listener) {
		actionIconListener = listener;
	}

	/**
	 * 设置菜单事件监听
	 * @param listener
	 */
	public void setActionMenuListener(ActionMenuListener listener) {
		actionMenuListener = listener;
	}

	/**
	 * 设置输入框文字变化事件监听
	 * @param listener
	 */
	public void setActionSearchListener(ActionSearchListener listener) {
		actionSearchListener = listener;
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (actionMenuListener != null) {
			return  actionMenuListener.onMenuItemClick(item);
		}
		return false;
	}

	public int getHeight() {
		return context.getResources().getDimensionPixelSize(R.dimen.actionbar_height);
	}

	public void onConfiguration(int orientation) {
		if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
			onLandscape();
		}
		else {
			onVertical();
		}
	}

	public void onLandscape() {

	}

	public void onVertical() {

	}

	public void clearActionIcon() {
		for (View v:currentButtons) {
			v.setVisibility(View.GONE);
		}
		currentButtons.clear();
	}

	public void useMenuLeftIcon() {
		backButton.setVisibility(View.GONE);
		menuLeftButton.setVisibility(View.VISIBLE);
	}

	public void addBackIcon() {
		currentButtons.add(backButton);
		backButton.setVisibility(View.VISIBLE);
	}
	public void addMenuIcon() {
		currentButtons.add(menuButton);
		menuButton.setVisibility(View.VISIBLE);
	}
	public void addAddIcon() {
		currentButtons.add(addButton);
		addButton.setVisibility(View.VISIBLE);
	}
	public void addColorIcon() {
		currentButtons.add(colorButton);
		colorButton.setVisibility(View.VISIBLE);
	}
	public void addGalleryIcon() {
		currentButtons.add(galleryButton);
		galleryButton.setVisibility(View.VISIBLE);
	}
	public void addFullScreenIcon() {
		currentButtons.add(fullScreenButton);
		fullScreenButton.setVisibility(View.VISIBLE);
	}
	public void addRefreshIcon() {
		currentButtons.add(refreshButton);
		refreshButton.setVisibility(View.VISIBLE);
	}
	public void addChangeIcon() {
		currentButtons.add(changeButton);
		changeButton.setVisibility(View.VISIBLE);
	}
	public void addSearchIcon() {
		currentButtons.add(searchButton);
		searchButton.setVisibility(View.VISIBLE);
	}
	public void addSortIcon() {
		currentButtons.add(sortButton);
		sortButton.setVisibility(View.VISIBLE);
	}
	public void addThumbIcon() {
		currentButtons.add(thumbButton);
		thumbButton.setVisibility(View.VISIBLE);
	}

	private void closeSearch() {

		Animation animation = AnimationUtils.loadAnimation(context, R.anim.disappear);
		searchLayout.startAnimation(animation);
		searchLayout.setVisibility(View.GONE);

		animation = AnimationUtils.loadAnimation(context, R.anim.appear);
		iconContainer.startAnimation(animation);
		iconContainer.setVisibility(View.VISIBLE);
		leftLayout.startAnimation(animation);
		leftLayout.setVisibility(View.VISIBLE);
	}

	private void showSearchLayout() {

		Animation animation = AnimationUtils.loadAnimation(context, R.anim.disappear);
		iconContainer.startAnimation(animation);
		iconContainer.setVisibility(View.GONE);
		leftLayout.startAnimation(animation);
		leftLayout.setVisibility(View.GONE);

		animation = AnimationUtils.loadAnimation(context, R.anim.appear);
		searchLayout.startAnimation(animation);
		searchLayout.setVisibility(View.VISIBLE);

	}

	public void setTitle(String text) {
		titleView.setText(text);
	}
	public String getTitle() {
		return titleView.getText().toString();
	}

	public void setSelectionMode() {
		normalIconContainer.setVisibility(View.GONE);
		leftBackTitleLayout.setVisibility(View.GONE);
		selectCheckBox.setVisibility(View.VISIBLE);
		selectionIconContainer.setVisibility(View.VISIBLE);
	}

	public void cancelSelectionMode() {
		normalIconContainer.setVisibility(View.VISIBLE);
		leftBackTitleLayout.setVisibility(View.VISIBLE);
		selectCheckBox.setVisibility(View.GONE);
		selectionIconContainer.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {
		if (view == backButton) {
			actionIconListener.onBack();
		}
		else if (view == menuButton) {
			if (popupMenu == null) {

				createMenu();
			}
			else {
				actionMenuListener.onPrepareMenu(popupMenu.getMenuInflater(), popupMenu.getMenu());
			}
			popupMenu.show();
		}
		else if (view == searchButton) {
			showSearchLayout();
		}
		else if (view == closeButton) {
			closeSearch();
		}
		else {
			actionIconListener.onIconClick(view);
		}
	}

	private void createMenu() {
		popupMenu = new PopupMenu(context, menuButton);
		actionMenuListener.createMenu(popupMenu.getMenuInflater(), popupMenu.getMenu());
		//menuWindow.setWidth(context.getResources().getDimensionPixelSize(R.dimen.actionbar_menu_width));
		popupMenu.setOnMenuItemClickListener(this);
	}
	
	public boolean isHidden() {
		return layout.getVisibility() == View.GONE ? true:false;
	}

	public boolean isShowing() {
		return layout.getVisibility() == View.VISIBLE ? true:false;
	}
	
	public void hide() {
		layout.startAnimation(getDisapplearAnim());
		layout.setVisibility(View.GONE);
	}
	
	public void show() {
		layout.startAnimation(getApplearAnim());
		layout.setVisibility(View.VISIBLE);
	}
	public boolean dismissMenu() {
		return false;
	}

	public Animation getApplearAnim() {
		Animation appearAnim = AnimationUtils.loadAnimation(context, R.anim.appear);
		return appearAnim;
	}

	public Animation getDisapplearAnim() {
		Animation disappearAnim = AnimationUtils.loadAnimation(context, R.anim.disappear);
		return disappearAnim;
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		actionSearchListener.onTextChanged(s.toString(), start, before, count);
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	public void setBackgroundColor(int color) {
		layout.setBackgroundColor(color);
	}
}
