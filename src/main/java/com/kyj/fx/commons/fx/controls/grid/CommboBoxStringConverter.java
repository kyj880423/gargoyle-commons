/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 11. 25.
 *	프로젝트 : SOS 미어캣 프로젝트
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.reflect.Field;

import javafx.util.StringConverter;

/**
 * 그리드 콤보박스에서 사용되는 스트링 컨버터
 * 
 * @author KYJ
 *
 */
public class CommboBoxStringConverter<T> extends StringConverter<T> {

	@SuppressWarnings("unused")
	private String code;
	private String codeNm;

	// private ObservableList<T> codeList;

	// 아직까지는 codeList가 쓰일일이 없어서 주석처리함.. 과연 필요한 케이스가 생길지...?
	public CommboBoxStringConverter(/* ObservableList<T> codeList, */String code, String codeNm) {
		this.code = code;
		this.codeNm = codeNm;
		// this.codeList = codeList;

	}

	@Override
	public String toString(T object) {
		if (object == null)
			return "";

		return getFieldValue(object);
	}

	@Override
	public T fromString(String string) {
		/*
		 * 2015-11-25 현재 상황에선 이메소드가 호출이 될 일이 없음. 발생된 케이스가 있다면 수정해야함.
		 */
		return null;
	}

	String getFieldValue(T object) {
		String returnValue = "";
		try {
			Field declaredField = object.getClass().getDeclaredField(codeNm);
			declaredField.setAccessible(true);
			Object value = declaredField.get(object);
			return value.toString();
		} catch (Exception e) {
			returnValue = object.toString();
		}
		return returnValue;
	}

}
