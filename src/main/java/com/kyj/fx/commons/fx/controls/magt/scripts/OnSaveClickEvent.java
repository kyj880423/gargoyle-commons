/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import java.util.function.Function;

import com.kyj.fx.commons.utils.DialogUtil;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class OnSaveClickEvent implements ReleaseScriptEvent {

	private Function<EventScriptDVO, EventScriptDVO> onBeforeSaveAction = d -> d;

	@Override
	public void onAceept(EventMeta event) {
		ReleaseScriptComposite rootComposite = event.getReleaseScriptComposite();
		TabPane tpScript = rootComposite.getTpScript();
		Tab tab = tpScript.getSelectionModel().getSelectedItem();
		if (tab == null)
			return;

		ReleaseScriptTab currentTab = (ReleaseScriptTab) tab;
		// 화면의 텍스트를 데이터셋에 매핑시킨다.
		currentTab.updateDataset();
		
		EventScriptDVO eventScriptDVO = currentTab.getEventScriptDVO();

		try {

			if (eventScriptDVO == null)
				throw new NullPointerException("eventScriptDVO is empty.");

			EventScriptDVO before = getOnBeforeSaveAction().apply(eventScriptDVO);
			@SuppressWarnings("unchecked")
			EventScriptDVO onSave = event.getEventManager().onSave(before);

			if (onSave == null)
				return;

			tpScript.getSelectionModel().select(currentTab);
			onComplete(onSave);
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
		}
	}

	/**
	 * @return the onBeforeSaveAction
	 */
	public Function<EventScriptDVO, EventScriptDVO> getOnBeforeSaveAction() {
		return onBeforeSaveAction;
	}

	/**
	 * @param onBeforeSaveAction
	 *            the onBeforeSaveAction to set
	 */
	public void setOnBeforeSaveAction(Function<EventScriptDVO, EventScriptDVO> onBeforeSaveAction) {
		this.onBeforeSaveAction = onBeforeSaveAction;
	}

	/**
	 * @param onSave
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 2.
	 */
	protected void onComplete(EventScriptDVO onSave) {
		DialogUtil.showMessageDialog("success.!");
	}

}
