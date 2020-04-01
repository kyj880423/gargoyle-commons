/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.dock.tab
 *	작성일   : 2016. 10. 26.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.dock.tab;

import com.kyj.fx.commons.fx.controls.dock.pane.DockNode;

import javafx.scene.Node;

/**
 * @author KYJ
 *
 */
public class DockTabContent extends DockNode {

	/**
	 * @param contents
	 * @param title
	 */
	public DockTabContent(Node contents, String title) {
		super(contents, title);
		setDockTitleBar(new DockTabTitleContentBar(this));

	}

}
