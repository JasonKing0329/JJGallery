package com.jing.app.jjgallery.viewsystem.sub.thumb;

/**
 * Created by 景阳 on 2017/1/11.
 */

public interface IThumbSelector {
    void onSelectFolderThumb();
    void onSelectOrderThumb();

    void onSelectImage(String imagePath);

    void onCancel();
}
