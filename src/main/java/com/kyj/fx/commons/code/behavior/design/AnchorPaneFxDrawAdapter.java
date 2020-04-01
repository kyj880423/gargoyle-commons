/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 6. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.FxCollectors;
import com.kyj.fx.commons.utils.FxContextManager;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Labeled;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * 
 * Parent를 BorderPane로 갖는 패널을 그린 객체를 리턴함. <br/>
 * 
 * @author KYJ
 *
 */
public class AnchorPaneFxDrawAdapter extends AbstractFxDrawAdapter<Node> implements IUserCustomFxDrawAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnchorPaneFxDrawAdapter.class);

	private AnchorPane root;
	private BooleanProperty showTooltip = new SimpleBooleanProperty(false);
	private List<BehaviorPannelable> controls = FXCollections.observableArrayList();

	private BehaviorFxDesigner behaviorFxDesigner;

	private ContextMenu contextMenu;

	public AnchorPaneFxDrawAdapter(BehaviorFxDesigner behaviorFxDesigner) {
		this.behaviorFxDesigner = behaviorFxDesigner;
		root = new AnchorPane();

		AnchorPane.setTopAnchor(root, 5.0);
		AnchorPane.setBottomAnchor(root, 20.0);
		AnchorPane.setLeftAnchor(root, 5.0);
		AnchorPane.setRightAnchor(root, 5.0);

		root.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		// root.setPadding(FxUtil.getInserts(15));

		CheckMenuItem miShowItemId = new CheckMenuItem("Show/Hide Id");
		miShowItemId.setOnAction(AnchorPaneFxDrawAdapter.this::miShowItemIdOnAction);

		CheckMenuItem miPrintBehaviorValues = new CheckMenuItem("Print Behavior Values");
		miPrintBehaviorValues.setOnAction(AnchorPaneFxDrawAdapter.this::miPrintBehaviorValuesOnAction);

		// MenuItem miRun = new CheckMenuItem("Run");
		// miRun.setOnAction(AnchorPaneFxDrawAdapter.this::miRunOnAction);

		FxContextManager contextManager = new FxContextManager(root, miShowItemId, miPrintBehaviorValues);
		contextMenu = contextManager.getContextMenu();
		FxUtil.installContextMenu(contextManager);

	}

	/**
	 * @return the controls
	 */
	public List<BehaviorPannelable> getControls() {
		return controls;
	}

	/**
	 * @return the contextMenu
	 */
	public ContextMenu getContextMenu() {
		return contextMenu;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 2.
	 * @param e
	 */
	void miShowItemIdOnAction(ActionEvent e) {
		CheckMenuItem c = (CheckMenuItem) e.getSource();
		showTooltip.setValue(c.isSelected());
	}

	/**
	 * print behavior values <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 26.
	 * @param e
	 */
	void miPrintBehaviorValuesOnAction(ActionEvent e) {

		TextArea text = new TextArea(this.getBehaviorValues());
		FxUtil.createStageAndShow(text, stage -> {
			stage.setWidth(1200d);
			stage.setHeight(800d);
		});
	}

	// void miRunOnAction(ActionEvent e) {
	// executeBehavior();
	// }

	// 편집 불가 핸들러
	private BiConsumer<Node, BehaviorPropertyVo> customHandler = (n, p) -> {
		// if (n instanceof TextArea) {
		// TextArea _a = (TextArea) n;
		// } else if (n instanceof BehaviorChoiceBox) {
		// BehaviorChoiceBox _a = (BehaviorChoiceBox) n;
		// }
	};

	/**
	 * type에 매핑되는 컨트롤 요소를 리턴함. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 19.
	 * @param type
	 * @param object
	 * @return
	 */
	BehaviorPannelable createLayout(String type, BehaviorObjectVO object) {
		BehaviorPannelable p = null;

		String[] split = type.split(",");
		type = split[0];
		switch (type) {
		case "DMI.RA.Behaviors.Runtime.Components.Button":
			BehaviorDesignButton btn = new BehaviorDesignButton();
			btn.setMinHeight(23d);
			btn.setPrefHeight(23d);
			btn.setPrefWidth(147d);
			btn.setFont(Font.font(8.25));
			Tooltip.install(btn, new Tooltip(object.getName()));
			p = btn;
			break;
		case "DMI.RA.Behaviors.Runtime.Components.TextBox":
			BehaviorDesignTextArea tx = new BehaviorDesignTextArea();
			tx.getStyleClass().add("behavior-textarea");
			tx.setMinHeight(23d);
			tx.setPrefHeight(23d);
			tx.setPrefWidth(147d);
			tx.setWrapText(false);
			tx.setFont(Font.font(8.25));
			Tooltip.install(tx, new Tooltip(object.getName()));
			p = tx;
			break;
		case "DMI.RA.Behaviors.Runtime.Components.PictureBox":
			BehaviorDesignImageView iv = new BehaviorDesignImageView();
			Tooltip.install(iv, new Tooltip(object.getName()));
			p = iv;
			break;
		case "DMI.RA.Behaviors.Runtime.Components.Label":
			BehaviorDesignLabel l = new BehaviorDesignLabel();
			l.getStyleClass().add("behavior-label");
			l.setFont(Font.font(8.25));
			l.setPrefWidth(23d);
			l.setPrefHeight(100d);
			l.setWrapText(true);
			l.setTextOverrun(OverrunStyle.ELLIPSIS);
			Tooltip.install(l, new Tooltip(object.getName()));
			p = l;
			break;
		case "DMI.RA.Behaviors.Runtime.Components.ComboBox":
			BehaviorDesignComboBox c = new BehaviorDesignComboBox();
			c.setBackground(FxUtil.getBackgroundColor(Color.WHITE));
			Tooltip.install(c, new Tooltip(object.getName()));
			p = c;
			break;
		case "DMI.RA.Behaviors.Runtime.Components.GroupBox": {
			BehaviorDesignGroupPane pan = new BehaviorDesignGroupPane();
			pan.getStyleClass().add("behavior-grouppane");
			Tooltip.install(pan, new Tooltip(object.getName()));
			p = pan;
		}
			break;
		default: {
			BehaviorBorderPane pan = new BehaviorBorderPane();
			LOGGER.info("unexpected behavior type : {} ", type);
			pan.getStyleClass().add("behavior-borderpane");
			Tooltip.install(pan, new Tooltip(object.getName()));
			p = pan;
		}
			break;
		}
		controls.add(p);

		p.setBehaviorObjectVO(object);
		return p;
	}

	/**
	 * 컨트롤 요소들의 디자인 속성 처리를 위한 함수 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 19.
	 * @param n
	 * @param properties
	 */
	void propertyBind(Node n, String name, List<BehaviorPropertyVo> properties) {

		if(properties == null)
			return;
		
		for (BehaviorPropertyVo p : properties) {
			String key = p.getName();
			LOGGER.debug("type : {}  key : {}  - {} ", n.getClass().getName(), key, p.getText());
			switch (key) {
			case "Location": {
				String text = p.getText();
				String[] split = text.split(",");
				int x = Integer.parseInt(split[0].trim());
				int y = Integer.parseInt(split[1].trim());

				n.setLayoutX(x);
				n.setLayoutY(y);
			}
				break;

			case "Name":
				n.setId(p.getText());
				break;
			case "TabIndex":
				// Nothing..
				break;
			case "Size": {
				String text = p.getText();
				String[] split = text.split(",");
				double w = Double.parseDouble(split[0].trim());
				double h = Double.parseDouble(split[1].trim());

				String style = n.getStyle() == null ? "" : n.getStyle();
				style = style + "-fx-pref-width : " + w + ";-fx-pref-height:" + h + ";";
				n.setStyle(style);
			}
				break;

			case "Text":

				String text = p.getText();

				if (n instanceof TextInputControl) {
					((TextInputControl) n).setText(text);
				} else if (n instanceof Labeled) {
					((Labeled) n).setText(text);
				} else if (n instanceof BehaviorDesignChoiceBox) {
					// ((BehaviorChoiceBox) n).setValue(text);
					((BehaviorDesignChoiceBox) n).getSelectionModel().select(text);
				}

				break;

			case "Image": {

				if (n instanceof ImageView && p instanceof BehaviorPropertyItemsVo) {
					BehaviorPropertyItemsVo v = (BehaviorPropertyItemsVo) p;
					BehaviorBinaryVO binary = v.getBinary();
					String base64 = binary.getText();

					// byte[] decode = Base64.getDecoder().decode(base64);
					// iv = new Image(new ByteArrayInputStream(decode));
					ImageView _v = (ImageView) n;
					_v.setImage(FxUtil.createImage(base64));

				}
			}
			case "BackColor": {

				if (n instanceof Region) {
					String t = p.getText();
					if ("Control".equals(t))
						break;
					String[] split = t.split(",");
					Color c = null;
					if (split.length == 1) {
						try {
							c = Color.valueOf(t);
						} catch (Exception e) {
							LOGGER.debug(ValueUtil.toString("replace setup white color", e));
							c = Color.WHITE;
						}

					} else if (split.length == 3) {
						int r = Integer.parseInt(split[0].trim());
						int g = Integer.parseInt(split[1].trim());
						int b = Integer.parseInt(split[2].trim());
						c = Color.rgb(r, g, b);
					}

					((Region) n).setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
				}

			}
				break;

			case "Items": {
				if (n instanceof BehaviorDesignChoiceBox && p instanceof BehaviorPropertyItemsVo) {
					BehaviorPropertyItemsVo item = (BehaviorPropertyItemsVo) p;
					ObservableList<String> collect = item.getItems().stream().map(v -> v.getText())
							.collect(FxCollectors.toObservableList());

					LOGGER.debug("{}", collect);

					BehaviorDesignChoiceBox box = (BehaviorDesignChoiceBox) n;
					box.setItems(collect);
				}
				else if (n instanceof BehaviorDesignComboBox && p instanceof BehaviorPropertyItemsVo) {
					BehaviorPropertyItemsVo item = (BehaviorPropertyItemsVo) p;
					ObservableList<String> collect = item.getItems().stream().map(v -> v.getText())
							.collect(FxCollectors.toObservableList());

					LOGGER.debug("{}", collect);

					BehaviorDesignComboBox box = (BehaviorDesignComboBox) n;
					box.setItems(collect);
				}
			}
				break;
			} // end switch

			if (customHandler != null)
				customHandler.accept(n, p);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.commons.code.behavior.design.AbstractFxDrawAdapter#draw(com.
	 * kyj.fx.commons.code.behavior.design.BehaviorObjectVO)
	 */
	@Override
	public Node draw() {
		controls.clear();
		// String type = object.getType();
		BehaviorVo behaviorVo = this.behaviorFxDesigner.getBehaviorVo();
		BehaviorObjectVO object = behaviorVo.getDesignerLayoutVo().getBehaviorObjectVO();
		String name = object.getName();
		// root.setStyle("-fx-font-size : 8.25pt;");
		root.getStylesheets().add(AnchorPaneFxDrawAdapter.class.getResource("AnchorPaneFxDrawAdapter.css").toExternalForm());

		propertyBind(root, name, object.getProperties());

		List<BehaviorObjectVO> objects = object.getObjects();
		if (objects != null) {
			for (BehaviorObjectVO obj : objects) {
				draw(root, obj);
			}
		}

		// 원래 사이즈로 처리하면 너무 작아보이므로 1.2배 배율로 조정
		// root.getTransforms().add(new Scale(1.2, 1.2));
		// root.setScaleX(1.2d);
		// root.setScaleY(1.2d);
		showTooltip.addListener(showToltipChangeListener);

		return root;
	}

	ChangeListener<Boolean> showToltipChangeListener = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> oba, Boolean o, Boolean n) {

			/* Root에 묶인 항목 표현. */
			ObservableList<Node> children = root.getChildren();
			showTooltip(root, n, children);

			/* GroupPane에 묶인 항목 표현 */
			List<Parent> collect = root.getChildrenUnmodifiable().stream().filter(v -> v instanceof BehaviorDesignGroupPane)
					.filter(v -> v instanceof Parent).map(v -> (Parent) v).collect(Collectors.toList());
			for (Parent p : collect) {
				showTooltip((AnchorPane) p, n, p.getChildrenUnmodifiable());
			}

		}

		void showTooltip(AnchorPane root, boolean n, List<Node> children) {
			final ObservableList<Node> appendItems = FXCollections.observableArrayList();

			if (n) {
				for (final Node node : children) {

					if (node instanceof BehaviorIdLabel)
						continue;

					if (node instanceof BehaviorPannelable && getIsShowTooltipLabel().test((BehaviorPannelable) node)) {
						LOGGER.debug("{}", node);
						final double layoutX = node.getLayoutX();
						final double layoutY = node.getLayoutY();

						node.setOpacity(0.4);
						final BehaviorIdLabel lblId = new BehaviorIdLabel(node.getId());
						lblId.setFont(Font.font(8d));
						lblId.setTextFill(Color.BLUE);
						lblId.setLayoutX(layoutX);
						lblId.setLayoutY(layoutY);
						lblId.toFront();
						appendItems.add(lblId);
					}

				}
				root.getChildren().addAll(appendItems);

			} else {
				for (final Node node : children) {
					if (node instanceof BehaviorIdLabel)
						appendItems.add(node);
					else
						node.setOpacity(1.0);
				}
				root.getChildren().removeAll(appendItems);
			}

		}
	};

	// private List<Node> behaviorNodes = FXCollections.observableArrayList();
	/**
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 19.
	 * @param root
	 * @param object
	 */
	public void draw(AnchorPane root, BehaviorObjectVO object) {
		String type = object.getType();

		String name = object.getName();
		BehaviorPannelable n = createLayout(type, object);
		Node node = (Node) n;
		root.getChildren().add((Node) node);

		propertyBind(node, name, object.getProperties());

		// 하위 요소들 처리
		List<BehaviorObjectVO> objects = object.getObjects();
		if (objects != null) {
			for (BehaviorObjectVO o : objects) {
				// Group팬 하위 요소들을 처리하기위한 분기
				if (n instanceof GroupPane) {
					draw((AnchorPane) n, o);
				} else
					draw(root, o);
			}
		}

	}

	@Override
	public void setItemHandler(BiConsumer<Node, BehaviorPropertyVo> customHandler) {
		this.customHandler = customHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.commons.code.behavior.design.AbstractFxDrawAdapter#
	 * showTooltipText(boolean)
	 */
	@Override
	public void showTooltipText(boolean flag) {
		this.showTooltip.set(flag);
	}

	/*
	 * behavior value xml 을 리턴 <br/> (non-Javadoc)
	 * 
	 * @see com.kyj.fx.commons.code.behavior.design.AbstractFxDrawAdapter#
	 * getBehaviorValues()
	 */
	@Override
	public String getBehaviorValues() {

		StringBuffer sb = new StringBuffer();
		
		
		
	
		sb.append("<BehaviorValues>");
		for (BehaviorPannelable p : controls) {
			
			if (p instanceof BehaviorControlable
					&& (p instanceof TextInputControl || p instanceof BehaviorDesignTextArea || p instanceof BehaviorDesignChoiceBox || p instanceof BehaviorDesignComboBox)) {
				sb.append("\n");
				LOGGER.debug("{}", p);
				Node n = (Node) p;
				String name = n.getId();
				String value = ((BehaviorControlable) n).getValueString();
				
				
				if (ValueUtil.isEmpty(name))
					continue;
				if (ValueUtil.isNotEmpty(name) && value == null) {
					sb.append(String.format("<Object name='%s'></Object>", name));
				} else {
					value = ValueUtil.removeSpace(value);
//
					value = value.replaceAll("&", "&amp;");
					value = value.replaceAll(" ", "#x20"); 
					value = value.replaceAll("\n", "&#10;");
					value = value.replaceAll("\"", "&quot;");
					value = value.replaceAll("\'", "&apos;");
					value = value.replaceAll("<", "&lt;");
					value = value.replaceAll(">", "&gt;");
					

//					value = "<![CDATA[" + value + "]]>";
//					value = value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
					sb.append(String.format("<Object name='%s'>%s</Object>", name, value));
//					sb.append(String.format("<Object name='%s'>%s</Object>", name, value));
				}
			}

		}
		sb.append("</BehaviorValues>");
		
//		try {
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			
//			factory.setValidating(false);
//			factory.setIgnoringComments(true);
//			factory.setNamespaceAware(false);
//			factory.setXIncludeAware(false);
//			factory.setIgnoringElementContentWhitespace(true);
//			
//			DocumentBuilder b = factory.newDocumentBuilder();
//			
//			Document doc = b.parse(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			XMLUtils.print(doc, out);
//			return out.toString("UTF-8");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return sb.toString();
	}

	@Override
	public BehaviorVo getBehaviorVo() {
		return behaviorFxDesigner.getBehaviorVo();
	}

	@Override
	public Node getRoot() {
		return this.root;
	}

}
