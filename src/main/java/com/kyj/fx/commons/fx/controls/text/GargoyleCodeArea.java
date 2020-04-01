/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.commons.fx.controls.text
 *	작성일   : 2018. 4. 26.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.Codec;
import org.fxmisc.richtext.model.StyledDocument;
import org.fxmisc.richtext.model.StyledSegment;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.util.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.IndexRange;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * 
 * 클립보드 관련 유틸 기능 추가 제공 <br/>
 * 
 * @author KYJ
 *
 */
public class GargoyleCodeArea extends CodeArea {

	private static Logger LOGGER = LoggerFactory.getLogger(GargoyleCodeArea.class);

	/**
	 * @최초생성일 2017. 7. 14.
	 */
	private static List<ClipboardContent> fixedSizeClipboardItems;
	//
	static {
		fixedSizeClipboardItems = new LinkedList<ClipboardContent>() {

			int FIXED_SIZE = 20;

			@Override
			public boolean add(ClipboardContent e) {
				if (FIXED_SIZE < size()) {
					removeFirst();
				}
				return super.add(e);
			}

			@Override
			public boolean addAll(Collection<? extends ClipboardContent> c) {
				throw new UnsupportedOperationException("Elements may not be cleared from a fixed size List.");
			}

			@Override
			public boolean addAll(int index, Collection<? extends ClipboardContent> c) {
				throw new UnsupportedOperationException("Elements may not be cleared from a fixed size List.");
			}

			@Override
			public void add(int index, ClipboardContent element) {
				throw new UnsupportedOperationException("Elements may not be cleared from a fixed size List.");
			}

		};
	}
	{
		getStyleClass().add("code-area");

		// load the default style that defines a fixed-width font
		// getStylesheets().add(CodeArea.class.getResource("code-area.css").toExternalForm());

		// don't apply preceding style to typed text
		setUseInitialStyleForInsertion(true);
	}

	public GargoyleCodeArea() {
		this("");
	}

	private InputMap<KeyEvent> tabKeyEvent = InputMap.consume(EventPattern.keyPressed(KeyCode.TAB), a -> {
		/* Nothing.. CodeAreaHelper에서 TabKey 이벤트에 대한 내용 처리. */

		IndexRange selection = getSelection();

		if (selection.getStart() == selection.getEnd())
			replaceSelection("\t");

	});

	/**
	 * Creates a text area with initial text content. Initial caret position is
	 * set at the beginning of text content.
	 *
	 * @param text
	 *            Initial text content.
	 */
	public GargoyleCodeArea(String text) {
		super();

		Nodes.addInputMap(this, tabKeyEvent);

		appendText(text);
		getUndoManager().forgetHistory();
		getUndoManager().mark();

		scrollYToPixel(1);

	}

	/**
	 * 
	 * return ClipboardItems.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 7. 14.
	 * @return
	 */
	public List<ClipboardContent> getClipBoardItems() {
		return new ReadOnlyListWrapper<>(FXCollections.observableArrayList(fixedSizeClipboardItems));
	}

	/**
	 * 클립보드 복사 기능을 좀 더 확장하기위해 코드처리.
	 */
	@Override
	public void copy() {

		/*
		 * copy code from ClipboardActions.java, default method 'copy()';
		 */
		IndexRange selection = getSelection();
		if (selection.getLength() > 0) {
			ClipboardContent content = new ClipboardContent();

			content.putString(getSelectedText());

			getStyleCodecs().ifPresent(new Consumer<Tuple2<Codec<Collection<String>>, Codec<StyledSegment<String, Collection<String>>>>>() {
				@Override
				public void accept(Tuple2<Codec<Collection<String>>, Codec<StyledSegment<String, Collection<String>>>> styleCodec) {

					// Codec<Collection<String>> get2 = styleCodec.get2();
					// Codec<StyledDocument<Collection<String>, String,
					// Collection<String>>> codec = ReadOnlyStyledDocument
					// .codec(styleCodec.get1(), get2, get2);
					DataFormat format = DataFormat.RTF;

					// DataFormat format = dataFormat(codec.getName());
					StyledDocument<Collection<String>, String, Collection<String>> doc = subDocument(selection.getStart(),
							selection.getEnd());
					// ByteArrayOutputStream os = new ByteArrayOutputStream();
					// DataOutputStream dos = new DataOutputStream(os);

					// try {
					String text = doc.getText();
					// codec.encode(dos, doc);
					content.put(format, text);
					// } catch (IOException e) {
					// System.err.println("Codec error: Exception in
					// encoding '" + codec.getName() + "':");
					// e.printStackTrace();
					// }
				}
			});

			fixedSizeClipboardItems.add(content);
			Clipboard.getSystemClipboard().setContent(content);
		}
	}

}