package com.jing.app.jjgallery.service.data.dao;

import com.jing.app.jjgallery.service.image.ImageValue;

import java.sql.Connection;
import java.util.List;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public interface ImageValueDao {
    ImageValue queryImageValue(String key, Connection connection);
    boolean insertImagePixel(ImageValue value, Connection connection);
    void queryImageValues(List<String> keyList, List<ImageValue> values, Connection connection);
}
