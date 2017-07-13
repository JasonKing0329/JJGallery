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

	/**
	 * select/deselect all action listener
	 */
	public interface ActionSelectAllListener {
		void onSelectAll();
		void onDeselectall();
	}

	/**
	 * action icon listener, define it by view.getId()
	 */
	public interface ActionIconListener {
		void onBack();
		void onIconClick(View view);
	}

	/**
	 * custom menu action listener
	 */
	public interface ActionMenuListener {
		void createMenu(MenuInflater menuInflater, Menu menu);
		void onPrepareMenu(MenuInflater menuInflater, Menu menu);
		boolean onMenuItemClick(MenuItem item);
	}

	/**
	 * text change action listener
	 */
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
	private ImageView backButton, menuLeftButton, addButton, homeButton
		, surfButton, sortButton, searchButton, closeButton, refreshButton
		, changeButton, colorButton, randomSurfButton, coverButton
		, showButton, hideButton, groupAddButton, favorButton, indexButton;

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
		homeButton = (ImageView) view.findViewById(R.id.actionbar_home);
		menuButton = (ImageView) view.findViewById(R.id.actionbar_menu);
		menuLeftButton = (ImageView) view.findViewById(R.id.actionbar_menu_left);
		addButton = (ImageView) view.findViewById(R.id.actionbar_add);
		surfButton = (ImageView) view.findViewById(R.id.actionbar_surf);
		refreshButton = (ImageView) view.findViewById(R.id.actionbar_refresh);
		searchButton = (ImageView) view.findViewById(R.id.actionbar_search);
		sortButton = (ImageView) view.findViewById(R.id.actionbar_sort);
		favorButton = (ImageView) view.findViewById(R.id.actionbar_favor);
		indexButton = (ImageView) view.findViewById(R.id.actionbar_index);
		changeButton = (ImageView) view.findViewById(R.id.actionbar_change);
		randomSurfButton = (ImageView) view.findViewById(R.id.actionbar_fullscreen);
		closeButton = (ImageView) view.findViewById(R.id.actionbar_search_close);
		colorButton = (ImageView) view.findViewById(R.id.actionbar_color);
		coverButton = (ImageView) view.findViewById(R.id.actionbar_cover);
		showButton = (ImageView) view.findViewById(R.id.actionbar_show);
		hideButton = (ImageView) view.findViewById(R.id.actionbar_hide);
		groupAddButton = (ImageView) view.findViewById(R.id.actionbar_group_add);

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
		homeButton.setOnClickListener(this);
		menuButton.setOnClickListener(this);
		menuLeftButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		refreshButton.setOnClickListener(this);
		sortButton.setOnClickListener(this);
		favorButton.setOnClickListener(this);
		indexButton.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		randomSurfButton.setOnClickListener(this);
		closeButton.setOnClickListener(this);
		changeButton.setOnClickListener(this);
		surfButton.setOnClickListener(this);
		colorButton.setOnClickListener(this);
		coverButton.setOnClickListener(this);
		showButton.setOnClickListener(this);
		hideButton.setOnClickListener(this);
		groupAddButton.setOnClickListener(this);

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

	/**
	 * 获取actionbar高度
	 * @return
     */
	public int getHeight() {
		return context.getResources().getDimensionPixelSize(R.dimen.actionbar_height);
	}

	/**
	 * 处理转屏变化
	 * @param orientation
     */
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

	/**
	 * 清空所有normal icons
	 */
	public void clearActionIcon() {
		for (View v:currentButtons) {
			v.setVisibility(View.GONE);
		}
		currentButtons.clear();
	}

	/**
	 * 使用左侧菜单按钮，屏蔽返回按钮（用于AbsHomeActivity）
	 */
	public void useMenuLeftIcon() {
		backButton.setVisibility(View.GONE);
		menuLeftButton.setVisibility(View.VISIBLE);
	}

	/**
	 * back icon
	 */
	public void addBackIcon() {
		currentButtons.add(backButton);
		backButton.setVisibility(View.VISIBLE);
	}

	/**
	 * home icon
	 */
	public void addHomeIcon() {
		currentButtons.add(homeButton);
		homeButton.setVisibility(View.VISIBLE);
	}

	/**
	 * menu icon
	 */
	public void addMenuIcon() {
		currentButtons.add(menuButton);
		menuButton.setVisibility(View.VISIBLE);
	}

	/**
	 * add icon
	 */
	public void addAddIcon() {
		currentButtons.add(addButton);
		addButton.setVisibility(View.VISIBLE);
	}

	/**
	 * edit color icon
	 */
	public void addColorIcon() {
		currentButtons.add(colorButton);
		colorButton.setVisibility(View.VISIBLE);
	}

	/**
	 * surf icon
	 */
	public void addSurfIcon() {
		currentButtons.add(surfButton);
		surfButton.setVisibility(View.VISIBLE);
	}

	/**
	 * random surf icon
	 */
	public void addRandomSurfIcon() {
		currentButtons.add(randomSurfButton);
		randomSurfButton.setVisibility(View.VISIBLE);
	}

	/**
	 * refresh icon
	 */
	public void addRefreshIcon() {
		currentButtons.add(refreshButton);
		refreshButton.setVisibility(View.VISIBLE);
	}

	/**
	 * change icon
	 */
	public void addChangeIcon() {
		currentButtons.add(changeButton);
		changeButton.setVisibility(View.VISIBLE);
	}

	/**
	 * search icon
	 */
	public void addSearchIcon() {
		currentButtons.add(searchButton);
		searchButton.setVisibility(View.VISIBLE);
	}

	/**
	 * sort by icon
	 */
	public void addSortIcon() {
		currentButtons.add(sortButton);
		sortButton.setVisibility(View.VISIBLE);
	}

	/**
	 * index icon
	 */
	public void addIndexIcon() {
		currentButtons.add(indexButton);
		indexButton.setVisibility(View.VISIBLE);
	}

	/**
	 * favor icon
	 */
	public void addFavorIcon() {
		currentButtons.add(favorButton);
		favorButton.setVisibility(View.VISIBLE);
	}

	/**
	 * sorder cover icon
	 */
	public void addCoverIcon() {
		currentButtons.add(coverButton);
		coverButton.setVisibility(View.VISIBLE);
	}

	/**
	 * sorder cover icon
	 */
	public void addCoverIcon(OnClickListener listener) {
		currentButtons.add(coverButton);
		coverButton.setVisibility(View.VISIBLE);
		coverButton.setOnClickListener(listener);
	}

	/**
	 * show icon
	 */
	public void addShowIcon() {
		currentButtons.add(showButton);
		showButton.setVisibility(View.VISIBLE);
	}

	/**
	 * hide icon
	 */
	public void addHideIcon() {
		currentButtons.add(hideButton);
		hideButton.setVisibility(View.VISIBLE);
	}

	/**
	 * group add icon
	 */
	public void addGroupAddIcon() {
		currentButtons.add(groupAddButton);
		groupAddButton.setVisibility(View.VISIBLE);
	}

	/**
	 * group add icon
	 */
	public void addGroupAddIcon(OnClickListener listener) {
		addGroupAddIcon();
		if (listener != null) {
			groupAddButton.setOnClickListener(listener);
		}
	}

	public boolean isSearchVisible() {
		return searchLayout.getVisibility() == View.VISIBLE;
	}

	/**
	 * hide search layout
	 */
	public void closeSearch() {

		Animation animation = AnimationUtils.loadAnimation(context, R.anim.disappear);
		searchLayout.startAnimation(animation);
		searchLayout.setVisibility(View.GONE);

		animation = AnimationUtils.loadAnimation(context, R.anim.appear);
		iconContainer.startAnimation(animation);
		iconContainer.setVisibility(View.VISIBLE);
		leftLayout.startAnimation(animation);
		leftLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * show search layout
	 */
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

	/**
	 * set actionbar title
	 * @param text title name
     */
	public void setTitle(String text) {
		titleView.setText(text);
	}

	/**
	 * get actionbar title
	 * @return name
     */
	public String getTitle() {
		return titleView.getText().toString();
	}

	/**
	 * make actionbar show selection mode
	 * includes select all on the left and selection icons on the right
	 */
	public void setSelectionMode() {
		normalIconContainer.setVisibility(View.GONE);
		leftBackTitleLayout.setVisibility(View.GONE);
		selectCheckBox.setVisibility(View.VISIBLE);
		selectionIconContainer.setVisibility(View.VISIBLE);
	}

	/**
	 * exit selection mode
	 * re show normal mode
	 */
	public void cancelSelectionMode() {
		normalIconContainer.setVisibility(View.VISIBLE);
		leftBackTitleLayout.setVisibility(View.VISIBLE);
		selectCheckBox.setVisibility(View.GONE);
		selectionIconContainer.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {
		if (view == backButton) {// back
			actionIconListener.onBack();
		}
		else if (view == menuButton) {// menu
			if (popupMenu == null) {

				createMenu();
			}
			else {
				actionMenuListener.onPrepareMenu(popupMenu.getMenuInflater(), popupMenu.getMenu());
			}
			popupMenu.show();
		}
		else if (view == searchButton) {// search
			showSearchLayout();
		}
		else if (view == closeButton) {// close search
			closeSearch();
		}
		else {// all other icons
			actionIconListener.onIconClick(view);
		}
	}

	/**
	 * create popup menu
	 */
	private void createMenu() {
		popupMenu = new PopupMenu(context, menuButton);
		actionMenuListener.createMenu(popupMenu.getMenuInflater(), popupMenu.getMenu());
		//menuWindow.setWidth(context.getResources().getDimensionPixelSize(R.dimen.actionbar_menu_width));
		popupMenu.setOnMenuItemClickListener(this);
	}

	/**
	 * whether actionbar is hidden
	 * @return
     */
	public boolean isHidden() {
		return layout.getVisibility() == View.GONE ? true:false;
	}

	/**
	 * whether actionbar is showing
	 * @return
	 */
	public boolean isShowing() {
		return layout.getVisibility() == View.VISIBLE ? true:false;
	}

	/**
	 * hide actionbar
	 */
	public void hide() {
		layout.startAnimation(getDisapplearAnim());
		layout.setVisibility(View.GONE);
	}

	/**
	 * show action bar, default status is show
	 */
	public void show() {
		layout.startAnimation(getApplearAnim());
		layout.setVisibility(View.VISIBLE);
	}

	/**
	 * actionbar show animation
	 * @return
     */
	public Animation getApplearAnim() {
		Animation appearAnim = AnimationUtils.loadAnimation(context, R.anim.appear);
		return appearAnim;
	}

	/**
	 * actionbar hide animation
	 * @return
	 */
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

	/**
	 * set background color
	 * @param color
     */
	public void setBackgroundColor(int color) {
		layout.setBackgroundColor(color);
	}
}
