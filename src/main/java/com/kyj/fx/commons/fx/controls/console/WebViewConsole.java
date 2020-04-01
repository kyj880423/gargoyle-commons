/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.console
 *	작성일   : 2017. 4. 26.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.console;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author KYJ
 *
 */
public class WebViewConsole extends BorderPane {

	private WebView wb;
	private TextArea txtCommand;
	private TextArea txtResult;
	private Button btnExec;

	public WebViewConsole(WebView wb) {
		this.wb = wb;
		init();
	}

	private void init() {
		txtCommand = new TextArea();
		txtResult = new TextArea();
		btnExec = new Button(" Apply ");
		SplitPane value = new SplitPane(txtResult, txtCommand);

		value.setOrientation(Orientation.VERTICAL);

		setCenter(value);
		setBottom(btnExec);

		btnExec.setOnMouseClicked(this::btnExecOnMouseClick);

		txtCommand.setOnKeyPressed(this::txtCommandOnKeyClick);
	}

	public void txtCommandOnKeyClick(KeyEvent e) {

		if (e.getCode() == KeyCode.ENTER && e.isControlDown()) {
			exec();
		}
	}

	public void btnExecOnMouseClick(MouseEvent e) {
		exec();
	}

	private void exec() {
		try {
			WebEngine engine = wb.getEngine();
			Object obj = engine.executeScript(txtCommand.getText());
			//			 JSObject obj = (JSObject)
			if (obj instanceof String) {
				txtResult.appendText(obj.toString());
			} else if (obj instanceof HTMLDocument) {
				txtResult.appendText(toHtmlElement((HTMLDocument) obj));
			} else if (obj instanceof Node) {
				Node n = (Node) obj;
				txtResult.appendText(toNodeInfo(n));
			} else {
				txtResult.appendText(obj.toString());
			}
			txtResult.appendText("\n");
		} catch (NullPointerException e) {
			txtResult.appendText("undefinded");
			txtResult.appendText("\n");
		} catch (Exception e) {
			txtResult.appendText(e.getMessage());
			txtResult.appendText("\n");
		}
	}

	private String toHtmlElement(HTMLDocument obj) {
		StringBuffer sb = new StringBuffer();
		sb.append(obj.toString()).append("\n");
		NamedNodeMap attributes = obj.getAttributes();
		return sb.toString();
	}

	private String toNodeInfo(Node n) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(n.getNodeName());
		sb.append(" ");

		NamedNodeMap attributes = n.getAttributes();
		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			Node sub = attributes.item(i);
			String nodeName = sub.getNodeName();
			String nodeValue = sub.getNodeValue();
			sb.append(nodeName).append("='").append(nodeValue).append("'");
			sb.append(" ");
		}
		sb.append(" >");
		sb.append(n.getTextContent());
		sb.append("</");
		sb.append(n.getNodeName());
		sb.append(">");
		return sb.toString();
	}
}
