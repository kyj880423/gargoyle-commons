package com.kyj.fx.commons.models;

import java.awt.Color;

public class ExcelColDVO {

	private int colSeq;

	private String colName;

	private int columnWidth;

	private short alignment;

	private Color backgroundColor;

	public ExcelColDVO(int colSeq, String colName, int columnWidth, short alignment) {
		super();
		this.colSeq = colSeq;
		this.colName = colName;
		this.columnWidth = columnWidth;
		this.alignment = alignment;
	}

	public ExcelColDVO(int colSeq, String colName, int columnWidth) {

		this(colSeq, colName);
		this.columnWidth = columnWidth;
	}

	public ExcelColDVO(int colSeq, String colName) {
		this.colSeq = colSeq;
		this.colName = colName;
	}

	public ExcelColDVO() {
		super();
	}

	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor
	 *            the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getColSeq() {
		return colSeq;
	}

	public void setColSeq(int colSeq) {
		this.colSeq = colSeq;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}

	public short getAlignment() {
		return alignment;
	}

	public void setAlignment(short alignment) {
		this.alignment = alignment;
	}

	@Override
	public String toString() {
		return "ExcelColDVO [colSeq=" + colSeq + ", colName=" + colName + ", columnWidth=" + columnWidth + "]";
	}

}
