/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 5. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

/**
 * @author KYJ
 *
 */
public interface GargoyleDefaultOpenTabPolicy extends GargoyleOpenTabPolicy {

	/* 
	 * 이미 열린 같은 탭 이름이 있는경우  <br/>
	 * 
	 * 탭 이름 기준으로 중복 열기 가능 여부를 지정함. <br/>
	 * 
	 * true이면 열기 가능 <br/>
	 * false면  불가. <br/>
	 * 
	 * 디폴트 false
	 */
	@Override
	default OpenTabPolicy allowSameNameTabOpen() {
		return OpenTabPolicy.ALLOW_DEFAULT_NOT_ALLOW_OTHERS;
	}

	/**
	 * 정책에 반영할 디폴트 탭 이름
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 14. 
	 * @return
	 */
	public abstract String defaultTabName();
	
}
