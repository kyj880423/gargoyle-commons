/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.grid
 *	작성일   : 2018. 6. 1.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * AbstractDVO의 특정 필드위에 해당 어노테이션속성을 붙이는경우 <br/>
 * 공통그리드 UI에서 CommonsClicked라는 필터 보임 여부를 지정할 수 있다.
 * 
 * @UseCommonClick(boolean) 사용예제 public class Person {
 * 
 * @author KYJ
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCommonClick {

	/**
	 * 아래 어노테이션을 정의하게되면 공통컬럼 보임여부를 지정가능하다.  </br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 1.
	 * @return
	 */
	public boolean value() default true;
}
