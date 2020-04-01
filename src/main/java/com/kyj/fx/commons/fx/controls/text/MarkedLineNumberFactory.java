/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 10. 13.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.function.IntFunction;

import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.model.Paragraph;
import org.reactfx.collection.LiveList;
import org.reactfx.value.Val;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 * @author KYJ
 *
 */
public class MarkedLineNumberFactory implements IntFunction<Node> {

	/**
	 * 투명
	 * @최초생성일 2016. 10. 13.
	 */
	public static final String TRANSPARENT_STYLE = "-fx-fill:transparent";
	/**
	 * 일반적인 표시를 해주기 위한 css 스타일,
	 * @최초생성일 2016. 10. 12.
	 */
	public static final String ADDED_COMMNET_STYLE = "-fx-fill:olivedrab";
	/**
	 * 에러 상태를 표현해주기 위한 css 스타일, 빨간색
	 * @최초생성일 2016. 10. 13.
	 */
	public static final String ERROR_LINE_COMMNET_STYLE = "-fx-fill:red";

	private static final Insets DEFAULT_INSETS = new Insets(0.0, 5.0, 0.0, 5.0);
	private static final Paint DEFAULT_TEXT_FILL = Color.web("#666");
	private static final Font DEFAULT_FONT = Font.font("monospace", FontPosture.ITALIC, 13);
	private static final Background DEFAULT_BACKGROUND = new Background(new BackgroundFill(Color.web("#ddd"), null, null));

	public static IntFunction<Node> get(StyledTextArea<?, ?> area) {
		return get(area, digits -> "%0" + digits + "d");
	}

	public static IntFunction<Node> get(StyledTextArea<?, ?> area, IntFunction<String> format) {
		return new MarkedLineNumberFactory(area, format);
	}

	private final Val<Integer> nParagraphs;
	private final IntFunction<String> format;
	private StyledTextArea<?, ?> area;
	private int appendLineKeywordLength = JavaCodeAreaHelper.appendLineKeyword.length();
	private ObjectProperty<LineMapper<Integer>> lineMarkFactory = new SimpleObjectProperty<>();
	private ObjectProperty<GraphicsMapper<Node>> graphicsMapperFactory = new SimpleObjectProperty<>();

	private MarkedLineNumberFactory(StyledTextArea<?, ?> area, IntFunction<String> format) {
		this.area = area;
		nParagraphs = LiveList.sizeOf(area.getParagraphs());
		this.format = format;
		lineMarkFactory.set(defaultLineMarkFactory());
		graphicsMapperFactory.set(defaultGraphicsMapperFactory());
	}

	/**
	 * @author KYJ
	 *
	 * @param <T>
	 */
	public static interface LineMapper<T> {

		public T map(int row, Paragraph<?, ? , ?> pra);

	}

	/**
	 * @author KYJ
	 *
	 * @param <T>
	 */
	public static interface GraphicsMapper<T extends Node> {

		public T map(int row, Paragraph<?, ?,  ?> pra, int typeValue);
	}

	/**
	 *  default
	 *  mark하기 위한 라인에 대한 정의를 구현
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 13.
	 * @return
	 */
	public LineMapper<Integer> defaultLineMarkFactory() {

		return new LineMapper<Integer>() {

			@Override
			public Integer map(int row, Paragraph<?, ?, ?> pra) {

				String substring = pra.substring(0, appendLineKeywordLength);
				if (JavaCodeAreaHelper.appendLineKeyword.equals(substring)) {
					return 1;
				}
				return 0;
			}
		};
	}

	/**
	 *  default
	 * mark표시를 하기위한 라인 색 표시
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 13.
	 * @return
	 */
	public GraphicsMapper<Node> defaultGraphicsMapperFactory() {

		return new GraphicsMapper<Node>() {

			@Override
			public Node map(int row, Paragraph<?, ?, ?> pra, int typeValue) {
				Circle g = new Circle(5d);
				g.setStyle("-fx-fill:transparent");

				if (typeValue == 1)
					g.setStyle(ADDED_COMMNET_STYLE);

				return g;

			}
		};
	}

	/*
	 * Graph 처리.
	 *  (non-Javadoc)
	 * @see java.util.function.IntFunction#apply(int)
	 */
	@Override
	public Node apply(int idx) {
		int row = idx + 1;

		Val<String> formatted = nParagraphs.map(n -> {
			return format(row, n);
		});

		if(idx == -1)
			idx = 0;
		Paragraph<?, ?,  ?> paragraph = this.area.getParagraph(idx);

		//		Optional<Boolean> findAny = paragraph.chars().limit(1L).mapToObj(v -> {
		//
		//			String substring = paragraph.substring(0, appendLineKeywordLength);
		//			if (JavaCodeAreaHelper.appendLineKeyword.equals(substring)) {
		//				return true;
		//			}
		//
		//			return false;
		//		}).findAny();

		Label lineNo = new Label();
		lineNo.setFont(DEFAULT_FONT);
		lineNo.setBackground(DEFAULT_BACKGROUND);
		lineNo.setTextFill(DEFAULT_TEXT_FILL);
		lineNo.setPadding(DEFAULT_INSETS);
		lineNo.getStyleClass().add("lineno");

		//		Rectangle rectangle = new Rectangle();
		//		rectangle.setWidth(10d);
		//		rectangle.setHeight(10d);
		//		if ( /*findAny.isPresent() && findAny.get()*/ lineMarkFactory.get().map(row, paragraph))
		//			rectangle.setStyle(ADDED_COMMNET_STYLE);
		//		else
		//			rectangle.setStyle("-fx-fill:transparent");
		Integer typeValue = lineMarkFactory.get().map(row, paragraph);
		lineNo.setGraphic(graphicsMapperFactory.get().map(row, paragraph, typeValue));

		// bind label's text to a Val that stops observing area's paragraphs
		// when lineNo is removed from scene
		lineNo.textProperty().bind(formatted.conditionOnShowing(lineNo));

		return lineNo;
	}

	/**
	 * 라인 표시 포멧
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 13.
	 * @param x
	 * @param max
	 * @return
	 */
	private String format(int x, int max) {
		int digits = (int) Math.floor(Math.log10(max)) + 1;
		return String.format(format.apply(digits), x);
	}

	/**********************************************************************************************************************************/
	// 일반 속성 프로퍼티 정의

	public final ObjectProperty<LineMapper<Integer>> lineMarkFactoryProperty() {
		return this.lineMarkFactory;
	}

	public final MarkedLineNumberFactory.LineMapper<Integer> getLineMarkFactory() {
		return this.lineMarkFactoryProperty().get();
	}

	public final void setLineMarkFactory(final MarkedLineNumberFactory.LineMapper<Integer> lineMarkFactory) {
		this.lineMarkFactoryProperty().set(lineMarkFactory);
	}

	public final ObjectProperty<GraphicsMapper<Node>> graphicsMapperFactoryProperty() {
		return this.graphicsMapperFactory;
	}

	public final MarkedLineNumberFactory.GraphicsMapper<Node> getGraphicsMapperFactory() {
		return this.graphicsMapperFactoryProperty().get();
	}

	public final void setGraphicsMapperFactory(final MarkedLineNumberFactory.GraphicsMapper<Node> graphicsMapperFactory) {
		this.graphicsMapperFactoryProperty().set(graphicsMapperFactory);
	}

	/**********************************************************************************************************************************/

}
