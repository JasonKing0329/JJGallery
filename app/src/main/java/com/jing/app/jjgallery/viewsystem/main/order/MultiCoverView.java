package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.image.SImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 多种样式封面
 * 目前支持single/cascade/cascade rotate/grid四种
 * 为了配合adapter的view holder模式
 * 采用addxxx/applyxxx的方式create imageview
 * 采用set的方式给image view设置图片
 */
public class MultiCoverView extends FrameLayout {

	public enum Style3Value {
		STYLE3_SIZE_1, STYLE3_SIZE_2, STYLE3_SIZE_4
	}

	private final String TAG = "MultiCoverView";
	private List<String> imageList;
	private List<ImageView> imageViewList;
	private int mWidth, mHeight;

	public MultiCoverView(Context context) {
		super(context);
		init();
	}

	public MultiCoverView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		imageList = new ArrayList<>();
		imageViewList = new ArrayList<>();
	}

	public void setSize(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	/**
	 * add bitmap and create ImageView
	 * @param imgPath
	 */
	public void addCover(String imgPath) {
		ImageView imageView = new ImageView(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(params);
		SImageLoader.getInstance().displayImage(imgPath, imageView);
		imageView.setScaleType(ScaleType.MATRIX);
		addView(imageView);
		imageList.add(imgPath);
		imageViewList.add(imageView);
	}

	/**
	 * 创建单封
	 * @param imgPath
	 */
	public void addSingleCover(String imgPath) {
		ImageView imageView = new ImageView(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(params);
		SImageLoader.getInstance().displayImage(imgPath, imageView);
		imageView.setScaleType(ScaleType.FIT_CENTER);
		addView(imageView);

		imageList.add(imgPath);
		imageViewList.add(imageView);

	}

	/**
	 * 给单封设置图片
	 * @param imgPath
     */
	public void setSingleCover(String imgPath) {
		SImageLoader.getInstance().displayImage(imgPath, imageViewList.get(0));
	}

	/**
	 * 封面层叠等度数旋转
	 * MultiCoverView width = height
	 * call addCover before call this
	 */
	public void applyStyleCascadeRotate() {
		ImageView imageView = null;
		float degree = 0;
		float scale = 0;
		int centerX = 0;
		int centerY = 0;
		float diagonal = 0;
		int imgWidth = 0;
		int imgHeight = 0;
		float translationX = 0;
		float translationY = 0;
		int total = getChildCount();
		int degreeStep = 180 / total;
		for (int i = total - 1; i > -1; i --) {

			imageView = (ImageView) getChildAt(i);
//			imgWidth = imageList.get(i).getWidth();
//			imgHeight = imageList.get(i).getHeight();
			imgWidth = 100;
			imgHeight = 100;
			degree = degreeStep * (total - 1 - i);
			if (degree > 90) {//始终保持图片正面朝上
				degree = -90 + (degree - 90);
			}
			diagonal = (float) Math.sqrt(imgWidth * imgWidth + imgHeight * imgHeight);
			scale = (float) mWidth / diagonal;
			centerX = (int) (imgWidth * scale / 2);
			centerY = (int) (imgHeight * scale / 2);
			translationX = mWidth/2 - centerX;
			translationY = mHeight/2 - centerY;

			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			matrix.postRotate(degree, centerX, centerY);
			matrix.postTranslate(translationX, translationY);
			imageView.setImageMatrix(matrix);

		}
	}

	/**
	 * 封面右上方层叠
	 * 会将每张图像都处理为相同大小
	 * call addCover before call this
	 */
	public void applyStyleCascade(int spaceX, int spaceY, int number) {
		setStyle2(spaceX, spaceY, number);
	}

	/**
	 * 封面右上方层叠
	 * 会将每张图像都处理为相同大小
	 * call addCover before call this
	 */
	public void applyStyleCascade() {
		setStyle2(30, 30, 4);
	}
	private void setStyle2(int spaceX, int spaceY, int number) {
		ImageView imageView = null;
		int total = getChildCount();
		if (total == 1) {
			addSingleCover(imageList.get(0));
			return;
		}

		if (total > number) {
			for (int i = total - 1; i >= number; i --) {
				imageView = (ImageView) getChildAt(i);
				imageView.setVisibility(View.GONE);
			}
			total = number;
		}

		int padding = 0;
		int imgWidth = mWidth - padding * 2 - spaceX * (total - 1);
		int imgHeight = imgWidth * 3 / 4;
		int baseY = (mHeight - imgHeight - spaceY * (total - 1))/2;//center in vertical
//		float scaleX = 0;
//		float scaleY = 0;
		float translationX = 0;
		float translationY = 0;
		float alpha = 1;
		float alphaStep = 0.7f/total;
		for (int i = total - 1; i > -1; i --) {

			imageView = (ImageView) getChildAt(i);
//			scaleX = (float) imgWidth / (float) imageList.get(i).getWidth();
//			scaleY = (float) imgHeight / (float) imageList.get(i).getHeight();
			translationX = padding + spaceX * (total - 1 - i);
			translationY = baseY + padding + spaceY * i;

			LayoutParams params = (LayoutParams) imageView.getLayoutParams();
			params.width = imgWidth;
			params.height = imgHeight;
			params.leftMargin = (int) translationX;
			params.topMargin = (int) translationY;
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setAlpha(alpha);
			alpha -= alphaStep;

			//matrix 的方式在adapter滑动过程中总是有问题，scale是对的，但是imageview控件相对父控件framelayout的位置和大小似乎就有问题，不知道什么原因
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleX, scaleY);
//			matrix.postTranslate(translationX, translationY);
//			imageView.setImageMatrix(matrix);

		}

	}
	/**
	 * 设置层叠封面图片
	 * @param imgPath
	 */
	public void setCascadeCovers(List<String> imgPath) {
		for (int i = 0; i < imageViewList.size(); i ++) {
			SImageLoader.getInstance().displayImage(imgPath.get(i), imageViewList.get(i));
		}
	}

	/**
	 * 创建宫格封面
	 * @param imgPath
     */
	public void addCoverPath(String imgPath) {
		imageList.add(imgPath);
	}

	/**
	 * 创建宫格封面
	 * 顺序方格平铺
	 * 自动匹配模式
	 * call addBitmap before call this
	 */
	public void applyStyleGrid() {
		if (imageList == null) {
			return;
		}

		int total = imageList.size();
		if (total < 1) {
			return;
		}

		Style3Value style = Style3Value.STYLE3_SIZE_1;
		if (total >= 4) {
			style = Style3Value.STYLE3_SIZE_4;
		}
		else if (total >= 2) {
			style = Style3Value.STYLE3_SIZE_2;
		}

		setStyle3(style);
	}

	/**
	 * 顺序方格平铺
	 * call addBitmap before call this
	 * @param value image充足的情况下强制模式显示
	 */
	public void applyStyleGrid(Style3Value value) {
		if (imageList == null) {
			return;
		}

		int total = imageList.size();
		if (total < 1) {
			return;
		}

		Style3Value style = Style3Value.STYLE3_SIZE_1;
		if (total >= 4) {
			style = Style3Value.STYLE3_SIZE_4;
			if (value.compareTo(style) < 0) {
				style = value;
			}
		}
		else if (total >= 2) {
			style = Style3Value.STYLE3_SIZE_2;
			if (value.compareTo(style) < 0) {
				style = value;
			}
		}

		setStyle3(value);
	}

	private void setStyle3(Style3Value style) {
		if (style == Style3Value.STYLE3_SIZE_1) {
			removeAllViews();
			ImageView imageView = new ImageView(getContext());
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			SImageLoader.getInstance().displayImage(imageList.get(0), imageView);
			addView(imageView);
			imageViewList.add(imageView);
		}
		else if (style == Style3Value.STYLE3_SIZE_2) {
			removeAllViews();
			LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext())
					.inflate(R.layout.multicover_style3_size_2, null);
			ImageView imageView1 = (ImageView) linearLayout.findViewById(R.id.style3_size2_image1);
			ImageView imageView2 = (ImageView) linearLayout.findViewById(R.id.style3_size2_image2);
			SImageLoader.getInstance().displayImage(imageList.get(0), imageView1);
			SImageLoader.getInstance().displayImage(imageList.get(1), imageView2);
			imageViewList.add(imageView1);
			imageViewList.add(imageView2);
			addView(linearLayout);
		}
		else if (style == Style3Value.STYLE3_SIZE_4) {
			removeAllViews();
			LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext())
					.inflate(R.layout.multicover_style3_size_4, null);
			ImageView imageView1 = (ImageView) linearLayout.findViewById(R.id.style3_size4_image1);
			ImageView imageView2 = (ImageView) linearLayout.findViewById(R.id.style3_size4_image2);
			ImageView imageView3 = (ImageView) linearLayout.findViewById(R.id.style3_size4_image3);
			ImageView imageView4 = (ImageView) linearLayout.findViewById(R.id.style3_size4_image4);
			SImageLoader.getInstance().displayImage(imageList.get(0), imageView1);
			SImageLoader.getInstance().displayImage(imageList.get(1), imageView2);
			SImageLoader.getInstance().displayImage(imageList.get(2), imageView3);
			SImageLoader.getInstance().displayImage(imageList.get(3), imageView4);
			imageViewList.add(imageView1);
			imageViewList.add(imageView2);
			imageViewList.add(imageView3);
			imageViewList.add(imageView4);
			addView(linearLayout);
		}
	}

	/**
	 * 设置宫格封面图片
	 * @param imgPath
     */
	public void setGridCovers(List<String> imgPath) {
		for (int i = 0; i < imageViewList.size(); i ++) {
			SImageLoader.getInstance().displayImage(imgPath.get(i), imageViewList.get(i));
		}
	}
}
