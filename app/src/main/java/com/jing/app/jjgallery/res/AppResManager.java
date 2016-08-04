package com.jing.app.jjgallery.res;

import android.content.Context;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class AppResManager {
    /**
     * 主界面默认选项
     * @param context
     * @return
     */
    public List<ColorPickerSelectionData> getHomeList(Context context) {
        List<ColorPickerSelectionData> list = new ArrayList<>();
        ColorPickerSelectionData data = new ColorPickerSelectionData();
        data.setKey(ColorRes.ACTIONBAR_BK);
        data.setName("工具栏背景");
        data.setColor(JResource.getColor(context, ColorRes.ACTIONBAR_BK, ThemeManager.getInstance().getBasicColorResId(context)));
        list.add(data);
        return list;
    }

    /**
     * Thumb folder页面
     * @param context
     * @return
     */
    public List<ColorPickerSelectionData> getThumbList(Context context) {
        List<ColorPickerSelectionData> list = new ArrayList<>();
        ColorPickerSelectionData data = new ColorPickerSelectionData();
        data.setKey(ColorRes.ACTIONBAR_BK);
        data.setName("工具栏背景");
        data.setColor(JResource.getColor(context, ColorRes.ACTIONBAR_BK, ThemeManager.getInstance().getBasicColorResId(context)));
        list.add(data);
        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.FM_THUMB_INDEX_NORMAL_COLOR);
        data.setName("Index正常背景");
        data.setColor(JResource.getColor(context, ColorRes.FM_THUMB_INDEX_NORMAL_COLOR, ThemeManager.getInstance().getBasicColorResId(context)));
        list.add(data);
        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.FM_THUMB_TEXT_COLOR);
        data.setName("Index文字颜色");
        data.setColor(JResource.getColor(context, ColorRes.FM_THUMB_TEXT_COLOR, R.color.white));
        list.add(data);
        return list;
    }

    /**
     * SOrderChooser
     * @param context
     * @return
     */
    public List<ColorPickerSelectionData> getSorderChooserList(Context context) {
        List<ColorPickerSelectionData> list = new ArrayList<ColorPickerSelectionData>();
        ColorPickerSelectionData data = new ColorPickerSelectionData();
        int colorId = ThemeManager.getInstance().getBasicColorResId(context);
        data.setKey(ColorRes.SORDER_CHOOSER_BK);
        data.setName("背景");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CHOOSER_BK, R.color.sorder_chooser_bk));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CHOOSER_TITLE);
        data.setName("标题");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CHOOSER_TITLE, R.color.sorder_chooser_title));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CHOOSER_TITLE_BK);
        data.setName("标题背景");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CHOOSER_TITLE_BK, R.color.sorder_chooser_title_bk));
        list.add(data);

        // 取消边框，弃用
//		data = new ColorPickerSelectionData();
//		data.setKey(ColorRes.SORDER_CHOOSER_TITLE_BOARDER);
//		data.setName("标题边框");
//		data.setColor(JResource.getColor(context, ColorRes.SORDER_CHOOSER_TITLE_BOARDER, colorId));
//		list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CHOOSER_ICON_BK);
        data.setName("工具栏图标背景");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CHOOSER_ICON_BK, R.color.sorder_chooser_icon_bk));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CHOOSER_DIVIDER);
        data.setName("分界线");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CHOOSER_DIVIDER, R.color.sorder_chooser_icon_bk));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CHOOSER_LIST_TEXT);
        data.setName("普通文字");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CHOOSER_LIST_TEXT, R.color.sorder_chooser_text));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CHOOSER_LIST_TEXT_PRIORITY);
        data.setName("优先文字");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CHOOSER_LIST_TEXT_PRIORITY, R.color.sorder_chooser_text_priority));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CHOOSER_LIST_SELECTED);
        data.setName("列表项选中状态");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CHOOSER_LIST_SELECTED, R.color.sorder_chooser_list_selected));
        list.add(data);
        return list;
    }

    /**
     * SorderCreater
     * @param context
     * @return
     */
    public List<ColorPickerSelectionData> getSorderCreaterList(Context context) {
        List<ColorPickerSelectionData> list = new ArrayList<ColorPickerSelectionData>();
        ColorPickerSelectionData data = new ColorPickerSelectionData();
        int colorId = ThemeManager.getInstance().getBasicColorResId(context);
        data.setKey(ColorRes.SORDER_CREATER_BK);
        data.setName("背景");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CREATER_BK, colorId));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CREATER_TITLE);
        data.setName("标题");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CREATER_TITLE, colorId));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CREATER_TITLE_BK);
        data.setName("标题背景");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CREATER_TITLE_BK, R.color.sorder_chooser_title_bk));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CREATER_ICON_BK);
        data.setName("工具栏图标背景");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CREATER_ICON_BK, colorId));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CREATER_DIVIDER);
        data.setName("分界线");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CREATER_DIVIDER, R.color.white));
        list.add(data);

        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.SORDER_CREATER_TEXT);
        data.setName("普通文字");
        data.setColor(JResource.getColor(context, ColorRes.SORDER_CREATER_TEXT, R.color.white));
        list.add(data);
        return list;
    }
}
