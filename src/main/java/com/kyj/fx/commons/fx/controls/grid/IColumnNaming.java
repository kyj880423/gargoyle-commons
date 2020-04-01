/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

/**
 * @author KYJ
 *
 */
public interface IColumnNaming {

	/**
	 * 컬럼명에 해당하는 다른 이름을 할당
	 *
	 * @Date 2015. 10. 8.
	 * @param columnName
	 * @return
	 * @User KYJ
	 */
	public String convert(String columnName);

	/**
	 * UI에 보여줄 컬럼 사이즈 처리
	 *
	 * @Date 2015. 10. 10.
	 * @param columnName
	 * @return
	 * @User KYJ
	 */
	default public int columnSize(String columnName) {
		return -1;
	}

	/**
	 * 편집가능속성지정
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 12.
	 * @param columnName
	 * @return
	 */
	default public boolean editable(String columnName) {
		return true;
	}
}
