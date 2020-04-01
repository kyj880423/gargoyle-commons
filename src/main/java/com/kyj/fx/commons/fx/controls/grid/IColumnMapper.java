/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import javafx.scene.control.TableColumn;

/**
 * 컬럼값에 일치하는 테이블컬럼을 리턴
 * 
 * @author KYJ
 *
 */
public interface IColumnMapper<T> {

	/**
	 * 컬럼값에 일치하는 테이블컬럼을 리턴
	 * 
	 * @param classType
	 * 
	 * @Date 2015. 10. 8.
	 * @param columnName
	 * @param columnNaming
	 * @return
	 * @User KYJ
	 */
	public TableColumn<T, ?> generateTableColumns(Class<?> classType, String columnName, IOptions columnNaming);

}
