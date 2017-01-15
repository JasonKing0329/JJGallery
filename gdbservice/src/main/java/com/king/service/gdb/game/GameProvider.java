package com.king.service.gdb.game;

import android.text.TextUtils;

import com.king.service.gdb.SqlConnection;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;
import com.king.service.gdb.game.dao.CoachDao;
import com.king.service.gdb.game.dao.SeasonDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class GameProvider {
    private String databasePath;

    public GameProvider(String databasePath) {
        this.databasePath = databasePath;
    }

    /**
     * query season list
     * @return
     */
    public List<SeasonBean> getSeasonList() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new SeasonDao().querySeasonList(SqlConnection.getInstance().getConnection());
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
            return new SeasonDao().querySeansonBean(id, SqlConnection.getInstance().getConnection());
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
            SeasonDao dao = new SeasonDao();
            SqlConnection.getInstance().connect(databasePath);
            if (seasonBean.getId() == -1) {
                boolean result = dao.insertSeason(seasonBean, SqlConnection.getInstance().getConnection());
                // id是自增的，因此插入后查询出来
                seasonBean.setId(dao.queryLastSeasonSequence(SqlConnection.getInstance().getConnection()));
                // add/update season id for every coach
                updateCoachSeason(dao);

                return result;
            }
            else {
                boolean result = dao.updateSeason(seasonBean, SqlConnection.getInstance().getConnection());

                // add/update season id for every coach
                updateCoachSeason(dao);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return false;
    }

    /**
     * 修改当前coach 的season ids
     * @param seasonDao
     */
    private void updateCoachSeason(SeasonDao seasonDao) {

        CoachDao coachDao = new CoachDao();
        List<CoachBean> list = coachDao.queryCoachList(SqlConnection.getInstance().getConnection());
        for (CoachBean coach:list) {
            String seasonIds = seasonDao.queryCoachSeasonIds(coach, SqlConnection.getInstance().getConnection());
            coach.setSeasonIds(seasonIds);
        }
        coachDao.updateCoaches(list, SqlConnection.getInstance().getConnection());
    }

    /**
     * query coach list
     * @return
     */
    public List<CoachBean> getCoachList() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new CoachDao().queryCoachList(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * query coach by id
     * @param id
     * @return
     */
    public CoachBean getCoachById(int id) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new CoachDao().queryCoachBean(id, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * modify coach
     * @param bean
     * @return
     */
    public boolean updateCoach(CoachBean bean) {
        try {
            CoachDao dao = new CoachDao();
            SqlConnection.getInstance().connect(databasePath);
            if (bean.getId() == -1) {
                boolean result = dao.insertCoach(bean, SqlConnection.getInstance().getConnection());
                // id是自增的，因此插入后查询出来
                bean.setId(dao.queryLastCoachSequence(SqlConnection.getInstance().getConnection()));
                return result;
            }
            else {
                return dao.updateCoach(bean, SqlConnection.getInstance().getConnection());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return false;
    }

}
