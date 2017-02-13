package com.king.service.gdb.game.dao;

import com.king.service.gdb.game.Constants;
import com.king.service.gdb.game.bean.GroupBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public class GroupDao {
    public List<GroupBean> queryGroupList(int seasonId, Connection connection) {
        List<GroupBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TABLE_GROUP + " WHERE _seasonId=" + seasonId;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                GroupBean bean = parseGroupBean(set);
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

    public List<GroupBean> queryGroupList(int seasonId, int coachId, Connection connection) {
        List<GroupBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TABLE_GROUP + " WHERE _seasonId=" + seasonId
                + " AND _coachId=" + coachId;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                GroupBean bean = parseGroupBean(set);
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

    private GroupBean parseGroupBean(ResultSet set) throws SQLException {
        GroupBean bean = new GroupBean();
        bean.setId(set.getInt(1));
        bean.setSeasonId(set.getInt(2));
        bean.setCoachId(set.getInt(3));
        bean.setPlayerId(set.getInt(4));
        bean.setPlayerName(set.getString(5));
        bean.setType(set.getInt(6));
        return bean;
    }

    public void inserGroupBean(GroupBean bean, Connection connection) {
        String sql = "INSERT INTO " + Constants.TABLE_GROUP +
                "(_seasonId,_coachId,_playerId,_playerName,_type) VALUES(?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, bean.getSeasonId());
            stmt.setInt(2, bean.getCoachId());
            stmt.setInt(3, bean.getPlayerId());
            stmt.setString(4, bean.getPlayerName());
            stmt.setInt(5, bean.getType());
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

    public List<GroupBean> queryGroupBeanList(Connection connection) {
        List<GroupBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TABLE_GROUP;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                GroupBean bean = parseGroupBean(set);
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

    public void deletePlayer(int playerId, int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_GROUP + " WHERE _playerId=" + playerId + " AND _seasonId=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSeason(int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_GROUP + " WHERE _seasonId=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
