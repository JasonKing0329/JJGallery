package com.king.service.gdb.game.dao;

import com.king.service.gdb.game.Constants;
import com.king.service.gdb.game.bean.PlayerBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public class PlayerDao {
    public PlayerBean queryPlayerBean(int id, Connection connection) {
        PlayerBean bean = null;
        String sql = "SELECT * FROM " + Constants.TABLE_PLAYER + " WHERE _id=" + id;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            if (set.next()) {
                bean = parsePlayerBean(set);
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
        return bean;
    }

    public PlayerBean queryPlayerBean(String name, Connection connection) {
        // name里带单引号在sqlite中要作为特殊符号转化
        if (name.contains("'")) {
            name = name.replace("'", "''");
        }
        PlayerBean bean = null;
        String sql = "SELECT * FROM " + Constants.TABLE_PLAYER + " WHERE _name='" + name + "'";
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            if (set.next()) {
                bean = parsePlayerBean(set);
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
        return bean;
    }

    private PlayerBean parsePlayerBean(ResultSet set) throws SQLException {
        PlayerBean bean = new PlayerBean();
        bean.setId(set.getInt(1));
        bean.setName(set.getString(2));
        bean.setTopSeasonId(set.getInt(3));
        bean.setTopRound(set.getString(4));
        bean.setTopCoachId(set.getInt(5));
        bean.setBottomSeasonId(set.getInt(6));
        bean.setBottomRound(set.getString(7));
        bean.setBottomCoachId(set.getInt(8));
        return bean;
    }

    public boolean insertPlayer(PlayerBean bean, Connection connection) {
        String sql = "INSERT INTO " + Constants.TABLE_PLAYER +
                "(_name,_tSeasonId,_tRound,_tCoachId,_bSeasonId,_bRound,_bCoachId) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, bean.getName());
            stmt.setInt(2, bean.getTopSeasonId());
            stmt.setString(3, bean.getTopRound());
            stmt.setInt(4, bean.getTopCoachId());
            stmt.setInt(5, bean.getBottomSeasonId());
            stmt.setString(6, bean.getBottomRound());
            stmt.setInt(7, bean.getBottomCoachId());
            stmt.executeUpdate();
            return true;
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
        return false;
    }

    /**
     *
     * @param bean
     * @param type 全部修改传-1，只修改_t部分传1，只修改_b部分传0
     * @param connection
     * @return
     */
    public boolean updatePlayer(PlayerBean bean, int type, Connection connection) {

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            StringBuffer buffer = new StringBuffer("UPDATE ");
            buffer.append(Constants.TABLE_PLAYER).append(" SET _name='").append(bean.getName()).append("'");
            if (type == 1 || type == -1) {
                buffer.append(", _tSeasonId=").append(bean.getTopSeasonId())
                        .append(", _tRound='").append(bean.getTopRound())
                        .append("', _tCoachId=").append(bean.getTopCoachId());
            }
            else if (type == 0 || type == -1) {
                buffer.append(", _bSeasonId=").append(bean.getBottomSeasonId())
                        .append(", _bRound='").append(bean.getBottomRound())
                        .append("', _bCoachId=").append(bean.getBottomRound());
            }
            buffer.append(" WHERE _id=").append(bean.getId());
            stmt.executeUpdate(buffer.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public int queryLastPlayerSequence(Connection connection) {
        String sql = "SELECT * FROM " + Constants.TABLE_SEQUENCE + " WHERE name='" + Constants.TABLE_PLAYER + "'";
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

    public void deletePlayer(int playerId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_PLAYER + " WHERE _id=" + playerId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSeason(int seasonId, Connection connection) {
        String sql1 = "UPDATE " + Constants.TABLE_PLAYER + " SET _tSeasonId=-1, _tRound=0, _tCoachId=-1 WHERE _tSeasonId=" + seasonId;
        String sql2 = "UPDATE " + Constants.TABLE_PLAYER + " SET _bSeasonId=-1, _bRound=0, _bCoachId=-1 WHERE _bSeasonId=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updatePlayersResult(List<PlayerBean> playerList, int round, Connection connection) {
        for (PlayerBean bean:playerList) {
            String sql = "UPDATE " + Constants.TABLE_PLAYER;
            String set;
            if (bean.getTopCoachId() > 0) {
                set = " SET _tRound=" + round;
            }
            else {
                set = " SET _bRound=" + round;
            }
            sql = sql + set + " WHERE _id=" + bean.getId();

            try {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
