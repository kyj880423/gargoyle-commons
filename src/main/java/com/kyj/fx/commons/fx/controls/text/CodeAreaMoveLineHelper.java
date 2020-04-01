/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 10. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.Collection;
import java.util.List;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.Paragraph;

import javafx.collections.ObservableList;

/**
 * 2016-10-13 by kyj 텍스트의 라인보다 입력라인이 큰경우 발생되는 moveToLine IndexOfOfBoundException
 * 수정
 *
 * CodeArea클래스의 도움을 주는 Helper 클래스. 주로 이동관련 처리를 담당함.
 *
 * @author KYJ
 *
 */
public class CodeAreaMoveLineHelper {

	private CodeArea codeArea;

	public CodeAreaMoveLineHelper(CodeArea codeArea) {
		this.codeArea = codeArea;
	}

	public Integer getCurrentLine() {
		return codeArea.getCurrentParagraph();
	}

	/**
	 * 특정라인으로 이동처리하는 메소드
	 *
	 * 특정라인블록 전체를 선택처리함.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param moveToLine
	 */
	public void moveToLine(int moveToLine) {
		moveToLine(moveToLine, 0);
	}

	@SuppressWarnings("rawtypes")
	public void moveToLine(int moveToLine, int startCol) {
		int position = 0;
		int row = moveToLine - 1;
		ObservableList<Paragraph<Collection<String>, String, Collection<String>>> paragraphs = codeArea.getParagraphs();
		if (paragraphs.size() < moveToLine) {
//			moveToLine = paragraphs.size();
//			row = moveToLine - 1;
			return;
		}

		List<Paragraph<Collection<String>, String, Collection<String>>> subList = paragraphs.subList(0, row);
		for (Paragraph par : subList) {
			position += par.length() + 1; // account for line terminators
		}
		Paragraph<Collection<String>, String, Collection<String>> paragraph = paragraphs.get(row);

		int length = paragraph.length();

		int lineStartLength = position + startCol;
		int lineEndLength = position + length;

		// codeArea.moveTo(lineStartLength, lineEndLength);
		codeArea.selectRange(lineStartLength, lineEndLength);
		

//		int absolutePosition = codeArea.getAbsolutePosition( moveToLine - 1, startCol);
//		codeArea.scrollYToPixel(absolutePosition);
		
//		int currentParagraph = codeArea.getCurrentParagraph();
//		int absolutePosition = codeArea.getAbsolutePosition( currentParagraph , startCol);
//		codeArea.scrollYToPixel(absolutePosition);
		codeArea.showParagraphAtTop(row);
	}

	@SuppressWarnings("rawtypes")
	public void moveToLine(int moveToLine, int startCol, int endCol) {

		int position = 0;
		int row = moveToLine - 1;

		ObservableList<Paragraph<Collection<String>, String, Collection<String>>> paragraphs = codeArea.getParagraphs();
		if (paragraphs.size() < moveToLine)
			return;

		for (Paragraph par : paragraphs.subList(0, row)) {
			position += par.length() + 1; // account for line terminators
		}
		// Paragraph<Collection<String>> paragraph =
		// codeArea.getParagraphs().get(row);
		// int length = paragraph.length();

		int lineStartLength = position + startCol;
		int lineEndLength = position + endCol;
		codeArea.selectRange(lineStartLength, lineEndLength);
		// codeArea.moveTo(lineStartLength, lineEndLength);

		
//		int currentParagraph = codeArea.getCurrentParagraph();
//		int absolutePosition = codeArea.getAbsolutePosition( currentParagraph , endCol);
//		codeArea.scrollYToPixel(absolutePosition);
		codeArea.showParagraphAtTop(row);
	}

	
}
