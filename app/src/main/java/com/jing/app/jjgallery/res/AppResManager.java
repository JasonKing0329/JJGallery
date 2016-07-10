package com.jing.app.jjgallery.res;

import android.content.Context;

import com.jing.app.jjgallery.R;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public class AppResManager {
    public List<ColorPickerSelectionData> getHomeList(Context context) {
        List<ColorPickerSelectionData> list = new ArrayList<>();
        ColorPickerSelectionData data = new ColorPickerSelectionData();
        data.setKey(ColorRes.ACTIONBAR_BK);
        data.setName("工具栏背景");
        data.setColor(JResource.getColor(context, ColorRes.ACTIONBAR_BK, R.color.actionbar_bk_blue));
        list.add(data);
        return list;
    }
    public List<ColorPickerSelectionData> getThumbList(Context context) {
        List<ColorPickerSelectionData> list = new ArrayList<>();
        ColorPickerSelectionData data = new ColorPickerSelectionData();
        data.setKey(ColorRes.ACTIONBAR_BK);
        data.setName("工具栏背景");
        data.setColor(JResource.getColor(context, ColorRes.ACTIONBAR_BK, R.color.actionbar_bk_blue));
        list.add(data);
        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.FM_THUMB_INDEX_NORMAL_COLOR);
        data.setName("Index正常背景");
        data.setColor(JResource.getColor(context, ColorRes.FM_THUMB_INDEX_NORMAL_COLOR, R.color.actionbar_bk_blue));
        list.add(data);
        data = new ColorPickerSelectionData();
        data.setKey(ColorRes.FM_THUMB_TEXT_COLOR);
        data.setName("Index文字颜色");
        data.setColor(JResource.getColor(context, ColorRes.FM_THUMB_TEXT_COLOR, R.color.white));
        list.add(data);
        return list;
    }
}
