package com.king.service.gdb.game.dao;

import com.king.service.gdb.game.Constants;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.BattleResultBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 查询season:coach对应的battle list，需要按照round进行升序排序
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:25
 */
public class BattleDao {
    public List<BattleBean> queryBattleList(int seasonId, int coachId, Connection connection) {
        List<BattleBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TABLE_BATTLE + " WHERE _seasonId=" + seasonId
                + " AND _coachId=" + coachId + " ORDER BY _round ASC";
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                BattleBean bean = parseBattleBean(set);
                list.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private BattleBean parseBattleBean(ResultSet set) throws SQLException {
        BattleBean bean = new BattleBean();
        bean.setId(set.getInt(1));
        bean.setSeasonId(set.getInt(2));
        bean.setCoachId(set.getInt(3));
        bean.setTopPlayerId(set.getInt(4));
        bean.setBottomPlayerId(set.getInt(5));
        bean.setRound(set.getInt(6));
        bean.setScene(set.getString(7));
        bean.setScore(set.getString(8));
        return bean;
    }

    public void inserBattleBean(BattleBean bean, Connection connection) {
        String sql = "INSERT INTO " + Constants.TABLE_BATTLE +
                "(_seasonId,_coachId,_tPlayerId,_bPlayerId,_round,_scene,_score) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, bean.getSeasonId());
            stmt.setInt(2, bean.getCoachId());
            stmt.setInt(3, bean.getTopPlayerId());
            stmt.setInt(4, bean.getBottomPlayerId());
            stmt.setInt(5, bean.getRound());
            stmt.setString(6, bean.getScene());
            stmt.setString(7, bean.getScore());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateBattleBean(BattleBean bean, Connection connection) {
        StringBuffer buffer = new StringBuffer("UPDATE ");
        buffer.append(Constants.TABLE_BATTLE).append(" SET _seasonId=").append(bean.getSeasonId())
                .append(",_coachId=").append(bean.getCoachId())
                .append(", _tPlayerId=").append(bean.getTopPlayerId())
                .append(", _bPlayerId=").append(bean.getBottomPlayerId())
                .append(", _round=").append(bean.getRound())
                .append(",_scene='").append(bean.getScene())
                .append("',_score='").append(bean.getScore())
                .append("' WHERE _id=").append(bean.getId());
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(buffer.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteBattle(int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_BATTLE + " WHERE _id=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int queryLastBattleSequence(Connection connection) {
        String sql = "SELECT * FROM " + Constants.TABLE_SEQUENCE + " WHERE name='" + Constants.TABLE_BATTLE + "'";
        Statement statement = null;
        int id = 0;
        try {
            statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);
            if (set.next()) {
                id = set.getInt(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public void deletePlayer(int playerId, int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_BATTLE + " WHERE _seasonId=" + seasonId + " AND (_tPlayerId=" + playerId + " OR _bPlayerId=" + playerId + ")";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlayerResult(int playerId, int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_BATTLE_RESULT + " WHERE _seasonId=" + seasonId + " AND _playerId=" + playerId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSeason(int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_BATTLE + " WHERE _seasonId=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSeasonResult(int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_BATTLE_RESULT + " WHERE _seasonId=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean saveBattleResultBeans(List<BattleResultBean> datas, Connection connection) {
        boolean result = true;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO " + Constants.TABLE_BATTLE_RESULT +
                "(_seasonId,_coachId,_rank,_score,_playerId,_type) VALUES(?,?,?,?,?,?)";
        try {
            for (BattleResultBean bean:datas) {
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, bean.getSeasonId());
                stmt.setInt(2, bean.getCoachId());
                stmt.setInt(3, bean.getRank());
                stmt.setInt(4, bean.getScore());
                stmt.setInt(5, bean.getPlayerId());
                stmt.setInt(6, bean.getType());
                stmt.executeUpdate();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 检查battle result记录是否已生成过
     * @param seasonId
     * @param coachId
     * @param connection
     * @return
     */
    public boolean isBattleResultExist(int seasonId, int coachId, Connection connection) {
        String sql = "SELECT _id FROM " + Constants.TABLE_BATTLE_RESULT + " WHERE _seasonId=" + seasonId
                + " AND _coachId=" + coachId;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public void deleteBattleResults(int seasonId, int coachId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_BATTLE_RESULT + " WHERE _seasonId=" + seasonId + " AND _coachId=" + coachId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * query battle result with rank <= rankMax
     * @param seasonId
     * @param coachId
     * @param rankMax
     * @param connection
     * @return
     */
    public List<BattleResultBean> queryBattleResultList(int seasonId, int coachId, int rankMax, int type, Connection connection) {
        List<BattleResultBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TABLE_BATTLE_RESULT + " WHERE _seasonId=" + seasonId
                + " AND _coachId=" + coachId + " AND _rank<=" + rankMax + " AND _type=" + type;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                BattleResultBean bean = parseBattleResultBean(set);
                list.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private BattleResultBean parseBattleResultBean(ResultSet set) throws SQLException {
        BattleResultBean bean = new BattleResultBean();
        bean.setId(set.getInt(1));
        bean.setSeasonId(set.getInt(2));
        bean.setCoachId(set.getInt(3));
        bean.setRank(set.getInt(4));
        bean.setScore(set.getInt(5));
        bean.setPlayerId(set.getInt(6));
        bean.setType(set.getInt(7));
        return bean;
    }

}
