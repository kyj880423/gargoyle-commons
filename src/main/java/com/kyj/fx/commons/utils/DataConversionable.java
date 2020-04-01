/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.file.conversion
 *	작성일   : 2018. 3. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

/**
 * @author KYJ
 *
 */
public interface DataConversionable<T> {

	/*
	 * 변환 작업 처리 수행<br/>
	 * 
	 * @작성자 : KYJ
	 * 
	 * @작성일 : 2018. 3. 6.
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public T conversion() throws Exception;
}
