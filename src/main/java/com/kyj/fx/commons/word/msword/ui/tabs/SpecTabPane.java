/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.ui.controls
 *	작성일   : 2016. 2. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.ui.tabs;

import java.io.File;

import com.kyj.fx.commons.exceptions.GargoyleResourceException;
import com.kyj.fx.commons.word.msword.ui.model.SpecResource;
import com.kyj.fx.commons.word.msword.util.ProgramSpecUtil;
import com.kyj.fx.commons.word.msword.vo.ProgramSpecSVO;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/***************************
 *
 * 프로그램 사양서 처리를 위한 tabPane
 *
 * @author KYJ
 *
 ***************************/
public class SpecTabPane extends TabPane {

	/**
	 * 샤양서 생성시 필요한 resource 집합.
	 *
	 * @최초생성일 2016. 8. 5.
	 */
	private final SpecResource resource;

	/**
	 * 기본기능외 추가적인 tab을 더함.
	 *
	 * @param tabs
	 * @throws GargoyleResourceException
	 */
	public SpecTabPane(SpecResource resource, Tab... tabs) throws Exception {
		this(resource);
		getTabs().addAll(tabs);
	}

	public SpecTabPane(SpecResource resource) throws Exception {
		this.resource = resource;
		if (resource == null)
			throw new GargoyleResourceException("resource cannot be null.");

		getTabs().add(new ProjectInfoBaseInfoTab("사양서 기본 정보", this));
		getTabs().add(new TableInfoTab("테이블 정의", this));
		getTabs().add(new EtcDefineTab("기타정의 사항", this));

	}

	public SpecTabPane(ProgramSpecSVO svo) throws Exception {
		this(new SpecResource(svo));
	}

	/**
	 * @return the resource
	 */
	public final SpecResource getResource() {
		return resource;
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 사양서 생성 작업처리
	 * 
	 * @param location
	 * @return 성공하면 File값이 존재. 실패하면 null
	 ********************************/
	public final File createDocument(File location) {
		ProgramSpecSVO svo = new ProgramSpecSVO();

		ObservableList<Tab> tabs = getTabs();
		for (Tab t : tabs) {
			if (t instanceof AbstractSpecTab) {
				AbstractSpecTab abtab = (AbstractSpecTab) t;
				abtab.createDocumentAction(svo);
			}
		}
		if (ProgramSpecUtil.createDefault(svo, location)) {
			return location;
		}
		return null;
	}

}
