/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 7. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
/**
 * FxUtil.createStage 함수 사용시 <br/>
 * 아래 어노테이션이 붙은 Composite의 경우 <br/>
 * 무조건 하나의 팝업만 오픈하도록 설정하게된다. <br/>
 * 
 * 
 * 단, Parent에 대항하는 객체 인스턴스가 새로 생성되어 호출되는 경우는 제외된다. <br/>
 * 
 * @author KYJ
 *
 */
public @interface SingtonPopupStage {

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 18.
	 * @return
	 */
	boolean singleton() default true;

}
