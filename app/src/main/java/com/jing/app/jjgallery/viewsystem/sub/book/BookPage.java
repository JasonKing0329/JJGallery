package com.jing.app.jjgallery.viewsystem.sub.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.image.ImageValue;
import com.jing.app.jjgallery.service.image.SImageLoader;

import java.util.List;

/**
 * 由于lru包里的ImageLoader是用imageView默认的setTag进行异步加载图片的
 * 所以在进行onClick的tag设定中不能占用默认的setTag()，可以用setTag(key, object)
 */
public class BookPage {

	public static final int ITEM_ONE = 1;
	public static final int ITEM_TWO = 2;
	public static final int ITEM_THREE = 3;
	public static final int ITEM_FOUR = 4;
	private static final String TAG = "BookPage";
	private View.OnClickListener imageListener;
	
	private ImageView[] imageViews;
	private Context mContext;
	
	public BookPage(Context context) {
		mContext = context;
	}

	public void setImageListener(View.OnClickListener listener) {
		imageListener = listener;
	}

	public View getConvertView(List<ImageValue> imageValues) {
		View view = null;
		imageViews = new ImageView[imageValues.size()];
		int size = imageValues.size();
		switch (size) {
			case 2:
				view = LayoutInflater.from(mContext).inflate(R.layout.bookpage_2item, null);
				imageViews[0] = (ImageView) view.findViewById(R.id.bookpage_2item_image1);
				imageViews[1] = (ImageView) view.findViewById(R.id.bookpage_2item_image2);
				break;
			case 3:
				int tag = (int) imageValues.get(0).getTag();
				if (tag == 1) {
					view = LayoutInflater.from(mContext).inflate(R.layout.bookpage_3item_1, null);
				}
				else if (tag == 2) {
					view = LayoutInflater.from(mContext).inflate(R.layout.bookpage_3item_2, null);
				}
				else if (tag == 3) {
					view = LayoutInflater.from(mContext).inflate(R.layout.bookpage_3item_2, null);
				}
				imageViews[0] = (ImageView) view.findViewById(R.id.bookpage_3item_image1);
				imageViews[1] = (ImageView) view.findViewById(R.id.bookpage_3item_image2);
				imageViews[2] = (ImageView) view.findViewById(R.id.bookpage_3item_image3);
				break;
			case 4:
				view = LayoutInflater.from(mContext).inflate(R.layout.bookpage_4item, null);
				imageViews[0] = (ImageView) view.findViewById(R.id.bookpage_4item_image1);
				imageViews[1] = (ImageView) view.findViewById(R.id.bookpage_4item_image2);
				imageViews[2] = (ImageView) view.findViewById(R.id.bookpage_4item_image3);
				imageViews[3] = (ImageView) view.findViewById(R.id.bookpage_4item_image4);
				break;
			case 1:
			default:
				LinearLayout layout = new LinearLayout(mContext);
				imageViews[0] = new ImageView(mContext);
				imageViews[0].setScaleType(ScaleType.FIT_CENTER);
				layout.addView(imageViews[0], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				view = layout;
				break;
		}
		return view;
	}

	public void bindDatas(List<ImageValue> imageValues) {
		int size = imageValues.size();
		// 由于lru包里的ImageLoader是用imageView默认的setTag进行异步加载图片的
		// 所以在进行onClick的tag设定中不能占用默认的setTag()，可以用setTag(key, object)
		switch (size) {
			case 2:
				SImageLoader.getInstance().displayImage(imageValues.get(0).getPath(), imageViews[0]);
				SImageLoader.getInstance().displayImage(imageValues.get(1).getPath(), imageViews[1]);
				imageViews[0].setTag(R.id.book_item_position, imageValues.get(0));
				imageViews[1].setTag(R.id.book_item_position, imageValues.get(1));
				imageViews[0].setOnClickListener(imageListener);
				imageViews[1].setOnClickListener(imageListener);
				break;
			case 3:
				SImageLoader.getInstance().displayImage(imageValues.get(0).getPath(), imageViews[0]);
				SImageLoader.getInstance().displayImage(imageValues.get(1).getPath(), imageViews[1]);
				SImageLoader.getInstance().displayImage(imageValues.get(2).getPath(), imageViews[2]);
				imageViews[0].setTag(R.id.book_item_position, imageValues.get(0));
				imageViews[1].setTag(R.id.book_item_position, imageValues.get(1));
				imageViews[2].setTag(R.id.book_item_position, imageValues.get(2));
				imageViews[0].setOnClickListener(imageListener);
				imageViews[1].setOnClickListener(imageListener);
				imageViews[2].setOnClickListener(imageListener);
				break;
			case 4:
				SImageLoader.getInstance().displayImage(imageValues.get(0).getPath(), imageViews[0]);
				SImageLoader.getInstance().displayImage(imageValues.get(1).getPath(), imageViews[1]);
				SImageLoader.getInstance().displayImage(imageValues.get(2).getPath(), imageViews[2]);
				SImageLoader.getInstance().displayImage(imageValues.get(3).getPath(), imageViews[3]);
				imageViews[0].setTag(R.id.book_item_position, imageValues.get(0));
				imageViews[1].setTag(R.id.book_item_position, imageValues.get(1));
				imageViews[2].setTag(R.id.book_item_position, imageValues.get(2));
				imageViews[3].setTag(R.id.book_item_position, imageValues.get(3));
				imageViews[0].setOnClickListener(imageListener);
				imageViews[1].setOnClickListener(imageListener);
				imageViews[2].setOnClickListener(imageListener);
				imageViews[3].setOnClickListener(imageListener);
				break;
			case 1:
			default:
				SImageLoader.getInstance().displayImage(imageValues.get(0).getPath(), imageViews[0]);
				imageViews[0].setTag(R.id.book_item_position, imageValues.get(0));
				imageViews[0].setOnClickListener(imageListener);
				break;
		}
	}
}
