package com.king.service.gdb.game.dao;

import com.king.service.gdb.game.Constants;
import com.king.service.gdb.game.bean.BattleBean;

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
 * <p/>创建时间: 2017/2/8 11:25
 */
public class BattleDao {
    public List<BattleBean> queryBattleList(int seasonId, int coachId, Connection connection) {
        List<BattleBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TABLE_BATTLE + " WHERE _seasonId=" + seasonId
                + " AND _coachId=" + coachId;
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
}
