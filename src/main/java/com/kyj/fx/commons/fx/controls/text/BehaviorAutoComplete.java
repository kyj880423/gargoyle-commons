/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 3. 13.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.LoggerFactory;

import javafx.scene.control.ListView;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class BehaviorAutoComplete extends AutoComplete {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BehaviorAutoComplete.class);

	Pattern tagPattern;

	String[] functionsKeywords = new String[] { "CreateObject", "Cstr", "Year", "Month", "Day", "Hour", "Minute", "Second", "InStr",
			"UCase", "DateAdd", "Trim", "Len" };

	String[] behaviorKeywords = new String[] { "CS_MSG", "CS_RESULT", "CS_PARAMETERVALUE", "CS_OMORDERUID", "CS_ORDERNUMBER", "CS_OPERATOR",
			"CS_SAGUID", "CS_PROCESSSEGMENTNAME", "CS_INSTRUCTIONINSTANCE", "CS_INSTRUCTIONNUMBER", "CS_ITEMINSTANCEGUID",
			"CS_PROCESSSEGMENTWEIGHINGSEQUENCE", "CS_OMSEGMENTSTEPNAME", "CS_OMSEGMENTSTEPGUID", "CS_PCSCONTROLLED", "CS_PHASESERVERURL",
			"CS_UNITPROCEDURESTEPNAME", "CS_OPERATIONSTEPNAME", "CS_RECIPEDOCUMENTID", "CS_BEHAVIOREXECUTED", "CS_RECIPENAME",
			"CS_PARENTITEMINSTANCEGUID", "CS_STEPXML", "CS_AUTOMATIONUNIT", "CS_AUTOMATIONGROUPNAME", "CS_AUTOMATIONGROUPTYPE",
			"CS_AUTOMATIONGROUPGUID", "CS_PRODUCTCODE", "CS_PRODUCTNAME", "CS_PRODUCTSIZE", "CS_PRODUCTUOM", "CS_ALGORITHMPATHTEXT",
			"CS_EVENTFAILED", "CS_EVENTPASSED", "CS_EXTERNALORDERGUID", "CS_EXTERNALITEMGUID", "CS_EXTERNALITEMCODE" };

	String[] vbKeywords = new String[] { "Dim", "If", "Else", "Then", "Select", "Case", "False", "Function", "Set", "Nothing", "Sub",
			"Exit", "Is", "End", "Do", "Not", "ElseIf", "Next", "On", "Error", "Resume", "Call", "While" };

	public BehaviorAutoComplete() {
		super();

		/* ignore case. */
		this.setMatcher((suggestion, query) -> {
			return suggestion.toUpperCase().startsWith(query.toUpperCase());
		});

		/* IF VALUE IS XML TAG, APPEND THE LAST TAG */
		this.setOnQuery(new XmlAutoCompleteQuery() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.kyj.fx.commons.fx.controls.text.XmlAutoComplete#onQuery(org.
			 * fxmisc.richtext.CodeArea, int, java.lang.String)
			 */
			@Override
			public void onQuery(CodeArea codeArea, int position, String query) {
				LOGGER.debug(query);
				super.onQuery(codeArea, position, query);
			}

		});

		// Behavior Keyword
		addAllKeywords(behaviorKeywords);

		// VB Keyword
		addAllKeywords(vbKeywords);

		// Base Function
		addAllKeywords(functionsKeywords);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.commons.fx.controls.text.AutoComplete#getSuggestionsList(java.
	 * lang.String)
	 */
	@Override
	public ListView<String> getSuggestionsList(String query) {
		return super.getSuggestionsList(query);
	}

}
