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
public interface GargoyleMultiOpenTabPolicy extends GargoyleOpenTabPolicy {

	/*
	 * 중복된 탭을 여는 정책 기준을 정의한다. <br/>
	 * 
	 */
	@Override
	default OpenTabPolicy allowSameNameTabOpen() {
		return OpenTabPolicy.ALLOW_DEFAULT_NOT_ALLOW_OTHERS;
	}

}
