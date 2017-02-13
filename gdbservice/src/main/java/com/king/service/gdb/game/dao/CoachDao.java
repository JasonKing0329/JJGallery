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

    /**
     * 删除coach关联的seasonId记录
     * seasonIds字段在数据库中的设计是以“,”分隔每个seasonId的，最好的办法是先查询出全部bean,在bean中进行修改
     * coach中内容条数比较少，也不会耗时
     * @param seasonId
     * @param connection
     */
    public void deleteSeason(int seasonId, Connection connection) {
        // load all coach bean
        List<CoachBean> list = queryCoachList(connection);

        // update _seasonIds, _bestSeasonId, _seasonCount
        for (CoachBean bean:list) {
            try {
                String[] seasonIds = bean.getSeasonIds().split(",");
                List<String> idList = new ArrayList<>();
                for (String id:seasonIds) {
                    idList.add(id);
                }
                // remove seasonId
                boolean isRemoved = false;
                for (int i = 0; i < idList.size(); i ++) {
                    if (seasonId == Integer.parseInt(idList.get(i))) {
                        idList.remove(i);
                        isRemoved = true;
                        break;
                    }
                }

                // 不包含seasonId，直接略过
                if (!isRemoved) {
                    continue;
                }

                // 包含seasonId，更新字段
                // format new seasonIds
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < idList.size(); i ++) {
                    if (i == 0) {
                        buffer.append(idList.get(i));
                    }
                    else {
                        buffer.append(".").append(idList.get(i));
                    }
                }
                bean.setSeasonIds(buffer.toString());
                bean.setSeasonCount(bean.getSeasonCount() - 1);

                updateCoach(bean, connection);
                //FIXME _bestSeasonId暂时无法更新
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
