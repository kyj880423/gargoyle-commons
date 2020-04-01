/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.ui.skin
 *	작성일   : 2016. 8. 21.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.ui.tabs;

import com.kyj.fx.commons.word.msword.ui.skin.EtcDefineComposite;
import com.kyj.fx.commons.word.msword.vo.EtcDefineDVO;
import com.kyj.fx.commons.word.msword.vo.ProgramSpecSVO;

import javafx.scene.layout.BorderPane;

/***************************
 * 
 * 기타정의사항
 * 
 * @author KYJ
 *
 ***************************/

public class EtcDefineTab extends AbstractSpecTab {

	private EtcDefineComposite etcDefineComposite;

	public EtcDefineTab(String title, SpecTabPane specTabPane) throws Exception {
		super(title, specTabPane);
	}

	//	public EtcDefineComposite(String string, SpecTabPane specTabPane) throws Exception {
	//	
	//		FxUtil.loadRoot(EtcDefineComposite.class, this);
	//	}

	@Override
	public BorderPane supplyNode() throws Exception {
		etcDefineComposite = new EtcDefineComposite(this);
		return etcDefineComposite;
	}

	@Override
	public void createDocumentAction(ProgramSpecSVO svo) {
		EtcDefineDVO etcDefineDVO = etcDefineComposite.getEtcDefineDVO();
		svo.setEtcDefineDVO(etcDefineDVO);
	}

}
