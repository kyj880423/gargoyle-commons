/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 3. 16.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class XmlAutoCompleteQuery implements AutoCompleteQuery {

	Pattern tagPattern = Pattern.compile("^(<)([a-zA-Z_가-힣]+)(>)$");

	Pattern commentPattern = Pattern.compile("^<!--$");
	
	@Override
	public void onQuery(CodeArea codeArea, int position, String query) {

		Matcher m2 = commentPattern.matcher(query);
		if(m2.find())
		{
			codeArea.insertText(position, "-->");
			codeArea.moveSelectedText(position);
		}
		else
		{
			
			Matcher m = tagPattern.matcher(query);
			if (m.find()) {
				String group = m.group(2);
				codeArea.insertText(position, "</" + group + ">");
				codeArea.moveSelectedText(position);
			}
			
		}
		
	}

}
