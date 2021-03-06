package com.jing.app.jjgallery.viewsystem.sub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jing.app.jjgallery.Application;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.image.PictureManagerUpdate;
import com.jing.app.jjgallery.model.main.file.MoveController;
import com.jing.app.jjgallery.presenter.main.order.SOrderProvider;
import com.jing.app.jjgallery.presenter.main.order.SOrderProviderCallback;
import com.jing.app.jjgallery.service.image.CropHelper;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.CropInforView;
import com.jing.app.jjgallery.viewsystem.publicview.CropView;
import com.jing.app.jjgallery.service.image.ZoomListener;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.sub.gifview.MyGifManager;
import com.king.lib.saveas.SaveAsDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowImageDialog extends Dialog implements View.OnClickListener
		, CropView.OnCropAreaChangeListener, Callback, OnSeekBarChangeListener, SOrderProviderCallback{

	private final String TAG = "ShowImageDialog";
	private final boolean DEBUG = Application.DEBUG;
	/**
	 * Gif 模式下，seizeButton将被zoomButton替代
	 */
	private ImageView addButton, moveButton, closeButton, seizeButton, saveButton
			, cropFullScreenButton, cropAreaSizeButton
			, detailsButton, setCoverButton, setAsMenuBkButton;
	private ActionListener actionListener;
	private ImageView showImageView;
	private TextView filenameView;
	private String imagePath, cropImagePath, displayImagePath;
	private Bitmap bitmap, cropBitmap;
	private boolean isOrienChanged;
//	private SpictureController controller;
	private int actionbarHeight;
	private ListPopupWindow setAsMenuBkPopup, cropAreaSizePopup, zoomPopup;
	private CropView cropView;
	private CropInforView cropInforView;
	private LinearLayout cropActionLayout;
	private LinearLayout actionbar, cropActionbar;
	private TextView doneButton, cancelButton;
	private String[] cropAreaSizeArray;

	private FolderDialog folderDialog;
	private MoveController moveController;
	private String moveObjectPath;

	private LinearLayout gifContainer;
	private MyGifManager gifManager;
	private SeekBar gifSeekBar;

	private SOrderProvider sOrderProvider;
	private ProgressProvider progressProvider;
	/**
	 *
	 * @param context
	 * @param listener if null, execute default action(dialog自定义“添加至列表”、“设置封面”、“查看详情”实现功能，也提供listener可由引用处定义)
	 * @param actionbarHeight define if window height should consider outside actionbarHeight
	 */
	public ShowImageDialog(Context context, ActionListener listener, int actionbarHeight) {
		super(context, R.style.TransparentDialog);
		if (context instanceof  ProgressProvider) {
			progressProvider = (ProgressProvider) context;
		}
		setContentView(R.layout.dialog_showimage_l);
		this.actionListener = listener;
		this.actionbarHeight = actionbarHeight;

		sOrderProvider = new SOrderProvider(context, this);

		addButton = (ImageView) findViewById(R.id.actionbar_add);
		moveButton = (ImageView) findViewById(R.id.actionbar_movetofolder);
		closeButton = (ImageView) findViewById(R.id.actionbar_close);
		detailsButton = (ImageView) findViewById(R.id.actionbar_details);
		setCoverButton = (ImageView) findViewById(R.id.actionbar_setcover);
		seizeButton = (ImageView) findViewById(R.id.actionbar_seize);
		cropFullScreenButton = (ImageView) findViewById(R.id.actionbar_crop_fullscreen);
		saveButton = (ImageView) findViewById(R.id.actionbar_save);
		cropAreaSizeButton = (ImageView) findViewById(R.id.actionbar_crop_areasize);
		setAsMenuBkButton = (ImageView) findViewById(R.id.actionbar_setasmenubk);
		showImageView = (ImageView) findViewById(R.id.showimage_imageview);
		cropView = (CropView) findViewById(R.id.showimage_cropview);
		filenameView = (TextView) findViewById(R.id.showimage_filename);
		cropView.setOnCropAreaChangeListener(this);
		cropInforView = (CropInforView) findViewById(R.id.showimage_cropvinfor);
		actionbar = (LinearLayout) findViewById(R.id.showimage_actionbar);
		cropActionbar = (LinearLayout) findViewById(R.id.showimage_crop_actionbar);
		cropActionLayout = (LinearLayout) findViewById(R.id.showimage_crop_actionview);
		doneButton = (TextView) findViewById(R.id.showimage_crop_action_done);
		cancelButton = (TextView) findViewById(R.id.showimage_crop_action_cancel);
		gifContainer = (LinearLayout) findViewById(R.id.showimage_gifview);
		gifManager = new MyGifManager(context, gifContainer);
		gifSeekBar = (SeekBar) findViewById(R.id.showimage_gif_seekbar);

		addButton.setOnClickListener(this);
		moveButton.setOnClickListener(this);
		closeButton.setOnClickListener(this);
		detailsButton.setOnClickListener(this);
		setCoverButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		seizeButton.setOnClickListener(this);
		cropAreaSizeButton.setOnClickListener(this);
		cropFullScreenButton.setOnClickListener(this);
		setAsMenuBkButton.setOnClickListener(this);
		doneButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		showImageView.setOnTouchListener(new ZoomListener(true));
		gifSeekBar.setOnSeekBarChangeListener(this);
		filenameView.setOnTouchListener(filenameListener);

		initWindowParams();
//		controller = new SpictureController(context);
	}

	View.OnTouchListener filenameListener = new View.OnTouchListener() {

		private String originPath;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					originPath = filenameView.getText().toString();
					filenameView.setText(imagePath);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_CANCEL:
					filenameView.setText(originPath);
					break;
			}
			return true;
		}
	};

	public void setOrientationChanged() {
		isOrienChanged = true;
	}

	private void initWindowParams() {
		WindowManager.LayoutParams params = getWindow().getAttributes();
		if (DisplayHelper.isFullScreen()) {
			params.y = actionbarHeight;
		}
		else {
			params.y = getContext().getResources().getDimensionPixelSize(R.dimen.screen_notifybar_height) + actionbarHeight;
		}

		Point point = DisplayHelper.getScreenSize(getContext());
		params.width = point.x;
		params.height = point.y - params.y;
		getWindow().setAttributes(params);
	}

	public void onConfigChange() {
		initWindowParams();

		//很奇怪如果不重新用代码设置，imageview将充不满父控件
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) showImageView.getLayoutParams();
		params.width = FrameLayout.LayoutParams.MATCH_PARENT;
		params.height = FrameLayout.LayoutParams.MATCH_PARENT;

		//cropView和cropInforView都是根据屏幕宽高计算的move区域，
		//orientation改变之后一定要重新初始化参数（主要是屏幕宽高）
		cropView.initParams();
		cropInforView.init();

		fitImageView();
		if (folderDialog != null) {
			folderDialog.updateHeight();
		}
	}

	public void fitImageView() {
		if (bitmap != null) {
			Matrix matrix = new Matrix();
			WindowManager.LayoutParams attr = getWindow().getAttributes();
			int maxWidth = attr.width;
			int maxHeight = attr.height;//initWindowParams has already calculated dialog height
			int imageWidth = bitmap.getWidth();
			int imageHeight = bitmap.getHeight();

			float factor = -1;
			//1. check image scale > dialog scale
			/*********
			 * if configuration is portrait(width < height), first check height then width
			 * if configuration is landscape(width > height), first check width then height
			 * in this condition, it can make sure that image will not over dialog screen
			 */
			if (maxWidth < maxHeight) {
				if (imageHeight > maxHeight) {
					factor = (float)maxHeight/(float)imageHeight;
					imageHeight = (int) (((float) imageHeight) * factor);
					imageWidth = (int) (((float) imageWidth) * factor);
					matrix.postScale(factor, factor);
				}
				if (imageWidth > maxWidth) {
					factor = (float)maxWidth/(float)imageWidth;
					imageHeight = (int) (((float) imageHeight) * factor);
					imageWidth = (int) (((float) imageWidth) * factor);
					matrix.postScale(factor, factor);
				}
			}
			else {
				if (imageWidth > maxWidth) {
					factor = (float)maxWidth/(float)imageWidth;
					imageHeight = (int) (((float) imageHeight) * factor);
					imageWidth = (int) (((float) imageWidth) * factor);
					matrix.postScale(factor, factor);
				}
				if (imageHeight > maxHeight) {
					factor = (float)maxHeight/(float)imageHeight;
					imageHeight = (int) (((float) imageHeight) * factor);
					imageWidth = (int) (((float) imageWidth) * factor);
					matrix.postScale(factor, factor);
				}
			}

			//2. check image scale < dialog scale
			if (factor == -1) {
				int spaceY = getContext().getResources().getDimensionPixelOffset(R.dimen.show_image_dlg_space_y);
				int spaceX = getContext().getResources().getDimensionPixelOffset(R.dimen.show_image_dlg_space_x);
				if (maxWidth < maxHeight) {
					if (imageHeight + 2*spaceY < maxHeight) {
						factor = (float)(maxHeight - 2 * spaceY) / (float)imageHeight;
						imageWidth = (int) (((float) imageWidth) * factor);
						imageHeight = maxHeight - 2 * spaceY;
						matrix.postScale(factor, factor);
					}
					//经y方向放大后，检查x方向是否超过屏幕，超过则再缩小
					if (imageWidth > maxWidth){
						factor = (float)(maxWidth - 2 * spaceX) / (float)imageWidth;
						imageHeight = (int) (((float) imageHeight) * factor);
						imageWidth = maxWidth - 2 * spaceX;
						matrix.postScale(factor, factor);
					}
				}
				else {
					if (imageWidth + 2*spaceX < maxWidth) {
						factor = (float)(maxWidth - 2 * spaceX) / (float)imageWidth;
						imageHeight = (int) (((float) imageHeight) * factor);
						imageWidth = maxWidth - 2 * spaceX;
						matrix.postScale(factor, factor);
					}
					//经x方向放大后，检查y方向是否超过屏幕，超过则再缩小
					if (imageHeight > maxHeight) {
						factor = (float)(maxHeight - 2 * spaceY) / (float)imageHeight;
						imageWidth = (int) (((float) imageWidth) * factor);
						imageHeight = maxHeight - 2 * spaceY;
						matrix.postScale(factor, factor);
					}
				}
			}

			//3. set image center
			matrix.postTranslate(maxWidth/2 - imageWidth/2, maxHeight/2 - imageHeight/2);
			showImageView.setImageMatrix(matrix);
		}
	}

	/**
	 * 采用自定义内部action实现方式，通过设置图片路径初始化图片
	 * @param path
	 */
	public void setImagePath(String path) {
		imagePath = path;

		if (gifManager.showGifView(path)) {
			displayImagePath = imagePath;
			showImageView.setVisibility(View.GONE);
			enableZoomAction();
			gifSeekBar.setProgress(0);
			gifSeekBar.setVisibility(View.VISIBLE);
			return;
		}

		gifSeekBar.setVisibility(View.GONE);
		gifContainer.setVisibility(View.GONE);
		showImageView.setVisibility(View.VISIBLE);
		enableSeizeAction();

		String filename = "";
		File file = new File(imagePath);
		if (file != null && file.exists()) {
			bitmap = PictureManagerUpdate.getInstance().createHDBitmap(file.getPath());
			if (EncryptUtil.isEncrypted(file)) {
				filename = EncryptUtil.getOriginName(file);
			}
		}
		setImage(bitmap);
		displayImagePath = imagePath;

		filenameView.setText(filename);
	}

	private void enableSeizeAction() {
		seizeButton.setVisibility(View.VISIBLE);
	}

	private void enableZoomAction() {
		seizeButton.setVisibility(View.GONE);
	}

	/**
	 * 当imagePath=null, actionListener不为null时，提供给外部设置图片的接口
	 * @param bitmap
	 */
	public void setImage(Bitmap bitmap) {

		this.bitmap = bitmap;
		showImageView.setVisibility(View.VISIBLE);
		if (bitmap != null) {
			showImageView.setImageBitmap(bitmap);
			fitImageView();
		}
		else {
			showImageView.setImageResource(R.drawable.ic_launcher);
		}
	}

	@Override
	public void show() {
		if (isOrienChanged) {
			onConfigChange();
			isOrienChanged = false;
		}

		saveButton.setVisibility(View.GONE);
		setAsMenuBkButton.setVisibility(View.VISIBLE);
		setCoverButton.setVisibility(View.VISIBLE);
		addButton.setVisibility(View.VISIBLE);
		moveButton.setVisibility(View.VISIBLE);
		detailsButton.setVisibility(View.VISIBLE);
		super.show();
	}

	@Override
	public void onClick(View v) {
		if (v == closeButton) {
			dismiss();
		}
		else if (v == addButton) {
			List<String> list = new ArrayList<>();
			list.add(displayImagePath);
			sOrderProvider.openOrderChooserToAddItem(list);
			if (actionListener != null) {
				actionListener.onAddToOrder();
			}
		}
		else if (v == moveButton) {
			if (actionListener != null) {
				actionListener.onMoveToFolder();
			}
			List<String> list = new ArrayList<>();
			list.add(displayImagePath);
			sOrderProvider.openFolderDialogToMoveFiles(list);
		}
		else if (v == detailsButton) {
			sOrderProvider.viewDetails(displayImagePath);
			if (actionListener != null) {
				actionListener.onDetails();
			}
		}
		else if (v == setCoverButton) {
			sOrderProvider.openOrderChooserToSetCover(displayImagePath);
			if (actionListener != null) {
				actionListener.onSetCover();
			}
		}
		else if (v == setAsMenuBkButton) {
			sOrderProvider.openBackgroundSelector(displayImagePath);
		}
		else if (v == seizeButton) {
			cropActionLayout.setVisibility(View.VISIBLE);
			cropActionbar.setVisibility(View.VISIBLE);
			actionbar.setVisibility(View.GONE);

			WindowManager.LayoutParams params = getWindow().getAttributes();
			cropView.setCropArea(params.width/2 - 200, params.height/2 - 200, params.width/2 + 200, params.height/2 + 200);
			cropView.setVisibility(View.VISIBLE);
			cropInforView.setInfor(params.width/2 - 200, params.height/2 - 200, params.width/2 + 200, params.height/2 + 200);
			cropInforView.setArea(20, 20, 500, 260);
			cropInforView.setVisibility(View.VISIBLE);
			showImageView.setImageBitmap(bitmap);
		}
		else if (v == saveButton) {
			saveCropBitmap();
			saveButton.setVisibility(View.GONE);
			setAsMenuBkButton.setVisibility(View.VISIBLE);
			setCoverButton.setVisibility(View.VISIBLE);
			addButton.setVisibility(View.VISIBLE);
			detailsButton.setVisibility(View.VISIBLE);
		}
		else if (v == doneButton) {
			cropBitmap();
		}
		else if (v == cancelButton) {
			closeCropMode();
			if (cropBitmap != null) {
				showImageView.setImageBitmap(cropBitmap);
				displayImagePath = cropImagePath;
			}
			else {
				showImageView.setImageBitmap(bitmap);
				displayImagePath = imagePath;
			}
		}
		else if (v == cropFullScreenButton) {

			WindowManager.LayoutParams params = getWindow().getAttributes();
			setCropArea(0 - cropView.getOffset(), 0 - cropView.getOffset()
					, params.width + cropView.getOffset(), params.height + cropView.getOffset());

		}
		else if (v == cropAreaSizeButton) {
			showCropAreaSizePopup();
		}
	}

	private void setCropArea(int left, int top, int right, int bottom) {
		//先gone再visible才起作用
		cropView.setVisibility(View.GONE);
		cropView.setCropArea(left, top, right, bottom);
		onChange(left, top, right, bottom);
		cropView.setVisibility(View.VISIBLE);
	}
	private void setCropArea(int width, int height) {
		//先gone再visible才起作用
		cropView.setVisibility(View.GONE);
		cropView.setCropArea(width, height);
		onChange(width, height);
		cropView.setVisibility(View.VISIBLE);
	}

	private void setCropAreaCenter(int width, int height) {
		//先gone再visible才起作用
		WindowManager.LayoutParams params = getWindow().getAttributes();
		cropView.setVisibility(View.GONE);
		cropView.setCropArea(params.width/2 - width/2, params.height/2 - height/2, params.width/2 + width/2, params.height/2 + height/2);
		onChange(params.width/2 - width/2, params.height/2 - height/2, params.width/2 + width/2, params.height/2 + height/2);
		cropView.setVisibility(View.VISIBLE);
	}


	private void showCropAreaSizePopup() {
		if (cropAreaSizePopup == null) {
			cropAreaSizePopup = new ListPopupWindow(getContext());
			cropAreaSizePopup.setAnchorView(cropAreaSizeButton);
			cropAreaSizePopup.setWidth(600);
			cropAreaSizePopup.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_slidingmenuitem_bk_pressed));
			cropAreaSizeArray = getContext().getResources().getStringArray(R.array.crop_area_size);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext()
					, android.R.layout.simple_dropdown_item_1line, cropAreaSizeArray);
			cropAreaSizePopup.setAdapter(adapter);
			cropAreaSizePopup.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
										int position, long arg3) {
					String area = cropAreaSizeArray[position];
					String[] array = area.split("\\*");
					int width = Integer.parseInt(array[0]);
					int height = Integer.parseInt(array[1]);
					if (getContext().getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
						int temp = width;
						width = height;
						height = temp;
					}
					//setCropArea(width, height);
					setCropAreaCenter(width, height);

					progressProvider.showToastLong(getContext().getString(R.string.success), ProgressProvider.TOAST_SUCCESS);
					cropAreaSizePopup.dismiss();
				}
			});
		}
		cropAreaSizePopup.show();
		cropAreaSizePopup.getListView().setDivider(null);//getListView只有在show之后才不为null
	}

	private void closeCropMode() {
		actionbar.setVisibility(View.VISIBLE);
		cropView.setVisibility(View.GONE);
		cropActionLayout.setVisibility(View.GONE);
		cropInforView.setVisibility(View.GONE);
		cropActionbar.setVisibility(View.GONE);
	}

	private void saveCropBitmap() {
		String name = new File(imagePath).getName();
		final String ename = name.split("\\.")[0] + System.currentTimeMillis() + ".png";

		SaveAsDialog dialog = new SaveAsDialog(getContext(), new SaveAsDialog.OnSaveAsListener() {

			@Override
			public void onSaveAsCancel() {

			}

			@Override
			public void onSaveAs(String folderPath, String name) {
				String path = folderPath;
				if (!folderPath.endsWith("/")) {
					path = folderPath + "/" + name;
				}
				else {
					path = folderPath + name;
				}
				cropImagePath = CropHelper.saveBitmap(cropBitmap, path);
				displayImagePath = cropImagePath;
				if (progressProvider != null) {
					progressProvider.showToastLong(getContext().getString(R.string.save_success), ProgressProvider.TOAST_SUCCESS);
				}
			}

			@Override
			public String getDefaultFolderPath() {
				return Configuration.APP_DIR_CROP_IMG;
			}

			@Override
			public String getDefaultName() {
				return ename;
			}
		});
		dialog.show();
	}

	private void cropBitmap() {
		float[] dValues = new float[9];
		showImageView.getImageMatrix().getValues(dValues);
		Rect targetRect = CropHelper.computeRectArea(dValues, cropView.getCutPosition());

		cropBitmap = CropHelper.crop(bitmap, targetRect);

		//v5.9.1 fix v5.9 bug, crop area over image, FC occur
		if (cropBitmap != null) {
			closeCropMode();

			showImageView.setImageBitmap(cropBitmap);
			saveButton.setVisibility(View.VISIBLE);
			setAsMenuBkButton.setVisibility(View.GONE);
			setCoverButton.setVisibility(View.GONE);
			addButton.setVisibility(View.GONE);
			moveButton.setVisibility(View.GONE);
			detailsButton.setVisibility(View.GONE);
		}
		else {
			if (progressProvider != null) {
				progressProvider.showToastLong(getContext().getString(R.string.crop_area_error), ProgressProvider.TOAST_ERROR);
			}
		}
	}

	//To fix: showImageDialog>click setasslidingmenubk icon>popup listwindow
	//>back>show image dialog again>click setasslidingmenubk, there is no action
	@Override
	public void dismiss() {
		if (setAsMenuBkPopup != null && setAsMenuBkPopup.isShowing()) {
			setAsMenuBkPopup.dismiss();
		}
		if (zoomPopup != null && zoomPopup.isShowing()) {
			zoomPopup.dismiss();
		}

		if (gifContainer.getVisibility() == View.VISIBLE) {
			gifManager.finish();
		}

		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		//v5.9.1 fix v5.9 bug, 1.should close crop mode. 2.cropBitmap must be null when open dialog
		closeCropMode();
		if (cropBitmap != null) {
			cropBitmap.recycle();
			cropBitmap = null;
			cropImagePath = null;
		}
		super.dismiss();
	}

	@Override
	public void onMoveFinish(String folderPath) {
		String[] array = displayImagePath.split("/");
		String name = array[array.length - 1];
		if (folderPath.endsWith("/")) {
			displayImagePath = folderPath  + name;
		}
		else {
			displayImagePath = folderPath + "/" + name;
		}
		imagePath = displayImagePath;
		if (progressProvider != null) {
			
		}
		progressProvider.showToastLong(getContext().getString(R.string.success), ProgressProvider.TOAST_SUCCESS);
	}

	@Override
	public void onAddToOrderFinished() {

	}

	@Override
	public void onDeleteIndex(int index) {

	}

	@Override
	public void onDeleteFinished(int count) {

	}

	public interface ActionListener {
		public void onAddToOrder();
		public void onMoveToFolder();
		public void onDetails();
		public void onSetCover();
		public void onSetAsMenuBk();
	}

	@Override
	public void onChange(int left, int top, int right, int bottom) {
		cropInforView.setInfor(left, top, right - left, bottom - top);
		cropInforView.invalidate();
	}

	@Override
	public void onChange(int width, int height) {
		cropInforView.setInfor(width, height);
		cropInforView.invalidate();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {

			case Constants.STATUS_MOVE_FILE_DONE:
				Log.d(TAG, "handleMessage STATUS_MOVE_FILE_DONE");
				moveController.updateProgress();
				break;
			case Constants.STATUS_MOVE_FILE_FINISH:
				Log.d(TAG, "handleMessage STATUS_MOVE_FILE_FINISH");
				moveController.updateProgress();
				moveController.cancleProgress(true);
				if (moveObjectPath != null) {
					imagePath = moveObjectPath;
					displayImagePath = imagePath;
				}
				moveObjectPath = null;
				break;
			case Constants.STATUS_MOVE_FILE_UNSUPORT:
				Log.d(TAG, "handleMessage STATUS_MOVE_FILE_UNSUPORT");

				moveObjectPath = null;
				Bundle bundle = msg.getData();
				boolean isFinish = bundle.getBoolean(Constants.KEY_MOVETO_UNSUPPORT_FINISH);
				String src = bundle.getString(Constants.KEY_MOVETO_UNSUPPORT_SRC);
				String error = getContext().getResources().getString(R.string.move_src_to_src) + "\n" + src;

				moveController.addError(error);
				if (isFinish) {
					moveController.cancleProgress(false);
				}
				break;
			default:
				break;
		}
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar bar, int progress, boolean arg2) {
		float maxFactor = calculateMaxFactor();
		float factor = 1;
		float scale = (float) progress / (float) 100;
		factor = factor + (maxFactor - 1) * scale;
		gifManager.setZoomFactor(factor);
		gifManager.refresh();

		if (DEBUG) {
			Log.d(TAG, "onProgressChanged progress=" + progress);
			Log.d(TAG, "onProgressChanged factor=" + factor);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {

	}

	private float calculateMaxFactor() {
		float factor = 5;
		float gifWidth = gifManager.getGifOriginWidth();//这里需要使用bitmap的宽高，不能使用gifview的宽高，gifview的宽高在zoom后就改变了
		float gifHeight = gifManager.getGifOriginHeight();

		WindowManager.LayoutParams params = getWindow().getAttributes();
		float windowWidth = params.width;
		float windowHeight = params.height;
		if (gifHeight > 0) {
			if (gifWidth / gifHeight > windowWidth / windowHeight) {
				factor = windowWidth / gifWidth;
			}
			else {
				factor = windowHeight / gifHeight;
			}
		}

		if (DEBUG) {
			Log.d(TAG, "onProgressChanged maxFactor=" + factor);
		}
		return factor;
	}

	/**
	 * 以裁剪模式启动
	 */
	public void setStartWithCrop() {
		findViewById(R.id.showimage_bk_layout).setBackgroundColor(Color.BLACK);
		onClick(seizeButton);
	}

	public void applyBlackBackground() {
		findViewById(R.id.showimage_bk_layout).setBackgroundColor(Color.BLACK);
	}

	public void applyTransparentBackground() {
		findViewById(R.id.showimage_bk_layout).setBackgroundColor(Color.TRANSPARENT);
	}

}
