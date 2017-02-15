package com.king.service.gdb.game.dao;

import com.king.service.gdb.game.Constants;
import com.king.service.gdb.game.bean.CrossBean;
import com.king.service.gdb.game.bean.CrossResultBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/14 15:50
 */
public class CrossDao {
    public List<CrossBean> queryCrossList(int seasonId, int coach1Id, int coach2Id, Connection connection) {
        List<CrossBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TABLE_CROSS + " WHERE _seasonId=" + seasonId
                + " AND _coachId1=" + coach1Id + " AND _coachId2=" + coach2Id + " ORDER BY _round ASC";
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                CrossBean bean = parseCrossBean(set);
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

    private CrossBean parseCrossBean(ResultSet set) throws SQLException {
        CrossBean bean = new CrossBean();
        bean.setId(set.getInt(1));
        bean.setSeasonId(set.getInt(2));
        bean.setCoachId(set.getInt(3));
        bean.setCoach2Id(set.getInt(4));
        bean.setRematchFlag(set.getInt(5));
        bean.setTopPlayerId(set.getInt(6));
        bean.setBottomPlayerId(set.getInt(7));
        bean.setRound(set.getInt(8));
        bean.setScene(set.getString(9));
        bean.setScore(set.getString(10));
        return bean;
    }

    public void inserCrossBean(CrossBean bean, Connection connection) {
        String sql = "INSERT INTO " + Constants.TABLE_CROSS +
                "(_seasonId,_coachId1,_coachId2,_rematchFlag,_tPlayerId,_bPlayerId,_round,_scene,_score) VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, bean.getSeasonId());
            stmt.setInt(2, bean.getCoachId());
            stmt.setInt(3, bean.getCoach2Id());
            stmt.setInt(4, bean.getRematchFlag());
            stmt.setInt(5, bean.getTopPlayerId());
            stmt.setInt(6, bean.getBottomPlayerId());
            stmt.setInt(7, bean.getRound());
            stmt.setString(8, bean.getScene());
            stmt.setString(9, bean.getScore());
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

    public void updateCrossBean(CrossBean bean, Connection connection) {
        StringBuffer buffer = new StringBuffer("UPDATE ");
        buffer.append(Constants.TABLE_CROSS).append(" SET _seasonId=").append(bean.getSeasonId())
                .append(",_coachId1=").append(bean.getCoachId())
                .append(", _coachId2=").append(bean.getCoach2Id())
                .append(", _rematchFlag=").append(bean.getRematchFlag())
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

    public void deleteCross(int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_CROSS + " WHERE _id=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int queryLastCrossSequence(Connection connection) {
        String sql = "SELECT * FROM " + Constants.TABLE_SEQUENCE + " WHERE name='" + Constants.TABLE_CROSS + "'";
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

    public boolean isCrossResultExist(int seasonId, int coachId1, int coachId2, Connection connection) {
        String sql = "SELECT _id FROM " + Constants.TABLE_CROSS_RESULT + " WHERE _seasonId=" + seasonId
                + " AND (_coachId=" + coachId1 + " OR _coachId=" + coachId2 + ")";
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

    public void deleteCrossResults(int seasonId, int coachId1, int coachId2, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_CROSS_RESULT + " WHERE _seasonId=" + seasonId
                + " AND (_coachId=" + coachId1 + " OR _coachId=" + coachId2 + ")";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean saveCrossResultBeans(List<CrossResultBean> datas, Connection connection) {
        boolean result = true;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO " + Constants.TABLE_CROSS_RESULT +
                "(_seasonId,_coachId,_rematchFlag,_rank,_score,_playerId,_type) VALUES(?,?,?,?,?,?,?)";
        try {
            for (CrossResultBean bean:datas) {
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, bean.getSeasonId());
                stmt.setInt(2, bean.getCoachId());
                stmt.setInt(3, bean.getRematchFlag());
                stmt.setInt(4, bean.getRank());
                stmt.setInt(5, bean.getScore());
                stmt.setInt(6, bean.getPlayerId());
                stmt.setInt(7, bean.getType());
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

    public void deletePlayer(int playerId, int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_CROSS + " WHERE _seasonId=" + seasonId + " AND (_tPlayerId=" + playerId + " OR _bPlayerId=" + playerId + ")";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlayerResult(int playerId, int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_CROSS_RESULT + " WHERE _seasonId=" + seasonId + " AND _playerId=" + playerId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSeason(int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_CROSS + " WHERE _seasonId=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSeasonResult(int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_CROSS_RESULT + " WHERE _seasonId=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
