package com.kyj.fx.commons.fx.controls.text;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.GargoyleMultiOpenTabPolicy;
import com.kyj.fx.commons.utils.GargoyleOpenExtension;
import com.kyj.fx.commons.utils.JsonFormatter;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLFormatter;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class JsonEditor extends BorderPane implements GargoyleOpenExtension, GargoyleMultiOpenTabPolicy {

	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/(\\*)(.|\\R)*?\\*/\\1";

	private static final Pattern PATTERN = Pattern.compile("(?<BRACE>" + BRACE_PATTERN + ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
			+ "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<COMMENT>" + COMMENT_PATTERN + ")");

	private CodeArea codeArea;
	// private XMLTreeView xmlTreeView;
	private CodeAreaHelper<CodeArea> codeHelperDeligator;
	private CodeAreaAutoCompleteHelper<CodeArea> autoCompleteHelper;

	public JsonEditor() {
		codeArea = new GargoyleCodeArea();
		VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
		codeHelperDeligator = new CodeAreaHelper<>(codeArea);

		codeHelperDeligator.customMenuHandler(new CodeAreaCustomMenusHandler<CodeArea>() {

			@Override
			public void customMenus(CodeArea codeArea, ContextMenu contextMenu) {
				{
					MenuItem e = new MenuItem("Format (CTRL+SHIFT+F)");
					e.setOnAction(evt -> doformat());
					contextMenu.getItems().add(e);
				}

				{
					MenuItem e = new MenuItem("Remove Space (CTRL+SHIFT+R)");
					e.setOnAction(evt -> doRemoveSpace());
					contextMenu.getItems().add(e);
				}

				//
				{
					Menu menuAppicationCode = new Menu("Application Code");

					{
						MenuItem e = new MenuItem("Show Application StringBuffer Code [Java]");
						e.setOnAction(ev -> {
							FxUtil.EasyFxUtils.showJavaApplicationCode(codeArea.getSelectedText());
						});
						menuAppicationCode.getItems().add(e);

					}

					{
						MenuItem e = new MenuItem("Show Application List Code [Java]");
						e.setOnAction(ev -> {
							FxUtil.EasyFxUtils.showJavaApplicationCode(codeArea.getSelectedText());
						});
						menuAppicationCode.getItems().add(e);
					}

					{
						MenuItem e = new MenuItem("Show Application Code [C#]");
						e.setOnAction(ev -> {
							FxUtil.EasyFxUtils.showDotNetApplicationCode(codeArea.getSelectedText());
						});
						menuAppicationCode.getItems().add(e);
					}

					{
						MenuItem e = new MenuItem("Show Application Code [VBS]");
						e.setOnAction(ev -> {
							FxUtil.EasyFxUtils.showVbsApplicationCode(codeArea.getSelectedText());
						});
						menuAppicationCode.getItems().add(e);
					}

					contextMenu.getItems().add(menuAppicationCode);
				}

			}
		});

		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.setOnKeyPressed(this::codeAreaKeyClick);

		codeArea.textProperty().addListener((obs, oldText, newText) -> {
			codeArea.setStyleSpans(0, computeHighlighting(newText));
		});

		autoCompleteHelper = new CodeAreaAutoCompleteHelper<>(codeArea);
		autoCompleteHelper.setCreateAutoCompleteSupl(() -> {
			AutoComplete ac = new AutoComplete();
			ac.setOnQuery(new XmlAutoCompleteQuery());
			return ac;
		});
		autoCompleteHelper.init();
		// xmlTreeView = new XMLTreeView();
		// SplitPane sp = new SplitPane(codeArea, xmlTreeView);

		setCenter(scrollPane);

		this.getStylesheets().add(this.getClass().getResource("json-highlighting.css").toExternalForm());

	}

	/**
	 * XML 공간을 제거해 한줄로 바꿈. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 18.
	 */
	protected void doRemoveSpace() {
		String stringPrettyFormat = new XMLFormatter().removeSpace(codeArea.getText());
		codeHelperDeligator.setContent(stringPrettyFormat);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 9.
	 * @return
	 */
	public ObservableValue<String> textProperty() {
		return this.codeArea.textProperty();
	}

	public String getText() {
		return this.codeArea.getText();
	}

	public void setText(String text) {
		this.codeArea.replaceText(text);
	}

	private JsonFormatter formatter = new JsonFormatter();

	/**
	 * 키클릭 이벤트 처리
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 6.
	 * @param e
	 */
	public void codeAreaKeyClick(KeyEvent e) {

		codeHelperDeligator.codeAreaKeyClick(e);

		// CTRL + SHIFT + F do xml format.
		if (e.getCode() == KeyCode.F && e.isControlDown() && e.isShiftDown() && !e.isAltDown()) {
			if (e.isConsumed())
				return;
			e.consume();
			doformat();

		}
		// CTRL + SHIFT + ALT + R REMOVE Space
		else if (e.getCode() == KeyCode.R && e.isControlDown() && e.isShiftDown() && !e.isAltDown()) {
			if (e.isConsumed())
				return;
			e.consume();
			doRemoveSpace();

		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonEditor.class);

	/**
	 * 
	 * 17.09.07 포멧팅을 시도하면서 에러가 발생하는 경우 기존 텍스트에 대한 상태를 변경하지않게 하기위해 try ~ catch문을
	 * 추가적으로 작성.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 1.
	 */
	public void doformat() {
		try {
			String text = this.codeArea.getText();
			String format = formatter.format(text);
			setContent(format);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
	}

	/**
	 * setContent
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 1.
	 * @param content
	 */
	public void setContent(String content) {
		codeHelperDeligator.setContent(content);
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			String styleClass = matcher.group("BRACE") != null ? "paren"
					: matcher.group("BRACKET") != null ? "bracket"
							: matcher.group("STRING") != null ? "string" : matcher.group("COMMENT") != null ? "comment" : null;
			/* never happens */ assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

	public void setEditable(boolean editable) {
		codeArea.setEditable(editable);
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
	 * @작성일 : 2018. 3. 9.
	 * @return
	 */
	public ContextMenu getContextMenu() {
		return this.codeHelperDeligator.getContextMenu();
	}

	@Override
	public boolean canOpen(File file) {
		String ext = FileUtil.getFileExtension(file);
		return "json".equalsIgnoreCase(ext);
	}

	@Override
	public void setOpenFile(File file) {
		codeArea.replaceText(FileUtil.readConversion(file, StandardCharsets.UTF_8));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 10.
	 * @param string
	 */
	public void appendText(String string) {
		codeArea.appendText(string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.commons.utils.GargoyleOpenTabPolicy#defaultTabName()
	 */
	@Override
	public String defaultTabName() {
		return "Json Viewer";
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 31.
	 * @return
	 */
	public Integer getCurrentLine() {
		return codeHelperDeligator.getCurrentLine();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 31.
	 * @param paragraphIndex
	 * @param columnPosition
	 * @param text
	 */
	public void insertText(int paragraphIndex, int columnPosition, String text) {
		codeArea.insertText(paragraphIndex, columnPosition, text);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 31.
	 * @return
	 */
	public int getCaretColumn() {
		return codeArea.getCaretColumn();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 31.
	 * @return
	 */
	public String getSelectedText() {
		return codeArea.getSelectedText();
	}
	
	
	/**
	 * @return the codeArea
	 */
	public final CodeArea getCodeArea() {
		return codeArea;
	}

}