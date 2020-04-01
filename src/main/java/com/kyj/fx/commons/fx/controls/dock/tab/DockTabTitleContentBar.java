/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.dock.tab
 *	작성일   : 2016. 10. 27.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.dock.tab;

import com.kyj.fx.commons.fx.controls.dock.pane.DockNode;
import com.kyj.fx.commons.fx.controls.dock.pane.DockTitleBar;

/**
 * @author KYJ
 *
 */
public class DockTabTitleContentBar extends DockTitleBar {

	public DockTabTitleContentBar(DockNode dockNode) {
		super(dockNode);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("DockTabTitleContentBar [getLabel()=%s]", getLabel());
	}

}
