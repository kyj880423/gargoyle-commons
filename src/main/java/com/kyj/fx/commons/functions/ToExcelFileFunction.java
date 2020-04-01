/**
 * package : com.kyj.fx.voeditor.visual.functions
 *	fileName : ToExcelFunction.java
 *	date      : 2015. 11. 22.
 *	user      : KYJ
 *
 *  2018.05.15 repackae. KYJ.
 */
package com.kyj.fx.commons.functions;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kyj.fx.commons.models.BigDataDVO;
import com.kyj.fx.commons.models.ExcelColDVO;
import com.kyj.fx.commons.models.ExcelDataDVO;
import com.kyj.fx.commons.models.ExcelSVO;
import com.kyj.fx.commons.utils.ExcelUtil;

import javafx.scene.control.TableColumn;

/**
 * Excel로 변환
 *
 * @author KYJ
 *
 */
public class ToExcelFileFunction<T> {

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 8.
	 * @param saveFile
	 * @param columns
	 * @param param
	 * @return
	 */
	public boolean generate0(File saveFile, List<String> columns, List<Map<String, T>> param) {
		List<ExcelColDVO> cols = new ArrayList<>();
		if (columns != null && !columns.isEmpty()) {
			for (int i = 0; i < columns.size(); i++) {
				String column = columns.get(i);
				cols.add(new ExcelColDVO(i, column));
			}
		}
		return generate(saveFile, cols, param);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 8.
	 * @param saveFile
	 * @param columns
	 * @param param
	 * @return
	 */
	public boolean generate2(File saveFile, List<TableColumn<?, ?>> columns, List<Map<String, T>> param) {

		List<ExcelColDVO> cols = new ArrayList<>();

		if (columns != null && !columns.isEmpty()) {

			for (int i = 0; i < columns.size(); i++) {
				String column = columns.get(i).getText();
				cols.add(new ExcelColDVO(i, column));
			}
		}
		return generate(saveFile, cols, param);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 8.
	 * @param saveFile
	 * @param columns
	 * @param param
	 * @return
	 */
	public boolean generate(File saveFile, List<ExcelColDVO> columns, List<Map<String, T>> param) {

		ExcelSVO excelSVO = new ExcelSVO();
		String sheetName = "sheet1";

		if (columns != null && !columns.isEmpty()) {
			excelSVO.setColDvoList(sheetName, columns);
		}
		List<ExcelDataDVO> dataList = new ArrayList<>();
		for (int i = 0; i < param.size(); i++) {
			Map<String, T> map = param.get(i);

			Iterator<String> iterator = map.keySet().iterator();
			int col = 0;
			while (iterator.hasNext()) {
				String key = iterator.next();
				Object value = map.get(key);
				if (value instanceof BigDataDVO) {
					dataList.add(new ExcelDataDVO(i, col, ((BigDataDVO) value).getValue()));
				} else {
					dataList.add(new ExcelDataDVO(i, col, value));
				}

				col++;
			}
		}

		excelSVO.addSheetExcelDVO(sheetName, dataList);

		try {
			ExcelUtil.createExcel(saveFile.getAbsolutePath(), excelSVO, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
