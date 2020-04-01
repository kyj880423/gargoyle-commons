package com.kyj.fx.commons.models;

import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class SheetVO {
	private Sheet sheet;
	private Workbook wb;

	SheetVO() {
	}

	public SheetVO(Sheet sheet) {
		this.sheet = sheet;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public Cell setCell(Object str, int row, int column) throws Exception {
		Row row2 = sheet.getRow(row);
		Cell createCell = null;
		try {
			createCell = row2.createCell(column);
		} catch (NullPointerException e) {
			try {
				row2 = sheet.createRow(row);
				createCell = row2.createCell(column);
			} catch (Exception e2) {
				throw e2;
			}
		}
		if (str instanceof String) {
			createCell.setCellValue((String) str);
		} else if (str instanceof Integer) {
			createCell.setCellValue((Integer) str);
		} else if (str instanceof Double) {
			createCell.setCellValue((Double) str);
		} else if (str instanceof Float) {
			createCell.setCellValue((Float) str);
		} else {
			throw new Exception("뭘 입력하신겁니까?");
		}
		return createCell;
	}

	public String getCellValue(int row, int column, String dateType) throws Exception {
		String temp = null;

		if (sheet != null) {
			Row rowObj = sheet.getRow(row);

			if (rowObj != null) {
				Cell cell = rowObj.getCell(column);

				if (cell != null) {
					SimpleDateFormat f = null;

					if (dateType != null && dateType != "") {
						f = new SimpleDateFormat(dateType);
					}

					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC :
							// 셀값이 날짜일경우
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								temp = f.format(cell.getDateCellValue());
								// 셀값이 숫자일경우
							} else {
								temp = String.valueOf(cell.getNumericCellValue());
							}
							break;
						// 셀값이 문자열이면 ..
						case Cell.CELL_TYPE_STRING :
							temp = cell.getStringCellValue();
							break;
						// 셀값이 수식이라면 수식을 얻어온다.
						case Cell.CELL_TYPE_FORMULA :
							temp = cell.getCellFormula();
							break;
						/*
						 * case Cell.CELL_TYPE_BLANK: temp = ""; break;
						 */
						// 셀값이 boolean형태이면

						case Cell.CELL_TYPE_BOOLEAN :
							temp = String.valueOf(cell.getBooleanCellValue());
							break;
						default :
							temp = "";
							break;
					}
				}
			}
		}
		return temp;
	}

	private Cell getCell(int row, int column) throws Exception {
		Cell c = null;
		if (sheet == null) {
			throw new NullPointerException("Sheet is null");
		}
		try {
			sheet.getRow(row).getCell(column);
		} catch (NullPointerException e) {
			try {
				c = sheet.createRow(row).createCell(column);
			} catch (Exception ex) {
				throw ex;
			}
		}

		return c;

	}

	public void setCellSubtitle(int row, int column, String location, String strTo, String strFrom) throws FormulaParseException, Exception {
		String subTitle = "SUBSTITUTE(PROPER(" + location + ")," + strTo + "," + strFrom + ")";
		this.getCell(row, column).setCellFormula(subTitle);
	}

	public void setCellSubtitle(Cell cell, String location, String strTo, String strFrom) throws FormulaParseException, Exception {
		String subTitle = "SUBSTITUTE(PROPER(" + location + ")," + strTo + "," + strFrom + ")";
		cell.setCellFormula(subTitle);
	}

}
