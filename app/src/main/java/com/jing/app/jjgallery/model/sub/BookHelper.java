package com.jing.app.jjgallery.model.sub;

import android.util.Log;

import com.jing.app.jjgallery.bean.BookInforBean;
import com.jing.app.jjgallery.service.image.ImageValue;
import com.jing.app.jjgallery.service.image.ImageValueController;
import com.jing.app.jjgallery.util.DebugLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class BookHelper {

	private final String TAG = "BookHelper";

	private BookInforBean bookInforBean;

	public BookHelper() {

	}

	public List<List<ImageValue>> orderPageItems(List<String> imgPathList) {
		if (imgPathList == null) {
			return null;
		}
		List<List<ImageValue>> list = new ArrayList<>();
		Collections.shuffle(imgPathList);
		ImageValueController controller = new ImageValueController();

		List<ImageValue> values = new ArrayList<>();
		controller.queryImagePixelFrom(imgPathList, values);
		DebugLog.e("img list size:" + imgPathList.size() + ", values size:" + values.size());

		sortImageValue(list, values);
		return list;
	}

	/**
	 * sort规则:
	 * BookPage一共采用了6种显示布局模式
	 * 1张图片布局，2张图片布局，3张图片布局（包含3种）
	 * 对于1张图片布局，图片只要fitCenter全屏显示即可
	 * 对于2张图片布局，要求2张图片至少有一张是属于宽高差不多的
	 * 对于3张图片布局，3中图片模式对应了3中不同改的图片尺寸要求
	 * （1. 三种图片纵向顺序布局，适用于3张图片都是宽比高数值大，或者差不多
	 *  2. 左边2个纵向，右边一个充满，适用于3张图片都是高比宽数值大
	 *  3. 上面2个横向，下面一个充满，适用于2个高比宽数值大，1个宽比高数值大或差不多）
	 * 对于4张图片布局，要求4张图片满足宽高都差不多
	 *
	 *  实现过程：
	 *  采用3个队列，先将所有的图片根据value保存的宽高信息进行分类。分为宽比高数值大的widthQueue，高比宽数值大的heightQueue，
	 *  以及宽高差值在0.2倍之间的middleQueue。
	 *  为了能够匹配所有数量的图片，middleQueue的图片作为万能尺寸图片，即可当做widthQueue类图片，也可当做heightQueue类
	 *
	 *  采用while循环，利用随机数生成每页的图片数量
	 *  根据规则判断适合从哪个队列中弹出图片来组成满足规则的图片列表，每凑齐对应数量的图片就将代表该页图片的values加入至list中
	 * @param list 生成的page，以及page中应包含的图片列表
	 * @param values 当前全部图片的value信息列表
	 */
	private void sortImageValue(List<List<ImageValue>> list,
									   List<ImageValue> values) {

		bookInforBean = new BookInforBean();
		bookInforBean.setTotal(values.size());
		// 分配队列
		// 所有宽大于高1.2倍以上的图片
		Queue<ImageValue> widthQueue = new LinkedList<>();
		// 所有高大于宽1.2倍以上的图片
		Queue<ImageValue> heightQueue = new LinkedList<>();
		// 所有宽高比在1.2之内的图片
		Queue<ImageValue> middleQueue = new LinkedList<>();

		for (ImageValue value:values) {
			if (value.getWidth() > value.getHeight()) {
				if ((float) value.getWidth() / (float) value.getHeight() > 1.2f) {
					widthQueue.offer(value);
				}
				else {
					middleQueue.offer(value);
				}
			}
			else {
				if ((float) value.getHeight() / (float) value.getWidth() > 1.2f) {
					heightQueue.offer(value);
				}
				else {
					middleQueue.offer(value);
				}
			}
		}
		bookInforBean.setWidthQueue(widthQueue.size());
		bookInforBean.setHeightQueue(heightQueue.size());
		bookInforBean.setMiddleQueue(middleQueue.size());

		Random random = new Random();
		//支持的页面类型（页面包含的图片数）
		int[] sizeMode = new int[] {1, 2, 3, 4, 6};
		int size = 1;

		boolean hasMore = true;
		List<ImageValue> subList = null;

		// 从3个队列里遍历完所有图片，分配模式
		while (hasMore) {

			// 先随机应用一种模式
			size = sizeMode[Math.abs(random.nextInt()) % sizeMode.length];

			// 当前还剩下的图片总数
			int left = widthQueue.size() + heightQueue.size() + middleQueue.size();
			Log.d(TAG, "random size=" + size + ", left number=" + left);

			/**************************特殊情况先做判断**********************************/
			if (left < size) {//剩余图片不足随机数制定的图片张数
				size = left;
			}

			if (size == 6) {
				// 6图目前有两种模式，1.全部是middle 2.3个height，3个width
				if (middleQueue.size() < 6 || (heightQueue.size() < 3 && widthQueue.size() < 3)) {
					size = 4;//由于暂时没有5图模式，降级为4
				}
			}

			// 目前不支持5图模式
			if (size == 5) {
				size --;
			}

			if (size == 4) {
				if (heightQueue.size() < 4) {//4张图布局要求4张图片都是高比宽数值大
					size --;
				}
			}

			//3张图布局可以接纳任何图片，会在后面匹配3种模式

			if (size == 2) {//2个的模式需满足2个图片宽高都差不多或者宽比高数值大
				if (middleQueue.size() + widthQueue.size() < 2) {
					if (left > 2) {
						size = 3;
					}
					else {
						size = 1;
					}
				}
			}

			/************************************************************/
			/**
			 * 到这里，size代表的数的模式一定可以运用，即queue中的图片数量是一定满足模式的
			 */
			Log.d(TAG, "real size=" + size);
			if (size == 1) {//可以添加任意尺寸图片
				//从最多的queue里取
				Queue<ImageValue> offerQueue = null;
				if (widthQueue.size() > heightQueue.size()) {
					offerQueue = widthQueue.size() > middleQueue.size() ? widthQueue : middleQueue;
				}
				else {
					offerQueue = heightQueue.size() > middleQueue.size() ? heightQueue : middleQueue;
				}

				subList = new ArrayList<ImageValue>();
				subList.add(offerQueue.poll());
				list.add(subList);
			}

			else if (size == 2) {//添加两个是middleQueue或者widthQueue的图片，优先widthQueue
				int n = 0;
				subList = new ArrayList<ImageValue>();
				while (widthQueue.size() > 0 && n < 2) {
					subList.add(widthQueue.poll());
					n ++;
				}

				if (n == 0) {
					subList.add(middleQueue.poll());
					subList.add(middleQueue.poll());
				}
				else if (n == 1) {
					subList.add(middleQueue.poll());
				}
				list.add(subList);
			}
			else if (size == 3) {//有3种布局模式，最复杂

				List<Integer> availableMode = new ArrayList<>();

				// 模式1，3个纵向排列，要求3个都是width or middle
				if (widthQueue.size() + middleQueue.size() > 2) {
					availableMode.add(1);
				}
				// 模式2、3，两上一下或者一上两下，两个是height or middle，另一个是width or middle
				if ((heightQueue.size() + middleQueue.size()) > 1 && (widthQueue.size() + middleQueue.size()) > 0) {
					availableMode.add(2);
					availableMode.add(3);
				}

				// 如果上面两种都不满足，把两上一下(3)暂定义为百搭模式
				if (availableMode.size() == 0) {
					availableMode.add(3);
				}

				// 在可生成的模式中随机一种
				int mode = availableMode.get(Math.abs(random.nextInt()) % availableMode.size());

				if (mode == 1) {// 3个纵向排列
					subList = new ArrayList<>();

					//第一个添加模式标志，Bookpage在显示的时候根据该tag
					//从而判断运用哪种显示模式
					ImageValue value = widthQueue.size() == 0 ? middleQueue.poll() : widthQueue.poll();
					value.setTag(mode);
					subList.add(value);
					subList.add(widthQueue.size() == 0 ? middleQueue.poll() : widthQueue.poll());
					subList.add(widthQueue.size() == 0 ? middleQueue.poll() : widthQueue.poll());
					list.add(subList);
					if (widthQueue.size() == 0 && heightQueue.size() == 0 && middleQueue.size() == 0) {
						hasMore = false;
					}
				}
				else if (mode == 2) {// 一上两下
					subList = new ArrayList<>();

					ImageValue value = widthQueue.size() == 0 ? middleQueue.poll() : widthQueue.poll();
					value.setTag(mode);//第一个添加模式标志
					subList.add(value);
					subList.add(heightQueue.size() == 0 ? middleQueue.poll() : heightQueue.poll());
					subList.add(heightQueue.size() == 0 ? middleQueue.poll() : heightQueue.poll());
					list.add(subList);
				}
				else {// mode = 3, 两上一下
					subList = new ArrayList<>();

					ImageValue value = heightQueue.size() == 0 ? middleQueue.poll() : heightQueue.poll();
					value.setTag(mode);//第一个添加模式标志
					subList.add(value);
					subList.add(heightQueue.size() == 0 ? middleQueue.poll() : heightQueue.poll());
					subList.add(widthQueue.size() == 0 ? middleQueue.poll() : widthQueue.poll());
					list.add(subList);
				}
			}
			else if (size == 4) {// 4个height
				subList = new ArrayList<>();
				subList.add(heightQueue.poll());
				subList.add(heightQueue.poll());
				subList.add(heightQueue.poll());
				subList.add(heightQueue.poll());
				list.add(subList);
			}
			else if (size == 6) {

				List<Integer> availableMode = new ArrayList<>();
				if (middleQueue.size() > 5) {
					availableMode.add(1);
				}
				if (heightQueue.size() > 2 && widthQueue.size() > 2) {
					availableMode.add(2);
				}

				int mode = availableMode.get(Math.abs(random.nextInt()) % availableMode.size());
				subList = new ArrayList<>();
				if (mode == 1) {// 6个middle
					ImageValue value = middleQueue.poll();
					value.setTag(mode);
					subList.add(value);
					subList.add(middleQueue.poll());
					subList.add(middleQueue.poll());
					subList.add(middleQueue.poll());
					subList.add(middleQueue.poll());
					subList.add(middleQueue.poll());
				}
				else {// width,height; height, width; width, height
					ImageValue value = widthQueue.poll();
					value.setTag(mode);
					subList.add(value);
					subList.add(heightQueue.poll());
					subList.add(heightQueue.poll());
					subList.add(widthQueue.poll());
					subList.add(widthQueue.poll());
					subList.add(heightQueue.poll());
				}
				list.add(subList);
			}

			if (widthQueue.size() == 0 && heightQueue.size() == 0 && middleQueue.size() == 0) {
				hasMore = false;
			}
		}
	}

	public BookInforBean getBookInforBean() {
		return bookInforBean;
	}
}
