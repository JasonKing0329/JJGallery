package com.king.service.gdb.game.dao;

import com.king.service.gdb.game.Constants;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class SeasonDao {

    public List<SeasonBean> querySeasonList(Connection connection) {
        List<SeasonBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TABLE_SEASON;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                SeasonBean bean = parseSeasonBean(set);
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

    public SeasonBean querySeansonBean(int id, Connection connection) {
        SeasonBean bean = null;
        String sql = "SELECT * FROM " + Constants.TABLE_SEASON + " WHERE _id=" + id;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            if (set.next()) {
                bean = parseSeasonBean(set);
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

    private SeasonBean parseSeasonBean(ResultSet set) throws SQLException {
        SeasonBean bean = new SeasonBean();
        bean.setId(set.getInt(1));
        bean.setSequence(set.getInt(2));
        bean.setName(set.getString(3));
        bean.setMatchRule(set.getInt(4));
        bean.setCoachId1(set.getInt(5));
        bean.setCoachId2(set.getInt(6));
        bean.setCoachId3(set.getInt(7));
        bean.setCoachId4(set.getInt(8));
        bean.setCoverPath(set.getString(9));
        return bean;
    }

    public boolean updateSeason(SeasonBean seasonBean, Connection connection) {

        boolean result = false;
        StringBuffer buffer = new StringBuffer("UPDATE ");
        buffer.append(Constants.TABLE_SEASON).append(" SET _sequence=").append(seasonBean.getSequence())
                .append(" ,_name='").append(seasonBean.getName())
                .append("', _matchRule=").append(seasonBean.getMatchRule())
                .append(", _coachId1=").append(seasonBean.getCoachId1())
                .append(", _coachId2=").append(seasonBean.getCoachId2())
                .append(", _coachId3=").append(seasonBean.getCoachId3())
                .append(", _coachId4=").append(seasonBean.getCoachId4())
                .append(" ,_coverPath='").append(seasonBean.getCoverPath())
                .append("' WHERE _id=").append(seasonBean.getId());
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(buffer.toString());
            result = true;
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

    public boolean insertSeason(SeasonBean seasonBean, Connection connection) {
        String sql = "INSERT INTO " + Constants.TABLE_SEASON +
                "(_sequence,_name,_matchRule,_coachId1,_coachId2,_coachId3,_coachId4,_coverPath) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, seasonBean.getSequence());
            stmt.setString(2, seasonBean.getName());
            stmt.setInt(3, seasonBean.getMatchRule());
            stmt.setInt(4, seasonBean.getCoachId1());
            stmt.setInt(5, seasonBean.getCoachId2());
            stmt.setInt(6, seasonBean.getCoachId3());
            stmt.setInt(7, seasonBean.getCoachId4());
            stmt.setString(8, seasonBean.getCoverPath());
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

    public int queryLastSeasonSequence(Connection connection) {
        String sql = "SELECT * FROM " + Constants.TABLE_SEQUENCE + " WHERE name='" + Constants.TABLE_SEASON + "'";
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

    public String queryCoachSeasonIds(CoachBean coach, Connection connection) {
        String seasonIds = null;
        String sql = "SELECT _id FROM " + Constants.TABLE_SEASON
                + " WHERE _coachId1=" + coach.getId()
                + " OR _coachId2=" + coach.getId()
                + " OR _coachId3=" + coach.getId()
                + " OR _coachId4=" + coach.getId();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            int count = 0;
            while (set.next()) {
                if (seasonIds == null) {
                    seasonIds = String.valueOf(set.getInt(1));
                }
                else {
                    seasonIds = seasonIds.concat(",").concat(String.valueOf(set.getInt(1)));
                }
                count ++;
            }
            coach.setSeasonCount(count);
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
        return seasonIds;
    }

    public void deleteSeason(int seasonId, Connection connection) {
        String sql = "DELETE FROM " + Constants.TABLE_SEASON + " WHERE _id=" + seasonId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
