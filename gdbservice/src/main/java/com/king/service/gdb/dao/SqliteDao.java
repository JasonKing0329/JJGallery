package com.king.service.gdb.dao;

import android.text.TextUtils;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.king.service.gdb.bean.FavorBean;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordOneVOne;
import com.king.service.gdb.bean.SceneBean;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.bean.StarCountBean;


public class SqliteDao {

	private final String TABLE_SEQUENCE = "sqlite_sequence";
	private final String TABLE_CONF = "conf";
	private final String TABLE_STAR = "star";
	private final String TABLE_RECORD_1V1 = "record_1v1";
	private final String TABLE_FAVOR = "favor";

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

	private void parseOneVOneRecords(Connection connection, ResultSet set, List list) throws SQLException {
		while (set.next()) {
			RecordOneVOne record = new RecordOneVOne();
			record.setId(set.getInt(1));
			record.setSequence(set.getInt(2));
			record.setSceneName(set.getString(3));
			record.setDirectory(set.getString(4));
			record.setName(set.getString(7));
			record.setHDLevel(set.getInt(8));
			record.setScore(set.getInt(9));
			record.setScoreBasic(set.getInt(10));
			record.setScoreExtra(set.getInt(11));
			record.setScoreFeel(set.getInt(12));
			record.setScoreStar1(set.getInt(13));
			record.setScoreStar2(set.getInt(14));
			record.setScoreStar(set.getInt(15));
			record.setScoreStarC1(set.getInt(16));
			record.setScoreStarC2(set.getInt(17));
			record.setScoreStarC(set.getInt(18));
			record.setScoreRhythm(set.getInt(19));
			record.setScoreForePlay(set.getInt(20));
			record.setScoreBJob(set.getInt(21));
			record.setScoreFkType1(set.getInt(22));
			record.setScoreFkType2(set.getInt(23));
			record.setScoreFkType3(set.getInt(24));
			record.setScoreFkType4(set.getInt(25));
			record.setScoreFkType5(set.getInt(26));
			record.setScoreFkType6(set.getInt(27));
			record.setScoreFk(set.getInt(28));
			record.setScoreCum(set.getInt(29));
			record.setScoreScene(set.getInt(30));
			record.setScoreStory(set.getInt(31));
			record.setScoreNoCond(set.getInt(32));
			record.setScoreCShow(set.getInt(33));
			record.setScoreRim(set.getInt(34));
			record.setScoreSpeicial(set.getInt(35));
			record.setLastModifyTime(set.getLong(38));
			record.setSpecialDesc(set.getString(39));
			record.setDeprecated(Integer.parseInt(set.getString(40)));
			int star1Id = set.getInt(36);
			int star2Id = set.getInt(37);
			record.setStar1(queryStarById(connection, star1Id));
			record.setStar2(queryStarById(connection, star2Id));
			list.add(record);
		}
	}

	public boolean insertRecord(Connection connection, RecordOneVOne record) {
		StringBuffer buffer = new StringBuffer("INSERT INTO ");
		buffer.append(TABLE_RECORD_1V1)
				.append("(sequence,scene,directory,partner1,partner2,name,HDLevel,score,scoreBasic,scoreExtra")
				.append(",scoreFeel,scoreStar1,scoreStar2,scoreStar,scoreStarC1,scoreStarC2,scoreStarC,scoreRhythm")
				.append(",scoreForePlay,scoreBJob,scoreFkType1,scoreFkType2,scoreFkType3")
				.append(",scoreFkType4,scoreFkType5,scoreFkType6,scoreFk,scoreCum,scoreScene,scoreStory")
				.append(",scoreNoCond,scoreCShow,scoreRim,scoreSpecial")
				.append(",star1_id,star2_id,lastModifyDate,specialDesc,deprecated)")
				.append(" VALUES(?");
		for (int i = 0; i < 38; i ++) {
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
			stmt.setInt(9, record.getScoreBasic());
			stmt.setInt(10, record.getScoreExtra());
			stmt.setInt(11, record.getScoreFeel());
			stmt.setInt(12, record.getScoreStar1());
			stmt.setInt(13, record.getScoreStar2());
			stmt.setInt(14, record.getScoreStar());
			stmt.setInt(15, record.getScoreStarC1());
			stmt.setInt(16, record.getScoreStarC2());
			stmt.setInt(17, record.getScoreStarC());
			stmt.setInt(18, record.getScoreRhythm());
			stmt.setInt(19, record.getScoreForePlay());
			stmt.setInt(20, record.getScoreBJob());
			stmt.setInt(21, record.getScoreFkType1());
			stmt.setInt(22, record.getScoreFkType2());
			stmt.setInt(23, record.getScoreFkType3());
			stmt.setInt(24, record.getScoreFkType4());
			stmt.setInt(25, record.getScoreFkType5());
			stmt.setInt(26, record.getScoreFkType6());
			stmt.setInt(27, record.getScoreFk());
			stmt.setInt(28, record.getScoreCum());
			stmt.setInt(29, record.getScoreScene());
			stmt.setInt(30, record.getScoreStory());
			stmt.setInt(31, record.getScoreNoCond());
			stmt.setInt(32, record.getScoreCShow());
			stmt.setInt(33, record.getScoreRim());
			stmt.setInt(34, record.getScoreSpeicial());
			stmt.setInt(35, record.getStar1().getId());
			stmt.setInt(36, record.getStar2().getId());
			stmt.setLong(37, record.getLastModifyTime());
			stmt.setString(38, record.getSpecialDesc());
			stmt.setInt(39, record.getDeprecated());
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

	private Star parseStar(ResultSet set) throws SQLException {

		Star star = new Star();
		star.setId(set.getInt(1));
		star.setName(set.getString(2));
		star.setRecordNumber(set.getInt(3));
		star.setBeTop(set.getInt(4));
		star.setBeBottom(set.getInt(5));
		star.setAverage(set.getFloat(6));
		star.setMax(set.getInt(7));
		star.setMin(set.getInt(8));
		star.setcAverage(set.getFloat(9));
		star.setcMax(set.getInt(10));
		star.setcMin(set.getInt(11));
		return star;
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
				star = parseStar(set);
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
		StringBuffer buffer = new StringBuffer("UPDATE ");
		buffer.append(TABLE_STAR).append(" SET name='").append(name)
				.append("' ,records=").append(star.getRecordList().size())
				.append(", betop=").append(star.getBeTop())
				.append(", bebottom=").append(star.getBeBottom())
				.append(", average=").append(star.getAverage())
				.append(", max=").append(star.getMax())
				.append(", min=").append(star.getMin())
				.append(", caverage=").append(star.getcAverage())
				.append(", cmax=").append(star.getcMax())
				.append(", cmin=").append(star.getcMin())
				.append(" WHERE id=").append(star.getId());
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

	public Star queryStarById(Connection connection, int id) {
		String sql = "SELECT * FROM " + TABLE_STAR + " WHERE id=" + id;
		Statement statement = null;
		Star star = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				star = parseStar(set);
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
		return queryAllStars(connection, null);
	}

	public List<Star> queryAllStars(Connection connection, String where) {
		List<Star> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_STAR;
		if (where != null) {
			sql = sql + " WHERE " + where;
		}
		Statement statement = null;
		Star star = null;
		try {
			statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				star = parseStar(set);
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
	/**
	 * 作为保留项目，star每次只更新
	 * @param connection
	 */
	@Deprecated
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

	public List<RecordOneVOne> queryLatestRecords(int from, int number, Connection connection) {
		List<RecordOneVOne> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_RECORD_1V1 + " ORDER BY lastModifyDate DESC LIMIT " + from + "," + number;
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

	public StarCountBean queryStarCount(Connection connection) {
		StarCountBean bean = new StarCountBean();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "SELECT COUNT(id) FROM " + TABLE_STAR;
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				bean.setAllNumber(set.getInt(1));
			}
			set.close();
			sql = "SELECT COUNT(id) FROM " + TABLE_STAR + " WHERE betop>0 AND bebottom=0";
			set = statement.executeQuery(sql);
			if (set.next()) {
				bean.setTopNumber(set.getInt(1));
			}
			set.close();
			sql = "SELECT COUNT(id) FROM " + TABLE_STAR + " WHERE bebottom>0 AND betop=0";
			set = statement.executeQuery(sql);
			if (set.next()) {
				bean.setBottomNumber(set.getInt(1));
			}
			set.close();
			sql = "SELECT COUNT(id) FROM " + TABLE_STAR + " WHERE bebottom>0 and betop>0";
			set = statement.executeQuery(sql);
			if (set.next()) {
				bean.setHalfNumber(set.getInt(1));
			}
			set.close();
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
		return bean;
	}

	public StarCountBean queryFavorStarCount(Connection connection) {
		StarCountBean bean = new StarCountBean();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "SELECT COUNT(id) FROM " + TABLE_FAVOR;
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				bean.setAllNumber(set.getInt(1));
			}
			set.close();
			sql = "SELECT COUNT(a.id) FROM " + TABLE_STAR + " a, " + TABLE_FAVOR + " b WHERE a.id=b.star_id AND a.betop>0 AND a.bebottom=0";
			set = statement.executeQuery(sql);
			if (set.next()) {
				bean.setTopNumber(set.getInt(1));
			}
			set.close();
			sql = "SELECT COUNT(a.id) FROM " + TABLE_STAR + " a, " + TABLE_FAVOR + " b WHERE a.id=b.star_id AND a.bebottom>0 AND a.betop=0";
			set = statement.executeQuery(sql);
			if (set.next()) {
				bean.setBottomNumber(set.getInt(1));
			}
			set.close();
			sql = "SELECT COUNT(a.id) FROM " + TABLE_STAR + " a, " + TABLE_FAVOR + " b WHERE a.id=b.star_id AND a.bebottom>0 AND a.betop>0";
			set = statement.executeQuery(sql);
			if (set.next()) {
				bean.setHalfNumber(set.getInt(1));
			}
			set.close();
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
		return bean;
	}

    public List<FavorBean> queryFavors(Connection connection) {
		List<FavorBean> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_FAVOR + " ORDER BY favor DESC";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			parseFavorBean(set, list);
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

	public List<FavorBean> queryTopFavors(int number, Connection connection) {
		List<FavorBean> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_FAVOR + " ORDER BY favor DESC LIMIT " + number;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			parseFavorBean(set, list);
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

	private void parseFavorBean(ResultSet set, List<FavorBean> list) throws SQLException {
		while (set.next()) {
			FavorBean bean = new FavorBean();
			bean.setId(set.getInt(1));
			bean.setStarId(set.getInt(2));
			bean.setStarName(set.getString(3));
			bean.setFavor(set.getInt(4));
			list.add(bean);
		}
	}

	public boolean isStarFavor(Connection connection, int starId) {
		String sql = "SELECT favor FROM " + TABLE_FAVOR + " WHERE star_id=" + starId;
		boolean isExist = false;
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			if (set.next()) {
				if (set.getInt(1) > 0) {
					isExist = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	public void saveFavor(Connection connection, FavorBean bean) {
		String sql;
		boolean isExist = isStarFavor(connection, bean.getId());
		if (isExist) {
			sql = "UPDATE " + TABLE_FAVOR + " SET star_id=?, star_name=?, favor=? WHERE star_id=?";
		}
		else {
			sql = "INSERT INTO " + TABLE_FAVOR + "(star_id, star_name, favor) VALUES(?, ?, ?)";
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, bean.getStarId());
			pstmt.setString(2, bean.getStarName());
			pstmt.setInt(3, bean.getFavor());
			if (isExist) {
				pstmt.setInt(4, bean.getStarId());
			}
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveFavorList(Connection connection, List<FavorBean> favorList) {
		String  sql = "INSERT INTO " + TABLE_FAVOR + "(star_id, star_name, favor) VALUES(?, ?, ?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			for (FavorBean bean:favorList) {
				pstmt.setInt(1, bean.getStarId());
				pstmt.setString(2, bean.getStarName());
				pstmt.setInt(3, bean.getFavor());
				pstmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

    public List<RecordOneVOne> getRandomRecords(int number, Connection connection) {
		List<RecordOneVOne> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_RECORD_1V1 + " ORDER BY RANDOM() limit " + number;
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

	/**
	 * name符合关键词nameLike，按sortColumn desc/asc 排序，从第from条记录开始取number条记录
	 * @param sortColumn
	 * @param desc
	 * @param includeDeprecated
	 * @param from
	 * @param number
	 * @param connection
	 * @param nameLike
	 * @return
	 */
    public List<Record> getRecords(String sortColumn, boolean desc, boolean includeDeprecated, int from, int number, String nameLike, String scene, Connection connection) {
		List<Record> list = new ArrayList<>();
		StringBuffer buffer = new StringBuffer("SELECT * FROM ");
		buffer.append(TABLE_RECORD_1V1).append(" WHERE 1=1");
		if (!TextUtils.isEmpty(nameLike)) {
			buffer.append(" AND name LIKE '%").append(nameLike).append("%'");
		}
		if (!includeDeprecated) {
			buffer.append(" AND deprecated=0");
		}
		if (!TextUtils.isEmpty(scene)) {
			buffer.append(" AND scene='").append(scene).append("'");
		}
		if (!TextUtils.isEmpty(sortColumn)) {
			buffer.append(" ORDER BY ").append(sortColumn);
		}
		buffer.append(" ").append(desc ? "DESC":"ASC");
		if (from != -1 && number != -1) {
			buffer.append(" LIMIT ").append(from).append(",").append(number);
		}
		String sql = buffer.toString();
		Log.e("SqliteDao", "getRecords " + sql);
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

	public List<SceneBean> getScenes(Connection connection) {
		List<SceneBean> list = new ArrayList<>();
		String sql = "SELECT scene, COUNT(scene) AS count, AVG(score) AS average, MAX(score) AS max FROM " + TABLE_RECORD_1V1 + " GROUP BY scene ORDER BY scene";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			parseSceneBean(set, list);
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

	private void parseSceneBean(ResultSet set, List<SceneBean> list) throws SQLException {
		while (set.next()) {
			SceneBean bean = new SceneBean();
			bean.setScene(set.getString(1));
			bean.setNumber(set.getInt(2));
			bean.setAverage(set.getFloat(3));
			bean.setMax(set.getInt(4));
			list.add(bean);
		}
	}

	public List<Record> getRecordsByScene(String scene, Connection connection) {
		List<Record> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_RECORD_1V1 + " WHERE scene='" + scene + "' ORDER BY name";
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

	public boolean isFavorTableExist(Connection connection) {
		String sql = "SELECT COUNT(*) FROM sqlite_master where type='table' and name='" + TABLE_FAVOR + "'";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			if (set.next()) {
				int count = set.getInt(1);
				if (count != 0) {
					return true;
				}
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

	public void createFavorTable(Connection connection) {
		String sql = "CREATE TABLE \"favor\" (\n" +
				"    \"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
				"    \"star_id\" INTEGER,\n" +
				"    \"star_name\" TEXT,\n" +
				"    \"favor\" INTEGER\n" +
				")";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
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
