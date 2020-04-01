/********************************
 *	프로젝트 : sos-client
 *	패키지   : com.samsung.sds.sos.client.util
 *	작성일   : 2016. 9. 6.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.excel;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javafx.util.Callback;

/**
 * ExcelData부 컨버터
 * 
 * 엑셀데이터 처리에 대한 처리핸들링 함수들 추상화.
 * 
 * S -> 시트 
 * D -> 데이터셋 
 * 
 * 개발자 편의를 위해 Map대신 LinkedMap을 사용함. (입력 순서 유지.)
 * @author KYJ
 */

public interface IExcelDataSetHandler<S extends Sheet, D extends LinkedHashMap<ExcelColumnExpression, List<Object>>> {

	/**
	 * 헤더 부분이 시작되는 로우 인덱스를 가리킨다.
	 * @최초생성일 2016. 9. 20.
	 */
	public static final int HEADER_ROW_INDEX = 4;
	/**
	 * 헤더부분이 끝나고 데이터가 쌓이는 로우인덱스를 가리킨다.
	 * @최초생성일 2016. 9. 20.
	 */
	public static final int DATA_ROW_INDEX = HEADER_ROW_INDEX + 1;
	/**
	 * 엑셀이 만들어지는 시작 컬럼인덱스를 가리킨다.
	 * @최초생성일 2016. 9. 20.
	 */
	public static final int START_COLUMN_INDEX = 0;

	/**
	 * javafx 컬럼width와 excel컬럼 width 사이의 너비를 맞추기위한 비율값.
	 * @최초생성일 2016. 9. 20.
	 */
	public static final int EXCEL_SIZE_UNIT = 40;

	/**
	 * Sheet정보와 DataSet정보파라미터를 받으면
	 * Sheet에 필요한정보를 매핑하는 내용을 기술한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 6. 
	 * @param startrow 데이터 입력이 시작될 로우인덱스
	 * @param s Sheet 클래스
	 * @param d DataSet클래스
	 */
	public void accept(final int startRow, S s, D d);

	/**
	 * 셀 생성 
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 6. 
	 * @param sheet
	 * @param value
	 * @param row
	 * @param column
	 * @return
	 * @throws Exception
	 */
	public default Cell createCell(Sheet sheet, Object value, int row, int column) throws Exception {
		Row row2 = sheet.getRow(row);

		if (row2 == null) {
			row2 = sheet.createRow(row);
		}
		Cell createCell = null;
		try {
			createCell = row2.getCell(column);
			if (createCell == null) {
				createCell = row2.createCell(column);
			}
		} catch (NullPointerException e) {
			createCell = row2.createCell(column);
		}

		if (value instanceof String) {
			createCell.setCellValue((String) value);
		} else if (value instanceof Integer) {
			createCell.setCellValue((Integer) value);
		} else if (value instanceof Double) {
			createCell.setCellValue((Double) value);
		} else if (value instanceof Float) {
			createCell.setCellValue((Float) value);
		} else if (value == null) {
			createCell.setCellValue("");
		} else if (value instanceof Date) {
			createCell.setCellValue((Date) value);
		}
		/*그 이외 특이 사항은 구현. else if로 구현할것.*/
		else {
			createCell.setCellValue(value.toString());
		}

		/* 
		 * Border 처리시 속도 저하의 원인... 
		 * 
		 */
		Callback<CellStyle, CellStyle> cellStyleFactory = dataCellStyleFactory();
		if (cellStyleFactory != null) {
			CellStyle call = cellStyleFactory.call(sheet.getWorkbook().createCellStyle());
			if (call != null)
				createCell.setCellStyle(call);
		}

		return createCell;
	}

	/**
	 * 데이터부 셀에 표현되는 디폴트 스타일.
	 * 
	 * 데이터부에 셀 표현처리시 속도저하의 원인이된다.
	 * 
	 * @최초생성일 2016. 9. 9.
	 */
	@Deprecated
	static final Callback<CellStyle, CellStyle> DEFAULT_DATA_CELL_STYLE_FACTORY = new Callback<CellStyle, CellStyle>() {
		@Override
		public CellStyle call(CellStyle style) {
			//			style.setBorderTop(CellStyle.BORDER_THIN);
			//			style.setBorderLeft(CellStyle.BORDER_THIN);
			//			style.setBorderRight(CellStyle.BORDER_THIN);
			//			style.setBorderBottom(CellStyle.BORDER_THIN);
			return style;
		}
	};

	/**
	 * 데이터부 셀에 표현할 스타일 팩토리 정의.
	 * 
	 * 참고로 데이터가 많을수록 아래처리를 지원하게되면  속도저하의 원인이됨.
	 * 
	 * 기능자체는 지원.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 9. 
	 * @return
	 */
	public default Callback<CellStyle, CellStyle> dataCellStyleFactory() {
		return null;
	}

	/**
	 * 사용자 화면 정보를 담는 스크린 핸들러. input
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8. 
	 * @param screenHandler
	 */
	public void setExcelScreenHandler(IExcelScreenHandler screenHandler);

	/**
	 * 사용자 화면 정보를 담는 스크린 핸들러. output
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8. 
	 * @return
	 */
	public IExcelScreenHandler getExcelScreenHandler();

	/**
	 * 컬럼 자동 사이즈조절 처리 여부
	 * 
	 * 기본으로 입력되는 너비값 : [  IExcelScreenHandler.isApplyAutoFit함수 주석 참조  ]
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8. 
	 * @return
	 */
	public default boolean isApplyAutoFit() {
		IExcelScreenHandler screenHandler = getExcelScreenHandler();
		if (screenHandler != null)
			return screenHandler.isApplyAutoFit();
		return false;
	}

	/**
	 * 각 시트마다 공통기능이외의 처리가 필요한경우 
	 * 해당 API를 오버라이드해서 기능을 처리할 수 있게 지원한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8. 
	 * @param sheetIndex
	 * @param sheet
	 */
	default void customSheetHandle(int sheetIndex, Sheet sheet) {
		IExcelScreenHandler screenHandler = getExcelScreenHandler();
		if (screenHandler != null)
			screenHandler.customSheetHandle(sheetIndex, sheet);
	}
}
