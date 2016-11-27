package com.king.service.gdb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.Star;


public class SqliteDao {

	private final String TABLE_SEQUENCE = "sqlite_sequence";
	private final String TABLE_CONF = "conf";
	private final String TABLE_STAR = "star";
	private final String TABLE_RECORD_1V1 = "record_1v1";

	public SqliteDao() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection connect(String dbFile) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public String queryVersion(Connection connection) {
		String sql = "SELECT value FROM " + TABLE_CONF + " WHERE key='version'";
		Statement statement = null;
		String version = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				version = set.getString(1);
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
		return version;
	}

	public boolean updateVersion(Connection connection, String version) {
		String sql = "UPDATE " + TABLE_CONF + " SET value='" + version
				+ "' WHERE key='version'";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(sql);
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

	public List<RecordOneVOne> queryOneVOneRecords(Connection connection) {
		List<RecordOneVOne> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_RECORD_1V1;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			parseOneVOneRecords(connection, set, list);
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

	private void parseOneVOneRecords(Connection connection, ResultSet set, List<RecordOneVOne> list) throws SQLException {
		while (set.next()) {
			RecordOneVOne record = new RecordOneVOne();
			record.setId(set.getInt(1));
			record.setSequence(set.getInt(2));
			record.setSceneName(set.getString(3));
			record.setDirectory(set.getString(4));
			int star1Id = set.getInt(39);
			int star2Id = set.getInt(40);
			record.setStar1(queryStarById(connection, star1Id));
			record.setStar2(queryStarById(connection, star2Id));
			record.setName(set.getString(7));
			record.setHDLevel(set.getInt(8));
			record.setScore(set.getInt(9));
			record.setScoreFeel(set.getInt(10));
			record.setScoreStar1(set.getInt(11));
			record.setScoreStar2(set.getInt(12));
			record.setScoreStarC1(set.getInt(13));
			record.setScoreStarC2(set.getInt(14));
			record.setScoreRhythm(set.getInt(15));
			record.setScoreForePlay(set.getInt(16));
			record.setScoreRim(set.getInt(17));
			record.setScoreBJob(set.getInt(18));
			record.setScoreFkType1(set.getInt(19));
			record.setScoreFkType2(set.getInt(20));
			record.setScoreFkType3(set.getInt(21));
			record.setScoreFkType4(set.getInt(22));
			record.setScoreFkType5(set.getInt(23));
			record.setScoreFkType6(set.getInt(24));
			record.setScoreCum(set.getInt(25));
			record.setScoreScene(set.getInt(26));
			record.setScoreStory(set.getInt(27));
			record.setScoreNoCond(set.getInt(28));
			record.setScoreCShow(set.getInt(29));
			record.setScoreFoot(set.getInt(30));
			record.setScoreSpeicial(set.getInt(31));
			record.setScoreFk(set.getInt(32));
			record.setRateFkType1(Integer.parseInt(set.getString(33)));
			record.setRateFkType2(Integer.parseInt(set.getString(34)));
			record.setRateFkType3(Integer.parseInt(set.getString(35)));
			record.setRateFkType4(Integer.parseInt(set.getString(36)));
			record.setRateFkType5(Integer.parseInt(set.getString(37)));
			record.setRateFkType6(Integer.parseInt(set.getString(38)));
			record.setLastModifyTime(set.getLong(41));
			list.add(record);
		}
	}

	public boolean insertRecord(Connection connection, RecordOneVOne record) {
		StringBuffer buffer = new StringBuffer("INSERT INTO ");
		buffer.append(TABLE_RECORD_1V1)
				.append("(sequence,scene,directory,partner1,partner2,name,HDLevel,score")
				.append(",scoreFeel,scoreStar1,scoreStar2,scoreStarC1,scoreStarC2,scoreRhythm")
				.append(",scoreForePlay,scoreRim,scoreBJob,scoreFkType1,scoreFkType2,scoreFkType3")
				.append(",scoreFkType4,scoreFkType5,scoreFkType6,scoreCum,scoreScene,scoreStory")
				.append(",scoreNoCond,scoreCShow,scoreFoot,scoreSpecial,scoreFk,rateFkType1,rateFkType2")
				.append(",rateFkType3,rateFkType4,rateFkType5,rateFkType6,star1_id,star2_id,lastModifyDate)")
				.append(" VALUES(?");
		for (int i = 0; i < 39; i ++) {
			buffer.append(",?");
		}
		buffer.append(")");
		String sql = buffer.toString();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, record.getSequence());
			stmt.setString(2, record.getSceneName());
			stmt.setString(3, record.getDirectory());
			stmt.setString(4, record.getStar1().getName());
			stmt.setString(5, record.getStar2().getName());
			stmt.setString(6, record.getName());
			stmt.setInt(7, record.getHDLevel());
			stmt.setInt(8, record.getScore());
			stmt.setInt(9, record.getScoreFeel());
			stmt.setInt(10, record.getScoreStar1());
			stmt.setInt(11, record.getScoreStar2());
			stmt.setInt(12, record.getScoreStarC1());
			stmt.setInt(13, record.getScoreStarC2());
			stmt.setInt(14, record.getScoreRhythm());
			stmt.setInt(15, record.getScoreForePlay());
			stmt.setInt(16, record.getScoreRim());
			stmt.setInt(17, record.getScoreBJob());
			stmt.setInt(18, record.getScoreFkType1());
			stmt.setInt(19, record.getScoreFkType2());
			stmt.setInt(20, record.getScoreFkType3());
			stmt.setInt(21, record.getScoreFkType4());
			stmt.setInt(22, record.getScoreFkType5());
			stmt.setInt(23, record.getScoreFkType6());
			stmt.setInt(24, record.getScoreCum());
			stmt.setInt(25, record.getScoreScene());
			stmt.setInt(26, record.getScoreStory());
			stmt.setInt(27, record.getScoreNoCond());
			stmt.setInt(28, record.getScoreCShow());
			stmt.setInt(29, record.getScoreFoot());
			stmt.setInt(30, record.getScoreSpeicial());
			stmt.setInt(31, record.getScoreFk());
			stmt.setString(32, "" + record.getRateFkType1());
			stmt.setString(33, "" + record.getRateFkType2());
			stmt.setString(34, "" + record.getRateFkType3());
			stmt.setString(35, "" + record.getRateFkType4());
			stmt.setString(36, "" + record.getRateFkType5());
			stmt.setString(37, "" + record.getRateFkType6());
			stmt.setInt(38, record.getStar1().getId());
			stmt.setInt(39, record.getStar2().getId());
			stmt.setLong(40, record.getLastModifyTime());
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

	public Star queryStarByName(Connection connection, String name) {
		// 带"'"号的要特殊处理
		if (name.contains("'")) {
			name = name.replace("'", "''");
		}
		String sql = "SELECT * FROM " + TABLE_STAR + " WHERE name='" + name + "'";
		Statement statement = null;
		Star star = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				star = new Star();
				star.setId(set.getInt(1));
				star.setName(set.getString(2));
				star.setRecordNumber(set.getInt(3));
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
		return star;
	}

	public boolean insertStar(Connection connection, Star star) {
		String sql = "INSERT INTO " + TABLE_STAR + "(name) VALUES(?)";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, star.getName());
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

	public boolean updateStar(Connection connection, Star star) {
		// 带"'"号的要特殊处理
		String name = star.getName();
		if (name.contains("'")) {
			name = name.replace("'", "''");
		}
		String sql = "UPDATE " + TABLE_STAR + " SET name='" + name
				+ "' ,records=" + star.getRecordList().size() + " WHERE id=" + star.getId();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(sql);
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

	public Star queryStarById(Connection connection, int id) {
		String sql = "SELECT * FROM " + TABLE_STAR + " WHERE id=" + id;
		Statement statement = null;
		Star star = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				star = new Star();
				star.setId(set.getInt(1));
				star.setName(set.getString(2));
				star.setRecordNumber(set.getInt(3));
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
		return star;
	}

	public List<Star> queryAllStars(Connection connection) {
		List<Star> list = new ArrayList<>();
		String sql = "SELECT id,name,records FROM " + TABLE_STAR;
		Statement statement = null;
		Star star = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				star = new Star();
				star.setId(set.getInt(1));
				star.setName(set.getString(2));
				star.setRecordNumber(set.getInt(3));
				list.add(star);
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
		return list;
	}

	public int queryLastStarSequence(Connection connection) {
		String sql = "SELECT * FROM " + TABLE_SEQUENCE + " WHERE name='" + TABLE_STAR + "'";
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

	public int queryLastRecordSequence(Connection connection) {
		String sql = "SELECT * FROM " + TABLE_SEQUENCE + " WHERE name='" + TABLE_RECORD_1V1 + "'";
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

	public void loadStarRecords(Connection connection, Star star) {
		Statement stmt = null;
		List<Record> rList = new ArrayList<>();

		try {
			// load 1v1 records
			String sql = "SELECT * FROM " + TABLE_RECORD_1V1 + " WHERE star1_id=" + star.getId() + " OR star2_id=" + star.getId();
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			List<RecordOneVOne> list = new ArrayList<>();

			parseOneVOneRecords(connection, set, list);

			for (RecordOneVOne record:list) {
				rList.add(record);
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

		star.setRecordList(rList);
	}

	/**
	 * 加载star对应的record数量
	 * @param connection
	 * @param star
     */
	public void loadStarRecordNumber(Connection connection, Star star) {
		if (star == null) {
			return;
		}
		String sql = "SELECT COUNT(id) FROM " + TABLE_RECORD_1V1 + " WHERE star1_id=" + star.getId() + " OR star2_id=" + star.getId();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				star.setRecordNumber(set.getInt(1));
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
	}

	public void clearTableRecord1v1(Connection connection) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + TABLE_RECORD_1V1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void clearTableStar(Connection connection) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + TABLE_STAR);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void clearTableSequence(Connection connection) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + TABLE_SEQUENCE);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
