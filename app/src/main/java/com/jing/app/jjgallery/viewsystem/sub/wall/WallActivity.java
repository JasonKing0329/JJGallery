package com.jing.app.jjgallery.viewsystem.sub.wall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.model.sub.WallController;
import com.jing.app.jjgallery.model.sub.WholeRandomManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.order.SOrderProvider;
import com.jing.app.jjgallery.presenter.main.order.SOrderProviderCallback;
import com.jing.app.jjgallery.service.image.PictureManagerUpdate;
import com.jing.app.jjgallery.util.ScreenUtils;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;
import com.jing.app.jjgallery.viewsystem.sub.surf.SurfActivity;
import com.jing.app.jjgallery.viewsystem.sub.surf.UiController;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author JingYang
 * @version create time：2016-1-29 下午4:30:16
 *
 */
public class WallActivity extends BaseActivity implements Callback
		, OnMenuItemClickListener, View.OnClickListener, WallAdapter.OnWallItemListener {

	public static final int MODE_FOLDER = 0;
	public static final int MODE_ORDER = 1;
	public static final int MODE_LIST = 2;
	public static final String MODE_KEY = "mode";
	public static final String MODE_VALUE_KEY = "value";

	/**
	 * 删除时的透明过程
	 */
	private final int TIME_GALLERY_ANIM_REMOVE = 200;
	/**
	 * 删除时的透明过程结束后后面的item向前挤压的过程
	 */
	private final int TIME_GALLERY_ANIM_MOVE = 500;

	private int currentMode;
	private SOrder currentOrder;
	private String currentPath;
	private RecyclerView wallGallery;
	private WallAdapter wallAdapter;
	private List<String> imagePathList;
	private WallController wallController;
	private SOrderProvider sOrderProvider;

	private View actionbarLayout;
	private View bottomLayout;
	private CheckBox checkBox;
	private RadioButton fitXYButton, centerCropButton, originButton;
	private ShowImageDialog showImageDialog;
	private ProgressDialog progressDialog;

	private int orientation;

	/**
	 * 防止多次重新触发动画
	 */
	private boolean isActionBarShow;

	/**
	 * show/hide actionbar gesture listener
	 */
	private ActionTouchListener actionTouchListener;

	@Override
	protected boolean isActionBarNeed() {
		return true;
	}

	@Override
	protected int getContentView() {
		return R.layout.layout_wall_update;
	}

	@Override
	protected void initController() {
		orientation = getResources().getConfiguration().orientation;
		isActionBarShow = true;

		wallController = new WallController(this);
		sOrderProvider = new SOrderProvider(this, new SOrderProviderCallback() {
			@Override
			public void onMoveFinish(String folderPath) {

			}

			@Override
			public void onAddToOrderFinished() {

			}

			@Override
			public void onDeleteIndex(int index) {
				//show animation
				imagePathList.remove(index);
				wallAdapter.notifyRemoved(index);
			}

			@Override
			public void onDeleteFinished(int count) {
				if (currentMode == MODE_ORDER) {
					currentOrder.setItemNumber(currentOrder.getItemNumber() - count);
				}
				//restore normal status
				wallAdapter.setSelectMode(false);
				setActionbarNormal();

				//notify adapter refresh in animation ending
				new Handler().postDelayed(new Runnable() {

											  @Override
											  public void run() {
												  resetGallery();
											  }
										  }  //这里扩大时间是为了避免出现try to use recycled bitmap异常
						//这是因为adapter在onRecycle和notifyDataSetChanged以及notifyItemRemoved的时序问题引发的
						, (TIME_GALLERY_ANIM_MOVE + TIME_GALLERY_ANIM_REMOVE) * 2);
			}
		});
	}

	@Override
	protected void initView() {

		requestActionbarFloating();

		mActionBar.setBackgroundColor(getResources().getColor(new ThemeManager(this).getWallActionbarColor()));
		setActionbarNormal();
		bottomLayout = findViewById(R.id.bottombar);
		actionbarLayout = findViewById(R.id.actionbar);

		wallGallery = (RecyclerView) findViewById(R.id.wall_gallery);

		//用view绑定的onTouchListener不好用，因为会和其他touch事件冲突，导致在listener中往往收不到ACTION_UP事件
		//改用Activity的dispatchTouchEvent来保证手势show/hide action bar始终有效
//		wallGallery.setOnTouchListener(new GalleryTouchListener());
		actionTouchListener = new ActionTouchListener();

		int row = getResources().getInteger(R.integer.wall_rows);
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
				row, StaggeredGridLayoutManager.HORIZONTAL);
		wallGallery.setLayoutManager(layoutManager);
		DefaultItemAnimator animator = new DefaultItemAnimator();
		animator.setRemoveDuration(TIME_GALLERY_ANIM_REMOVE);
		animator.setMoveDuration(TIME_GALLERY_ANIM_MOVE);
		wallGallery.setItemAnimator(animator);

		fitXYButton = (RadioButton) findViewById(R.id.wall_image_fitxy);
		centerCropButton = (RadioButton) findViewById(R.id.wall_image_centercrop);
		originButton = (RadioButton) findViewById(R.id.wall_image_origin);
		fitXYButton.setOnClickListener(this);
		centerCropButton.setOnClickListener(this);
		originButton.setOnClickListener(this);
		fitXYButton.setChecked(true);

		checkBox = (CheckBox) findViewById(R.id.wall_show_file_name);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				wallAdapter.setShowName(isChecked);
				wallAdapter.notifyDataSetChanged();
			}
		});

		showImageDialog = new ShowImageDialog(this, null, 0);
	}

	@Override
	protected void initBackgroundWork() {
		Bundle bundle = getIntent().getExtras();
		currentMode = bundle.getInt(MODE_KEY);
		if (currentMode == MODE_FOLDER) {
			currentPath = bundle.getString(MODE_VALUE_KEY);
			initFromFolder(currentPath);
		}
		else if (currentMode == MODE_ORDER) {
			int id = bundle.getInt(MODE_VALUE_KEY);
			initFromOrder(id);
		}
		else if (currentMode == MODE_LIST) {
			imagePathList = bundle.getStringArrayList(MODE_VALUE_KEY);
		}
		changeActionbarTitle();

		initGallery();
	}

	private void computeActionbarLayout() {
		if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
			mActionBar.onLandscape();
		}
		else {
			mActionBar.onVertical();
		}
	}

	private void setActionbarNormal() {
		mActionBar.clearActionIcon();
		mActionBar.addBackIcon();
		mActionBar.addMenuIcon();
		mActionBar.addRefreshIcon();
		mActionBar.addChangeIcon();
		mActionBar.addFullScreenIcon();
		computeActionbarLayout();
	}

	private void setActionbarSelectMode() {
		mActionBar.clearActionIcon();
		mActionBar.addMenuIcon();
		mActionBar.addDeleteIcon();
		computeActionbarLayout();
	}

	private void initFromFolder(String path) {
		imagePathList = wallController.loadFolderItems(path);
	}

	private void initFromOrder(int orderId) {
		currentOrder = wallController.loadOrder(orderId);
		imagePathList = currentOrder.getImgPathList();
	}

	private void initGallery() {
		wallAdapter = new WallAdapter(this, imagePathList);
		wallAdapter.setOnWallItemListener(this);
		int row = getResources().getInteger(R.integer.wall_rows);
		int screenHeight = ScreenUtils.getScreenHeight(this);
		int itemHeight = screenHeight / row;
		int itemWidth = (int) (itemHeight * 1.5f);
		wallAdapter.setImageItemSize(itemWidth, itemHeight);

		wallGallery.setAdapter(wallAdapter);
	}

	private void changeActionbarTitle() {
		if (currentMode == MODE_ORDER) {
			mActionBar.setTitle(currentOrder.getName());
		}
		else if (currentMode == MODE_FOLDER) {
			String[] array = currentPath.split("/");
			mActionBar.setTitle(array[array.length - 1]);
		}
		else if (currentMode == MODE_LIST) {
			mActionBar.setTitle(getResources().getString(R.string.menu_random));
		}
		else {
			mActionBar.setTitle(getResources().getString(R.string.wall_wallgallery));
		}
	}

	@Override
	public void onWallItemClick(View view, int position) {
		if (wallAdapter.isSelectMode()) {
			wallAdapter.setChecked(position);
		}
		else {
			showImageDialog.applyTransparentBackground();
			showImageDialog.setImagePath(imagePathList.get(position));
			showImageDialog.fitImageView();
			showImageDialog.show();
		}
	}

	/**
	 * wall item被放大后显示dialog
	 * 由于放大过程背景变暗，因此打开dialog后设置黑色背景
	 * @param path
	 */
	public void showImageWithDialog(String path) {
		showImageDialog.applyBlackBackground();
		showImageDialog.setImagePath(path);
		showImageDialog.fitImageView();
		showImageDialog.show();
	}

	@Override
	public void onWallItemLongClick(View view, int position) {
		if (!wallAdapter.isSelectMode()) {
			wallAdapter.resetMap();
			wallAdapter.setSelectMode(true);
			wallAdapter.setChecked(position);
			notifyGridViewRefresh();
			setActionbarSelectMode();
		}
	}

	@Override
	public void onBackPressed() {
		if (wallAdapter.isSelectMode()) {
			wallAdapter.setSelectMode(false);
			wallAdapter.resetMap();
			wallAdapter.disableImageRecycle();
			notifyGridViewRefresh();
			setActionbarNormal();
		}
		else if (wallAdapter.isMirrorMode()) {
			wallAdapter.closeMirror();
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {

		//wallParams.reload();
		//resizeGridView();
		if (orientation != newConfig.orientation) {
			orientation = newConfig.orientation;

			StaggeredGridLayoutManager layoutManager =
					(StaggeredGridLayoutManager) wallGallery.getLayoutManager();
			layoutManager.setSpanCount(getResources().getInteger(R.integer.wall_rows));
//			wallAdapter.resetWallRes();
//			horiGridView.reset();
//			horiGridView.setRow(getResources().getInteger(R.integer.wall_rows));
//			horiGridView.setWidth(ScreenUtils.getScreenWidth(this));
//			horiGridView.getItemWidth();
//			/**
//			 * 由于采用了重新setAdapter的方式，如果旋转前，已经滚动了一段距离，旋转后scrollview会默认scroll这么段距离
//			 * 而horiGridView内部的设计是根据scroll的位置来加载visible view的，所以要确保旋转后scroll to 0
//            **/
//			horiGridView.scrollTo(0, 0);
//			horiGridView.setAdapter(wallAdapter);

			if (showImageDialog != null) {
				showImageDialog.setOrientationChanged();
				if (showImageDialog.isShowing()) {
					showImageDialog.onConfigChange();
				}
			}
			computeActionbarLayout();
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBack() {
		finish();
	}

	private void deleteSelectedFile() {
		if (currentMode == MODE_FOLDER) {
			sOrderProvider.deleteItemFromFolder(getSelectedList());
		}
		else if (currentMode == MODE_ORDER) {
			sOrderProvider.deleteItemFromOrder(currentOrder, getSelectedIndex());
		}
	}

	@Override
	public void onIconClick(View view) {
		switch (view.getId()) {
			case R.id.actionbar_delete:
				if (currentMode == MODE_ORDER || currentMode == MODE_FOLDER) {
					deleteSelectedFile();
				}
				break;
			case R.id.actionbar_change:
				currentMode = MODE_LIST;
				changeActionbarTitle();

				progressDialog = new ProgressDialog(this);
				progressDialog.setMessage(getResources().getString(R.string.loading));
				progressDialog.show();

				loadRandomList();
				break;

			case R.id.actionbar_fullscreen:
				//FIXME full screen mode don't support SHOW_MODE_RANDOM currently
				if (currentMode == MODE_LIST) {
					Toast.makeText(this, R.string.spicture_fullscreen_not_support, Toast.LENGTH_LONG).show();
					return;
				}

				Bundle bundle = new Bundle();
				if (currentMode == MODE_FOLDER) {
					bundle.putInt("src_mode", UiController.SRC_MODE_FOLDER);
					bundle.putString("path", currentPath);
				}
				else if (currentMode == MODE_ORDER) {
					bundle.putInt("src_mode", UiController.SRC_MODE_ORDER);
					bundle.putInt("orderId", currentOrder.getId());
				}
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(this, SurfActivity.class);
				startActivity(intent);
				break;
			case R.id.actionbar_random_change:
				break;
			case R.id.actionbar_refresh:
				onRefresh();
				break;
			default:
				break;
		}
	}

	public void onRefresh() {
		if (currentMode == MODE_FOLDER) {
			initFromFolder(currentPath);
			notifyGridViewRefresh();
		}
		else if (currentMode == MODE_ORDER) {
			initFromOrder(currentOrder.getId());
			notifyGridViewRefresh();
		}
	}

	@Override
	public void createMenu(MenuInflater menuInflater, Menu menu) {
		loadMenu(menuInflater, menu);
	}

	@Override
	public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
		loadMenu(menuInflater, menu);
	}

	private void loadMenu(MenuInflater menuInflater, Menu menu) {
		menu.clear();
		menuInflater.inflate(R.menu.wallgallery, menu);
		if (wallAdapter.isSelectMode()) {
			menu.findItem(R.id.menu_wall_setaswall).setVisible(false);
			int checkedItemNum = wallAdapter.getCheckMap().size();
			if (checkedItemNum == 0) {
				menu.findItem(R.id.menu_thumb_deselectall).setVisible(false);
				menu.findItem(R.id.menu_thumb_setascover).setVisible(false);
				menu.findItem(R.id.menu_thumb_viewdetail).setVisible(false);
				menu.findItem(R.id.menu_thumb_selectall).setVisible(true);
				menu.findItem(R.id.menu_thumb_deselectall).setVisible(false);
			}
			else if (checkedItemNum == 1) {
				menu.findItem(R.id.menu_thumb_addtooder).setVisible(true);
				menu.findItem(R.id.menu_thumb_setascover).setVisible(true);
				menu.findItem(R.id.menu_thumb_viewdetail).setVisible(true);
				menu.findItem(R.id.menu_thumb_selectall).setVisible(true);
				menu.findItem(R.id.menu_thumb_deselectall).setVisible(true);
			}
			else if (checkedItemNum == imagePathList.size()) {
				menu.findItem(R.id.menu_thumb_addtooder).setVisible(true);
				menu.findItem(R.id.menu_thumb_setascover).setVisible(false);
				menu.findItem(R.id.menu_thumb_viewdetail).setVisible(false);
				menu.findItem(R.id.menu_thumb_selectall).setVisible(false);
				menu.findItem(R.id.menu_thumb_deselectall).setVisible(true);
			}
			else {
				menu.findItem(R.id.menu_thumb_addtooder).setVisible(true);
				menu.findItem(R.id.menu_thumb_setascover).setVisible(false);
				menu.findItem(R.id.menu_thumb_viewdetail).setVisible(false);
				menu.findItem(R.id.menu_thumb_selectall).setVisible(true);
				menu.findItem(R.id.menu_thumb_deselectall).setVisible(true);
			}
		}
		else {
			menu.findItem(R.id.menu_thumb_addtooder).setVisible(false);
			menu.findItem(R.id.menu_thumb_deselectall).setVisible(false);
			menu.findItem(R.id.menu_thumb_setascover).setVisible(false);
			menu.findItem(R.id.menu_thumb_viewdetail).setVisible(false);
			menu.findItem(R.id.menu_thumb_selectall).setVisible(false);
			menu.findItem(R.id.menu_thumb_deselectall).setVisible(false);
			menu.findItem(R.id.menu_wall_setaswall).setVisible(false);
			if (currentMode == MODE_LIST) {
				menu.findItem(R.id.menu_thumb_waterfall).setVisible(false);
			}
			else {
				menu.findItem(R.id.menu_thumb_waterfall).setVisible(true);
			}
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_wall_setaswall:
				wallController.saveDefaultWallRes();
				break;
			case R.id.menu_thumb_selectall:
				wallAdapter.selectAll();
				wallAdapter.disableImageRecycle();
				notifyGridViewRefresh();
				break;
			case R.id.menu_thumb_deselectall:
				wallAdapter.resetMap();
				wallAdapter.disableImageRecycle();
				notifyGridViewRefresh();
				break;
			case R.id.menu_thumb_addtooder:
				sOrderProvider.openOrderChooserToAddItem(getSelectedList());
				break;
			case R.id.menu_thumb_setascover:
				int index = wallAdapter.getCheckMap().keyAt(0);
				sOrderProvider.openOrderChooserToSetCover(imagePathList.get(index));
				break;
			case R.id.menu_thumb_viewdetail:
				int position = wallAdapter.getCheckMap().keyAt(0);
				viewDetails(position);
				break;
			case R.id.menu_thumb_waterfall:
				if (currentMode == MODE_FOLDER) {
					startFileWaterFallView();
				}
				else if (currentMode == MODE_ORDER) {
					startOrderWaterFallView();
				}
				break;
			default:
				break;
		}
		return false;
	}

	private void startFileWaterFallView() {
//		if (currentPath != null) {
//			File file = new File(currentPath);
//			if (file.list().length > Constants.WATERFALL_MIN_NUMBER) {
//				Intent intent = new Intent();
//				intent.setClass(this, WaterFallActivity.class);
//				intent.putExtra("filePath", file.getPath());
//				startActivity(intent);
//			}
//		}
	}

	private void startOrderWaterFallView() {
//		if (currentOrder != null && currentOrder.getImgPathList() != null) {
//			if (currentOrder.getImgPathList().size() > Constants.WATERFALL_MIN_NUMBER) {
//				Intent intent = new Intent();
//				intent.setClass(this, WaterFallActivity.class);
//				intent.putExtra("order", currentOrder.getId());
//				startActivity(intent);
//			}
//		}
	}

	private void viewDetails(int pos) {
		File file = new File(imagePathList.get(pos));
		if (file != null && file.exists()) {
			new DefaultDialogManager().openDetailDialog(this, file);
		}
	}

	protected List<Integer> getSelectedIndex() {
		List<Integer> list = null;
		SparseBooleanArray map = wallAdapter.getCheckMap();

		if (map.size() > 0) {
			list = new ArrayList<>();
			for (int i = 0; i < map.size(); i ++) {
				list.add(map.keyAt(i));
			}
			Collections.sort(list);
		}
		return list;
	}

	protected List<String> getSelectedList() {
		List<String> list = null;
		SparseBooleanArray map = wallAdapter.getCheckMap();

		if (map.size() > 0) {
			list = new ArrayList<>();
			for (int i = 0; i < map.size(); i ++) {
				list.add(imagePathList.get(map.keyAt(i)));
			}
		}
		return list;
	}

	@Override
	public void onTextChanged(String text, int start, int before, int count) {

	}

	private void loadRandomList() {
		new LoadRandomThread().run();
	}

	@Override
	public boolean handleMessage(Message msg) {

		resetGallery();
		progressDialog.cancel();
		return true;
	}

	private void resetGallery() {
		wallAdapter.notifyDataSetChanged();
//		wallAdapter.updatePathList(imagePathList);
//		horiGridView.setAdapter(wallAdapter);
	}

	private void notifyGridViewRefresh() {
		wallAdapter.notifyDataSetChanged();
	}

	private class LoadRandomThread extends Thread {

		private Handler handler = new Handler(WallActivity.this);

		@Override
		public void run() {
			if (imagePathList == null) {
				imagePathList = new ArrayList<String>();
			}
			else {
				imagePathList.clear();
			}

			WholeRandomManager manager = new WholeRandomManager();
			int total = SettingProperties.getCasualLookNumber(WallActivity.this);
			int max = manager.getTotal();
			if (max < total) {
				total = max;
			}

			String path = null;
			int maxTry = 1;

			for (int i = 0; i < total; i ++) {
				maxTry = 1;
				path = null;
				path = manager.getRandomPath();
				while (path == null && maxTry < 5) {
					path = manager.getRandomPath();
					maxTry ++;
				}
				imagePathList.add(path);
			}

			handler.sendMessage(new Message());
		}

	}

	@Override
	public void onClick(View v) {
		if (v == fitXYButton) {
			wallAdapter.changeScaleType(ImageView.ScaleType.FIT_XY);
		}
		else if (v == centerCropButton) {
			wallAdapter.changeScaleType(ImageView.ScaleType.CENTER_CROP);
		}
		else if (v == originButton) {
			wallAdapter.changeScaleType(ImageView.ScaleType.FIT_CENTER);
		}
		notifyGridViewRefresh();
	}

	@Override
	protected void onDestroy() {
		PictureManagerUpdate.getInstance().recycleWallItems();
		super.onDestroy();
	}

	/**
	 *
	 * @author JingYang
	 * 监听显示和隐藏action bar的手势操作
	 */
	private class ActionTouchListener {
		private final int OFFSET_X = 100;
		private final int OFFSET_Y = 200;

		private float startX, startY;

		public void onTouch(MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getRawX();
					startY = event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:

					break;
				case MotionEvent.ACTION_UP:
					float offsetX = event.getRawX() - startX;
					float offsetY = event.getRawY() - startY;

					if (Math.abs(offsetX) < OFFSET_X) {
						if (offsetY > OFFSET_Y) {//down show
							showActionBars();
						}
						else if (offsetY < -OFFSET_Y) {//up hide
							hideActionBars();
						}
					}
					break;

				default:
					break;
			}
		}
	}

	/**
	 * 由于视图中的view有touch事件处理，导致onTouchEvent没有响应，可能是被拦截了
	 * 只能在dispatchTouchEvent中处理一定要响应的touch操作
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		actionTouchListener.onTouch(event);
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	private void hideActionBars() {
		if (isActionBarShow) {
			actionbarLayout.startAnimation(AnimationManager.getToolbarOutAnim(this));
			bottomLayout.startAnimation(AnimationManager.getBottombarOutAnim(this));
			actionbarLayout.setVisibility(View.GONE);
			bottomLayout.setVisibility(View.GONE);
		}
		isActionBarShow = false;
	}
	private void showActionBars() {
		if (!isActionBarShow) {
			actionbarLayout.startAnimation(AnimationManager.getToolbarInAnim(this));
			bottomLayout.startAnimation(AnimationManager.getBottombarInAnim(this));
			actionbarLayout.setVisibility(View.VISIBLE);
			bottomLayout.setVisibility(View.VISIBLE);
		}
		isActionBarShow = true;
	}

}
