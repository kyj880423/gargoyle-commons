/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 10. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 *  코드 처리 관련 Helper 클래스
 *
 *  CodeArea클래스와 연관된 모든 공통처리내용이 구현된다.
 *
 * @author KYJ
 *
 */
public class SqlCodeAreaHelper extends CodeAreaHelper<CodeArea> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlCodeAreaHelper.class);

	/**
	 * @param codeArea
	 */
	public SqlCodeAreaHelper(CodeArea codeArea) {
		super(codeArea);
	}

	/**
	 * Sql 포멧처리.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 23.
	 */
	public void doSqlFormat() {
		String selectedText = codeArea.getSelectedText();
		if (ValueUtil.isNotEmpty(selectedText)) {
			// codeArea.replaceSelection(sqlFormatter.format(selectedText));
			replaceSelection(getSqlFormat(selectedText));
		} else {
			String format = getSqlFormat(codeArea.getText());
			// 2016.2.15 undo,redo처리를 위해 setContent로 변경
			// codeArea.clear();
			// codeArea.appendText(format);
			setContent(format);
		}
	}

	/**
	 * 키클릭 이벤트 처리
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 14.
	 * @param e
	 */
	public void codeAreaKeyClick(KeyEvent e) {
		
		// CTRL + SHIFT + F 포멧팅
		if (e.getCode() == KeyCode.F && (e.isControlDown() && e.isShiftDown())) {

			doSqlFormat();
			e.consume();
		}
		else
		{
			super.codeAreaKeyClick(e);
		}


	}

	/**
	 * 특정라인으로 이동처리하는 메소드
	 *
	 * 특정라인블록 전체를 선택처리함.
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param moveToLine
	 */
	public void moveToLine(int moveToLine) {
		codeMoveDeligator.moveToLine(moveToLine);
	}

	public void moveToLine(int moveToLine, int startCol) {
		codeMoveDeligator.moveToLine(moveToLine, startCol);
	}

	public void moveToLine(int moveToLine, int startCol, int endCol) {
		codeMoveDeligator.moveToLine(moveToLine, startCol, endCol);
	}

}
