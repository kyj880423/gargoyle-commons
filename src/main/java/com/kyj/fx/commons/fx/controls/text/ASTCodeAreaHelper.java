/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 12. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.Collection;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.Paragraph;

import com.kyj.fx.commons.fx.controls.srch.ResourceView;

/**
 * @author KYJ
 *
 */
public abstract class ASTCodeAreaHelper {

	/**
	 * 다이얼로그를 보여줄 뷰를 정의.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 9.
	 * @return
	 */
	public abstract <R> ResourceView<R> createResourceView();

	protected int getIndexOfValideWhiteSpace(String string) {
		for (int i = string.length() - 1; i >= 0; i--) {
			if (Character.isWhitespace(string.charAt(i))) {
				return i + 1;
			}
		}
		return 0;
	}

	protected Paragraph<Collection<String>, String, Collection<String>> currentParagraphRange(CodeArea textArea) {
		int currentParagraph = textArea.getCurrentParagraph();
		return textArea.getDocument().getParagraphs().get(currentParagraph);
	}

	protected String currentPragraph(CodeArea textArea) {
		Paragraph<Collection<String>, String, Collection<String>> x = currentParagraphRange(textArea);
		return x.getText();
//		return textArea.getSelectedText();
		
	}

	protected String markText(CodeArea textArea) {
		//replace api. because of this api return value wrong selection.
		//String string = currentPragraph(textArea);
		
		String string =  textArea.getSelectedText(); 
		int markTextColumnIndex = getIndexOfValideWhiteSpace(string);
		string = string.substring(markTextColumnIndex);
		return string;
	}
}
