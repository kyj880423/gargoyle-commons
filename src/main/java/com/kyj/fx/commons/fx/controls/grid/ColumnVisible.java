package com.kyj.fx.commons.fx.controls.grid;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2017. 04. 04.
 *	작성자   : KYJ
 *******************************/

/**
 * 
 * 컬럼 보임여부
 * @author KYJ
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnVisible {

	/**
	 * Javafx에서 컬럼 보임여부를 결정
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 22.
	 * @return
	 */
	public boolean value() default true;

}
