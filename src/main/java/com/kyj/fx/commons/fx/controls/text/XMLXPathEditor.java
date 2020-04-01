package com.kyj.fx.commons.fx.controls.text;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.fx.controls.tree.XMLTreeView;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLFormatter;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class XMLXPathEditor extends BorderPane {

	/**
	 * @최초생성일 2016. 10. 12.
	 */
	// private static final String REGEX =
	// "(?<ELEMENT>(</?\\h*)(\\w+)([^<>]*)(\\h*/?>))" +
	// "|(?<COMMENT><!--[^<>]+-->)";
	private static final String REGEX2 = "(?<ELEMENT>(</?\\h*)(\\w+)([^<>]*)(\\h*/?>))" + "|(?<COMMENT><!--[^!]+-->)";

	private static final Pattern XML_TAG = Pattern.compile(REGEX2);

	private static final Pattern ATTRIBUTES = Pattern.compile("([\\w+|]\\h*)(=)(\\h*\"[^\"]+\")");

	private static final int GROUP_OPEN_BRACKET = 2;
	private static final int GROUP_ELEMENT_NAME = 3;
	private static final int GROUP_ATTRIBUTES_SECTION = 4;
	private static final int GROUP_CLOSE_BRACKET = 5;
	private static final int GROUP_ATTRIBUTE_NAME = 1;
	private static final int GROUP_EQUAL_SYMBOL = 2;
	private static final int GROUP_ATTRIBUTE_VALUE = 3;

	private CodeArea codeArea;
	// private XMLTreeView xmlTreeView;
	private CodeAreaHelper<CodeArea> codeHelperDeligator;

	public XMLXPathEditor() {
		codeArea = new CodeArea();
		codeHelperDeligator = new CodeAreaHelper(codeArea);
		codeHelperDeligator.customMenuHandler(new CodeAreaCustomMenusHandler<CodeArea>() {

			@Override
			public void customMenus(CodeArea codeArea, ContextMenu contextMenu) {
				{
					MenuItem e = new MenuItem("Format");
					e.setOnAction(evt -> doformat());
					contextMenu.getItems().add(e);
				}

				{
					MenuItem e = new MenuItem("Show Application Code");
					e.setOnAction(evt -> FxUtil.EasyFxUtils.showApplicationCode(codeArea.getText()));
					contextMenu.getItems().add(e);
				}

				{
					MenuItem e = new MenuItem("Show XML Structure");
					e.setOnAction(evt -> {
						XMLTreeView xmlTreeView = new XMLTreeView();
						xmlTreeView.setXml(getText());
						xmlTreeView.setPrefSize(1200d, 800d);
						FxUtil.createStageAndShow(xmlTreeView, stage -> {
							stage.setTitle("XML Structure");
							stage.initOwner(FxUtil.getWindow(XMLXPathEditor.this));
						});
					});
					contextMenu.getItems().add(e);
				}
			}
		});

		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.setOnKeyPressed(this::codeAreaKeyClick);

		codeArea.textProperty().addListener((obs, oldText, newText) -> {
			codeArea.setStyleSpans(0, computeHighlighting(newText));
		});

		// xmlTreeView = new XMLTreeView();
		// SplitPane sp = new SplitPane(codeArea, xmlTreeView);

		setCenter(codeArea);

		this.getStylesheets().add(JavaTextArea.class.getResource("xml-highlighting.css").toExternalForm());

	}

	public String getText() {
		return this.codeArea.getText();
	}

	public void setText(String text) {
		this.codeArea.replaceText(text);
	}

	private XMLFormatter formatter = new XMLFormatter();

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
			doformat();
			e.consume();
		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(XMLXPathEditor.class);

	/**
	 * 
	 * 17.09.07 포멧팅을 시도하면서 에러가 발생하는 경우 기존 텍스트에 대한 상태를 변경하지않게 하기위해 try ~ catch문을
	 * 추가적으로 작성.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 1.
	 */
	private void doformat() {
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

		Matcher matcher = XML_TAG.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {

			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			if (matcher.group("COMMENT") != null) {
				spansBuilder.add(Collections.singleton("comment"), matcher.end() - matcher.start());
			} else {
				if (matcher.group("ELEMENT") != null) {
					String attributesText = matcher.group(GROUP_ATTRIBUTES_SECTION);

					spansBuilder.add(Collections.singleton("tagmark"), matcher.end(GROUP_OPEN_BRACKET) - matcher.start(GROUP_OPEN_BRACKET));
					spansBuilder.add(Collections.singleton("anytag"), matcher.end(GROUP_ELEMENT_NAME) - matcher.end(GROUP_OPEN_BRACKET));

					if (!attributesText.isEmpty()) {

						lastKwEnd = 0;

						Matcher amatcher = ATTRIBUTES.matcher(attributesText);
						while (amatcher.find()) {
							spansBuilder.add(Collections.emptyList(), amatcher.start() - lastKwEnd);
							spansBuilder.add(Collections.singleton("attribute"),
									amatcher.end(GROUP_ATTRIBUTE_NAME) - amatcher.start(GROUP_ATTRIBUTE_NAME));
							spansBuilder.add(Collections.singleton("tagmark"),
									amatcher.end(GROUP_EQUAL_SYMBOL) - amatcher.end(GROUP_ATTRIBUTE_NAME));
							spansBuilder.add(Collections.singleton("avalue"),
									amatcher.end(GROUP_ATTRIBUTE_VALUE) - amatcher.end(GROUP_EQUAL_SYMBOL));
							lastKwEnd = amatcher.end();
						}
						if (attributesText.length() > lastKwEnd)
							spansBuilder.add(Collections.emptyList(), attributesText.length() - lastKwEnd);
					}

					lastKwEnd = matcher.end(GROUP_ATTRIBUTES_SECTION);

					spansBuilder.add(Collections.singleton("tagmark"), matcher.end(GROUP_CLOSE_BRACKET) - lastKwEnd);
				}
			}
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

}