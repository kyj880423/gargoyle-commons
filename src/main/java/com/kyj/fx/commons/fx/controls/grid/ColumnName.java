/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 20.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * AbstractDVO의 특정 필드위에 해당 어노테이션속성을 붙이는경우 공통그리드 UI에서 컬럼 value에 해당하는 부분이 헤더텍스트로
 * 보여주게된다.
 * 
 * 사용예제 public class Person {
 * 
 * @ColumnName("사용자명") private String userNm;
 * 
 *                     ..... (이하 생략) }
 * 
 * 
 * 
 *                     2015.10.22 다국어 처리 필드 추가 messageId, by kyjun.kim
 * @author KYJ
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnName {

	/**
	 * 기본적으로 UI에 나타낼 텍스트를 기입.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 22.
	 * @return
	 */
	public String value();

	/**
	 * MessageId를 기입하여 다국어 처리가 가능하게한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 22.
	 * @return
	 */
	public String messageId() default "";
}
