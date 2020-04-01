/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 1. 4.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author KYJ
 *
 */
public class SQLKeywordFactory {

	private SQLKeywordFactory() {
		generate();
	}

	private static SQLKeywordFactory factory;

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 24.
	 * @return
	 */
	public static final SQLKeywordFactory getInstance() {
		if (factory == null)
			factory = new SQLKeywordFactory();
		return factory;
	}

	private List<String> sqlKeywords;
	private List<String> sqlFunctionKeywords;
	/* 대문자 */
	private String[] KEYWORDS = new String[] { "SELECT", "FROM", "GROUP", "BY", "WHERE", "JOIN", "AND", "UPDATE", "DELETE", "CREATE",
			"DROP", "SET", "NOT", "NULL", "INSERT into", "ALTER", "ORDER", "ENGINE", "COLLATE", "COMMENT", "FOREIGN", "KEY", "REFERENCES",
			"CONSTRAINT", "INDEX", "DEFAULT", "AUTO_INCREMENT", "TABLE", "PRIMARY", "DECLARE", "AS", "NVARCHAR", "BEGIN", "END", "CATCH",
			"TRY", "LEFT", "RIGHT", "OUTER", "INNER", "THEN", "DATETIME", "UNION", "ALL", "CASE", "WHEN", "NOLOCK", "LIKE", "WITH", "ON" };

	private String[] functionKeywords = new String[] { "MAX", "MIN", "SUM", "ISNULL" };

	/**
	 * @return the functionKeywords
	 */
	public List<String> getFunctionKeywords() {
		return sqlFunctionKeywords;
	}

	/**
	 * @param functionKeywords
	 *            the functionKeywords to set
	 */
	public void addFunctionKeywords(String... functionKeywords) {
		this.sqlFunctionKeywords.addAll(Stream.of(functionKeywords).collect(Collectors.toList()));
	}

	private void generate() {
		sqlKeywords = Stream.of(KEYWORDS).collect(() -> {
			return new ArrayList<String>();
		}, (collection, next) -> {
			collection.add(next);
			collection.add(next.toLowerCase());
		}, (collection, next) -> {
			collection.addAll(next);
		});

		sqlFunctionKeywords = Stream.of(functionKeywords).collect(() -> {
			return new ArrayList<String>();
		}, (collection, next) -> {
			collection.add(next);
			collection.add(next.toLowerCase());
		}, (collection, next) -> {
			collection.addAll(next);
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 24.
	 * @return
	 */
	public List<String> getKeywords() {
		return sqlKeywords;
	}

}
