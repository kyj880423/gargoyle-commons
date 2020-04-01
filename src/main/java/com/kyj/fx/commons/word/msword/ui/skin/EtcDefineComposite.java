/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.ui.skin
 *	작성일   : 2016. 8. 21.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.ui.skin;

import java.util.ArrayList;
import java.util.List;

import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.word.msword.ui.tabs.AbstractSpecTab;
import com.kyj.fx.commons.word.msword.vo.EtcDefineDVO;
import com.kyj.fx.fxloader.FXMLController;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/***************************
 *
 * 기타정의사항
 *
 * @author KYJ
 *
 ***************************/
@FXMLController(value = "EtcDefineView.fxml", isSelfController = true)
public class EtcDefineComposite extends BorderPane {

	private AbstractSpecTab abtabPane;

	@FXML
	private TextArea txtCodeMasterTable, txtMessage, txtAuthChk, txtRelFct, txtModuleEffect, txtTestCaut, txtDeployCaut, txtEtc;
	@FXML
	private GridPane gpContent;
	private EtcDefineDVO etcDefineDVO = new EtcDefineDVO();

	public EtcDefineComposite(AbstractSpecTab abtabPane) throws Exception {
		this.abtabPane = abtabPane;
		FxUtil.loadRoot(EtcDefineComposite.class, this);
	}

	@FXML
	public void initialize() {
		//		txtCodeMasterTable.setOnKeyPressed(ev -> {
		//			if (ev.isControlDown() && ev.getCode() == KeyCode.ENTER) {
		//				System.out.println("dd2");
		//			}
		//		});
		//		txtCodeMasterTable.addEventFilter(KeyEvent.KEY_PRESSED, ev -> {
		//			if (ev.isControlDown() && ev.getCode() == KeyCode.ENTER) {
		//				System.out.println("dd1");
		//			}
		//		});

//		txtCodeMasterTable.addEventHandler(KeyEvent.KEY_PRESSED, tabIndexKeyEvent);

	}

	@FXML
	public void txtOnKeyEvent(KeyEvent event) {
		tabIndexKeyEvent.handle(event);
	}

	/**
	 * CTRL + ENTER 키를 누를경우 다음 TabIndex로 이동처리하는 이벤트를 정의한다.
	 * @최초생성일 2016. 8. 22.
	 */
	private final EventHandler<? super KeyEvent> tabIndexKeyEvent = ev -> {

		if ((ev.isControlDown() && ev.getCode() == KeyCode.ENTER) || ev.getCode() == KeyCode.TAB) {
			Object source = ev.getSource();
			if (source instanceof TextArea) {
				TextArea _source = (TextArea) source;
				ObservableList<Node> childrenUnmodifiable = _source.getParent().getChildrenUnmodifiable();
				int current = childrenUnmodifiable.indexOf(_source);
				int MAX = childrenUnmodifiable.size();
				int nextIdx = current + 1;

				if (MAX <= nextIdx)
					nextIdx = 0;

				for (int idx = nextIdx; idx < MAX; idx++) {
					Node node = childrenUnmodifiable.get(idx);
					if (node instanceof TextArea) {
						TextArea tmp = (TextArea) node;
						tmp.requestFocus();
						break;
					}
				}
				ev.consume();
			}

			//				System.out.println("dd");
		}
	};

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 기타 정의사항 모델 리턴.
	 *
	 * @return
	 ********************************/
	public EtcDefineDVO getEtcDefineDVO() {
		etcDefineDVO.setCodeMasterList(getTxtCodeMasterTable());
		etcDefineDVO.setMessageList(getTxtMessage());
		etcDefineDVO.setChkAuthList(getTxtAuthChk());
		etcDefineDVO.setRelativeFactoryList(getTxtRelFct());
		etcDefineDVO.setEffectModuleList(getTxtModuleEffect());
		etcDefineDVO.setCautionTestList(getTxtTestCaut());
		etcDefineDVO.setCautionDeployList(getTxtDeployCaut());
		etcDefineDVO.setEtcList(getTxtEtc());
		return etcDefineDVO;
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 코드 마스터 정의
	 *
	 * @return
	 ********************************/
	List<String> getTxtCodeMasterTable() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(txtCodeMasterTable.getText());
		return arrayList;
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 메세지 처리
	 *
	 * @return
	 ********************************/
	List<String> getTxtMessage() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(txtMessage.getText());
		return arrayList;
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 권한 체크
	 *
	 * @return
	 ********************************/
	List<String> getTxtAuthChk() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(txtAuthChk.getText());
		return arrayList;
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 관련 사업장
	 *
	 * @return
	 ********************************/
	List<String> getTxtRelFct() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(txtRelFct.getText());
		return arrayList;
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 모듈 영향도
	 *
	 * @return
	 ********************************/
	List<String> getTxtModuleEffect() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(txtModuleEffect.getText());
		return arrayList;
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 테스트시 주의사항
	 *
	 * @return
	 ********************************/
	List<String> getTxtTestCaut() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(txtTestCaut.getText());
		return arrayList;
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 운영이관시 주의사항
	 *
	 * @return
	 ********************************/
	List<String> getTxtDeployCaut() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(txtDeployCaut.getText());
		return arrayList;
	}

	/********************************
	 * 작성일 : 2016. 8. 21. 작성자 : KYJ
	 *
	 * 기타
	 *
	 * @return
	 ********************************/
	List<String> getTxtEtc() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(txtEtc.getText());
		return arrayList;
	}

}
