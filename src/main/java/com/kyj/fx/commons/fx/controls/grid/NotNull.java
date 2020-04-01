/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component
 *	작성일   : 2016. 12. 08.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

	Policy policy() default Policy.NotEmpty;

}
