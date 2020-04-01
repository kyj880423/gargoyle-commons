package com.kyj.fx.commons.fx.controls.text;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.threads.ExecutorDemons;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.GargoyleOpenExtension;

import javafx.concurrent.Task;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class SqlKeywords extends BorderPane implements GargoyleOpenExtension {

	private static Logger LOGGER = LoggerFactory.getLogger(SqlKeywords.class);
	// private static final String[] KEYWORDS = new String[] {
	// /* 대문자 */
	// "SELECT", "FROM", "GROUP", "BY", "WHERE", "JOIN", "AND", "UPDATE",
	//
	// "DELETE", "CREATE", "DROP", "SET", "NOT NULL", "INSERT into", "ALTER",
	// "ORDER",
	// /* 소문자 */
	// "select", "from", "group", "by", "where", "join", "and", "update",
	// "delete", "create", "drop", "set", "not null", "insert into",
	// "alter", "order" };

	// 2016-08-27 정규식 (?i) 추가옵션을 주는경오 대소문자 구분을 무시한다.
	private static final String KEYWORD_PATTERN = "(?i)\\b(" + String.join("|", SQLKeywordFactory.getInstance().getKeywords()) + ")\\b";
	private static final String FUNCTION_PATTERN = "(?i)\\b(" + String.join("|", SQLKeywordFactory.getInstance().getFunctionKeywords())
			+ ")\\b";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	// private static final String COMMENT_PATTERN = "//[^\n]*" + "|" +
	// "/\\*(.|\\R)*?\\*/";
	private static final String COMMENT_PATTERN = "(?:/\\*[^;]*?\\*/)|(?:--[^\\n]*)";

	private static final Pattern PATTERN = Pattern.compile("(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "| (?<FUNCTION>" + FUNCTION_PATTERN + ")"
			+ "| (?<PAREN>" + PAREN_PATTERN + ")" + "|(?<BRACE>" + BRACE_PATTERN + ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
			+ "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")" + "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<COMMENT>" + COMMENT_PATTERN + ")");

	private CodeArea codeArea;

	/**
	 * 코드 처리관련 Helper 클래스
	 * 
	 * @최초생성일 2016. 10. 6.
	 */
	private CodeAreaHelper codeHelperDeligator;

	private Label lblLineInfo = new Label();

	public CodeArea getCodeArea() {
		return codeArea;
	}

	/**
	 * 인스턴스의 수와 상관없이 무조건 1개의 서비스로 사용. 처음에 생성자에 생성했는데, 스레드수가 너무 많이 늘어남. 사실 이 클래스는
	 * 어플리케이션당 하나만 사용하여 관리해도 상관없다고 판단.
	 * 
	 * @최초생성일 2016. 10. 18.
	 */
	private static final ExecutorService executor = ExecutorDemons.getGargoyleSystemExecutorSerivce();

	public SqlKeywords() {

		codeArea = new GargoyleCodeArea();
		VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);

		codeHelperDeligator = new SqlCodeAreaHelper(codeArea);

		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

		codeArea.richChanges().filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
				.successionEnds(Duration.ofMillis(500)).supplyTask(this::computeHighlightingAsync).awaitLatest(codeArea.richChanges())
				.filterMap(t -> {
					if (t.isSuccess()) {
						return Optional.of(t.get());
					} else {
						t.getFailure().printStackTrace();
						return Optional.empty();
					}
				}).subscribe(this::applyHighlighting);

		/**
		 * CodeArea 'Tab' Size handler
		 *************************************************************/
		// 17.01.14 Deprecated
		// codeArea.addEventHandler(KeyEvent.KEY_PRESSED, new
		// CodeAreaTabSizeHandler(codeArea));

		// 마우스 클릭이벤트 정의
		codeArea.addEventHandler(KeyEvent.KEY_PRESSED, this::codeAreaKeyClick);

		// codeArea.setOnKeyPressed(this::codeAreaKeyClick);
		//
		// codeArea.setOnMouseClicked(defaultSelectionHandler);

		// 선택라인정보 보여주는 기능 추가.
		codeArea.selectionProperty().addListener((oba, oldval, newval) -> {
			int start = newval.getStart();
			int end = newval.getEnd();
			int caretColumn = codeArea.getCaretColumn();

			String format = String.format("line : %d selectionStart : %d selectionEnd : %d column : %d ",
					codeArea.getCurrentParagraph() + 1, start + 1, end + 1, caretColumn + 1);

			lblLineInfo.setText(format);
		});

		codeArea.appendText("");
		lblLineInfo.setPrefHeight(USE_COMPUTED_SIZE);
		this.setCenter(scrollPane);
		this.setBottom(lblLineInfo);
		// this.getChildren().add();
		this.getStylesheets().add(SqlKeywords.class.getResource("sql-keywords.css").toExternalForm());
	}

	/**
	 * @return the codeHelperDeligator
	 */
	public final CodeAreaHelper getCodeHelperDeligator() {
		return codeHelperDeligator;
	}

	/**
	 * 키클릭 이벤트 처리
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 12. 14.
	 * @param e
	 */
	public void codeAreaKeyClick(KeyEvent e) {
		codeHelperDeligator.codeAreaKeyClick(e);
	}

	/**
	 * Sql 포멧처리.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 9. 23.
	 */
	public void doSqlFormat() {
		codeHelperDeligator.doSqlFormat();
	}

	public String getSqlFormat(String sql) {
		return codeHelperDeligator.getSqlFormat(sql);
	}

	private void setContent(int start, int end, String text) {
		codeHelperDeligator.setContent(start, end, text);
	}

	public void replaceSelection(String selection) {
		codeHelperDeligator.replaceSelection(selection);
	}

	public void setContent(String content) {
		codeHelperDeligator.setContent(content);
	}

	public void appendContent(String content) {
		codeHelperDeligator.appendContent(content);
	}

	public String getSelectedText() {
		return codeHelperDeligator.getSelectedText();
	}

	public String getText() {
		return codeHelperDeligator.getText();
	}

	public IndexRange getSelection() {
		return codeHelperDeligator.getSelection();
	}

	public Integer getCurrentLine() {
		return codeHelperDeligator.getCurrentLine();
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String _text) {
		String text = StringUtils.replace(_text, "\"", "'");

		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find(lastKwEnd)) {

			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("FUNCTION") != null ? "function"
							: matcher.group("PAREN") != null ? "paren"
									: matcher.group("BRACE") != null ? "brace"
											: matcher.group("BRACKET") != null ? "bracket"
													: matcher.group("SEMICOLON") != null ? "semicolon"
															: matcher.group("STRING") != null ? "string"
																	: matcher.group("COMMENT") != null ? "comment" : null; /*
																															 * never
																															 * happens
																															 */
			assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

	private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
		String text = codeArea.getText();
		Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
			@Override
			protected StyleSpans<Collection<String>> call() throws Exception {
				return computeHighlighting(text);
			}
		};

		executor.execute(task);

		return task;
	}

	private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
		codeArea.setStyleSpans(0, highlighting);
	}

	// TODO :: () 안에 문자들을 가식성이 좋도록 표시... StyleSpans이 중복되어 적용이 안됨..
	// computeHighlighting 메소드와 합쳐야함.
	// css 파일에서 -fx-background-color 기능이 동작 하지 않음..

	// private static StyleSpans<Collection<String>> groupBackgroundColor(String
	// text, int point) {
	//
	// StyleSpansBuilder<Collection<String>> spansBuilder = new
	// StyleSpansBuilder<>();
	//
	// Pattern pattern = Pattern.compile("\\([^)]+\\)");
	// Matcher matcher = pattern.matcher(text);
	// int lastKwEnd = 0;
	//
	// while (matcher.find()) {
	// String styleClass = "unGroup";
	// if (matcher.start() < point && matcher.end() > point) {
	// styleClass = "inGroup";
	// }
	// spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
	// spansBuilder.add(Collections.singleton(styleClass), matcher.end() -
	// matcher.start());
	// lastKwEnd = matcher.end();
	// }
	//
	// spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
	// return spansBuilder.create();
	// }

	public void setEditable(boolean editable) {
		codeArea.editableProperty().set(editable);
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
		codeHelperDeligator.moveToLine(moveToLine);
	}

	public void moveToLine(int moveToLine, int startCol) {
		codeHelperDeligator.moveToLine(moveToLine, startCol);
	}

	public void moveToLine(int moveToLine, int startCol, int endCol) {
		codeHelperDeligator.moveToLine(moveToLine, startCol, endCol);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 20.
	 * @param appendable
	 */
	public void customMenuHandler(CodeAreaCustomMenusHandler appendable) {
		codeHelperDeligator.customMenuHandler(appendable);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 31.
	 * @param b
	 */
	public void setWrapText(boolean b) {
		codeArea.setWrapText(b);
	}

	public ContextMenu getContextMenu() {
		return codeHelperDeligator.getContextMenu();
	}

	public void setContextMenu(ContextMenu menu) {
		codeHelperDeligator.setContextMenu(menu);

	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.commons.utils.GargoyleOpenExtension#canOpen(java.io.File)
	 */
	@Override
	public boolean canOpen(File file) {
		if (file != null) {
			return "sql".equals(FileUtil.getFileExtension(file)) && file.exists();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.kyj.fx.commons.utils.GargoyleOpenExtension#setOpenFile(java.io.File)
	 */
	@Override
	public void setOpenFile(File file) {
		String readConversion = FileUtil.readConversion(file);
		this.codeArea.replaceText(readConversion);
	}
}
