/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import javafx.scene.control.TableColumn;
import javafx.util.StringConverter;

/**
 * @author KYJ
 *
 */
@FunctionalInterface
public interface IOptions {

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

	/**
	 * 그리드행 번호를 보여줄지 말지 설정
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 21.
	 * @return
	 */
	default public boolean showRowNumber() {
		return true;
	}

	/**
	 * 콤보박스를 제공할 열
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 12.
	 * @param columnIndex
	 * @return
	 */
	default public CommboInfo<?> comboBox(String columnName) {
		return null;
	}

	/**
	 * 문자열 포멧 컨버터
	 *
	 * 텍스트값을 다른포멧을 가진형태로 변환함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 13.
	 * @param columnName
	 * @return
	 */
	public default StringConverter<Object> stringConverter(String columnName) {
		return null;
	}

	/**
	 * 컬럼 보임여부
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 17.
	 * @param columnName
	 * @return
	 */
	public default boolean visible(String columnName) {
		return true;
	}

	/**
	 * 컬럼 생성 여부를 결정
	 * @작성자 : KYJ
	 * @작성일 : 2016. 3. 26.
	 * @param columnName
	 * @return
	 */
	public default boolean isCreateColumn(String columnName) {
		return true;
	}

	/**
	 * 특수한 컬럼인경우 오버라이드하여 사용
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 18.
	 * @param columnName
	 * @return
	 */
	public default <T, K> TableColumn<T, K> customTableColumn(String columnName) {
		return null;
	}

	/**
	 * 공통 체크박스 기능을 사용할지 유무
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 11.
	 * @return
	 */
	default public boolean useCommonCheckBox() {
		return false;
	}

	/**
	 * Buttons 클래스를 이요하여 설정.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 14.
	 * @return
	 */
	default public int useButtons() {
		return Buttons.useCudButtons();
	}

	/**
	 * css 스타일 정보를 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 18.
	 * @param columnName
	 * @return
	 */
	public default String style(String columnName) {
		return "";
	}
}
