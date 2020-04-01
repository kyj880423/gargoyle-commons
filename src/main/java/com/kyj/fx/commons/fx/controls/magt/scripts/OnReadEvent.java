/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 18.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

import com.kyj.fx.commons.utils.DialogUtil;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Pair;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class OnReadEvent implements ReleaseScriptEvent {

	public OnReadEvent() {

	}

	/*
	 * 
	 * EventManager의 OnRead 함수를 호출하여 컨텐츠를 읽은후 <br/> TabPane에 Tab을 추가한뒤 Tab을 화면에
	 * 로드한다 <br/>
	 * 
	 * @see
	 * com.kyj.fx.commons.fx.controls.magt.scripts.ReleaseScriptEvent#onAceept(
	 * com.kyj.fx.commons.fx.controls.magt.scripts.EventMeta)
	 */
	@Override
	public void onAceept(EventMeta event) {
		ReleaseScriptComposite rootComposite = event.getReleaseScriptComposite();
		TreeView<EventScriptDVO> tv = rootComposite.getControlTreeView();
		TreeItem<EventScriptDVO> selectedItem = tv.getSelectionModel().getSelectedItem();
		TabPane tpScript = rootComposite.getTpScript();

		if (selectedItem == null || selectedItem.getValue() == null)
			return;
		if (rootComposite.treeRoot == selectedItem)
			return;

		try {

			EventScriptDVO onRead = event.getEventManager().onRead(selectedItem.getValue());
			if (onRead == null)
				return;

			Optional<Tab> findFirst = tpScript.getTabs().stream().filter(t -> {
				ReleaseScriptTab rt = (ReleaseScriptTab) t;
				return rt.getEventScriptDVO().getScriptId().equals(selectedItem.getValue().getScriptId());
			}).findFirst();
			// 이미 존재하는 탭인경우 종료.
			if (findFirst.isPresent()) {
				Tab t = findFirst.get();
				tpScript.getSelectionModel().select(t);

				ReleaseScriptTab rt = (ReleaseScriptTab) t;

				IntPredicate predicate = v -> !('\n' == v || '\r' == v);
				StringBuilder a = onRead.getScriptContent().chars().filter(predicate).collect(StringBuilder::new,
						StringBuilder::appendCodePoint, StringBuilder::append);
				StringBuilder b = rt.getCodeArea().getText().chars().filter(predicate).collect(StringBuilder::new,
						StringBuilder::appendCodePoint, StringBuilder::append);

				if (!a.toString().equals(b.toString())) {
					Runnable runnable = () -> {
						String title = "Reload";
						String message = "File was changed do you want reload ? ";
						Optional<Pair<String, String>> optional = DialogUtil.showYesOrNoDialog(title, message);
						Consumer<? super Pair<String, String>> consumer = v -> {
							if ("Y".equals(v.getValue())) {
								rt.replaceScriptCode(onRead.getScriptContent());
							}
						};
						optional.ifPresent(consumer);
					};
					javafx.application.Platform.runLater(runnable);

				}
				return;
			}

			ReleaseScriptTab newTab = rootComposite.getScriptGuiManager().createScriptPaneTab(onRead);
			if (newTab == null)
				return;

			tpScript.getTabs().add(newTab);
			tpScript.getSelectionModel().selectLast();
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
		}
	}

}
