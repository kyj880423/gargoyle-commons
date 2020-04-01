/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.TreeItem;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class OnLoadEvent implements ReleaseScriptEvent {

	private static final Logger LOGGER = LoggerFactory.getLogger(OnLoadEvent.class);

	@Override
	public void onAceept(EventMeta event) {
		ReleaseScriptComposite c = event.getReleaseScriptComposite();

		if (c == null) {
			LOGGER.error("ReleaseScriptComposite is empty.");
			return;
		}
		if (event.getEventManager() == null) {
			LOGGER.error("Event Manager is empty.");
			return;
		}

		TreeItem<EventScriptDVO> treeRoot = c.getTreeRoot();
		treeRoot.getChildren().clear();

		try {
			List<EventScriptDVO> items = event.getEventManager().onLoad();
			if (items != null) {
				for (EventScriptDVO d : items) {
					treeRoot.getChildren().add(new TreeItem<EventScriptDVO>(d));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
