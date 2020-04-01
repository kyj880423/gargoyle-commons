package com.kyj.fx.commons.models;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author KYJ 엑셀데이터 관리 그룹객체
 * 
 *         솔직히 별로 마음에 안드는 로직이다.
 *         
 *         참조하는 클래스가 많아 쉽게 고치기 어렵다.
 */
public class ExcelSVO {

	private File file;

	private Map<String, List<ExcelColDVO>> column = new HashMap<String, List<ExcelColDVO>>();

	private Map<String, List<ExcelDataDVO>> excel = new TreeMap<String, List<ExcelDataDVO>>();

	public Set<String> getKeySet() {
		return excel.keySet();
	}

	public List<ExcelDataDVO> getSheetExcelDVO(String key) {
		return excel.get(key);
	}

	/**
	 * 2014. 11. 4. KYJ
	 * 
	 * @param key
	 * @param dvo
	 * @처리내용 : sheetName에 데이터를 넣는다.
	 */
	public void addSheetExcelDVO(String key, ExcelDataDVO dvo) {
		if (excel.containsKey(key)) {
			excel.get(key).add(dvo);
		} else {
			ArrayList<ExcelDataDVO> value = new ArrayList<ExcelDataDVO>();
			value.add(dvo);
			excel.put(key, value);
		}
	}

	/**
	 * 2014. 11. 4. KYJ
	 * 
	 * @param sheetName
	 * @param dvoList
	 * @처리내용 : sheetName에 데이터리스트를 넣는다.
	 */
	public void addSheetExcelDVO(String sheetName, List<ExcelDataDVO> dvoList) {
		if (excel.containsKey(sheetName)) {
			List<ExcelDataDVO> arrayList = excel.get(sheetName);
			arrayList.addAll(dvoList);

		} else {
			excel.put(sheetName, dvoList);
		}

	}

	public Iterator<String> iterator() {
		return excel.keySet().iterator();
	}

	public List<ExcelColDVO> getColDvoList(String sheet) {
		return column.get(sheet);
	}

	public void setColDvoList(String sheet, List<ExcelColDVO> colDvoList) {
		this.column.put(sheet, colDvoList);
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the column
	 */
	public final Map<String, List<ExcelColDVO>> getColumn() {
		return column;
	}

}
