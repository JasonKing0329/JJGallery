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
import com.king.service.gdb.bean.RecordThree;
import com.king.service.gdb.bean.SceneBean;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.bean.StarCountBean;


public class SqliteDao {

	private final String TABLE_SEQUENCE = "sqlite_sequence";
	private final String TABLE_CONF = "conf";
	private final String TABLE_STAR = "star";
	private final String TABLE_RECORD_1V1 = "record_1v1";
	private final String TABLE_RECORD_3W = "record_3w";
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

	public List<RecordThree> queryLatest3WRecords(int from, int number, Connection connection) {
		List<RecordThree> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_RECORD_3W + " ORDER BY lastModifyDate DESC LIMIT " + from + "," + number;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			parse3WRecords(connection, set, list);
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

	public List<Star> getRandomStars(int number, Connection connection) {
		List<Star> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_STAR + " ORDER BY RANDOM() limit " + number;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			while (set.next()) {
				Star star = parseStar(set);
				list.add(star);
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

	public Record getRecordByName(String name, Connection connection) {
		// name里带单引号在sqlite中要作为特殊符号转化
		if (name.contains("'")) {
			name = name.replace("'", "''");
		}
		List<Record> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_RECORD_1V1 + " WHERE name='" + name + "'";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			parseOneVOneRecords(connection, set, list);
			return list.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			return null;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
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

	public List<RecordThree> query3WRecords(Connection connection) {
		List<RecordThree> list = new ArrayList<>();
		String sql = "SELECT * FROM " + TABLE_RECORD_1V1;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = stmt.executeQuery(sql);
			parse3WRecords(connection, set, list);
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

	private void parse3WRecords(Connection connection, ResultSet set, List list) throws SQLException {
		while (set.next()) {
			RecordThree record = new RecordThree();
			record.setId(set.getInt(1));
			record.setSequence(set.getInt(2));
			record.setSceneName(set.getString(3));
			record.setDirectory(set.getString(4));
			record.setStarTopName(set.getString(5));
			record.setStarBottomName(set.getString(6));
			record.setStarMixName(set.getString(7));
			record.setName(set.getString(8));
			record.setHDLevel(set.getInt(9));
			record.setScore(set.getInt(10));
			record.setScoreFeel(set.getInt(11));
			record.setScoreTop(set.getString(12));
			record.setScoreBottom(set.getString(13));
			record.setScoreMix(set.getString(14));
			record.setScoreStar(set.getInt(15));
			record.setScoreTopC(set.getString(16));
			record.setScoreBottomC(set.getString(17));
			record.setScoreMixC(set.getString(18));
			record.setScoreStarC(set.getInt(19));
			record.setScoreRhythm(set.getInt(20));
			record.setScoreForePlay(set.getInt(21));
			record.setScoreBJob(set.getInt(22));
			record.setScoreFkType1(set.getInt(23));
			record.setScoreFkType2(set.getInt(24));
			record.setScoreFkType3(set.getInt(25));
			record.setScoreFkType4(set.getInt(26));
			record.setScoreFkType5(set.getInt(27));
			record.setScoreFkType6(set.getInt(28));
			record.setScoreFkType7(set.getInt(29));
			record.setScoreFkType8(set.getInt(30));
			record.setScoreFk(set.getInt(31));
			record.setScoreCum(set.getInt(32));
			record.setScoreScene(set.getInt(33));
			record.setScoreStory(set.getInt(34));
			record.setScoreNoCond(set.getInt(35));
			record.setScoreCShow(set.getInt(36));
			record.setScoreRim(set.getInt(37));
			record.setScoreSpeicial(set.getInt(38));
			record.setStarTopId(set.getString(39));
			record.setStarBottomId(set.getString(40));
			record.setStarMixId(set.getString(41));
			record.setLastModifyTime(set.getLong(42));
			record.setSpecialDesc(set.getString(43));
			record.setDeprecated(set.getInt(44));

			String ids = record.getStarTopId();
			if (!TextUtils.isEmpty(ids)) {
				String[] starIds = ids.split(",");
				for (String startId:starIds) {
					Star star = queryStarById(connection, Integer.parseInt(startId));
					if (record.getStarTopList() == null) {
						record.setStarTopList(new ArrayList<Star>());
					}
					record.getStarTopList().add(star);
				}
			}
			ids = record.getStarBottomId();
			if (!TextUtils.isEmpty(ids)) {
				String[] starIds = ids.split(",");
				for (String startId:starIds) {
					Star star = queryStarById(connection, Integer.parseInt(startId));
					if (record.getStarBottomList() == null) {
						record.setStarBottomList(new ArrayList<Star>());
					}
					record.getStarBottomList().add(star);
				}
			}
			ids = record.getStarMixId();
			if (!TextUtils.isEmpty(ids)) {
				String[] starIds = ids.split(",");
				for (String startId:starIds) {
					Star star = queryStarById(connection, Integer.parseInt(startId));
					if (record.getStarMixList() == null) {
						record.setStarMixList(new ArrayList<Star>());
					}
					record.getStarMixList().add(star);
				}
			}

			list.add(record);
		}
	}

}
