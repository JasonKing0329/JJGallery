package com.jing.app.jjgallery.service.data.dao;

import java.sql.Connection;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public interface VersionDao {
    boolean isBelowVersion6_2(Connection connection);
    boolean isBelowVersion7_0(Connection connection);
    boolean addImagePixelTable(Connection connection);
    boolean addFilesTable(Connection connection);
}
