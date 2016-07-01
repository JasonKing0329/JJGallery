package com.jing.app.jjgallery.service.data.dao;

import com.jing.app.jjgallery.bean.filesystem.FileBean;

import java.sql.Connection;
import java.util.List;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public interface FileDao {
    boolean isFileBeanExist(String path, Connection connection);
    boolean insertFileBeans(List<FileBean> list, Connection connection);
    List<FileBean> queryAllFileBeans(Connection connection, String orderBy);
    boolean deleteFileBean(long id, Connection connection);
}
