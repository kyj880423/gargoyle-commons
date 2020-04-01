/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.script.nashorn
 *	작성일   : 2018. 2. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.xpath;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.kyj.fx.commons.fx.controls.text.XMLEditor;
import com.kyj.fx.commons.utils.DateUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLUtils;
import com.kyj.fx.fxloader.FXMLController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

/**
 * 
 * XPath Script Engine.
 * 
 * @author KYJ
 *
 */
@FXMLController(value = "XPathScriptView.fxml", isSelfController = true)
public class XpathScriptComposite extends BorderPane {

	private static final Logger LOGGER = LoggerFactory.getLogger(XpathScriptComposite.class);
	@FXML
	private XMLEditor txtXml;
	@FXML
	private TextArea txtConsole, txtXpath;

	private MenuItem miRun;

	public XpathScriptComposite() {

		FxUtil.loadRoot(XpathScriptComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	@FXML
	public void initialize() {

		ContextMenu cx = txtXml.getContextMenu();
		cx.getItems().add(new SeparatorMenuItem());
		miRun = new MenuItem("Run (F5)");
		miRun.setOnAction(this::miRunOnAction);
		cx.getItems().add(miRun);
		txtXml.setOnContextMenuRequested(ev -> {
			if (cx.isShowing())
				return;

			cx.show(this, ev.getScreenX(), ev.getScreenY());
		});
		txtXpath.setContextMenu(null);
		this.addEventFilter(KeyEvent.KEY_PRESSED, this::keyOnPress);
	}

	/**
	 * 키 이벤트
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 9.
	 * @param e
	 */
	public void keyOnPress(KeyEvent e) {

		// F5 클릭시 스크립트 실행
		if (KeyCode.F5 == e.getCode()) {
			if (e.isConsumed())
				return;

			miRunOnAction(new ActionEvent());
			e.consume();
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 9.
	 * @param e
	 */
	public void miRunOnAction(ActionEvent e) {
		LOGGER.debug("Script Engine Start.");

		txtConsole.clear();
		String script = txtXml.getText();
		String xpath = txtXpath.getText();
		Optional<NodeList> xpathNodes = XMLUtils.toXpathNodes(script, xpath, err -> {
			txtConsole.appendText(ValueUtil.toString(err));
		});
		long start = System.currentTimeMillis();
		txtConsole.appendText("Start : " + DateUtil.getDateString(start) + "\n");
		if (xpathNodes.isPresent()) {
			NodeList r = xpathNodes.get();
			int length = r.getLength();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < length; i++) {

				org.w3c.dom.Node item = r.item(i);

				// if (item instanceof
				// com.sun.org.apache.xerces.internal.dom.AttrImpl) {
				// com.sun.org.apache.xerces.internal.dom.AttrImpl attr =
				// (com.sun.org.apache.xerces.internal.dom.AttrImpl) item;
				// sb.append(attr.getValue()).append("\n");
				// } else
				toString(sb, item, 1);
				// String s = "[NodeName] :< " + item.getNodeName() + " > [Text
				// Cont] : " + item.getTextContent() + " [Node Value] : " +
				// item.getNodeValue() + " toString : " + item.toString();
				// sb.append(s).append("\n");
				// if( null != item.getAttributes()) {
				// sb.append("##Attibute##").append("\n");
				// NamedNodeMap attr = item.getAttributes();
				// int length2 = attr.getLength();
				// attr.item(index)
				// sb.append(item.getAttributes()).append("\n");
				// }

			}

			txtConsole.appendText(sb.toString() + "\n");

		}

		long end = System.currentTimeMillis();
		txtConsole.appendText("End : " + DateUtil.getDateString(end) + "\n");
		txtConsole.appendText("Cost : " + (end - start) + "\n");

		LOGGER.debug("Script Engine End.");
	}

	void toString(StringBuffer sb, org.w3c.dom.Node item, int level) {

		String s = String.format("<%s> Text Cont:[%s] ", item.getNodeName(), item.getNodeValue());
		sb.append("## Node info ##\n").append(s).append("\n");

		if (null != item.getAttributes()) {

			for (int i = 0; i < level; i++)
				sb.append(" ");

			sb.append("##Attibute##").append("\n");
			NamedNodeMap attr = item.getAttributes();

			int length = attr.getLength();
			for (int i = 0; i < length; i++) {
				sb.append("   ").append(attr.item(i).getNodeName()).append(":").append(attr.item(i).getNodeValue()).append("\n");
			}

		}
	}

	/**
	 * return control name
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 9.
	 * @return
	 */
	public static String getName() {
		return "XPath Script Engine";
	}

	/**
	 * set Xml <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 9.
	 * @param xml
	 */
	public void setXml(String xml) {
		this.txtXml.setText(xml);
	}
}
