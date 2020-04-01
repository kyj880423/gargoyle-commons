/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

import com.kyj.fx.commons.word.msword.vo.EtcDefineDVO.TYPE;

/**
 * 기타정의사항에 포함시킬 정보
 * 
 * @author KYJ
 *
 */
public class Summarize {
	// 주제 영역에 해당하는지 여부
	public boolean subTitle;
	// TYPE, 기타정의 사항에 표현될 유형.. 그룹개념으로 사용
	public TYPE group;
	// MSWord에 표현할때 굵게 표현할지 정함
	public boolean bold;
	// MSWord에 표현할 텍스트
	public String text;

	public Summarize(boolean subTitle, TYPE group, boolean bold, String text) {
		super();
		this.subTitle = subTitle;
		this.group = group;
		this.bold = bold;
		this.text = text;
	}

	public Summarize(TYPE group, boolean bold, String text) {
		this(false, group, bold, text);
	}

}
