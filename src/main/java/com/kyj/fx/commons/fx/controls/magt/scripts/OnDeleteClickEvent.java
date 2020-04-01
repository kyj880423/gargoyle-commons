/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import java.util.Optional;

import com.kyj.fx.commons.utils.DialogUtil;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Pair;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class OnDeleteClickEvent implements ReleaseScriptEvent {

	AbstractEventManager manager;

	@Override
	public void onAceept(EventMeta event) {
		ReleaseScriptComposite rootComposite = event.getReleaseScriptComposite();
		TreeView<EventScriptDVO> tv = rootComposite.getControlTreeView();
		TreeItem<EventScriptDVO> selectedItem = tv.getSelectionModel().getSelectedItem();
		// TabPane tpScript = rootComposite.getTpScript();

		EventScriptDVO value = selectedItem.getValue();
		if (selectedItem == null || value == null)
			return;
		if (rootComposite.treeRoot == selectedItem)
			return;

		String message = String.format("do you want really delete a this item ?\n%s ( id : %s)", value.getScriptTitle(),
				value.getScriptId());

		Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog("Delete ", message);
		showYesOrNoDialog.ifPresent(p -> {
			if ("Y".equals(p.getValue())) {

				boolean flag = false;
				try {
					flag = event.getEventManager().onDelete(value);
				} catch (Exception e) {
					DialogUtil.showExceptionDailog(e);
				}

				if (!flag)
					DialogUtil.showMessageDialog("delete failed.");
				else {
					selectedItem.getParent().getChildren().remove(selectedItem);
				}
			}
		});

	}

}
