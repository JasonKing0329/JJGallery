package com.jing.app.jjgallery.viewsystem.sub.surf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.model.sub.AutoPlayController;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.order.SOrderProvider;
import com.jing.app.jjgallery.presenter.main.order.SOrderProviderCallback;
import com.jing.app.jjgallery.presenter.sub.SurfPresenter;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JingYang
 * @version create time：2016-1-26 下午3:13:28
 *
 */
public class UiController implements SurfUiAction, OnMenuItemClickListener
		, Callback, SOrderProviderCallback{

	public static final int SRC_MODE_FOLDER = 0;
	public static final int SRC_MODE_ORDER = 1;
	public static final int SRC_MODE_RANDOM = 2;
	private int srcMode;

	private SurfActivity surfActivity;
	private ImageView deleteButton, favorateButton, playButton, moreButton
			, detailButton, coverButton, seizeButton;

	private SurfPresenter mPresenter;

	private List<String> mImageList;
	/**
	 * init from folder
	 */
	private String currentFolder;
	/**
	 * init from order
	 */
	private SOrder currentOrder;
	private int currentPosition;
	private String currentImagePath;

	private SOrderProvider sOrderProvider;
	private PopupMenu popupMenu;
	private AutoPlayController autoPlayController;

	public UiController(SurfActivity activity) {
		surfActivity = activity;
		DisplayHelper.keepScreenOn(surfActivity);

		mPresenter = new SurfPresenter();
		sOrderProvider = new SOrderProvider(activity, this);
	}

	@Override
	public void loadToolbar(RelativeLayout container) {
		View view = LayoutInflater.from(surfActivity).inflate(R.layout.activity_surf_toolbar, null);
		deleteButton = (ImageView) view.findViewById(R.id.surf_toolbar_delete);
		favorateButton = (ImageView) view.findViewById(R.id.surf_toolbar_addtoorder);
		playButton = (ImageView) view.findViewById(R.id.surf_toolbar_play);
		moreButton = (ImageView) view.findViewById(R.id.surf_toolbar_more);
		detailButton = (ImageView) view.findViewById(R.id.surf_toolbar_detail);
		coverButton = (ImageView) view.findViewById(R.id.surf_toolbar_setascover);
		seizeButton = (ImageView) view.findViewById(R.id.surf_toolbar_seize);
		deleteButton.setOnClickListener(toolbarListener);
		favorateButton.setOnClickListener(toolbarListener);
		playButton.setOnClickListener(toolbarListener);
		moreButton.setOnClickListener(toolbarListener);
		detailButton.setOnClickListener(toolbarListener);
		coverButton.setOnClickListener(toolbarListener);
		seizeButton.setOnClickListener(toolbarListener);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		container.addView(view, params);
	}

	@Override
	public void loadImages(Intent intent) {

		Bundle bundle = intent.getExtras();
		srcMode =  bundle.getInt("src_mode");
		if (srcMode == SRC_MODE_FOLDER) {
			currentFolder = bundle.getString("path");
			initDataFromFolder();
		}
		else if (srcMode == SRC_MODE_ORDER) {
			int orderId = bundle.getInt("orderId");
			initDataFromOrder(orderId);
		}
//		else if (srcMode == SRC_MODE_RANDOM) {
//			hideView = imageView1;
//			randomManager = new WholeRandomManager(encrypter);
//			executeWholeRandom();
//		}
	}

	@Override
	public List<String> getImageList() {
		return mImageList;
	}

	@Override
	public void onSwitchPage(int position) {
		currentPosition = position;
		currentImagePath = mImageList.get(position);
	}

	private void initDataFromFolder() {
		mImageList = mPresenter.loadFromFolder(currentFolder);
		if (mImageList.size() > 0) {
			currentImagePath = mImageList.get(0);
		}
	}

	private void initDataFromOrder(int orderId) {
		currentOrder = mPresenter.queryOrder(orderId);
		mPresenter.getOrderItemList(currentOrder);
		mImageList = currentOrder.getImgPathList();
		if (mImageList != null && mImageList.size() > 0) {
			currentImagePath = mImageList.get(0);
		}
	}

	OnClickListener toolbarListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (view == deleteButton) {
				if (currentImagePath != null) {
					showDeleteWarning();
				}
			}
			else if (view == favorateButton) {
				if (currentImagePath != null) {
					List<String> list = new ArrayList<>();
					list.add(currentImagePath);
					sOrderProvider.openOrderChooserToAddItem(list);
				}
			}
			else if (view == moreButton) {
				if (currentImagePath != null) {
					showMenu();
				}
			}
			else if (view == detailButton) {
				if (currentImagePath != null) {
					sOrderProvider.viewDetails(currentImagePath);
				}
			}
			else if (view == playButton) {
				if (currentImagePath != null) {
					surfActivity.hideToobarAndGuide();
					autoPlay();
				}
			}
			else if (view == coverButton) {
				if (currentImagePath != null) {
					sOrderProvider.openOrderChooserToSetCover(currentImagePath);
				}
			}
			else if (view == seizeButton) {
				if (mPresenter.isGifImage(currentImagePath)) {
					surfActivity.showToastLong(surfActivity.getString(R.string.surf_seize_not_support), ProgressProvider.TOAST_WARNING);
				}
				else {
					if (currentImagePath != null) {
						ShowImageDialog dialog = new ShowImageDialog(surfActivity, null, 0);
						dialog.setImagePath(currentImagePath);
						dialog.setStartWithCrop();
						dialog.show();
					}
				}
			}
		}
	};

	protected void showMenu() {

//		if (popupMenu == null) {
//			popupMenu = new PopupMenu(surfActivity, moreButton);
//			popupMenu.getMenuInflater().inflate(R.menu.surf_gallery, popupMenu.getMenu());
//			popupMenu.setOnMenuItemClickListener(this);
//		}
//		popupMenu.show();

		sOrderProvider.openBackgroundSelector(currentImagePath);
	}

	protected void showDeleteWarning() {
		if (srcMode == SRC_MODE_ORDER) {
			List<Integer> list = new ArrayList<>();
			list.add(currentPosition);
			sOrderProvider.deleteItemFromOrder(currentOrder, list);
		}
		else if (srcMode == SRC_MODE_FOLDER) {
			List<String> list = new ArrayList<>();
			list.add(currentImagePath);
			sOrderProvider.deleteItemFromFolder(list);
		}
//		new DefaultDialogManager().showWarningActionDialog(surfActivity
//				, surfActivity.getResources().getString(R.string.filelist_delete_msg)
//				, surfActivity.getResources().getString(R.string.ok)
//				, null
//				, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						if (which == DialogInterface.BUTTON_POSITIVE) {
//							if (srcMode == SRC_MODE_ORDER) {
//								deleteItemFromOrder();
//							}
//							else if (srcMode == SRC_MODE_FOLDER) {
//								deleteItemFromFolder();
//							}
//						}
//					}
//				});
	}

	private void deleteItem(boolean deleted) {
		String msg = null;
		if (deleted) {
			msg = surfActivity.getResources().getString(R.string.surf_delete_success);
			mImageList.remove(currentPosition);

			int removeIndex = currentPosition;
			if (currentPosition == mImageList.size()) {
				currentPosition --;
				currentImagePath = null;
			}
			else {
				onSwitchPage(currentPosition);
			}
			surfActivity.onPageDeleted(removeIndex, currentPosition);
		}
		else {
			msg = surfActivity.getResources().getString(R.string.surf_delete_fail);
		}

		surfActivity.showToastLong(msg, ProgressProvider.TOAST_INFOR);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_slidingmenu_left:
				break;
			case R.id.menu_slidingmenu_right:
				break;
			case R.id.menu_slidingmenu_left_land:
				break;
			case R.id.menu_slidingmenu_right_land:
				break;
			default:
				break;
		}
		return true;
	}

	protected void autoPlay() {

		if (autoPlayController == null) {
			autoPlayController = new AutoPlayController(surfActivity, this);
		}
		if (stopAutoPlay()) {
			return;
		}

		if (srcMode == SRC_MODE_RANDOM) {
			playButton.setImageResource(R.drawable.ic_stop_white_36dp);
			autoPlayController.startWholeRandomAutoPlay(SettingProperties.getAnimationSpeed(surfActivity));
		}
		else {
			autoPlayController.setFileNameList(mImageList);
			if (autoPlayController.canPlay()) {
				playButton.setImageResource(R.drawable.ic_stop_white_36dp);
				autoPlayController.startAutoPlay(SettingProperties.getAnimationSpeed(surfActivity));
			}
			else {
				String msg = surfActivity.getResources().getString(R.string.spicture_autoplay_tooless);
				msg = String.format(msg, SettingProperties.getMinNumberToPlay(surfActivity));
				surfActivity.showToastLong(msg, ProgressProvider.TOAST_WARNING);
			}
		}
	}

	@Override
	public boolean stopAutoPlay() {
		if (autoPlayController != null && autoPlayController.isAutoPlaying()) {
			autoPlayController.stopAutoPlay();
			playButton.setImageResource(R.drawable.ic_play_arrow_white_36dp);
			surfActivity.showToobarAndGallery();
			return true;
		}
		return false;
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == AutoPlayController.AUTO_SPECIFIED_LIST) {
			surfActivity.playPage(currentPosition);
			onSwitchPage(currentPosition);
			currentPosition ++;
			if (currentPosition == mImageList.size()) {
				currentPosition --;
				stopAutoPlay();
				surfActivity.showToastLong(surfActivity.getString(R.string.spicture_autoplay_finish), ProgressProvider.TOAST_INFOR);
			}
		}
		return false;
	}

	@Override
	public void onMoveFinish(String folderPath) {

	}

	@Override
	public void onAddToOrderFinished() {

	}

	@Override
	public void onDeleteIndex(int index) {

	}

	@Override
	public void onDeleteFinished(int count) {
		deleteItem(true);
	}
}
