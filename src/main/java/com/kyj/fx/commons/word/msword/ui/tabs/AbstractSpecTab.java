/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.ui.controls
 *	작성일   : 2016. 2. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.ui.tabs;

import com.kyj.fx.commons.functions.SupplySkin;
import com.kyj.fx.commons.word.msword.ui.model.SpecResource;
import com.kyj.fx.commons.word.msword.vo.ProgramSpecSVO;

import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 * 사양서의 기본정보를 담는 AbstractTab객체
 *
 * @author KYJ
 *
 */
public abstract class AbstractSpecTab extends Tab implements SupplySkin<BorderPane> {

	private SpecTabPane specTabPane;

	public AbstractSpecTab(String title, SpecTabPane specTabPane) throws Exception {
		this.specTabPane = specTabPane;
		setContent(supplyNode());
		setText(title);
		setClosable(false);
	}

	//	public abstract String getTitle();

	public SpecTabPane getSpecTabPane() {
		return specTabPane;
	}

	public final SpecResource getSpecResource() {
		return this.specTabPane.getResource();
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 사양서 생성하기 위해 SVO에 필요값을 SET 처리함.
	 * 
	 * @param svo
	 ********************************/
	public abstract void createDocumentAction(ProgramSpecSVO svo);

}
