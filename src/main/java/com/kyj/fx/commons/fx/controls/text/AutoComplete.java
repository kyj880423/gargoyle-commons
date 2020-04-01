/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2019. 3. 13.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fxmisc.richtext.CodeArea;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

/**
 *  자동완성기능을 구현하는 객체 <br/>
 *  
 *  keywrods 변수를 통해 자동완성에 필요한 요소들을 등록. <br/>
 *  
 *  
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class AutoComplete {

	private Set<String> keywords;

	private ObjectProperty<AutoCompleteQuery> onQuery = new SimpleObjectProperty<>();

	public AutoComplete() {
		keywords = Collections.newSetFromMap(new ConcurrentHashMap<>());
	}

	protected int wordLengthLimit = 45;

	/**
	 * @return the keywords
	 */
	public Set<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public synchronized void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 13.
	 * @param keyword
	 */
	public void addKeyword(String keyword) {
		this.keywords.add(keyword);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 13.
	 * @param keywords
	 */
	public void addAllKeywords(List<String> keywords) {
		if (keywords != null)
			this.keywords.addAll(keywords);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 13.
	 * @param keywords
	 */
	public void addAllKeywords(String... keywords) {
		if (keywords == null)
			return;
		addAllKeywords(Stream.of(keywords).collect(Collectors.toList()));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16.
	 * @param codeArea
	 * @param position
	 * @return
	 */
	public final String getQuery(CodeArea codeArea, int position) {
		// 공백이면 skip
		if(" ".equals(codeArea.getText().substring(position -1, position)))
			return " ";
		
		int limit = (position > wordLengthLimit) ? wordLengthLimit : position;
		String keywords = codeArea.getText().substring(position - limit, position);
		
		//특수만자 제거. Behavior 및 XML 특수문자에서 영향받는데 특수문자도 활용하므로 아래 \\p{Punct} 정규식은 제거.
//		keywords = keywords.replaceAll("\\p{Punct}", " ").trim();
		keywords = keywords.replaceAll("\\n", " ").trim();
		int last = keywords.lastIndexOf(" ");
		String substring = keywords.substring(last + 1);
		if (onQuery.get() != null)
			onQuery.get().onQuery(codeArea, position, substring);
//		System.out.println(substring);
		return substring;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16. 
	 * @param query
	 * @return
	 */
	public ListView<String> getSuggestionsList(String query) {
		List<String> suggestions = getQuerySuggestions(query);
		ListView<String> suggestionsList = new ListView<>();
		suggestionsList.getItems().clear();
		suggestionsList.getItems().addAll(FXCollections.observableList(suggestions));
		int listViewLength = (suggestions.size() * 30 > 120) ? 120 : suggestions.size() * 30;
		suggestionsList.setPrefHeight(listViewLength);
		return suggestionsList;
	}

	/**
	 * @최초생성일 2019. 3. 16.
	 */
	private BiPredicate<String, String> matcher = (suggestion, query) -> suggestion.startsWith(query);

	/**
	 * @return the matcher
	 */
	public BiPredicate<String, String> getMatcher() {
		return matcher;
	}

	/**
	 * descibe the match value between query and suggestion <br/>
	 * 
	 * @param matcher
	 *            the matcher to set
	 */
	public void setMatcher(BiPredicate<String, String> matcher) {
		this.matcher = matcher;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 13.
	 * @param query
	 * @return
	 */
	private final List<String> getQuerySuggestions(String query) {
		return keywords.parallelStream().filter(suggestion -> matcher.test(suggestion, query)).collect(Collectors.toList());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16. 
	 * @return
	 */
	public final ObjectProperty<AutoCompleteQuery> onQueryProperty() {
		return this.onQuery;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16. 
	 * @return
	 */
	public final AutoCompleteQuery getOnQuery() {
		return this.onQueryProperty().get();
	}

	/**
	 * this function can be decribe the custom keyword action <br/>
	 *  
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 16. 
	 * @param onQuery
	 */
	public final void setOnQuery(final AutoCompleteQuery onQuery) {
		this.onQueryProperty().set(onQuery);
	}

}
