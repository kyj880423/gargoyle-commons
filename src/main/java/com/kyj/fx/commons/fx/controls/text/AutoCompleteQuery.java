/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 3. 16.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import org.fxmisc.richtext.CodeArea;

/**
 * 
 * 
 * 키워드를 인식하면 발생되는 이벤트
 * 
 * @author KYJ (callakrsos@naver.com)
 *
 */
public interface AutoCompleteQuery {

	/**
	 * 키워드를 인식할떄 발생되는 이벤트.
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16.
	 * @param codeArea
	 * @param position
	 * @param query
	 *            키워드
	 */
	public void onQuery(CodeArea codeArea, int position, String query);
}
