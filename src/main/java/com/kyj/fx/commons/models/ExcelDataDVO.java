package com.kyj.fx.commons.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.kyj.fx.commons.utils.ObjectUtil;

public class ExcelDataDVO {

	private int row;

	private int col;

	private Object data;

	private Color backgroundColor;

	@Deprecated
	private String backColorRgb;

	public ExcelDataDVO(int row, int col, Object data) {

		this.row = row;
		this.col = col;
		this.data = data;
	}

	public ExcelDataDVO(int rowIndex, int columnIndex, String stringCellValue, String backColorRgb) {
		this(rowIndex, columnIndex, stringCellValue);
		this.backColorRgb = backColorRgb;
	}

	public ExcelDataDVO(int rowIndex, int columnIndex, String stringCellValue, Color backgroundColor) {
		this(rowIndex, columnIndex, stringCellValue);
		this.backgroundColor = backgroundColor;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
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

	/**
	 * @return the backColorRgb
	 */
	public String getBackColorRgb() {
		return backColorRgb;
	}

	/**
	 * @param backColorRgb
	 *            the backColorRgb to set
	 */
	public void setBackColorRgb(String backColorRgb) {
		this.backColorRgb = backColorRgb;
	}

	@Override
	public String toString() {
		return "ExcelDataDVO [row=" + row + ", col=" + col + ", data=" + data + "]";
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 28. 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<ExcelDataDVO> createList(List<T> list) {
		List<ExcelDataDVO> arrayList = new ArrayList<>();
		int colIdx = 0;
		for (int i = 0, len = list.size(); i < len; i++) {
			T d = list.get(i);
			Map<String, Object> map = ObjectUtil.toMap(TreeMap::new, d);
			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				colIdx ++;
				Entry<String, Object> e = iterator.next();
				arrayList.add(new ExcelDataDVO(i, colIdx, e.getValue()));
			}
			colIdx = 0;
		}
		return Collections.emptyList();
	}

}
