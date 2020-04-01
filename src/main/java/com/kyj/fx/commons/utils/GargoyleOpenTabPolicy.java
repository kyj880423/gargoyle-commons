/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 5. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

/**
 * Gargoyle에서 Tab으로 화면을 열때 정책적인 부분을 적용 <br/>
 * 
 * @author KYJ
 *
 */
public interface GargoyleOpenTabPolicy {

	public enum OpenTabPolicy {
		/* 무조건 새로운 탭을 여는것을 허용함. */
		ALLOW_MULTI_OPEN,
		/* 탭 이름이 같으면 중복 오픈을 허용하지 않음 */
		NAME_CHECK,

		/*
		 * 디폴트 이름에 해당하는 경우는 무조건 여러 탭을 여는걸 허용하지만 디폴트 이름이 아닌 탭명같은경우는 허용하지않음.
		 */
		ALLOW_DEFAULT_NOT_ALLOW_OTHERS
		/* 동일한 클래스면 무조건 허용하지않음. */
		/* SAME_TYPE_NOT_ALLOW */

	}

	/**
	 * 이미 열린 같은 탭 이름이 있는경우 <br/>
	 * 
	 * 탭 이름 기준으로 중복 열기 가능 여부를 지정함. <br/>
	 * 
	 * 노드에 이 값이 지정되지않는경우 기본적으로 탭 이름 체크 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 14.
	 * @return
	 */
	public OpenTabPolicy allowSameNameTabOpen();

	/**
	 * 정책에 반영할 디폴트 탭 이름
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 14.
	 * @return
	 */
	default String defaultTabName() {
		return getClass().getSimpleName();
	}
}
