/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

import java.util.ArrayList;
import java.util.List;

import com.kyj.fx.commons.word.msword.interfaces.ITextureBlock;

/**
 * 사용자 정의사항을 처리하기 위한 데이터셋
 * 
 * @author KYJ
 *
 */
public class EtcDefineDVO {
	private List<String> messageList = new ArrayList<String>();
	private List<String> codeMasterList = new ArrayList<String>();
	private List<String> chkAuthList = new ArrayList<String>();
	private List<String> relativeFactoryList = new ArrayList<String>();
	private List<String> effectModuleList = new ArrayList<String>();
	private List<String> cautionTestList = new ArrayList<String>();
	private List<String> cautionDeployList = new ArrayList<String>();
	private List<String> etcList = new ArrayList<String>();

	/**
	 * 기타 정의사항에서 사용할 유형
	 * 
	 * @author KYJ
	 *
	 */
	public enum TYPE {
		TITLE("■ 기타 정의 사항"), MESSAGE("■ 메세지 처리"), CODE_MASTER("■ Code 마스터 테이블 정의"), CHK_AUTH("■ 권한 체크"), RELATIVE_FACTORY(
				"■ 관련 사업장 (법인/사업장 특화)"), EFFECT_MODULE("■ 타 모듈 영향도"), CAUTION_TEST("■ 테스트시, 주의사항"), CAUTION_DEPLOY("■ 운영 이관 시, 주의사항"), ETC(
				"■ 기타");
		private String text;

		// 열거 값에 (String) 값 span 에 대입
		TYPE(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	public void clear() {
		messageList.clear();
		codeMasterList.clear();
		chkAuthList.clear();
		relativeFactoryList.clear();
		effectModuleList.clear();
		cautionTestList.clear();
		cautionDeployList.clear();
		etcList.clear();
	}

	/**
	 * 타입에 맞는 배열 반환
	 * 
	 * @param type
	 * @return
	 */
	public List<String> getList(TYPE type) {
		List<String> rsltList = null;
		switch (type) {
		case MESSAGE:
			rsltList = messageList;
			break;
		case CODE_MASTER:
			rsltList = codeMasterList;
			break;
		case CHK_AUTH:
			rsltList = chkAuthList;
			break;
		case RELATIVE_FACTORY:
			rsltList = relativeFactoryList;
			break;
		case EFFECT_MODULE:
			rsltList = effectModuleList;
			break;
		case CAUTION_TEST:
			rsltList = cautionTestList;
			break;
		case CAUTION_DEPLOY:
			rsltList = cautionDeployList;
			break;
		case ETC:
			rsltList = etcList;
			break;
		case TITLE:
			break;
		default:
			break;
		}
		return rsltList;

	}

	public List<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}

	public List<String> getCodeMasterList() {
		return codeMasterList;
	}

	public void setCodeMasterList(List<String> codeMasterList) {
		this.codeMasterList = codeMasterList;
	}

	public List<String> getChkAuthList() {
		return chkAuthList;
	}

	public void setChkAuthList(List<String> chkAuthList) {
		this.chkAuthList = chkAuthList;
	}

	public List<String> getRelativeFactoryList() {
		return relativeFactoryList;
	}

	public void setRelativeFactoryList(List<String> relativeFactoryList) {
		this.relativeFactoryList = relativeFactoryList;
	}

	public List<String> getEffectModuleList() {
		return effectModuleList;
	}

	public void setEffectModuleList(List<String> effectModuleList) {
		this.effectModuleList = effectModuleList;
	}

	public List<String> getCautionTestList() {
		return cautionTestList;
	}

	public void setCautionTestList(List<String> cautionTestList) {
		this.cautionTestList = cautionTestList;
	}

	public List<String> getCautionDeployList() {
		return cautionDeployList;
	}

	public void setCautionDeployList(List<String> cautionDeployList) {
		this.cautionDeployList = cautionDeployList;
	}

	public List<String> getEtcList() {
		return etcList;
	}

	public void setEtcList(List<String> etcList) {
		this.etcList = etcList;
	}

	/**
	 * MSWord에 작성되기 편하도록 간소화시킴.
	 * 
	 * @return
	 */
	public List<Summarize> toSummarize() {
		return toSummarize(false);
	}

	public List<Summarize> toSummarize(boolean autoNumbering) {
		final List<Summarize> arrayList = new ArrayList<Summarize>();
		// [START] #### ⓝ 4. 기타 정의 사항 ####

		arrayList.add(new Summarize(true, TYPE.TITLE, true, TYPE.TITLE.getText()));
		arrayList.add(new Summarize(true, TYPE.MESSAGE, true, TYPE.MESSAGE.getText()));

		doNotEmptyTextureScope(messageList, new ITextureBlock() {

			@Override
			public void doScope(String text) {
				arrayList.add(new Summarize(TYPE.MESSAGE, false, text));
			}

			@Override
			public void emptyThen() {
				arrayList.add(new Summarize(TYPE.MESSAGE, false, "N/A"));
			}
		}, autoNumbering);
		arrayList.add(new Summarize(true, TYPE.CODE_MASTER, true, TYPE.CODE_MASTER.getText()));
		doNotEmptyTextureScope(codeMasterList, new ITextureBlock() {

			@Override
			public void doScope(String text) {
				arrayList.add(new Summarize(TYPE.CODE_MASTER, false, text));
			}

			@Override
			public void emptyThen() {
				arrayList.add(new Summarize(TYPE.CODE_MASTER, false, "N/A"));
			}
		}, autoNumbering);
		arrayList.add(new Summarize(true, TYPE.CHK_AUTH, true, TYPE.CHK_AUTH.getText()));
		doNotEmptyTextureScope(chkAuthList, new ITextureBlock() {

			@Override
			public void doScope(String text) {
				arrayList.add(new Summarize(TYPE.CHK_AUTH, false, text));
			}

			@Override
			public void emptyThen() {
				arrayList.add(new Summarize(TYPE.CHK_AUTH, false, "N/A"));
			}
		}, autoNumbering);
		arrayList.add(new Summarize(true, TYPE.RELATIVE_FACTORY, true, TYPE.RELATIVE_FACTORY.getText()));
		doNotEmptyTextureScope(relativeFactoryList, new ITextureBlock() {

			@Override
			public void doScope(String text) {
				arrayList.add(new Summarize(TYPE.RELATIVE_FACTORY, false, text));
			}

			@Override
			public void emptyThen() {
				arrayList.add(new Summarize(TYPE.RELATIVE_FACTORY, false, "N/A"));
			}
		}, autoNumbering);
		arrayList.add(new Summarize(true, TYPE.EFFECT_MODULE, true, TYPE.EFFECT_MODULE.getText()));
		doNotEmptyTextureScope(effectModuleList, new ITextureBlock() {

			@Override
			public void doScope(String text) {
				arrayList.add(new Summarize(TYPE.EFFECT_MODULE, false, text));
			}

			@Override
			public void emptyThen() {
				arrayList.add(new Summarize(TYPE.EFFECT_MODULE, false, "N/A"));
			}
		}, autoNumbering);
		arrayList.add(new Summarize(true, TYPE.CAUTION_TEST, true, TYPE.CAUTION_TEST.getText()));
		doNotEmptyTextureScope(cautionTestList, new ITextureBlock() {

			@Override
			public void doScope(String text) {
				arrayList.add(new Summarize(TYPE.CAUTION_TEST, false, text));
			}

			@Override
			public void emptyThen() {
				arrayList.add(new Summarize(TYPE.CAUTION_TEST, false, "N/A"));
			}
		}, autoNumbering);
		arrayList.add(new Summarize(true, TYPE.CAUTION_DEPLOY, true, TYPE.CAUTION_DEPLOY.getText()));
		doNotEmptyTextureScope(cautionDeployList, new ITextureBlock() {

			@Override
			public void doScope(String text) {
				arrayList.add(new Summarize(TYPE.CAUTION_DEPLOY, false, text));
			}

			@Override
			public void emptyThen() {
				arrayList.add(new Summarize(TYPE.CAUTION_DEPLOY, false, "N/A"));
			}
		}, autoNumbering);
		arrayList.add(new Summarize(true, TYPE.ETC, true, TYPE.ETC.getText()));
		doNotEmptyTextureScope(etcList, new ITextureBlock() {

			@Override
			public void doScope(String text) {
				arrayList.add(new Summarize(TYPE.ETC, false, text));
			}

			@Override
			public void emptyThen() {
				arrayList.add(new Summarize(TYPE.ETC, false, "N/A"));
			}
		}, autoNumbering);

		// [END] #### ⓝ 4. 기타 정의 사항 ####

		return arrayList;
	}

	/**
	 * list의 데이터가 비어있지 않는 데이터를 block에 전달.
	 * 
	 * @param list
	 * @param block
	 */
	public static void doNotEmptyTextureScope(List<String> list, ITextureBlock block) {
		doNotEmptyTextureScope(list, block, true);
	}

	/**
	 * list의 데이터가 비어있지 않는 데이터를 block에 전달.
	 * 
	 * @param list
	 * @param block
	 * @param autoNumbering
	 *            텍스트에 자동으로 넘버링값을 추가할지 유무
	 */
	public static void doNotEmptyTextureScope(List<String> list, ITextureBlock block, boolean autoNumbering) {
		if (list == null || list.isEmpty()) {
			block.emptyThen();
			return;
		}

		int seq = 1;
		for (String str : list) {
			if (str != null) {
				if (autoNumbering) {
					block.doScope(String.valueOf(seq++).concat(". ").concat(str));
				} else {
					block.doScope(str);
				}
			}

		}
	}
}
