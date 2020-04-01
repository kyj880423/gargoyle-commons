/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 18.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import org.fxmisc.richtext.CodeArea;

import com.kyj.fx.commons.fx.controls.text.GargoyleCodeArea;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DefaultScriptPaneManager extends AbstractScriptPaneManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.commons.fx.controls.magt.scripts.AbstractScriptPaneManager#
	 * createScriptPane()
	 */
	@Override
	public CodeArea createScriptPane() {
		return new GargoyleCodeArea();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.commons.fx.controls.magt.scripts.AbstractScriptPaneManager#
	 * createScriptPaneTab(com.kyj.fx.commons.fx.controls.magt.scripts.
	 * EventMeta)
	 */
	@Override
	public ReleaseScriptTab createScriptPaneTab(EventScriptDVO onRead) {
		CodeArea createScriptPane = createScriptPane();
		createScriptPane.appendText(onRead.getScriptContent() == null ? "" : onRead.getScriptContent());
		ReleaseScriptTab tab = new ReleaseScriptTab(onRead, createScriptPane);
		return tab;
	}

}
