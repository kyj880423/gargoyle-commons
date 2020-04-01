package com.kyj.fx.commons.fx.controls.text;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import javafx.scene.Node;

/**
 *
 * 자동화 코멘트 화면에서 필요한 텍스팅 포멧처리
 *
 * 자동화된 코멘트가 달린 라인이라는걸 표현하기 위해
 * getLineFactory()함수를 오버라이드
 * @author KYJ
 *
 */
public class JavaTextAreaForAutoComment extends JavaTextArea {

	/**
	 * @param appendLineKeyword
	 */
	public JavaTextAreaForAutoComment() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.voeditor.visual.component.text.JavaTextArea#getLineFactory()
	 */
	@Override
	public IntFunction<Node> getLineFactory() {
		return MarkedLineNumberFactory.get(this.getCodeArea());
	}

	@Override
	protected void setContent(List<String> readLines) {

		String collect = readLines.stream().collect(Collectors.joining("\n"));
		setContent(collect);
	}

}
