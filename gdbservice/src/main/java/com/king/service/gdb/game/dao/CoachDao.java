package com.king.service.gdb.game.dao;

import com.king.service.gdb.game.Constants;
import com.king.service.gdb.game.bean.CoachBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/15 0015.
 */

public class CoachDao {
    public List<CoachBean> queryCoachList(Connection connection) {
        List<CoachBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TABLE_COACH;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                CoachBean bean = parseCoachBean(set);
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

    public CoachBean queryCoachBean(int id, Connection connection) {
        CoachBean bean = null;
        String sql = "SELECT * FROM " + Constants.TABLE_COACH + " WHERE _id=" + id;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            if (set.next()) {
                bean = parseCoachBean(set);
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

    private CoachBean parseCoachBean(ResultSet set) throws SQLException {
        CoachBean bean = new CoachBean();
        bean.setId(set.getInt(1));
        bean.setName(set.getString(2));
        bean.setSeasonIds(set.getString(3));
        bean.setBestSeasonId(set.getInt(4));
        bean.setSeasonCount(set.getInt(5));
        bean.setType(set.getString(6));
        bean.setImagePath(set.getString(7));
        return bean;
    }
    public boolean updateCoach(CoachBean bean, Connection connection) {

        StringBuffer buffer = new StringBuffer("UPDATE ");
        buffer.append(Constants.TABLE_COACH).append(" SET _name='").append(bean.getName())
                .append("' ,_seasonIds='").append(bean.getSeasonIds())
                .append("', _bestSeasonId=").append(bean.getBestSeasonId())
                .append(", _seasonCount=").append(bean.getSeasonCount())
                .append(", _type='").append(bean.getType())
                .append("' ,_imagePath='").append(bean.getImagePath())
                .append("' WHERE _id=").append(bean.getId());
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(buffer.toString());
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

    public boolean updateCoaches(List<CoachBean> list, Connection connection) {

        Statement stmt;
        try {
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        for (CoachBean bean:list) {
            StringBuffer buffer = new StringBuffer("UPDATE ");
            buffer.append(Constants.TABLE_COACH).append(" SET _name='").append(bean.getName())
                    .append("' ,_seasonIds='").append(bean.getSeasonIds())
                    .append("', _bestSeasonId=").append(bean.getBestSeasonId())
                    .append(", _seasonCount=").append(bean.getSeasonCount())
                    .append(", _type='").append(bean.getType())
                    .append("' ,_imagePath='").append(bean.getImagePath())
                    .append("' WHERE _id=").append(bean.getId());
            try {
                stmt.executeUpdate(buffer.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertCoach(CoachBean bean, Connection connection) {
        String sql = "INSERT INTO " + Constants.TABLE_COACH +
                "(_name,_seasonIds,_bestSeasonId,_seasonCount,_type,_imagePath) VALUES(?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, bean.getName());
            stmt.setString(2, bean.getSeasonIds());
            stmt.setInt(3, bean.getBestSeasonId());
            stmt.setInt(4, bean.getSeasonCount());
            stmt.setString(5, bean.getType());
            stmt.setString(6, bean.getImagePath());
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

    public int queryLastCoachSequence(Connection connection) {
        String sql = "SELECT * FROM " + Constants.TABLE_SEQUENCE + " WHERE name='" + Constants.TABLE_COACH + "'";
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

}
