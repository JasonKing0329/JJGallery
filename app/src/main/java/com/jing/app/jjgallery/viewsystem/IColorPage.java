package com.jing.app.jjgallery.viewsystem;

import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.List;

/**
 * Created by Administrator on 2016/7/10 0010.
 */
public interface IColorPage {
    void onColorChanged(String key, int newColor);
    void onApplyDefaultColors();
    void applyExtendColors();
    List<ColorPickerSelectionData> getColorPickerSelectionData();
}
