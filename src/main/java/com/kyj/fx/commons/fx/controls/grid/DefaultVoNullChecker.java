/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2016. 12. 8.
 *	프로젝트 : OPERA
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.util.List;

/**
 * @author KYJ
 *
 */
public class DefaultVoNullChecker<T extends AbstractDVO> extends AbstractVoNullChecker<T> {

	public DefaultVoNullChecker() {
		super();
	}

	public DefaultVoNullChecker(List<T> list) {
		super(list);
	}

	public DefaultVoNullChecker(CrudBaseGridView<T> view) {
		super(view);
	}

}
