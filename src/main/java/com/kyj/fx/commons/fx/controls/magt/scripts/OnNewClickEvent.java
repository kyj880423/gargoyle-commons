/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.IdGenUtil;

import javafx.scene.control.TreeItem;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class OnNewClickEvent implements ReleaseScriptEvent {

	@Override
	public void onAceept(EventMeta event) {
		try {
			EventScriptDVO d = new EventScriptDVO();
			d.setScriptId(IdGenUtil.generate());
			EventScriptDVO onNew = event.getEventManager().onNew(d);
			if (onNew == null)
				return;
			TreeItem<EventScriptDVO> treeRoot = event.getReleaseScriptComposite().getTreeRoot();
			treeRoot.getChildren().add(new TreeItem<EventScriptDVO>(onNew));
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
		}
	}

}
