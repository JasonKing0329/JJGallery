package com.jing.app.jjgallery.viewsystem.sub.surf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.model.sub.AutoPlayController;
import com.jing.app.jjgallery.model.sub.WholeRandomManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.presenter.main.order.SOrderProvider;
import com.jing.app.jjgallery.presenter.main.order.SOrderProviderCallback;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.util.ScreenUtils;
import com.jing.app.jjgallery.viewsystem.sub.gifview.MyGifManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 全屏浏览器
 * 只处理全部随机浏览模式
 */
public class RandomSurfActivity extends BaseActivity implements OnClickListener
		, OnMenuItemClickListener, Callback {

	private final String TAG = "RandomSurfActivity";

	private final int FLING_SWITCH_HORIZONTAL_MIN = 100;
	private final int FLING_SWITCH_VERTICAL_MAX = 100;

	private final int FLING_TOOLBAR_VERTICAL_MIN = 100;
	private final int FLING_TOOLBAR_HORIZONTAL_MAX = 100;

	private List<String> imagePathList;
	private ImageView imageView1, imageView2;
	private View hideView, forgroundView;
	private ImageView previousView, nextView;
	private LinearLayout gifLayout;
	private MyGifManager gifManager;

	private int currentPosition;
	private String currentImagePath;
	private LinearLayout toolbar;
	private ImageView deleteButton, favorateButton, playButton, moreButton, detailButton;

	private GestureDetector gestureDetector;
	private MyGestureDetector myGestureDetector;
	private Random random;

	private WholeRandomManager randomManager;
	//for SRC_MODE_RANDOM
	private String lastImagePath, nextImagePath;
	private PopupMenu popupMenu;

	private AutoPlayController autoPlayController;

	private AnimRecycleListener animRecycleListener;
	private SOrderProvider sOrderProvider;

	@Override
	protected boolean isActionBarNeed() {
		return false;
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_fullscreen_surf;
	}

	@Override
	protected void initController() {

		random = new Random();
		myGestureDetector = new MyGestureDetector();
		gestureDetector = new GestureDetector(this, myGestureDetector);
		animRecycleListener = new AnimRecycleListener();
		sOrderProvider = new SOrderProvider(this, new SOrderProviderCallback() {
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

			}
		});
	}

	@Override
	protected void initView() {
		DisplayHelper.keepScreenOn(this);

		imageView1 = (ImageView) findViewById(R.id.fullscreen_image1);
		imageView2 = (ImageView) findViewById(R.id.fullscreen_image2);
		gifLayout = (LinearLayout) findViewById(R.id.fullscreen_gifview);
		previousView = (ImageView) findViewById(R.id.fullscreen_previous);
		nextView = (ImageView) findViewById(R.id.fullscreen_next);
		toolbar = (LinearLayout) findViewById(R.id.fullscreen_toolbar);
		deleteButton = (ImageView) findViewById(R.id.fullscreen_toolbar_delete);
		favorateButton = (ImageView) findViewById(R.id.fullscreen_toolbar_addtoorder);
		playButton = (ImageView) findViewById(R.id.fullscreen_toolbar_play);
		moreButton = (ImageView) findViewById(R.id.fullscreen_toolbar_more);
		detailButton = (ImageView) findViewById(R.id.fullscreen_toolbar_detail);
		deleteButton.setOnClickListener(toolbarListener);
		favorateButton.setOnClickListener(toolbarListener);
		playButton.setOnClickListener(toolbarListener);
		moreButton.setOnClickListener(toolbarListener);
		detailButton.setOnClickListener(toolbarListener);
		previousView.setOnClickListener(this);
		nextView.setOnClickListener(this);
		gifManager = new MyGifManager(this, gifLayout);

		hideView = imageView1;
	}

	@Override
	protected void initBackgroundWork() {
		randomManager = new WholeRandomManager();
		executeWholeRandom();

		if (imagePathList != null && imagePathList.size() > 0) {
			gifManager.setParentSize(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
			if (gifManager.showGifView(imagePathList.get(0), null, MyGifManager.MATCH_PARENT)) {
				forgroundView = gifLayout;
				hideView = imageView2;
				return;
			}
			SImageLoader.getInstance().displayImage(imagePathList.get(0), imageView2);
			forgroundView = imageView2;
			hideView = imageView1;
		}
	}

	private void executeWholeRandom() {
		createRandomPath();
		changeImage();
	}

	private void createRandomPath() {
		int maxTry = 1;
		currentImagePath = randomManager.getRandomPath();
		while (currentImagePath == null && maxTry < 5) {
			currentImagePath = randomManager.getRandomPath();
			maxTry ++;
		}
	}

	OnClickListener toolbarListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (view == deleteButton) {
				List<String> list = new ArrayList<>();
				list.add(currentImagePath);
				sOrderProvider.deleteItemFromFolder(list);
			}
			else if (view == favorateButton) {
				List<String> list = new ArrayList<>();
				list.add(currentImagePath);
				sOrderProvider.openOrderChooserToAddItem(list);
			}
			else if (view == moreButton) {
				sOrderProvider.openBackgroundSelector(currentImagePath);
//				showMenu();
			}
			else if (view == detailButton) {
				sOrderProvider.viewDetails(currentImagePath);
			}
			else if (view == playButton) {
				autoPlay();
			}
		}
	};

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_fullscreen_setascover:
				sOrderProvider.openOrderChooserToSetCover(currentImagePath);
				break;
			case R.id.menu_fullscreen_swipemode:
				new AlertDialog.Builder(RandomSurfActivity.this)
						.setItems(getResources().getStringArray(R.array.fullscreen_setting)
								, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int index) {

									}
								}).show();
				break;
			default:
				break;
		}
		return true;
	}

	protected void autoPlay() {

		if (autoPlayController == null) {
			autoPlayController = new AutoPlayController(this, this);
		}
		if (stopAutoPlay()) {
			return;
		}

		playButton.setImageResource(R.drawable.actionbar_stop);
		autoPlayController.startWholeRandomAutoPlay(SettingProperties.getAnimationSpeed(this));
	}

	protected void showMenu() {
		if (popupMenu == null) {
			popupMenu = new PopupMenu(this, moreButton);
			popupMenu.getMenuInflater().inflate(R.menu.fullscreen, popupMenu.getMenu());
			popupMenu.setOnMenuItemClickListener(this);
		}
		popupMenu.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gestureDetector.onTouchEvent(event);
	}

	private void changeImage() {
		Log.d(TAG, "changeImage");
		if (currentImagePath == null) {
			Log.i(TAG, "currentImagePath is null");
			return;
		}

		gifManager.setParentSize(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
		if (forgroundView == gifLayout) {
			gifManager.finish();
		}
		if (gifManager.showGifView(currentImagePath, myGestureDetector.getApplearAnim(), MyGifManager.MATCH_PARENT)) {
			gifManager.refresh();
			if (forgroundView == imageView1) {
				imageView1.startAnimation(myGestureDetector.getDisapplearAnim());
				imageView1.setVisibility(View.GONE);
			}
			else if (forgroundView == imageView2) {
				imageView2.startAnimation(myGestureDetector.getDisapplearAnim());
				imageView2.setVisibility(View.GONE);
			}
			forgroundView = gifLayout;
			return;
		}

		gifLayout.setVisibility(View.GONE);
		if (hideView == imageView2) {
			if (forgroundView == imageView1) {
				imageView1.startAnimation(myGestureDetector.getDisapplearAnim());
				imageView1.setVisibility(View.GONE);
			}
			hideView = imageView1;
			forgroundView = imageView2;

			SImageLoader.getInstance().displayImage(currentImagePath, imageView2);
			imageView2.startAnimation(myGestureDetector.getApplearAnim());
			imageView2.setVisibility(View.VISIBLE);
		}
		else if (hideView == imageView1) {
			if (forgroundView == imageView2) {
				imageView2.startAnimation(myGestureDetector.getDisapplearAnim());
				imageView2.setVisibility(View.GONE);
			}
			hideView = imageView2;
			forgroundView = imageView1;

			SImageLoader.getInstance().displayImage(currentImagePath, imageView1);
			imageView1.startAnimation(myGestureDetector.getApplearAnim());
			imageView1.setVisibility(View.VISIBLE);
		}
	}

	private void flingToNext() {
		Log.d(TAG, "flingToNext");
		if (nextImagePath == null) {
			lastImagePath = currentImagePath;
			executeWholeRandom();
		}
		else {//用于记录临时的前一张和后一张
			lastImagePath = currentImagePath;
			currentImagePath = nextImagePath;
			nextImagePath = null;
			changeImage();
		}
	}

	private void flingToPrevious() {
		Log.d(TAG, "flingToPrevious");
		if (lastImagePath == null) {
			nextImagePath = currentImagePath;
			executeWholeRandom();
		}
		else {//用于记录临时的前一张和后一张
			nextImagePath = currentImagePath;
			currentImagePath = lastImagePath;
			lastImagePath = null;
			changeImage();
		}
	}

	private void flingRandomRepeat() {
		currentPosition = Math.abs(random.nextInt()) % imagePathList.size();
	}

	private class MyGestureDetector extends SimpleOnGestureListener {

		private Animation toolbarInAnim, toolbarOutAnim, galleryInAnim, galleryOutAnim;
		private Animation appearAnim, disappearAnim;

		public MyGestureDetector() {
			disappearAnim = AnimationUtils.loadAnimation(RandomSurfActivity.this, R.anim.disappear);
		}

		public Animation getApplearAnim() {
			if (appearAnim == null) {
				appearAnim = AnimationUtils.loadAnimation(RandomSurfActivity.this, R.anim.appear);
				appearAnim.setAnimationListener(animRecycleListener);
			}
			return appearAnim;
		}

		public Animation getDisapplearAnim() {
			if (disappearAnim == null) {
				disappearAnim = AnimationUtils.loadAnimation(RandomSurfActivity.this, R.anim.disappear);
				disappearAnim.setAnimationListener(animRecycleListener);
			}
			return disappearAnim;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
							   float velocityY) {
			float x1 = e1.getX();
			float x2 = e2.getX();
			float y1 = e1.getY();
			float y2 = e2.getY();
			if (x2 - x1 > FLING_SWITCH_HORIZONTAL_MIN && y2 - y1 < FLING_SWITCH_VERTICAL_MAX && y2 - y1 > -FLING_SWITCH_VERTICAL_MAX) {
				flingToPrevious();
				Log.i("FileEncryption", "position = " + currentPosition);
			}
			else if (x2 - x1 < -FLING_SWITCH_HORIZONTAL_MIN && y2 - y1 < FLING_SWITCH_VERTICAL_MAX && y2 - y1 > -FLING_SWITCH_VERTICAL_MAX) {
				flingToNext();
				Log.i("FileEncryption", "position = " + currentPosition);
			}
			if (y2 - y1 > FLING_TOOLBAR_VERTICAL_MIN && x2 - x1 < FLING_TOOLBAR_HORIZONTAL_MAX) {
				toolbar.setVisibility(View.VISIBLE);
				if (toolbarInAnim == null) {
					toolbarInAnim = AnimationUtils.loadAnimation(RandomSurfActivity.this, R.anim.push_down_in);
				}
				toolbar.startAnimation(toolbarInAnim);
				previousView.setVisibility(View.VISIBLE);
				nextView.setVisibility(View.VISIBLE);
			}
			else if (y2 - y1 < -FLING_TOOLBAR_VERTICAL_MIN && x2 - x1 < FLING_TOOLBAR_HORIZONTAL_MAX) {
				toolbar.setVisibility(View.GONE);
				if (toolbarOutAnim == null) {
					toolbarOutAnim = AnimationUtils.loadAnimation(RandomSurfActivity.this, R.anim.push_up_out);
				}
				toolbar.startAnimation(toolbarOutAnim);
				previousView.setVisibility(View.GONE);
				nextView.setVisibility(View.GONE);
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}

	@Override
	public void onClick(View view) {
		if (view == previousView) {
			flingToPrevious();
		}
		else if (view == nextView) {
			flingToNext();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == AutoPlayController.AUTO_SPECIFIED_LIST) {
			handleAutoPlay(msg);
		}
		else if (msg.what == AutoPlayController.AUTO_WHOLE_RANDOM) {
			handleWholeAutoPlay();
		}
		return true;
	}

	private class AnimRecycleListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation anim) {
			Log.d(TAG, "onAnimationEnd");
//			if (hideView instanceof ImageView) {
//				((ImageView) hideView).setImageBitmap(null);
//			}
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}
	}

	/**
	 * 自动播放模式下，不播放gif动画，仅仅播放gif的唯一帧
	 */
	private void handleWholeAutoPlay() {
		Log.d(TAG, "handleWholeAutoPlay");
		createRandomPath();

		if (forgroundView == gifLayout) {
			gifLayout.setVisibility(View.GONE);
			forgroundView = imageView1;
			imageView1.setVisibility(View.VISIBLE);
		}

		ImageView target = imageView1;
		hideView = imageView2;
		if (forgroundView == imageView2) {
			target = imageView2;
			hideView = imageView1;
		}
		SImageLoader.getInstance().displayImage(currentImagePath, target);

		Animation animation = autoPlayController.randomAnimation();
		animation.setAnimationListener(animRecycleListener);

		target.startAnimation(animation);
	}

	/**
	 * 自动播放模式下，不播放gif动画，仅仅播放gif的唯一帧
	 */
	private void handleAutoPlay(Message msg) {

		Log.d(TAG, "handleAutoPlay");
		Bundle bundle = msg.getData();
		String finish = bundle.getString("autoplay_finish");
		if (finish != null && finish.equals("true")) {
			stopAutoPlay();
			Toast.makeText(this, R.string.spicture_autoplay_finish, Toast.LENGTH_LONG).show();
			return;
		}
		int index = bundle.getInt("autoplay_index");
		//boolean scroll = bundle.getBoolean("autoplay_scroll");

		if (forgroundView == gifLayout) {
			gifLayout.setVisibility(View.GONE);
			forgroundView = imageView1;
			imageView1.setVisibility(View.VISIBLE);
		}

		playImageAt(currentImagePath, index);

		Animation animation = autoPlayController.randomAnimation();
		animation.setAnimationListener(animRecycleListener);

		if (forgroundView == imageView1) {
			imageView1.startAnimation(animation);
		}
		else {
			imageView2.startAnimation(animation);
		}
	}

	private void playImageAt(String path, int index) {

		currentImagePath = imagePathList.get(index);
		if (imageView1.getVisibility() == View.VISIBLE) {
			SImageLoader.getInstance().displayImage(path, imageView1);
			hideView = imageView2;
		}
		else {
			SImageLoader.getInstance().displayImage(path, imageView2);
			hideView = imageView1;
		}
	}

	private boolean stopAutoPlay() {
		if (autoPlayController != null && autoPlayController.isAutoPlaying()) {
			autoPlayController.stopAutoPlay();
			playButton.setImageResource(R.drawable.actionbar_play);
			return true;
		}
		return false;
	}

	@Override
	protected void onResume() {
		stopAutoPlay();
		super.onResume();
	}

	@Override
	protected void onPause() {
		stopAutoPlay();
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		if (stopAutoPlay()) {
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		if (forgroundView == gifLayout) {
			gifManager.updateParentSize(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
			gifManager.refresh();
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}
}
