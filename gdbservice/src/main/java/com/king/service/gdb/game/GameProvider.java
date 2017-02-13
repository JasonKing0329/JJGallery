package com.king.service.gdb.game;

import com.king.service.gdb.SqlConnection;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.BattleResultBean;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.GroupBean;
import com.king.service.gdb.game.bean.PlayerBean;
import com.king.service.gdb.game.bean.SeasonBean;
import com.king.service.gdb.game.dao.BattleDao;
import com.king.service.gdb.game.dao.CoachDao;
import com.king.service.gdb.game.dao.GroupDao;
import com.king.service.gdb.game.dao.PlayerDao;
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

    /**
     * query group data
     * @param seasonId
     * @return
     */
    public List<GroupBean> getGroupList(int seasonId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new GroupDao().queryGroupList(seasonId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * query group data
     * @param seasonId
     * @return
     */
    public List<GroupBean> getGroupList(int seasonId, int coachId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new GroupDao().queryGroupList(seasonId, coachId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * query player
     * @param playerId
     * @return
     */
    public PlayerBean getPlayerById(int playerId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new PlayerDao().queryPlayerBean(playerId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    /**
     * delete season
     * @param seasonId
     */
    public void deleteSeason(int seasonId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            // 在season表中删除
            new SeasonDao().deleteSeason(seasonId, SqlConnection.getInstance().getConnection());
            // 在coach表中删除对应的season
            new CoachDao().deleteSeason(seasonId, SqlConnection.getInstance().getConnection());
            // 在player表中删除对应的season
            new PlayerDao().deleteSeason(seasonId, SqlConnection.getInstance().getConnection());
            // 在_group表中删除对应的season
            new GroupDao().deleteSeason(seasonId, SqlConnection.getInstance().getConnection());
            // 在battle表中删除对应的season
            new BattleDao().deleteSeason(seasonId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * insert or update player
     * @param bean
     * @param type
     * @return
     */
    public int savePlayer(PlayerBean bean, int type) {
        int id = -1;
        try {
            SqlConnection.getInstance().connect(databasePath);
            PlayerDao playerDao = new PlayerDao();
            PlayerBean player = playerDao.queryPlayerBean(bean.getName(), SqlConnection.getInstance().getConnection());
            // insert as new item
            if (player == null) {
                playerDao.insertPlayer(bean, SqlConnection.getInstance().getConnection());
                id = playerDao.queryLastPlayerSequence(SqlConnection.getInstance().getConnection());
            }
            // update item data
            else {
                id = player.getId();
                playerDao.updatePlayer(bean, type, SqlConnection.getInstance().getConnection());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return id;
    }

    /**
     * insert group data
     * @param bean
     */
    public void insertGroupBean(GroupBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            new GroupDao().inserGroupBean(bean, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * query all group bean data
     * @return
     */
    public List<GroupBean> getGroupBeanList() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new GroupDao().queryGroupBeanList(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    public void deletePlayer(int playerId, int seasonId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            // 在player表中删除
            new PlayerDao().deletePlayer(playerId, SqlConnection.getInstance().getConnection());
            // 在_group表中删除对应season的player
            new GroupDao().deletePlayer(playerId, seasonId, SqlConnection.getInstance().getConnection());
            // 在battle表中删除对应的player记录
            new BattleDao().deletePlayer(playerId, seasonId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * insert battle data
     * @param bean
     */
    public void updateBattleBean(BattleBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            BattleDao dao = new BattleDao();
            //insert
            if (bean.getId() == -1) {
                dao.inserBattleBean(bean, SqlConnection.getInstance().getConnection());
                bean.setId(dao.queryLastBattleSequence(SqlConnection.getInstance().getConnection()));
            }
            //update
            else {
                dao.updateBattleBean(bean, SqlConnection.getInstance().getConnection());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * query all battle bean data
     * @return
     */
    public List<BattleBean> getBattleList(int seasonId, int coachId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new BattleDao().queryBattleList(seasonId, coachId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    public void deleteBattleBean(int battleId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            new BattleDao().deleteBattle(battleId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * 生成battle result数据，并更新eliminated player的id
     * @param datas
     * @param promoteNum
     * @return
     */
    public boolean saveBattleResultBeans(List<BattleResultBean> datas, int promoteNum) {
        try {
            SqlConnection.getInstance().connect(databasePath);

            // save battle_result record
            boolean isResultSaved = new BattleDao().saveBattleResultBeans(datas, SqlConnection.getInstance().getConnection());

            // update eliminated players result(rank value > promoteNum)
            List<PlayerBean> eliminatedIdList = new ArrayList<>();
            for (BattleResultBean bean:datas) {
                if (bean.getRank() > promoteNum) {
                    PlayerBean pb = new PlayerBean();
                    pb.setId(bean.getId());
                    if (bean.getType() == 1) {
                        pb.setTopCoachId(bean.getCoachId());
                    }
                    else {
                        pb.setBottomCoachId(bean.getCoachId());
                    }
                    eliminatedIdList.add(pb);
                }
            }
            boolean isPlayerUpdated = new PlayerDao().updatePlayersResult(eliminatedIdList, Constants.ROUND_BATTLE, SqlConnection.getInstance().getConnection());

            return isResultSaved && isPlayerUpdated;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return false;
    }

    public boolean isBattleResultExist(int seasonId, int coachId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new BattleDao().isBattleResultExist(seasonId, coachId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return false;
    }

    public void deleteBattleResults(int seasonId, int coachId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            new BattleDao().deleteBattleResults(seasonId, coachId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }
}
