package com.jing.app.jjgallery.service.data.dao;

import com.jing.app.jjgallery.service.image.ImageValue;

import java.sql.Connection;
import java.util.List;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public interface ImageValueDao {

    /**
     * 查询图片大小等参数
     * @param key
     * @param connection
     * @return
     */
    ImageValue queryImageValue(String key, Connection connection);

    /**
     * 查询一批ImageValue
     * @param pathList image path list
     * @param values should be not null but size is 0.
     * @param connection
     */
    void queryImageValues(List<String> pathList, List<ImageValue> values, Connection connection);
}
