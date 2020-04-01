/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 18.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ReleaseScriptTab extends Tab {

	private CodeArea codeArea;
	private EventScriptDVO eventScriptDVO;
	private ContextMenu ctm;

	public ReleaseScriptTab(EventScriptDVO eventScriptDVO, CodeArea codeArea) {
		super(eventScriptDVO.getName(), new VirtualizedScrollPane<CodeArea>(codeArea));
		this.eventScriptDVO = eventScriptDVO;
		this.codeArea = codeArea;
		ctm = new ContextMenu();
		ctm.getItems().addAll(createContextMenu());
		this.setContextMenu(ctm);
	}

	/**
	 * @return the eventScriptDVO
	 */
	public EventScriptDVO getEventScriptDVO() {
		return eventScriptDVO;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 18.
	 * @return
	 */
	protected List<MenuItem> createContextMenu() {

		MenuItem closeAll = new MenuItem("Close All");
		closeAll.setOnAction(ev -> {
			TabPane tabPane = this.getTabPane();
			ObservableList<Tab> tabs = tabPane.getTabs();
			tabs.clear();
		});

		MenuItem closeOther = new MenuItem("Close Others");
		closeOther.setOnAction(ev -> {
			TabPane tabPane = this.getTabPane();
			Tab selectedItem = tabPane.getSelectionModel().getSelectedItem();
			ObservableList<Tab> tabs = tabPane.getTabs();
			List<Tab> collect = tabs.stream().filter(t -> t != selectedItem).collect(Collectors.toList());
			tabPane.getTabs().removeAll(collect);
		});

		return Arrays.asList(closeAll, closeOther);
	}

	/**
	 * CodeArea의 텍스트를 데이터셋으로 교체. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 18.
	 */
	public void updateDataset() {
		String text = getCodeArea().getText();
		this.eventScriptDVO.setScriptContent(text);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 19.
	 * @param scriptContent
	 */
	public void replaceScriptCode(String scriptContent) {
		this.codeArea.replaceText(scriptContent);
		this.eventScriptDVO.setScriptContent(scriptContent);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 18.
	 * @return
	 */
	public CodeArea getCodeArea() {
		return this.codeArea;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return eventScriptDVO.toString();
	}

}
