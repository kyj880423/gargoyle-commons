/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

/**
 * @author KYJ
 *
 */
public class BaseOptions implements IOptions {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.tmp.application.template.grid.commons.IColumnNaming#convert(java
	 * .lang.String) KYJ
	 */
	@Override
	public String convert(String columnName) {
		return columnName;
	}

}
