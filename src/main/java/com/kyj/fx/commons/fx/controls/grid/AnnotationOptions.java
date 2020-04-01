/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.kyj.fx.commons.utils.ValueUtil;

/**
 * 어노테이션기반 텍스트헤더를 처리함.
 *
 * @COLUMN("텍스트")
 * @author KYJ
 *
 */
public class AnnotationOptions<T> extends BaseOptions {

	private Class<T> clazz;

	public AnnotationOptions(Class<T> clazz) {
		this.clazz = clazz;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.kyj.tmp.application.template.grid.commons.IColumnNaming#convert(java
	 * .lang.String) KYJ
	 */
	@Override
	public String convert(String columnName) {
		return getColumnHeader(columnName);
	}

	/**
	 * 테이블 헤더를 어노테이션기반 텍스트 형태로 사용한다. 우선순위는 먼저 어노테이션 텍스트존재하면 먼저 맵핑하고 어노테이션이 없는경우
	 * columnName을 반환한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 21.
	 * @param classType
	 * @param columnName
	 * @param options
	 * @return
	 */
	String getColumnHeader(String columnName) {

		String headerText = "";

		ColumnName annotationsByType = getAnnotationClass(ColumnName.class, columnName);

		// 어노테이션 밸류로 헤더텍스트 처리.[기본.]
//		if (SysUtil.isEmpty(headerText)) {
			if (annotationsByType != null) {
				headerText = annotationsByType.value();
			}
//		}

		// 우선순위[상] 어노테이션 msgId 존재유무 확인
		if (annotationsByType != null) {

			String messageId = annotationsByType.messageId();
			if (ValueUtil.isNotEmpty(messageId)) {
				headerText = ValueUtil.getMessage(messageId);
			}
		}

		//convert에 정의된 값이 존재하면 처리.
//		if (ValueUtil.isEmpty(headerText)) {
			//무한 재귀호출 가능성이 있으므로 제거.
			//headerText = convert(columnName);
//		}


		//그래도 없다면 컬럼명으로 매핑.
		if (ValueUtil.isEmpty(headerText)) {
			headerText = columnName;
		}

		//불필요코드 제거.
//		if (headerText == null || headerText.isEmpty())
//			headerText = columnName;

		return headerText;
	}

	/**
	 * 어노테이션 클래스를 반환, 없으면 null 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 21.
	 * @param annotationClass
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("hiding")
	private <T extends Annotation> T getAnnotationClass(Class<T> annotationClass, String fieldName) {
		try {
			Field declaredField = this.clazz.getDeclaredField(fieldName);
			declaredField.setAccessible(true);
			return declaredField.getDeclaredAnnotation(annotationClass);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 1. 
	 * @param annotationClass
	 * @return
	 */
	private <T extends Annotation> T getAnnotationClass(Class<T> annotationClass) {
		return this.clazz.getDeclaredAnnotation(annotationClass);
	}
	
	/**
	 * 어노테이션 존재여부를 확인한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 21.
	 * @param annotationClass
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("hiding")
	private <T extends Annotation> boolean containsAnotation(Class<T> annotationClass, String fieldName) {
		try {
			Field declaredField = this.clazz.getDeclaredField(fieldName);
			declaredField.setAccessible(true);
			return declaredField.getDeclaredAnnotation(annotationClass) == null ? false : true;
		} catch (Exception e) {
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.kyj.tmp.application.template.grid.commons.IOptions#editable(java.
	 * lang.String)
	 */
	@Override
	public boolean editable(String columnName) {

		if (containsAnotation(NonEditable.class, columnName)) {
			return false;
		}
		return super.editable(columnName);
	}

	@Override
	public boolean useCommonCheckBox() {
		UseCommonClick annotationClass = getAnnotationClass(UseCommonClick.class);
		if(annotationClass == null)
			return true;
		return annotationClass.value();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.samsung.sds.sos.client.component.grid.IOptions#columnSize(java.lang
	 * .String)
	 */
	@Override
	public int columnSize(String columnName) {

		if (CommonConst.COMMONS_FILEDS_COMMONS_CLICKED.equals(columnName)) {
			return 30;
		}
		ColumnWidth colVisible = getAnnotationClass(ColumnWidth.class, columnName);
		if(colVisible!=null)
			return colVisible.value();
		
		return super.columnSize(columnName);
	}

	@Override
	public boolean visible(String columnName) {
		boolean visible = true;
		ColumnVisible colVisible = getAnnotationClass(ColumnVisible.class, columnName);

		if(colVisible != null)
			visible  =  colVisible.value();
		
		return visible;
	}

	
}
