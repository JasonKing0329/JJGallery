package com.king.service.gdb.dao;

public class SqliteTable {

	private String tableName;
	private String[] columns;
	private String[] types;

	public SqliteTable(String name) {
		tableName = name;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String...columns) {
		this.columns = columns;
	}
}
