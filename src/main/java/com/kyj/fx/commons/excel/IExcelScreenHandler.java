/********************************
 *	프로젝트 : sos-client
 *	패키지   : com.samsung.sds.sos.client.core.screen.utiles
 *	작성일   : 2016. 9. 6.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.excel;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.kyj.fx.commons.utils.ValueUtil;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Excel을 데이터 매퍼 처리. 기본값은 구현되있으며, 특화 데이터는 상속후 처리해야함.
 * 
 * AbstractScreen객체가 인터페이스로  갖는다.
 * 
 * 참조) 
 * 테이블과 테이블 컬럼이 와잉드카드(<?>)로 표현한이유는 하나의 뷰에 다양한 데이터셋이 사용가능하기에 한가지 타입으로 표현하면안됨.
 * 
 * 화면에서 엑셀핸들링 커스텀처리를 지원하기 위해 구현됨.
 * 
 * 
 * @author KYJ
 *
 */
public interface IExcelScreenHandler {

	/**
	 * 테이블컬럼의 row에 해당하는 데이터가 무엇인지 정의한 값에 따라 엑셀에 표현됨.
	 * 
	 * StringConverter를 이용한 TableCell인경우 정의된 StringConvert를 이용한 데이터를 Excel의
	 * Cell에 쓰고, StringConverter를 이용하지않는 UI의 TableCell의 경우 데이터셋에 바인드된 값을 사용하게됨.
	 * 
	 * 작성된 API내에서 적절한 값이 아니라고 판단되는경우 Ovrride해서 재정의하도록한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 9.
	 * @param table
	 *            사용자 화면에 정의된 tableView
	 * @param column
	 *            사용자 화면에 정의된 tableColumn
	 * @param columnIndex
	 *            사용자 화면에 정의된 tableColumn의 인덱스
	 * @param rowIndex
	 *            사용자 화면에 정의된 tableCell의 인덱스
	 * @return Object 테이블셀에 정의된 데이터를 리턴할 값으로, 리턴해주는 값이 엑셀에 write된다.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public default <T> Object valueMapper(TableView<?> table, TableColumn<?, ?> column, int columnIndex, int rowIndex) {

		Callback cellFactory = column.getCellFactory();
		if (cellFactory != null) {
			TableCell cell = (TableCell) cellFactory.call(column);

			if (cell != null) {
				StringConverter converter = null;
				if (cell instanceof TextFieldTableCell) {
					TextFieldTableCell txtCell = (TextFieldTableCell) cell;
					converter = txtCell.getConverter();
				} 
				
//				else if (cell instanceof TextAreaTableCell) {
//					TextAreaTableCell txtCell = (TextAreaTableCell) cell;
//					converter = txtCell.getConverter();
//				} 
				else if (cell instanceof ComboBoxTableCell) {
					ComboBoxTableCell txtCell = (ComboBoxTableCell) cell;
					converter = txtCell.getConverter();
				} 
				
//				else if (cell instanceof HyperlinkTableCell) {
//					HyperlinkTableCell txtCell = (HyperlinkTableCell) cell;
//					converter = txtCell.getConverter();
//				}
				/* else 기본값. */
				else {
					try {
						Method m = cell.getClass().getMethod("converterProperty");
						if (m != null) {
							Object object = m.invoke(cell);
							if (object != null && object instanceof ObjectProperty) {
								ObjectProperty<StringConverter> convert = (ObjectProperty<StringConverter>) object;
								converter = convert.get();
							}
						}
					} catch (Exception e) {
						// Nothing...
					}
				}
				
				
				if (converter != null) {
					Object cellData = column.getCellData(rowIndex);
					return converter.toString(cellData);
				}
			}
		}

		return column.getCellData(rowIndex);
	}

	/**
	 * 헤더로  표현하기 위한 커스터마이즈한 값을 리턴해주는 코드작성.
	 * 정의하지않는경우 TableColumn Header텍스트로 표현.
	 * 
	 * 1. 우선 헤더텍스트값을 먼저 살펴봄
	 * 2. JAVAFX의 경우 헤더텍스트가 커스텀으로 구현되어 Graphics가 적용된 경우도 있으므로 
	 *   텍스트가 빈값으로 인지된경우 Graphics객체를 리턴받은후 라벨을 리턴받아 적용
	 *   
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 6.
	 * @param table
	 * @param tableColumn
	 * @return
	 */
	public default <T> String columnHeaderMapper(TableView<?> table, TableColumn<?, ?> tableColumn) {

		String headerText = tableColumn.getText();

		/* 2016-09-19 만약 headerText값이 null이라면 graphics에서 리턴받아 처리 시도. */
		if (ValueUtil.isEmpty(headerText)) {
			Node graphic = tableColumn.getGraphic();
			if (graphic != null && graphic instanceof Labeled) {
				Labeled lbl = (Labeled) graphic;
				headerText = lbl.getText();
			}
		}

		return headerText;
	}

	/**
	 * 엑셀 시트(sheet)명 을 리턴
	 * 
	 * 정의하지않는경우 sheet0, sheet1 ... 순으로 만들어짐.
	 * 
	 * 주의))) 
	 *    만약 이름이 중복되면 값도 덮어씌워진다.
	 *    오버라이드시, 시트명이 중복되지않게 할것. 
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 6.
	 * @param table
	 * @return
	 */
	public default String toSheetName(TableView<?> table) {
		return null;
	}

	/**
	 * 엑셀 시트로 사용할 테이블뷰인지 여부.
	 * false로 리턴되는경우 엑셀에 포함되지않는다.
	 * 
	 * 예상되는 필요 케이스)
	 *   View에서 두개이상의 테이블뷰가 보여지는데 시트하나로 표현이 필요한경우, 
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8.
	 * @param table
	 * @return
	 */
	public default Predicate<TableView<?>> useTableViewForExcel() {
		return t -> true;
	}

	/**
	 * 엑셀 시트로 사용할 테이블컬럼인지 여부.
	 *  
	 * 인터페이스 구현후 각 컬럼에 대한 true/false를 리턴한는 callback함수 구현.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 9.
	 * @return
	 */
	public default ITableColumnForExcel useTableColumnForExcel() {
		return new DefaultTableColumnForExcelImpl();
	}

	/**
	 * 컬럼 자동 사이즈조절 처리 여부,
	 * 깔끔하게 지원되지않지만, 엑셀내에 컬럼 사이즈를 자동으로 맞춰준다.
	 * 디폴트값은 false
	 * 
	 * 기본으로 적용되는 너비 값의 기준은 아래와 같다.
	 * 
	 *   IExcelDataSetHandler.EXCEL_SIZE_UNIT 상수값 * TableColumn.width (javafx)
	 *    
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8.
	 * @return
	 */
	public default boolean isApplyAutoFit() {
		return false;
	}

	/**
	 * 각 시트마다 공통기능이외의 처리가 필요한경우 해당 API를 오버라이드해서 기능을 처리할 수 있게 지원한다.
	 * 
	 * Sheet클래스내 기본값으로 구성된 데이터를 overrite하여 덮어 쓸수 있는 기회 제공
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 8.
	 * @param sheetIndex
	 * @param sheet
	 */
	default void customSheetHandle(int sheetIndex, Sheet sheet) {
	}

	/**
	 * 유저 커스텀 Workbook handler
	 * 
	 * 디폴트로 제공되는 sheet처리만으로 엑셀처리가 불가능한경우 workbook자체를 커스텀처리할 수 있는 기능 제공,
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 9.
	 * @param createNewWorkBookXlsx
	 */
	default void customWorkbookHandle(Workbook createNewWorkBookXlsx) {
	}

}
