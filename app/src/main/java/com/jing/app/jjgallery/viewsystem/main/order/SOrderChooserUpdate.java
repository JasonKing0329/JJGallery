package com.jing.app.jjgallery.viewsystem.main.order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.order.SOrder;
import com.jing.app.jjgallery.model.main.order.PriorityController;
import com.jing.app.jjgallery.model.main.order.SOrderCallback;
import com.jing.app.jjgallery.model.main.order.SOrderChooserController;
import com.jing.app.jjgallery.res.AppResManager;
import com.jing.app.jjgallery.res.AppResProvider;
import com.jing.app.jjgallery.res.ColorRes;
import com.jing.app.jjgallery.res.JResource;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.util.ScreenUtils;
import com.jing.app.jjgallery.viewsystem.sub.dialog.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.WaveSideBarView;
import com.king.lib.colorpicker.ColorPicker;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author JingYang
 * description: add to order dialog
 */
public class SOrderChooserUpdate extends CustomDialog implements OnItemClickListener
		, OnItemLongClickListener, ColorPicker.OnColorPickerListener, SOrderCallback {

	/**
	 * select order action
	 */
	public interface OnOrderSelectListener {
		void onSelect(SOrder order);
	}
	private OnOrderSelectListener onOrderSelectListener;

	private ListView listView;
	private SOrderChooserAdapter listAdapter;
	private TextView noOrderText;
	private ImageView addButton;
	private ImageView colorButton;

	/**
	 * all orders
	 */
	private List<SOrder> totalOrderList;
	/**
	 * orders to show(search result)
	 */
	private List<SOrder> orderList;
	/**
	 * selected order index
	 */
	private int chosenIndex = -1;
	/**
	 * control the priority of items
	 */
	private PriorityController priorityController;
	/**
	 * priority menu array
	 */
	private int currentMenuResId;

	private WaveSideBarView waveSideBarView;

	private ColorPicker colorPicker;

	private SOrderChooserController controller;

	public SOrderChooserUpdate(Context context, OnCustomDialogActionListener listener) {
		super(context, listener);
		requestSaveAction(true);
		init();
	}

	/**
	 * outside call this to only get chosen order
	 * @param context
	 * @param listener
	 */
	public SOrderChooserUpdate(Context context, OnOrderSelectListener listener) {
		super(context, null);
		onOrderSelectListener = listener;
		init();
	}

	public void init() {
		priorityController = new PriorityController(context);
		controller = new SOrderChooserController();
		controller.setSOrderCallback(this);
		requestCancelAction(true);
		requestSearchAction(true);
		computeHeight();
		enableZoom(false);

		initOrders();
	}

	private void resetColors() {
		setBackgroundColor(JResource.getColor(context
				, ColorRes.SORDER_CHOOSER_BK, R.color.sorder_chooser_bk));
		setTitleColor(JResource.getColor(context
				, ColorRes.SORDER_CHOOSER_TITLE, R.color.sorder_chooser_title));
		setDividerColor(JResource.getColor(context
				, ColorRes.SORDER_CHOOSER_DIVIDER
				, R.color.white));
		updateIconBk(addButton, JResource.getColor(context
				, ColorRes.SORDER_CHOOSER_ICON_BK, R.color.sorder_chooser_icon_bk));
		updateIconBk(colorButton, JResource.getColor(context
				, ColorRes.SORDER_CHOOSER_ICON_BK, R.color.sorder_chooser_icon_bk));
		updateToobarIconBk(JResource.getColor(context
				, ColorRes.SORDER_CHOOSER_ICON_BK, R.color.sorder_chooser_icon_bk));
		updateTitleBk(JResource.getColor(context
				, ColorRes.SORDER_CHOOSER_TITLE_BK
				, R.color.sorder_chooser_title_bk));
		updateTitleBorderColor(JResource.getColor(context
				, ColorRes.SORDER_CHOOSER_TITLE_BOARDER, R.color.actionbar_bk_blue));
		if (listAdapter != null) {
			listAdapter.resetColor();
		}
	}

	private void initOrders() {
		controller.loadAllOrders();
	}

	@Override
	public void onQueryAllOrders(List<SOrder> list, int orderBy) {
		totalOrderList = list;
		controller.sortOrderByName(list);//sort by name
		priorityController.updateOrdersByPriority(totalOrderList);
		if (totalOrderList != null && totalOrderList.size() > 0) {
			// show side index bar
			showSideBar();

			orderList = new ArrayList<SOrder>();
			for (SOrder order:totalOrderList) {
				orderList.add(order);
			}
			notifyRefresh();
			noOrderText.setVisibility(View.GONE);
		}
		else {
			noOrderText.setVisibility(View.VISIBLE);
		}
	}

	private void showSideBar() {
		waveSideBarView.setVisibility(View.VISIBLE);
		// post刷新mSideBarView，根据调试发现重写初始化后WaveSideBarView会重新执行onMeasure(width,height=0)->onDraw->onMeasure(width,height=正确值)
		// 缺少重新onDraw的过程，因此通过delay执行mSideBarView.invalidate()可以激活onDraw事件，根据正确的值重新绘制
		// 用mSideBarView.post/postDelayed总是不准确
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				waveSideBarView.invalidate();
			}
		}, 100);
	}

	/**
	 * compute dialog's height in different orientation and device
	 */
	public void computeHeight() {
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			int screenHeight = ScreenUtils.getScreenHeight(context);
			if (DisplayHelper.isTabModel(context)) {
				setHeight(screenHeight * 3 / 4);
			}
			else {
				setHeight(screenHeight * 4 / 5);
			}
		}
		else {
			int screenHeight = ScreenUtils.getScreenHeight(context);
			if (DisplayHelper.isTabModel(context)) {
				setHeight(screenHeight * 2 / 3);
			}
			else {
				setHeight(screenHeight - 80);
			}
		}
	}

	private void notifyRefresh() {
		if (listAdapter == null) {
			listAdapter = new SOrderChooserAdapter(context, orderList);
			listAdapter.setPriorityNumber(priorityController.getTotalPriorityNumbers());
			listView.setAdapter(listAdapter);
		}
		else {
			listAdapter.setPriorityNumber(priorityController.getTotalPriorityNumbers());
			listAdapter.notifyDataSetChanged();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		//outside only call get chosen order
		if (onOrderSelectListener != null) {
			onOrderSelectListener.onSelect(orderList.get(position));
			dismiss();
			return;
		}

		chosenIndex = position;
		listAdapter.setSelectedIndex(position);
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   int position, long id) {
		chosenIndex = position;
		currentMenuResId = R.array.sorder_new_priority;
		if (priorityController.isPriorityOrder(orderList.get(chosenIndex)) != null) {
			currentMenuResId = R.array.sorder_priority;
		}
		new AlertDialog.Builder(getContext())
				.setTitle(null)
				.setItems(currentMenuResId, popMenuListener)
				.show();
		return true;
	}

	DialogInterface.OnClickListener popMenuListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (currentMenuResId == R.array.sorder_new_priority) {
				if (which == 0) {//set
					SOrder order = orderList.get(chosenIndex);
					priorityController.setAsPriorityController(order);
					orderList.remove(chosenIndex);
					orderList.add(0, order);
					notifyRefresh();
				}
			}
			else if (currentMenuResId == R.array.sorder_priority) {
				switch (which) {
					case 0://cancel
						SOrder order = orderList.get(chosenIndex);
						priorityController.cancelPriority(order);
						orderList.remove(chosenIndex);
						orderList.add(order);
						notifyRefresh();
						break;
					case 1://increase
						order = orderList.get(chosenIndex);
						priorityController.increasePriority(order);
						if (chosenIndex > 0) {
							orderList.set(chosenIndex, orderList.get(chosenIndex - 1));
							orderList.set(chosenIndex - 1, order);
							notifyRefresh();
						}
						break;
					case 2://decrease
						order = orderList.get(chosenIndex);
						priorityController.decreasePriority(order);
						if (chosenIndex < priorityController.getTotalPriorityNumbers() - 1) {
							orderList.set(chosenIndex, orderList.get(chosenIndex + 1));
							orderList.set(chosenIndex + 1, order);
							notifyRefresh();
						}
						break;

					default:
						break;
				}
			}
		}
	};
	@Override
	public void onClick(View v) {

		if (v == saveIcon) {
			if (chosenIndex > -1) {
				actionListener.onSave(orderList.get(chosenIndex));
				controller.reloadOrders();
			}
		}

		else if (v.getId() == R.id.order_chooser_add_order) {
			SOrderCreaterUpdate creater = new SOrderCreaterUpdate(context, new OnCustomDialogActionListener() {

				@Override
				public boolean onSave(Object object) {
					if (object != null) {
						SOrder order = (SOrder) object;
						if (orderList == null) {
							orderList = new ArrayList<SOrder>();
						}
						orderList.add(order);
						noOrderText.setVisibility(View.GONE);
						notifyRefresh();
						listView.setSelected(true);
						listView.setSelection(orderList.size() - 1);
					}
					return false;
				}

				@Override
				public void onLoadData(HashMap<String, Object> data) {

				}

				@Override
				public boolean onCancel() {
					return false;
				}
			});
			creater.show();
		}
		else if (v == colorButton) {
			if (colorPicker == null) {
				colorPicker = new ColorPicker(context, this);
				colorPicker.setResourceProvider(new AppResProvider(context));
			}
			colorPicker.setSelectionData(new AppResManager().getSorderChooserList(context));
			colorPicker.show();
		}
		super.onClick(v);
	}

	private class SearchTextWather implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence text, int start, int before,
								  int count) {
			Log.i("SOrderChooser", "onTextChanged(" + text + "," + start + "," + before + "," + count + ")");
			orderList.clear();
			if (text.toString().trim().length() == 0) {
				listAdapter.hasPriorityColor(true);
				for (int i = 0; i < totalOrderList.size(); i ++) {
					orderList.add(totalOrderList.get(i));
				}
				notifyRefresh();
				return;
			}

			//startWith排在前面，contains排在后面
			listAdapter.hasPriorityColor(false);
			String target = null, prefix = text.toString().toLowerCase();
			for (int i = 0; i < totalOrderList.size(); i ++) {
				target = totalOrderList.get(i).getName().toLowerCase();
				if (target.startsWith(prefix)) {
					orderList.add(totalOrderList.get(i));
				}
			}
			for (int i = 0; i < totalOrderList.size(); i ++) {
				target = totalOrderList.get(i).getName().toLowerCase();
				if (!target.startsWith(prefix) && target.contains(prefix)) {
					orderList.add(totalOrderList.get(i));
				}
			}
			notifyRefresh();
		}

	}

	@Override
	protected View getCustomView() {

		View view = LayoutInflater.from(getContext()).inflate(R.layout.sorder_chooser_customview, null);
		listView = (ListView) view.findViewById(R.id.order_chooser_list);
		noOrderText = (TextView) view.findViewById(R.id.order_chooser_noorder);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		waveSideBarView = (WaveSideBarView) view.findViewById(R.id.order_chooser_side);
		waveSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
			@Override
			public void onLetterChange(String letter) {
				if (listAdapter != null) {
					int position = listAdapter.getLetterPosition(letter);
					listView.setSelection(position);
				}
			}
		});
		return view;
	}
	@Override
	protected View getCustomToolbar() {

		View view = LayoutInflater.from(getContext()).inflate(R.layout.sorder_chooser_actionbar_l, null);
		addButton = (ImageView) view.findViewById(R.id.order_chooser_add_order);
		addButton.setOnClickListener(this);
		colorButton = (ImageView) view.findViewById(R.id.order_chooser_color);
		colorButton.setOnClickListener(this);
		registTextChangeListener(new SearchTextWather());
		return view;
	}

	@Override
	public void onColorChanged(String key, int newColor) {
		if (key.equals(ColorRes.SORDER_CHOOSER_BK)) {
			setBackgroundColor(newColor);
		}
		else if (key.equals(ColorRes.SORDER_CHOOSER_TITLE)) {
			setTitleColor(newColor);
		}
		else if (key.equals(ColorRes.SORDER_CHOOSER_ICON_BK)) {
			updateIconBk(addButton, newColor);
			updateIconBk(colorButton, newColor);
			updateToobarIconBk(newColor);
		}
		else if (key.equals(ColorRes.SORDER_CHOOSER_TITLE_BK)) {
			updateTitleBk(newColor);
		}
		else if (key.equals(ColorRes.SORDER_CHOOSER_TITLE_BOARDER)) {
			updateTitleBorderColor(newColor);
		}
		else if (key.equals(ColorRes.SORDER_CHOOSER_DIVIDER)) {
			setDividerColor(newColor);
		}
		else if (key.equals(ColorRes.SORDER_CHOOSER_LIST_TEXT)) {
			listAdapter.updateTextColor(newColor);
		}
		else if (key.equals(ColorRes.SORDER_CHOOSER_LIST_TEXT_PRIORITY)) {
			listAdapter.updatePriorityTextColor(newColor);
		}
		else if (key.equals(ColorRes.SORDER_CHOOSER_LIST_SELECTED)) {
			listAdapter.updateListSelectedColor(newColor);
		}
	}

	private void updateIconBk(ImageView view, int color) {
		StateListDrawable stateDrawable = (StateListDrawable) view.getBackground();
		GradientDrawable drawable = (GradientDrawable) stateDrawable.getCurrent();
		drawable.setColor(color);
	}

	@Override
	public void onColorSelected(int color) {

	}

	@Override
	public void onColorSelected(List<ColorPickerSelectionData> list) {
		for (ColorPickerSelectionData data:list) {
			JResource.updateColor(data.getKey(), data.getColor());
		}
		JResource.saveColorUpdate(context);
	}

	@Override
	public void onColorCancleSelect() {
		resetColors();
	}

	@Override
	public void onApplyDefaultColors() {
		JResource.removeColor(ColorRes.SORDER_CHOOSER_BK);
		JResource.removeColor(ColorRes.SORDER_CHOOSER_TITLE);
		JResource.removeColor(ColorRes.SORDER_CHOOSER_TITLE_BK);
		JResource.removeColor(ColorRes.SORDER_CHOOSER_TITLE_BOARDER);
		JResource.removeColor(ColorRes.SORDER_CHOOSER_ICON_BK);
		JResource.removeColor(ColorRes.SORDER_CHOOSER_DIVIDER);
		JResource.removeColor(ColorRes.SORDER_CHOOSER_LIST_TEXT);
		JResource.removeColor(ColorRes.SORDER_CHOOSER_LIST_TEXT_PRIORITY);
		JResource.removeColor(ColorRes.SORDER_CHOOSER_LIST_SELECTED);
		JResource.saveColorUpdate(context);
		resetColors();
	}

	@Override
	public void show() {
		resetColors();
		super.show();
	}

}
