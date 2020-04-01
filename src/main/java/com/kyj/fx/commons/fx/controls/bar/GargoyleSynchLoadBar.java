/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.bar
 *	작성일   : 2016. 10. 7.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.bar;

import javafx.concurrent.Task;
import javafx.stage.Window;

/**
 *
 * 동기처리된 로딩바
 * @author KYJ
 *
 */
public class GargoyleSynchLoadBar<V> extends GargoyleLoadBar<V> {

	/**
	 * @param owner
	 * @param task
	 */
	public GargoyleSynchLoadBar(Window owner, Task<V> task) {
		super(owner, task);
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.voeditor.visual.component.bar.GargoyleLoadBar#getType()
	 */
	@Override
	public final Type getType() {
		return Type.synch;
	}
}
