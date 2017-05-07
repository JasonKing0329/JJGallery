package com.jing.app.jjgallery.gdb.view.pub;

import java.util.LinkedList;

import com.nineoldandroids.view.ViewHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 采用4个ImageView循环交替的方式实现自动滚动视图
 * 初始化时4个ImageView从左到右排列，仅2个在可视区域
 * 利用线程每隔50ms发送位置更新通知，使每个ImageView位置左移
 * 当首个ImageView完全隐藏后将其拼接到最末端，采用循环队列的思想
 * @author JingYang
 *
 */
public class AutoScrollView extends RelativeLayout {

	private static final int MIN_ITEM = 2;

	public static abstract class ViewHolder {

		private View contentView;
		public ViewHolder(View view) {
			this.contentView = view;
		}

		public View getContentView() {
			return contentView;
		}
	}

	private class ScrollItem {
		View view;
		LayoutParams layoutParams;
		float xPos;
		ViewHolder holder;
	}

	public class ItemNotEnoughException extends Exception {
		public ItemNotEnoughException() {
			super("There should be at least " + MIN_ITEM + " items.");
		}
	}

	/** 循环队列，实现ImageView的循环拼接。为了方便处理，用LinkedList扩展Queue的功能 **/
	private LinkedList<ScrollItem> imageQueue;

	/** 结合传统的Adapter思想，为item设置虚拟position **/
	private int itemPosition;

	private int itemWidth;
	private int SCROLL_DISTANCE_PER_TIME = 5;

	private AutoScrollAdapter autoScrollAdapter;

	private boolean isRun;
	private final int MSG_UPDATE_POSITION = 0;
	private ScrollThread scrollThread;

	public AutoScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
	}

	public void setAdapter(AutoScrollAdapter adapter) throws ItemNotEnoughException {
		if (adapter.getCount() < MIN_ITEM) {
			throw new ItemNotEnoughException();
		}
		else {
			autoScrollAdapter = adapter;
			autoScrollAdapter.setAutoScrollView(this);
			initAdapter();
			refreshData();
		}
	}

	private void initAdapter() {
		itemWidth = autoScrollAdapter.getItemWidth();

		ScrollItem scrollItem1 = new ScrollItem();
		scrollItem1.holder = autoScrollAdapter.onCreateViewHolder(this);
		scrollItem1.view = scrollItem1.holder.getContentView();
		scrollItem1.layoutParams = new LayoutParams(itemWidth, LayoutParams.MATCH_PARENT);
		scrollItem1.xPos = 0;
		addView(scrollItem1.view, scrollItem1.layoutParams);

		ScrollItem scrollItem2 = new ScrollItem();
		scrollItem2.holder = autoScrollAdapter.onCreateViewHolder(this);
		scrollItem2.view = scrollItem2.holder.getContentView();
		scrollItem2.layoutParams = new LayoutParams(itemWidth, LayoutParams.MATCH_PARENT);
		addView(scrollItem2.view, scrollItem2.layoutParams);
		scrollItem2.xPos = itemWidth;
		ViewHelper.setTranslationX(scrollItem2.view, itemWidth);

		ScrollItem scrollItem3 = new ScrollItem();
		scrollItem3.holder = autoScrollAdapter.onCreateViewHolder(this);
		scrollItem3.view = scrollItem3.holder.getContentView();
		scrollItem3.layoutParams = new LayoutParams(itemWidth, LayoutParams.MATCH_PARENT);
		addView(scrollItem3.view, scrollItem3.layoutParams);
		scrollItem3.xPos = itemWidth * 2;
		ViewHelper.setTranslationX(scrollItem3.view, itemWidth * 2);

		ScrollItem scrollItem4 = new ScrollItem();
		scrollItem4.holder = autoScrollAdapter.onCreateViewHolder(this);
		scrollItem4.view = scrollItem4.holder.getContentView();
		scrollItem4.layoutParams = new LayoutParams(itemWidth, LayoutParams.MATCH_PARENT);
		addView(scrollItem4.view, scrollItem4.layoutParams);
		scrollItem4.xPos = itemWidth * 3;
		ViewHelper.setTranslationX(scrollItem4.view, itemWidth * 3);

		imageQueue = new LinkedList<>();
		imageQueue.offer(scrollItem1);
		imageQueue.offer(scrollItem2);
		imageQueue.offer(scrollItem3);
		imageQueue.offer(scrollItem4);
	}

	private void refreshData() {
		if (autoScrollAdapter != null) {
			autoScrollAdapter.onBindView(getNextPosition(), imageQueue.get(0).holder);
			autoScrollAdapter.onBindView(getNextPosition(), imageQueue.get(1).holder);
			autoScrollAdapter.onBindView(getNextPosition(), imageQueue.get(2).holder);
			autoScrollAdapter.onBindView(getNextPosition(), imageQueue.get(3).holder);
		}
	}

	/**
	 * 超过最大数量时从0开始循环
	 * @return
     */
	private int getNextPosition() {
		if (itemPosition >= autoScrollAdapter.getCount()) {
			itemPosition = 0;
		}
		return itemPosition ++;
	}

	public void startScroll() {
		scrollThread = new ScrollThread();
		scrollThread.start();
	}

	public void stop() {
		isRun = false;
		if (scrollThread != null) {
			scrollThread.interrupt();
		}
	}

	public void restart() {
		if (scrollThread != null) {
			scrollThread = new ScrollThread();
			scrollThread.start();
		}
	}

	private class ScrollThread extends Thread {

		public ScrollThread() {
			isRun = true;
		}

		@Override
		public void run() {
			while (isRun) {
				try {
					Thread.sleep(20);
					mHandler.sendEmptyMessage(MSG_UPDATE_POSITION);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case MSG_UPDATE_POSITION:
					//所有view位置一起左移相同距离
					for (int i = 0; i < imageQueue.size(); i ++) {
						imageQueue.get(i).xPos = imageQueue.get(i).xPos - SCROLL_DISTANCE_PER_TIME;
						ViewHelper.setTranslationX(imageQueue.get(i).view, imageQueue.get(i).xPos);
					}

					//第一个view完全隐藏时将其拼接到末端
					ScrollItem head = imageQueue.peek();
					if (head.xPos < -itemWidth) {
						head.xPos = imageQueue.get(imageQueue.size() - 1).xPos + itemWidth;
						autoScrollAdapter.onBindView(getNextPosition(), head.holder);
						imageQueue.offer(imageQueue.poll());
					}
					break;

				default:
					break;
			}
			super.handleMessage(msg);
		}

	};

	public void notifyDataSetChanged() {
		refreshData();
	}

}
