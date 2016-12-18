package com.king.service.gdb.bean;

public class SheetBean {

	private Folder folder;
	private int[] sheetIndex;
	private int startRow;
	private int endRow;
	private int startCol;
	private int endCol;
	private int[] endRows;
	private String[] parents;
	
	public Folder getFolder() {
		return folder;
	}
	public void setFolder(Folder folder) {
		this.folder = folder;
	}
	public int[] getSheetIndex() {
		return sheetIndex;
	}
	public void setSheetIndex(int[] sheetIndex) {
		this.sheetIndex = sheetIndex;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public int getStartCol() {
		return startCol;
	}
	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}
	public int getEndCol() {
		return endCol;
	}
	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}
	public int[] getEndRows() {
		return endRows;
	}
	public void setEndRows(int[] endRows) {
		this.endRows = endRows;
	}
	public String[] getParents() {
		return parents;
	}
	public void setParents(String[] parents) {
		this.parents = parents;
	}
}
