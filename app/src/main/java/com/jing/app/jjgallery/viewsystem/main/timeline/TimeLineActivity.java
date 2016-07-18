package com.jing.app.jjgallery.viewsystem.main.timeline;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.timeline.TimeLinePresenter;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

public class TimeLineActivity extends BaseActivity implements ITimeLineView, OnClickListener
		, OnItemClickListener, OnHeadImageClickListener {

	private TextView mainButton;
	private TextView orderButton;
	private TextView settingButton;
	private TextView closeButton;

	private TimeLinePresenter mPresenter;

	private StickyGridHeadersGridView mGridView;
	private TimeLineAdapter timeLineAdapter;

	private View noContentView;

	@Override
	protected boolean isActionBarNeed() {
		return false;
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_timeline_update;
	}

	@Override
	protected void initController() {
		mPresenter = new TimeLinePresenter(this);
	}

	@Override
	protected void initView() {
		mainButton = (TextView) findViewById(R.id.timeline_indicator_main);
		orderButton = (TextView) findViewById(R.id.timeline_indicator_guide);
		settingButton = (TextView) findViewById(R.id.timeline_indicator_setting);
		closeButton = (TextView) findViewById(R.id.timeline_indicator_close);
		mGridView = (StickyGridHeadersGridView) findViewById(R.id.timeline_gridview);
		noContentView = findViewById(R.id.timeline_nocontent);

		mainButton.setOnClickListener(this);
		orderButton.setOnClickListener(this);
		closeButton.setOnClickListener(this);
		settingButton.setOnClickListener(this);

		mGridView.setOnItemClickListener(this);
//		mGridView.setOnHeaderClickListener(this);
//		mGridView.setOnHeaderLongClickListener(this);
		//必须设置这个header背景才会透明
		mGridView.setStickyHeaderIsTranscluent(true);

		mainButton.setBackground(getOvalDrawable(getResources().getColor(R.color.timeline_menu_main_bk)));
		orderButton.setBackground(getOvalDrawable(getResources().getColor(R.color.timeline_menu_order_bk)));
		closeButton.setBackground(getOvalDrawable(getResources().getColor(R.color.timeline_menu_setting_bk)));
		settingButton.setBackground(getOvalDrawable(getResources().getColor(R.color.timeline_menu_close_bk)));

	}

	@Override
	protected void initBackgroundWork() {
		showProgressCycler();
		mPresenter.loadTimeLineItems();
	}

	@Override
	public void onTimeLineItemsReady() {

		timeLineAdapter = new TimeLineAdapter(this, this, mPresenter);
		mGridView.setAdapter(timeLineAdapter);
		if (mPresenter.getFileBeanList() == null || mPresenter.getFileBeanList().size() == 0) {
			noContentView.setVisibility(View.VISIBLE);
			mainButton.setVisibility(View.VISIBLE);
			orderButton.setVisibility(View.VISIBLE);
			settingButton.setVisibility(View.VISIBLE);
//			closeButton.setVisibility(View.VISIBLE);
			mainButton.startAnimation(getMenuAppearAnimation(0));
			orderButton.startAnimation(getMenuAppearAnimation(1));
			settingButton.startAnimation(getMenuAppearAnimation(2));
//			closeButton.startAnimation(getMenuAppearAnimation(3));
		}
		dismissProgressCycler();
	}

	private ShapeDrawable getOvalDrawable(int color) {
		ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
		drawable.getPaint().setColor(color);
		return drawable;
	}


	@Override
	public void onClick(View v) {
		if (v == mainButton) {
//			Intent intent = new Intent().setClass(this, MainViewActivity.class);
//			startActivity(intent);
//			finish();
		}
		else if (v == orderButton) {
//			Intent intent = new Intent().setClass(this, GuideActivity.class);
//			startActivity(intent);
//			finish();
		}
		else if (v == settingButton) {
			Intent intent = new Intent().setClass(this, SettingsActivity.class);
			startActivity(intent);
		}
		else if (v == closeButton) {
			mainButton.setVisibility(View.GONE);
			orderButton.setVisibility(View.GONE);
			settingButton.setVisibility(View.GONE);
			closeButton.setVisibility(View.GONE);
			closeButton.startAnimation(getMenuDisappearAnimation(0));
			settingButton.startAnimation(getMenuDisappearAnimation(1));
			orderButton.startAnimation(getMenuDisappearAnimation(2));
			mainButton.startAnimation(getMenuDisappearAnimation(3));
		}
	}

	private Animation getMenuAppearAnimation(int index) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation tanimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
				, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0);
		AlphaAnimation aanimation = new AlphaAnimation(0, 1.0f);
		tanimation.setDuration(100 + index * 90);
		aanimation.setDuration(100 + index * 90);
		set.addAnimation(tanimation);
		set.addAnimation(aanimation);
		set.setStartOffset(index * 100 + index * (index - 1) * 90 / 2);//等差数列求和
		return set;
	}

	private Animation getMenuDisappearAnimation(int index) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation tanimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
				, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f);
		tanimation.setDuration(150);
		AlphaAnimation aanimation = new AlphaAnimation(1.0f, 0);
		aanimation.setDuration(150);
		set.addAnimation(tanimation);
		set.addAnimation(aanimation);
		set.setStartOffset(index * 150);
		return set;
	}
	/**
	 * origin codes issue: LinearInterpolator is not working in xml definition
	 * must use java codes to set it
	 * @return
	 */
	private Animation getRotateAnimation() {
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.thumb_folder_click);
		LinearInterpolator interpolator = new LinearInterpolator();
		animation.setInterpolator(interpolator);
		return animation;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		ShowImageDialog dialog = new ShowImageDialog(this, null, 0);
		String path = mPresenter.getFileBeanList().get(position).getPath();
		dialog.setImagePath(path);
		dialog.show();
	}

	@Override
	public void onHeadImageClicked(View parent, View view, int position) {
		if (parent == mGridView.getStickiedHeader()) {

//			view.startAnimation(getRotateAnimation());
			mainButton.setVisibility(View.VISIBLE);
			orderButton.setVisibility(View.VISIBLE);
			settingButton.setVisibility(View.VISIBLE);
			closeButton.setVisibility(View.VISIBLE);
			mainButton.startAnimation(getMenuAppearAnimation(0));
			orderButton.startAnimation(getMenuAppearAnimation(1));
			settingButton.startAnimation(getMenuAppearAnimation(2));
			closeButton.startAnimation(getMenuAppearAnimation(3));
		}
	}

}
