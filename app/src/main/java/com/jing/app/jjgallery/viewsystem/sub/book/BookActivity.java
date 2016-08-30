package com.jing.app.jjgallery.viewsystem.sub.book;

import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.MotionEvent;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;

public class BookActivity extends BaseActivity {

	private static final String TAG = "BookActivity";
	public static final String KEY_TYPE = "book_type";
	public static final int FOLDER = 0;
	public static final int SORDER = 1;

	public static final String KEY_FOLDER_PATH = "book_folder_path";
	public static final String KEY_ORDER_ID = "book_order_id";

	/**
	 * 转场动画时间
	 */
	private final int TIME_TRANS_ANIM_DURITION = 500;

	private BookFragment fragment;

	@Override
	public boolean isActionBarNeed() {
		return false;
	}

	@Override
	public int getContentView() {
		return R.layout.activity_book;
	}

	@Override
	public void initController() {

	}

	@Override
	public void initView() {
		touchStartRange = getResources().getDimensionPixelSize(R.dimen.book_touch_start_range);
		touchDistance = getResources().getDimensionPixelSize(R.dimen.book_touch_distance);
		onBookPage(getIntent().getIntExtra(KEY_TYPE, FOLDER));

		// 加入转场动画，从上个界面注册的transitionName = trans_wall的view放大而出
		findViewById(R.id.book_parent).setTransitionName(getString(R.string.trans_book));
		TransitionSet set = new TransitionSet();
		Slide slide = new Slide(Gravity.BOTTOM);
		slide.addTarget(R.id.book_parent);
		slide.setDuration(TIME_TRANS_ANIM_DURITION);
		getWindow().setEnterTransition(set);
	}

	/**
	 *
	 * @param type see FOLDER or SORDER
	 */
	public void onBookPage(int type) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if (fragment == null) {
			if (type == FOLDER) {
				FolderBookFragment fragment = new FolderBookFragment();
				fragment.setFolderPath(getIntent().getStringExtra(KEY_FOLDER_PATH));
				this.fragment = fragment;
			}
			else if (type == SORDER) {
				OrderBookFragment fragment = new OrderBookFragment();
				fragment.setOrderId(getIntent().getIntExtra(KEY_ORDER_ID, -1));
				this.fragment = fragment;
			}
		}

		ft.replace(R.id.book_container, fragment, "BookFragment");
		ft.commit();
	}

	@Override
	public void initBackgroundWork() {

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	private float touchStartY;
	private float touchStartRange;
	private float touchDistance;
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchStartY = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (touchStartY < touchStartRange) {
					if (event.getRawY() - touchStartY > touchDistance) {
						fragment.showInforView();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				break;
		}
		return super.dispatchTouchEvent(event);
	}
}
