/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.tree
 *	작성일   : 2017. 10. 23.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

import java.io.ByteArrayInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.kyj.fx.commons.functions.FxXmlSupplier;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.SAXPasrerUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * @author KYJ
 *
 */
public class XMLTreeView extends TreeView<XMLMeta> implements FxXmlSupplier {

	private StringProperty xml = new SimpleStringProperty();

	public XMLTreeView() {
		this.setCellFactory(TextFieldTreeCell.forTreeView(new StringConverter<XMLMeta>() {

			@Override
			public String toString(XMLMeta object) {
				if (object == null)
					return "";

				String name = object.getName() == null ? "" : object.getName();
				String text = ValueUtil.isEmpty(object.getText()) ? "" : String.format(" (%s)", object.getText());
				return name.concat(text);

			}

			@Override
			public XMLMeta fromString(String string) {
				return null;
			}
		}));

		this.setShowRoot(false);
		this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		xml.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (ValueUtil.isEmpty(newValue))
					return;

				TreeItem<XMLMeta> tiRoot = new TreeItem<>();
				XMLTreeView.this.setRoot(tiRoot);
				tiRoot.setExpanded(true);

				try {
					SAXPasrerUtil.getAll(new ByteArrayInputStream(newValue.getBytes()), new XMLTreeHandler(tiRoot));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		this.setOnKeyPressed(this::keyOnPressd);
	}

	private void keyOnPressd(KeyEvent e) {

		if (e.isControlDown() && e.getCode() == KeyCode.C) {

			if (e.isConsumed())
				return;

			ObservableList<TreeItem<XMLMeta>> items = this.getSelectionModel().getSelectedItems();
			Clipboard c = Clipboard.getSystemClipboard();
			ClipboardContent cc = new ClipboardContent();

			StringBuffer sb = new StringBuffer();
			for (TreeItem<XMLMeta> item : items) {
				sb.append(item.getValue().getText()).append("\n");
			}
			cc.putString(sb.toString());
			c.setContent(cc);

			e.consume();
		}

	}

	static class XMLTreeHandler extends DefaultHandler {

		boolean startEle;
		boolean endEle;

		TreeItem<XMLMeta> root;
		TreeItem<XMLMeta> parent;
		TreeItem<XMLMeta> current;

		int level = 0;
		public XMLTreeHandler(TreeItem<XMLMeta> parent) {
			this.root = parent;
			this.parent = parent;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);

			if (startEle) {
				level++;
				this.parent = current;
			}
			
			startEle = true;

			XMLMeta n = new XMLMeta();
			n.setName(qName);
			current = new TreeItem<>(n, createXmlTagImage());
//			current.setExpanded(true);

			this.parent.getChildren().add(current);

			int length = attributes.getLength();
			for (int i = 0; i < length; i++) {
				String qName2 = attributes.getQName(i);
				String value = attributes.getValue(i);

				XMLAttrMeta att = new XMLAttrMeta();
				att.setParent(n);
				att.setName(qName2);
				att.setText(value);
				current.getChildren().add(new TreeItem<>(att, createAttrivuteImage()));
			}
		}

		String tagPath = ClassLoader.getSystemClassLoader().getResource("META-INF/images/eclipse/obj16/html_tag_obj.gif").toExternalForm();
		private ImageView createXmlTagImage() {
			return new ImageView(tagPath);
		}

		String attrPath = ClassLoader.getSystemClassLoader().getResource("META-INF/images/eclipse/obj16/field_private_obj.gif").toExternalForm();
		private ImageView createAttrivuteImage() {
			return new ImageView(attrPath);
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (endEle) {
				level--;
			}
			this.current = current.getParent();
			endEle = true;
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			super.characters(ch, start, length);
			this.current.getValue().setText(new String(ch, start, length));
		}

	}

	public final StringProperty xmlProperty() {
		return this.xml;
	}

	public final String getXml() {
		return this.xmlProperty().get();
	}

	public final void setXml(final String xml) {
		this.xmlProperty().set(xml);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 5. 
	 * @param parent
	 * @return
	 */
	public static ContextMenu createContextMenu(FxXmlSupplier parent) {
		return FxUtil.ContextUtil.createXmlContextMenu(parent);

	}

	@Override
	public Node getNode() {
		return this;
	}
}
