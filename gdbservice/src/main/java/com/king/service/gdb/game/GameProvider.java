package com.king.service.gdb.game;

import com.king.service.gdb.SqlConnection;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.List;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class GameProvider {
    private String databasePath;
    private GameDao sqliteDao;

    public GameProvider(String databasePath) {
        this.databasePath = databasePath;
        sqliteDao = new GameDao();
    }

    /**
     * query season list
     * @return
     */
    public List<SeasonBean> getSeasonList() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.querySeasonList(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * query season by id
     * @param id
     * @return
     */
    public SeasonBean getSeasonById(int id) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return sqliteDao.querySeansonBean(id, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * modify season
     * @param seasonBean
     * @return
     */
    public boolean updateSeason(SeasonBean seasonBean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            if (seasonBean.getId() == -1) {
                boolean result = sqliteDao.insertSeason(seasonBean, SqlConnection.getInstance().getConnection());
                // id是自增的，因此插入后查询出来
                seasonBean.setId(sqliteDao.queryLastSeasonSequence(SqlConnection.getInstance().getConnection()));
                return result;
            }
            else {
                return sqliteDao.updateSeason(seasonBean, SqlConnection.getInstance().getConnection());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return false;
    }
}
