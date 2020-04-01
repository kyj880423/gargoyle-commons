/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 3. 13.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

/**
 * 
 * 데이터베이스 처리를 위한 자동 완성 기능 <br/>
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DatabaseAutoComplete extends AutoComplete {

	public DatabaseAutoComplete() {
		super();
		addAllKeywords(SQLKeywordFactory.getInstance().getKeywords());
		addAllKeywords(SQLKeywordFactory.getInstance().getFunctionKeywords());
	}

}
