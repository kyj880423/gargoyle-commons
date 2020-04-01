/**
 * 
 */
package com.kyj.fx.commons.fx.controls.text;

import java.util.Collection;

import org.fxmisc.richtext.CaretSelectionBind;
import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;

import javafx.scene.control.IndexRange;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * 
 * CTRL + J를 클릭하면 자동 주석 <br/>
 * 
 * @author KYJ
 *
 */
public class CodeAreaApplyCommentHelper<T extends CodeArea> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeAreaApplyCommentHelper.class);
	protected T codeArea;

	public CodeAreaApplyCommentHelper(T codeArea) {
		super();
		this.codeArea = codeArea;
	}
	
	/**
	 * comment Text.
	 * @최초생성일 2019. 9. 4.
	 */
	private String commentString = "//";

	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 4. 
	 * @return
	 */
	public String getCommentString() {
		return commentString;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 4. 
	 * @param commentString
	 */
	public void setCommentString(String commentString) {
		this.commentString = commentString;
	}

	/**
	 * 
	 */
	public void init() {
		codeArea.addEventHandler(KeyEvent.KEY_RELEASED, this::codeAreaReplaceCommentText);
	}


	/**
	 * CTRL + SLASH를 클릭시 해당 행을 코멘트 처리 한다. <br/>
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 4. 
	 * @param e
	 */
	public void codeAreaReplaceCommentText(KeyEvent e) {
		if (e.getCode() == KeyCode.SLASH && e.isControlDown() && !e.isAltDown() && !e.isShiftDown()) {
			if (e.isConsumed())
				return;
			e.consume();
			try {
				CaretSelectionBind<Collection<String>, String, Collection<String>> caretSelectionBind = this.codeArea
						.getCaretSelectionBind();
				// int subStartIdx = caretSelectionBind.getRange().getStart();
				int startIdx = getLineStartIndex(caretSelectionBind);
				int endIdx = getLineEndIndex(caretSelectionBind);

				
				
				if(startIdx > endIdx)
				{
					this.codeArea.replaceText(new IndexRange(caretSelectionBind.getRange().getStart(), 
							caretSelectionBind.getRange().getEnd()), commentString);
					return;
				}
				
				StringBuffer sb = new StringBuffer();
				String text = this.codeArea.getText(startIdx, endIdx); // .trim();

				String[] split = text.split("\n");
				
				
			
					int len = split.length;
					int limit = len - 1;
					for (int i=0; i<len; i++ ) {					 
						sb.append(commentString).append(split[i]);
						if(i != limit)
							sb.append("\n");
					}
					
				this.codeArea.replaceText(new IndexRange(startIdx, endIdx), sb.toString());
			} catch (Exception ex) {
				LOGGER.error(ValueUtil.toString(ex));				
			}

		}
	}

	/**
	 * 선택된 행의 첫번째 컬럼 위치 시작 인덱스를 리턴한다 <br/>
	 * 
	 * @param caretSelectionBind
	 * @return
	 */
	public int getLineStartIndex(CaretSelectionBind<Collection<String>, String, Collection<String>> caretSelectionBind) {
		IndexRange caretRange = caretSelectionBind.getRange();
		int startIdx = caretRange.getStart();
		while (startIdx >= 0) {
			if ("\n".equals(this.codeArea.getText(startIdx, startIdx + 1))) {
				startIdx = startIdx + 1;
				break;
			}
			startIdx--;
		}
		return startIdx;
	}

	/**
	 * 선택된 행의 첫번째 컬럼 위치 끝 인덱스를 리턴한다 <br/>
	 * 
	 * @param caretSelectionBind
	 * @return
	 */
	public int getLineEndIndex(CaretSelectionBind<Collection<String>, String, Collection<String>> caretSelectionBind) {
		IndexRange caretRange = caretSelectionBind.getRange();
		int endIdx = caretRange.getEnd();
		int len = this.codeArea.getText().length();
		while (endIdx < len) {
			if ("\n".contentEquals(this.codeArea.getText(endIdx, endIdx + 1))) {
				break;
			}
			endIdx++;
		}
		return endIdx;
	}
}
