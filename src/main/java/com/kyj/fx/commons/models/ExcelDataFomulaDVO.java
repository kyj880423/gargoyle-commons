/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.models
 *	작성일   : 2020. 6. 4.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.models;

import java.awt.Color;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ExcelDataFomulaDVO extends ExcelDataDVO {

	/**
	 * @param row
	 * @param col
	 * @param data
	 */
	public ExcelDataFomulaDVO(int row, int col, Object data) {
		super(row, col, data);
		
	}

	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @param stringCellValue
	 * @param backgroundColor
	 */
	public ExcelDataFomulaDVO(int rowIndex, int columnIndex, String stringCellValue, Color backgroundColor) {
		super(rowIndex, columnIndex, stringCellValue, backgroundColor);
	
	}

	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @param stringCellValue
	 * @param backColorRgb
	 */
	public ExcelDataFomulaDVO(int rowIndex, int columnIndex, String stringCellValue, String backColorRgb) {
		super(rowIndex, columnIndex, stringCellValue, backColorRgb);
		
	}

}
