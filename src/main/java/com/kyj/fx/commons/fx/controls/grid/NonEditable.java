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
 * AbstractDVO의 특정 필드위에 해당 어노테이션속성을 붙이는경우 공통그리드 UI에서 컬럼속성이 NonEditable로 인식하게 된다.
 * 
 * 사용예제 public class Person {
 * 
 * @NonEditable //키속성으로 인식 private String userId;
 * 
 *              ..... (이하 생략) }
 * 
 * 
 * @author KYJ
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NonEditable {

}
