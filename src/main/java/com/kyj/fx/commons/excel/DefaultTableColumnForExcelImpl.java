/********************************
 *	프로젝트 : sos-client
 *	패키지   : com.samsung.sds.sos.client.core.screen.utiles
 *	작성일   : 2016. 9. 9.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.excel;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * 
 * 엑셀 Export시 생략할 필요가 있는 테이블 컬럼에 대한 조건을 추가.
 * 
 * @author KYJ
 *
 */
public class DefaultTableColumnForExcelImpl implements ITableColumnForExcel {

	/* 
	 * API내에 구현된 기본기능))
	 *  공통체크박스는 엑셀에 처리안함.
	 *  
	 *  1. TableColumn의 id가 공통체크박스를 의미하는 id를 부여한경우 excel처리 안함. -> 공통그리드뷰에서 사용되고있음.
	 *  2. PropertyValueFactory 클래스를 사용하여 콜백을 구현한경우 excel처리 안함   ->  일반적인 UI에서 사용됨.
	 * 
	 *  
	 * (non-Javadoc)
	 * @see com.samsung.sds.sos.client.core.screen.utiles.ITableColumnForExcel#test(javafx.scene.control.TableView, javafx.scene.control.TableColumn)
	 */
	@Override
	public boolean test(TableView<?> t, TableColumn<?, ?> c) {

		if (c != null) {
			//공통그리드에서사용하는 컬럼 id인경우.
			if ("commonsClicked".equals(c.getId())) {
				return false;
			}

			//공통그리드에서 사용하는 프로퍼티 팩토리.
			Callback<?, ?> cellValueFactory = c.getCellValueFactory();
			if (cellValueFactory instanceof PropertyValueFactory) {
				@SuppressWarnings("rawtypes")
				PropertyValueFactory fac = (PropertyValueFactory) cellValueFactory;
				if ("commonsClicked".equals(fac.getProperty()))
					return false;
			}
		}
		return c.isVisible();
	}

}
