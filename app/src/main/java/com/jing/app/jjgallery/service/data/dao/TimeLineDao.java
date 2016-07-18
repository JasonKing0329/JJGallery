package com.jing.app.jjgallery.service.data.dao;

import com.jing.app.jjgallery.bean.filesystem.FileBean;

import java.sql.Connection;
import java.util.List;

/**
 * Created by JingYang on 2016/7/18 0018.
 * Description:
 */
public interface TimeLineDao {
    List<FileBean> queryAllFileBeans(Connection connection, String orderBy);
}
