package com.jing.app.jjgallery.model.sub;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.jing.app.jjgallery.service.encrypt.EncrypterFactory;
import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.service.image.ImageValue;
import com.jing.app.jjgallery.util.ScreenUtils;

import java.io.File;

/**
 * Created by JingYang on 2016/7/26 0026.
 * Description:
 */
public class WaterfallHelper {

    private Encrypter encrypter;
    public WaterfallHelper() {
        encrypter = EncrypterFactory.create();
    }
    public void calculateImageSize(ImageValue value) {
        byte datas[] = encrypter.decipherToByteArray(new File(value.getPath()));
        if (datas != null) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;// 对bitmap不分配空间，只是用于计算文件options的各种属性(本程序需要计算width,height)
            BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
            value.setWidth(opts.outWidth);
            value.setHeight(opts.outHeight);
        }
    }

    public void reseizeImage(ImageValue value, int nColumn, int margin, Context mContext) {
        int width = (ScreenUtils.getScreenWidth(mContext))/nColumn - margin * 2;
        int height = (int) (((float) width / (float) value.getWidth()) * (value.getHeight()));
        value.setWidth(width);
        value.setHeight(height);
    }
}
