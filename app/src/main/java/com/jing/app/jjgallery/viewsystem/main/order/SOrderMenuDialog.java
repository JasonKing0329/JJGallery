package com.jing.app.jjgallery.viewsystem.main.order;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.util.ScreenUtils;

public class SOrderMenuDialog extends Dialog implements Runnable {

	private final String TAG = "SOrderMenuDialog";
	private ImageView imageView;
	private CircleMenuView menuView;
	private int coverLeft, coverTop;
	private Bitmap cover;

	public SOrderMenuDialog(Context context, Bitmap cover, int left, int top
			, CircleMenuView.OnMenuItemListener listener) {
		super(context, R.style.TransparentDialog);
		this.cover = cover;
		coverLeft = left;
		coverTop = top;
		setContentView(R.layout.dialog_sorder_menu);
		imageView = (ImageView) findViewById(R.id.dlg_sorder_menu_cover);
		menuView = (CircleMenuView) findViewById(R.id.dlg_sorder_menu_view);
		menuView.setVisibility(View.VISIBLE);
		menuView.setOnMenuItemListener(listener);

		setFullScreen();

		imageView.setImageBitmap(cover);
		setCoverPosition();

		showCoverAnim();

		//delay set menuItem to make sure menuView already has width/height parameter
		//600刚好是对应sorder_longclick anim的scale部分的时间
		new Handler().postDelayed(this, 600);
	}

	private void showCoverAnim() {
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.sorder_longclick);
		imageView.startAnimation(animation);
	}

	private void setCoverPosition() {
		if (coverTop < 20) {
			coverTop = 20;
		}

		int screenHeight = ScreenUtils.getScreenHeight(getContext());
		if (coverTop + cover.getHeight() > screenHeight - 20) {
			coverTop = screenHeight - 20 - cover.getHeight();
		}

		MarginLayoutParams params = (MarginLayoutParams) imageView.getLayoutParams();
		params.leftMargin = coverLeft;
		params.topMargin = coverTop;
	}

	private void setFullScreen() {
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = ScreenUtils.getScreenWidth(getContext());
		params.height = ScreenUtils.getScreenHeight(getContext());
	}

	@Override
	public void show() {
		super.show();
	}


	@Override
	public void dismiss() {
		if (cover != null) {
			cover.recycle();
		}
		super.dismiss();
	}

	public void showMenu() {

		String menus[] = getContext().getResources().getStringArray(R.array.sorder_longclick);
		menuView.setObjectArea(coverLeft, coverTop, cover.getWidth(), cover.getHeight());
		menuView.setMenuItems(menus);
	}

	@Override
	public void run() {
		showMenu();
	}
}
