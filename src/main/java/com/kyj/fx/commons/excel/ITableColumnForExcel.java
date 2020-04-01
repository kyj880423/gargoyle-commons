/********************************
 *	프로젝트 : sos-client
 *	패키지   : com.samsung.sds.sos.client.core.screen.utiles
 *	작성일   : 2016. 9. 9.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.excel;

import java.util.function.BiPredicate;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * 
 * 엑셀 Export시 생략할 필요가 있는 테이블 컬럼을 처리하는 코드를 작성하는  인터페이스
 * 
 * @author KYJ
 *
 */
public interface ITableColumnForExcel extends BiPredicate<TableView<?>, TableColumn<?, ?>> {

	@Override
	public default boolean test(TableView<?> t, TableColumn<?, ?> c) {
		return c.isVisible();
	}

}
