package com.jing.app.jjgallery.viewsystem.publicview;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;

import java.util.ArrayList;
import java.util.List;

public class ActionBar implements OnClickListener, TextWatcher, OnMenuItemClickListener {

	private Context context;
	private ImageView backButton, menuButton, addButton, editButton
		, titleIcon, saveButton, cancelButton, galleryButton
		, searchButton, closeButton, refreshButton, changeButton
		, fullScreenButton, randomChangeButton, deleteButton, saveButton1, showButton, thumbButton;
	private TextView titleView;
	private EditText searchEdit;
	private ActionIconListener actionIconListener;
	private ActionMenuListener actionMenuListener;
	private ActionSearchListener actionSearchListener;
	private List<View> currentButtons, tempButotns;
	private HorizontalScrollView iconContainer;
	//private Spinner levelSpinner, courtSpinner;
	private LinearLayout layout;
	private RelativeLayout searchLayout;
	
	private PopupMenu popupMenu;
	
	public ActionBar(Context context, View view) {
		this.context = context;
		layout = (LinearLayout) view;
		searchLayout = (RelativeLayout) view.findViewById(R.id.actionbar_search_layout);
		backButton = (ImageView) view.findViewById(R.id.actionbar_back);
		menuButton = (ImageView) view.findViewById(R.id.actionbar_menu);
		addButton = (ImageView) view.findViewById(R.id.actionbar_add);
		editButton = (ImageView) view.findViewById(R.id.actionbar_edit);
		saveButton = (ImageView) view.findViewById(R.id.actionbar_save);
		saveButton1 = (ImageView) view.findViewById(R.id.actionbar_save1);
		cancelButton = (ImageView) view.findViewById(R.id.actionbar_cancel);
		galleryButton = (ImageView) view.findViewById(R.id.actionbar_gallery);
		refreshButton = (ImageView) view.findViewById(R.id.actionbar_refresh);
		searchButton = (ImageView) view.findViewById(R.id.actionbar_search);
		changeButton = (ImageView) view.findViewById(R.id.actionbar_change);
		randomChangeButton = (ImageView) view.findViewById(R.id.actionbar_random_change);
		fullScreenButton = (ImageView) view.findViewById(R.id.actionbar_fullscreen);
		closeButton = (ImageView) view.findViewById(R.id.actionbar_search_close);
		deleteButton = (ImageView) view.findViewById(R.id.actionbar_delete);
		showButton = (ImageView) view.findViewById(R.id.actionbar_show);
		titleIcon = (ImageView) view.findViewById(R.id.actionbar_title_icon);
		thumbButton = (ImageView) view.findViewById(R.id.actionbar_thumb);
		searchEdit = (EditText) view.findViewById(R.id.actionbar_search_edittext);
		iconContainer = (HorizontalScrollView) view.findViewById(R.id.actionbar_icon_container);
		backButton.setOnClickListener(this);
		menuButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		refreshButton.setOnClickListener(this);
		editButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		saveButton1.setOnClickListener(this);
		showButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		randomChangeButton.setOnClickListener(this);
		fullScreenButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		closeButton.setOnClickListener(this);
		changeButton.setOnClickListener(this);
		galleryButton.setOnClickListener(this);
		searchEdit.addTextChangedListener(this);
		thumbButton.setOnClickListener(this);
		titleView = (TextView) view.findViewById(R.id.actionbar_title);
		
		currentButtons = new ArrayList<View>();
		
	}

	public void setActionIconListener(ActionIconListener listener) {
		actionIconListener = listener;
	}

	public void setActionMenuListener(ActionMenuListener listener) {
		actionMenuListener = listener;
	}

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

	public interface ActionIconListener {
		public void onBack();
		public void onIconClick(View view);
	}

	public interface ActionMenuListener {
		public void createMenu(MenuInflater menuInflater, Menu menu);
		public void onPrepareMenu(MenuInflater menuInflater, Menu menu);
		public boolean onMenuItemClick(MenuItem item);
	}

	public interface ActionSearchListener {
		public void onTextChanged(String text, int start, int before, int count);
	}
	
	public interface ActionSpinnerListener {
		public void onTitleFilterListener(int indexLevel, int indexCourt);
	}

	public int getHeight() {
		return context.getResources().getDimensionPixelSize(R.dimen.actionbar_height);
	}
	
	public void onLandscape() {
		LayoutParams params = (LayoutParams) iconContainer.getLayoutParams();
		int iconNumber = currentButtons.size();
		if (currentButtons.contains(menuButton)) {
			iconNumber --;
		}
		if (iconNumber < context.getResources().getInteger(R.integer.actionbar_max_icon)) {
			params.width = context.getResources().getDimensionPixelSize(R.dimen.actionbar_icon_width) * iconNumber;
		}
		else {
			params.width = context.getResources().getDimensionPixelSize(R.dimen.actionbar_icon_max_width);
		}
	}

	public void onVertical() {
		LayoutParams params = (LayoutParams) iconContainer.getLayoutParams();
		int iconNumber = currentButtons.size();
		if (currentButtons.contains(menuButton)) {
			iconNumber --;
		}
		if (iconNumber < context.getResources().getInteger(R.integer.actionbar_max_icon)) {
			params.width = context.getResources().getDimensionPixelSize(R.dimen.actionbar_icon_width) * iconNumber;
		}
		else {
			params.width = context.getResources().getDimensionPixelSize(R.dimen.actionbar_icon_max_width);
		}
	}
	
	public void clearActionIcon() {
		for (View v:currentButtons) {
			v.setVisibility(View.GONE);
		}
		currentButtons.clear();
	}
	public void addEditIcon() {
		currentButtons.add(editButton);
		editButton.setVisibility(View.VISIBLE);
	}
	public void addMenuIcon() {
		currentButtons.add(menuButton);
		menuButton.setVisibility(View.VISIBLE);
	}
	public void addAddIcon() {
		currentButtons.add(addButton);
		addButton.setVisibility(View.VISIBLE);
	}
	public void addGalleryIcon() {
		currentButtons.add(galleryButton);
		galleryButton.setVisibility(View.VISIBLE);
	}
	public void addSaveIcon() {
		currentButtons.add(saveButton1);
		saveButton1.setVisibility(View.VISIBLE);
	}
	public void addShowIcon() {
		currentButtons.add(showButton);
		showButton.setVisibility(View.VISIBLE);
	}
	public void addFullScreenIcon() {
		currentButtons.add(fullScreenButton);
		fullScreenButton.setVisibility(View.VISIBLE);
	}
	public void addDeleteIcon() {
		currentButtons.add(deleteButton);
		deleteButton.setVisibility(View.VISIBLE);
	}
	public void addRandomChangeIcon() {
		currentButtons.add(randomChangeButton);
		randomChangeButton.setVisibility(View.VISIBLE);
	}
	public void addRefreshIcon() {
		currentButtons.add(refreshButton);
		refreshButton.setVisibility(View.VISIBLE);
	}
	public void addChangeIcon() {
		currentButtons.add(changeButton);
		changeButton.setVisibility(View.VISIBLE);
	}
	public void addCancelIcon() {
		currentButtons.add(cancelButton);
		cancelButton.setVisibility(View.VISIBLE);
	}
	public void addTitleIcon(int resId) {
		titleIcon.setImageResource(resId);
		currentButtons.add(titleIcon);
		titleIcon.setVisibility(View.VISIBLE);
	}
	public void addSearchIcon() {
		currentButtons.add(searchButton);
		searchButton.setVisibility(View.VISIBLE);
	}
	public void addThumbIcon() {
		currentButtons.add(thumbButton);
		thumbButton.setVisibility(View.VISIBLE);
	}

	public void closeSearch() {
		
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.disappear);
		searchLayout.startAnimation(animation);
		searchLayout.setVisibility(View.GONE);
		
		animation = AnimationUtils.loadAnimation(context, R.anim.appear);
		iconContainer.startAnimation(animation);
		iconContainer.setVisibility(View.VISIBLE);
	}
	public void addSearchLayout() {

		Animation animation = AnimationUtils.loadAnimation(context, R.anim.appear);
		searchLayout.startAnimation(animation);
		searchLayout.setVisibility(View.VISIBLE);

		animation = AnimationUtils.loadAnimation(context, R.anim.disappear);
		iconContainer.startAnimation(animation);
		iconContainer.setVisibility(View.GONE);
	}

	public void setTitle(String text) {
		titleView.setText(text);
	}
	public String getTitle() {
		return titleView.getText().toString();
	}
	
	@Override
	public void onClick(View view) {
		if (view == backButton) {
			actionIconListener.onBack();
		}
//		else if (view == addButton) {
//			setEditMode(true);
//		}
		else if (view == editButton) {
			setEditMode(true);
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
		else if (view == cancelButton) {
			setEditMode(false);
		}
		else if (view == saveButton) {
			setEditMode(false);
		}
		else if (view == searchButton) {
			addSearchLayout();
		}
		else if (view == closeButton) {
			closeSearch();
		}
		else {
			actionIconListener.onIconClick(view);
		}
	}

	private void setEditMode(boolean b) {
		if (b) {
			saveButton.setVisibility(View.VISIBLE);
			cancelButton.setVisibility(View.VISIBLE);
			for (View v:currentButtons) {
				v.setVisibility(View.GONE);
			}
		}
		else {
			saveButton.setVisibility(View.GONE);
			cancelButton.setVisibility(View.GONE);
			for (View v:currentButtons) {
				v.setVisibility(View.VISIBLE);
			}
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
		layout.setVisibility(View.GONE);
	}
	
	public void show() {
		layout.setVisibility(View.VISIBLE);
	}
	public boolean dismissMenu() {
		return false;
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
