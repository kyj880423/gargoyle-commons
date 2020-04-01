/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.file.conversion
 *	작성일   : 2018. 3. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

/**
 * 데이터 변환 처리 추상 클래스 <br/>
 * 
 * @author KYJ
 *
 */
public abstract class AbstractFileContentConversion<T> implements FileContentConversionable, DataConversionable<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.file.conversion.DataConversionable#
	 * conversion()
	 */
	public abstract T conversion() throws Exception;
}
