package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jing.app.jjgallery.Application;
import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.filesystem.FilePageItem;
import com.jing.app.jjgallery.controller.AccessController;
import com.jing.app.jjgallery.presenter.main.filesystem.FileChangeListener;
import com.jing.app.jjgallery.presenter.main.filesystem.FileListController;
import com.jing.app.jjgallery.presenter.main.filesystem.FileManagerPresenter;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DefaultDialogManager;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;
import com.king.lib.jindicator.IndicatorNode;
import com.king.lib.jindicator.IndicatorView;

import java.io.File;
import java.util.List;

public class FileManagerListPage implements IPage, FileChangeListener {

	private final String TAG = "FileManagerPage";

	private Context context;
	private View view;

	private IndicatorView indicatorView;
	private ImageView parentView;
	private TextView nameTagView, timeTagView, imageWHView;
	private ImageView nameSortIconView, timeSortIconView;

	private ListView fileListView;
	private FileListAdapter adapter;
	private RadioButton allRadio, encryptedRadio, unencryptedRadio;
	private FileListAction listAction;
	private FileListController listController;
	private FileManagerPresenter mPresenter;
	private ProgressDialog progressDialog;

	public FileManagerListPage(Context context, View view) {
		this.context = context;
		this.view = view;
		listAction = new FileListAction();
		listController = new FileListController(context, listAction.getHandler(), this);

		initViewElement();
		initView();
	}

	private void initView() {
		showParentItem();
		refresh();
	}

	public String getCurrentPath() {
		return listController.getCurrentPath();
	}

	private void initViewElement() {
		showCurPathView();
		parentView = (ImageView) view.findViewById(R.id.filelist_parent);
		nameTagView = (TextView) view.findViewById(R.id.filelist_tag_name);
		timeTagView = (TextView) view.findViewById(R.id.filelist_tag_time);
		imageWHView = (TextView) view.findViewById(R.id.filelist_tag_wh);
		nameSortIconView = (ImageView) view.findViewById(R.id.filelist_tag_name_sorticon);
		timeSortIconView = (ImageView) view.findViewById(R.id.filelist_tag_time_sorticon);
		fileListView = (ListView) view.findViewById(R.id.filelist_listview);
		allRadio = (RadioButton) view.findViewById(R.id.filelist_radio_all);
		encryptedRadio = (RadioButton) view.findViewById(R.id.filelist_radio_encrypted);
		unencryptedRadio = (RadioButton) view.findViewById(R.id.filelist_radio_unencrypted);

		fileListView.setOnItemClickListener(listAction);
		fileListView.setOnItemLongClickListener(listAction);
		parentView.setOnClickListener(listAction);
		nameTagView.setOnClickListener(listAction);
		timeTagView.setOnClickListener(listAction);
		nameSortIconView.setOnClickListener(listAction);
		timeSortIconView.setOnClickListener(listAction);

		if (Application.isLollipop()) {
			parentView.setBackgroundResource(R.drawable.ripple_filemanager_tag_bk);
			nameSortIconView.setBackgroundResource(R.drawable.ripple_filemanager_tag_bk);
			timeSortIconView.setBackgroundResource(R.drawable.ripple_filemanager_tag_bk);
		}

		resetTimeSortTag();
		resetNameSortTag();

		allRadio.setOnCheckedChangeListener(listAction);
		encryptedRadio.setOnCheckedChangeListener(listAction);
		unencryptedRadio.setOnCheckedChangeListener(listAction);
	}

	private void showCurPathView() {
		List<IndicatorNode> pathList = null;
		if (indicatorView != null) {
			indicatorView.setVisibility(View.GONE);
			pathList = indicatorView.getPathList();//转屏时取出之前的pathList，转屏后新的indicatorView以此初始化indicator
		}

		if (DisplayHelper.isTabModel(context)) {
			indicatorView = (IndicatorView) view.findViewById(R.id.filelist_current_dir_hor);
		}
		else {
			if (context.getResources().getConfiguration().orientation
					== android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
				indicatorView = (IndicatorView) view.findViewById(R.id.filelist_current_dir_hor);
			}
			else {
				indicatorView = (IndicatorView) view.findViewById(R.id.filelist_current_dir);
			}
		}
		indicatorView.setVisibility(View.VISIBLE);
		indicatorView.setPathIndicatorListener(listAction);

		if (pathList == null) {
			indicatorView.addPath(listController.getCurrentPath());
		}
		else {
			indicatorView.create(pathList);
		}
		//currentPathView.setText(listController.getCurrentPath());
	}

	private void resetNameSortTag() {
		nameSortIconView.setVisibility(View.INVISIBLE);
		nameSortIconView.setTag(true);//按名称默认升序
	}
	private void resetTimeSortTag() {
		timeSortIconView.setVisibility(View.INVISIBLE);
		timeSortIconView.setTag(false);//按时间默认降序
	}

	private void showImageWHView(boolean show) {
		if (DisplayHelper.isTabModel(context) || context.getResources().getConfiguration().orientation
				== android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
			imageWHView.setVisibility(show ? View.VISIBLE : View.GONE);
			adapter.showImageWH(show);
		}
		else {
			imageWHView.setVisibility(View.GONE);
			adapter.showImageWH(false);
		}
	}
	private void refresh() {
		listController.findFile();
	}

	private void notifyAdapterRefresh() {
		//v6.3.7 deprecated this display mode, replace it with indicator path view
		//currentPathView.setText(listController.getCurrentPath());
		if (listController.getFilePageItemList() == null) {
			((ProgressProvider) context).showToastLong(context.getString(R.string.error_app_root_not_exist), ProgressProvider.TOAST_ERROR);
			return;
		}
		Log.i(TAG, "notifyAdapterRefresh -> " + listController.getCurrentPath());

		if (adapter == null) {
			adapter = new FileListAdapter(listController.getFilePageItemList(), context);
			fileListView.setAdapter(adapter);
		}
		else {
			adapter.updateList(listController.getFilePageItemList());
			if (listController.getFilePageItemList() != null && listController.getFilePageItemList().size() > 0) {
				if (!listController.getFilePageItemList().get(0).getFile().isDirectory()) {
					showImageWHView(true);
				}
			}
			adapter.notifyDataSetChanged();
		}
	}

	private void showParentItem() {
		if (listController.isRootFolder()) {
			parentView.setVisibility(View.INVISIBLE);
		}
		else {
			parentView.setVisibility(View.VISIBLE);
		}
	}

	private void showProgress() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getResources().getString(R.string.loading));
		progressDialog.show();
	}
	private void cancelProgress() {
		if (progressDialog != null) {
			progressDialog.cancel();
		}
	}

	private void decipherCurFolder() {
		showProgress();
		listController.decipherCurFolder();
	}

	private void encryptCurFolder() {
		showProgress();
		listController.encryptCurFolder();
	}

	private void openCreateFolderDialog() {
		new DefaultDialogManager().openCreateFolderDialog(context
				, new DefaultDialogManager.OnDialogActionListener() {

					@Override
					public void onOk(String name) {
						String filePath = listController.getCurrentPath() + "/" + name;
						if (listController.createFolder(new File(filePath))) {
							((ProgressProvider) context).showToastLong(context.getString(R.string.success), ProgressProvider.TOAST_SUCCESS);
							refresh();
						}
						else {
							((ProgressProvider) context).showToastLong(context.getString(R.string.filelist_folder_already_exist), ProgressProvider.TOAST_WARNING);
						}
					}
				});
	}

	private ShowImageDialog imageDialog;

	private void showImage(String path) {
		if (imageDialog == null) {
			imageDialog = new ShowImageDialog(context, null, 0);
		}
		imageDialog.setImagePath(path);
		imageDialog.show();
	}

	private void openByWall(String path) {
		ActivityManager.startWallActivity((Activity) context, path);
	}

	private void startFullScreenActivity(String path) {
		ActivityManager.startSurfActivity((Activity) context, path);
	}

	private void startBookView(String path) {
		ActivityManager.startBookActivity((Activity) context, path);
	}

	@Override
	public void onFindFileFinish() {
		notifyAdapterRefresh();
	}

	@Override
	public void onBackToRoot() {
		showParentItem();
	}

	@Override
	public boolean onBack() {
		if (!indicatorView.isBackable()) {
			return true;
		}
		if (parentView.getVisibility() == View.VISIBLE) {
			indicatorView.backToUpper();
			if (listController != null) {
				listController.findParent();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onExit() {

	}

	@Override
	public void onIconClick(View view) {
		switch (view.getId()) {

			case R.id.actionbar_add:
				openCreateFolderDialog();
				break;
//			case R.id.actionbar_sort:
//				showSortPopup(v);
//				break;
			case R.id.actionbar_cover:
				break;
			case R.id.actionbar_refresh:
				refresh();
				break;
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

	public void loadMenu(MenuInflater menuInflater, Menu menu) {
		menu.clear();
		menuInflater.inflate(R.menu.home_file_manager, menu);
		menu.setGroupVisible(R.id.group_file, true);
		menu.setGroupVisible(R.id.group_home_public, true);
//		menu.findItem(R.id.menu_edit).setVisible(true);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
    	/*
		case R.id.menu_create_folder:
			openCreateFolderDialog();
			break;
			*/
			case R.id.menu_encrypt_current_folder:
				encryptCurFolder();
				break;
			case R.id.menu_decipher_current_folder:
				decipherCurFolder();
				break;
			/*
		case R.id.menu_thumb_folder:
			startThumbView();
			break;
			*/
		}
		return  true;
	}

	@Override
	public void onTextChanged(String text, int start, int before, int count) {

	}

	@Override
	public void initActionbar(ActionBar actionBar) {
		actionBar.clearActionIcon();
		actionBar.addAddIcon();
		actionBar.addRefreshIcon();
		actionBar.addColorIcon();
		actionBar.addMenuIcon();
		actionBar.onConfiguration(context.getResources().getConfiguration().orientation);
	}

	private class FileListAction implements Callback, OnItemClickListener
			, OnItemLongClickListener, OnClickListener, OnCheckedChangeListener
			, IndicatorView.PathIndicatorListener {

		private Handler handler = null;

		public FileListAction() {
			handler = new Handler(this);
		}
		public Handler getHandler() {
			return handler;
		}
		@Override
		public boolean handleMessage(Message msg) {

			Bundle bundle = msg.getData();
			boolean isok = bundle.getBoolean("result");
			if (isok) {
				Log.i(TAG, "FileListAction handleMessage " + isok);
				refresh();
			}
			else {
				if (msg.what == FileListController.FILE_TYPE_ENCRYPTED) {
					((ProgressProvider) context).showToastLong(context.getString(R.string.filelist_encrypt_fail), ProgressProvider.TOAST_ERROR);
				}
				else if (msg.what == FileListController.FILE_TYPE_UNENCRYPTED) {
					((ProgressProvider) context).showToastLong(context.getString(R.string.filelist_decipher_fail), ProgressProvider.TOAST_ERROR);
				}
			}
			cancelProgress();
			return true;
		}

		private boolean deleteFile(int position) {
			final File file = listController.getFilePageItemList().get(position).getFile();
			Log.i(TAG, " deleteFile " + file.getPath());
			boolean canDelete = true;

			if (!file.exists()) {
				return false;
			}
			String msg = context.getResources().getString(R.string.filelist_delete_msg);
			if (file.isDirectory()) {
				File[] subFiles = file.listFiles();
				if (subFiles.length == 0) {
					msg = context.getResources().getString(R.string.filelist_delete_empty_folder);
				}
				else if (subFiles.length < 10) {
					for (int i = 0; i < subFiles.length; i ++) {
						if (subFiles[i].isDirectory()) {
							msg = context.getResources().getString(R.string.filelist_delete_fodler_deny);
							canDelete = false;
							break;
						}
					}
					if (canDelete) {
						msg = context.getResources().getString(R.string.filelist_delete_folder);
					}
				}
				else if (subFiles.length > 10) {
					msg = context.getResources().getString(R.string.filelist_delete_fodler_deny);
					canDelete = false;
				}
			}
			final boolean canDeleted = canDelete;
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle(R.string.filelist_delete);
			dialog.setMessage(msg);
			if (canDeleted) {
				dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						listController.deleteFile(file);
						refresh();
						Log.i(TAG, " deleteFile ok");
					}
				})
						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						});
			}
			dialog.show();
			return true;
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
									   int position, long arg3) {
			final int pos = position;
			Log.i(TAG, "onItemLongClick" + pos);
			final FilePageItem item = listController.getFilePageItemList().get(position);
			if (item.getFile().isDirectory()) {
				String[] arrays = context.getResources().getStringArray(R.array.filelist_longclick_action_folder);
				new AlertDialog.Builder(context)
						.setItems(arrays, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0) {//delete
									deleteFile(pos);
								}
								else if (which == 1) {//open by wall
									openByWall(item.getFile().getPath());
								}
								else if (which == 2) {//full screen
									startFullScreenActivity(item.getFile().getPath());
								}
								else if (which == 3) {//book view
									startBookView(item.getFile().getPath());
								}
							}
						}).show();
			}
			else {
				if (listController.isEncrypted(item.getFile().getPath())) {
					String[] arrays = context.getResources().getStringArray(R.array.filelist_longclick_action_decipher);
					new AlertDialog.Builder(context)
							.setItems(arrays, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (which == 0) {
										showProgress();
										listController.decipherFile(pos);
									}
									else if (which == 1) {
										deleteFile(pos);
									}
								}
							}).show();
				}
				else {
					String[] arrays = context.getResources().getStringArray(R.array.filelist_longclick_action_encrypt);
					new AlertDialog.Builder(context)
							.setItems(arrays, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (which == 0) {
										showProgress();
										listController.encryptFile(pos);
									}
									else if (which == 1) {
										deleteFile(pos);
									}
								}
							}).show();
				}
			}

			return true;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
								long arg3) {
			Log.i(TAG, "onItemClick" + position);
			File file = listController.getFilePageItemList().get(position).getFile();
			if (file.isDirectory()) {
				listController.setCurrentPath(file.getPath());

				indicatorView.addPath(file.getPath());

				if (parentView.getVisibility() != View.VISIBLE) {
					showParentItem();
				}

				listController.findFile();

				//update scroll position
				listController.updateParentPosition(fileListView.getFirstVisiblePosition());
				//need be called after onFindFileFinish
//				fileListView.setSelection(listController.getScrollPosition());
			}
			else if (listController.isEncryptedFile(file)) {
				if (AccessController.getInstance().getAccessMode() > AccessController.ACCESS_MODE_PRIVATE) {
					showImage(file.getPath());
				}
			}
		}

		@Override
		public void onClick(View v) {
			if (v == parentView) {
				Log.i(TAG, "parent onClick");

				// 正在执行上一个返回动画，不允许执行下一个返回
				if (!indicatorView.isBackable()) {
					return;
				}

				showImageWHView(false);
				indicatorView.backToUpper();

				File file = new File(listController.getCurrentPath());
				if (!file.exists()) {
					listController.findFile();
				}
				else {
					listController.findParent();
					//update scroll position
					listController.updateChildPosition(fileListView.getFirstVisiblePosition());
					//need be called after onFindFileFinish
					fileListView.setSelection(listController.getScrollPosition());
				}
			}
			else if (v == nameTagView || v == nameSortIconView) {
				boolean decrease = !((Boolean) nameSortIconView.getTag());

				if (decrease) {
					nameSortIconView.setImageResource(R.drawable.sort_decrease);
				}
				else {
					nameSortIconView.setImageResource(R.drawable.sort_increase);
				}
				nameSortIconView.setTag(decrease);
				nameSortIconView.setVisibility(View.VISIBLE);
				resetTimeSortTag();
				listController.setSortMode(FileListController.SORT_BY_NAME, decrease);
				listController.sortByName(decrease);
			}
			else if (v == timeTagView || v == timeSortIconView) {

				boolean decrease = !((Boolean) timeSortIconView.getTag());

				if (decrease) {
					timeSortIconView.setImageResource(R.drawable.sort_decrease);
				}
				else {
					timeSortIconView.setImageResource(R.drawable.sort_increase);
				}
				timeSortIconView.setTag(decrease);
				timeSortIconView.setVisibility(View.VISIBLE);
				resetNameSortTag();
				listController.setSortMode(FileListController.SORT_BY_DATE, decrease);
				listController.sortByTime(decrease);
			}
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
									 boolean isChecked) {
			if (isChecked) {
				Log.i(TAG, "radio before check currentType = " + listController.getFileType());
				if (buttonView == allRadio) {
					encryptedRadio.setChecked(false);
					unencryptedRadio.setChecked(false);
					listController.setFindAll(true);
					listController.setFindEncrypted(false);
					listController.setFindUnEncrypted(false);
					if (listController.getFileType() != FileListController.FILE_TYPE_ALL) {
						listController.findFile();
					}
				}
				else if (buttonView == encryptedRadio) {
					allRadio.setChecked(false);
					unencryptedRadio.setChecked(false);
					listController.setFindEncrypted(true);
					listController.setFindAll(false);
					listController.setFindUnEncrypted(false);
					if (listController.getFileType() != FileListController.FILE_TYPE_ENCRYPTED) {
						listController.findFile();
					}
				}
				else if (buttonView == unencryptedRadio) {
					encryptedRadio.setChecked(false);
					allRadio.setChecked(false);
					listController.setFindUnEncrypted(true);
					listController.setFindEncrypted(false);
					listController.setFindAll(false);
					if (listController.getFileType() != FileListController.FILE_TYPE_UNENCRYPTED) {
						listController.findFile();
					}
				}

				Log.i(TAG, "radio after check currentType = " + listController.getFileType());
			}
		}
		@Override
		public void onClickPath(int index, String path) {
			listController.setCurrentPath(path);
			showParentItem();
			listController.findFile();
		}
	}

	@Override
	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		showCurPathView();
		if (!DisplayHelper.isTabModel(context)) {//normal phone show image size only in landscape
			notifyAdapterRefresh();
		}
//		if (imageDialog != null && imageDialog.isShowing()) {
//			imageDialog.onConfigChange();
//		}
	}

	@Override
	public void onTouchEvent(MotionEvent event) {

	}

	@Override
	public void initData() {

	}

	@Override
	public void setPresenter(BasePresenter presenter) {
		mPresenter = (FileManagerPresenter) presenter;
	}

	/*
	private void showSortPopup(View v) {
		PopupMenu menu = new PopupMenu(context, v);
		menu.getMenuInflater().inflate(R.menu.sort_order, menu.getMenu());
		menu.show();
		menu.setOnMenuItemClickListener(sortListener);
	}
	*/
	
	/*
	OnMenuItemClickListener sortListener = new OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.menu_by_date:
				sortByTime();
				break;
			case R.id.menu_by_name:
				sortByName();
				break;
				
			default:
				break;
			}
			return true;
		}
	};
	*/

}
