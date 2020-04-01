/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.parser
 *	작성일   : 2016. 6. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.java.parser;

import com.kyj.fx.commons.fx.controls.grid.AbstractDVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/***************************
 *
 * @author KYJ
 *
 ***************************/
public class FXTextElement extends AbstractDVO {

	private StringProperty url = new SimpleStringProperty();
	private StringProperty localName = new SimpleStringProperty();
	private StringProperty qname = new SimpleStringProperty();
	private StringProperty value = new SimpleStringProperty();

	public FXTextElement(String url, String localName, String qname, String value) {
		super();
		this.url.set(url);
		this.localName.set(localName);
		this.qname.set(qname);
		this.value.set(value);
	}

	public StringProperty urlProperty() {
		return this.url;
	}

	public String getUrl() {
		return this.urlProperty().get();
	}

	public void setUrl(final String url) {
		this.urlProperty().set(url);
	}

	public StringProperty localNameProperty() {
		return this.localName;
	}

	public String getLocalName() {
		return this.localNameProperty().get();
	}

	public void setLocalName(final String localName) {
		this.localNameProperty().set(localName);
	}

	public StringProperty qnameProperty() {
		return this.qname;
	}

	public String getQname() {
		return this.qnameProperty().get();
	}

	public void setQname(final String qname) {
		this.qnameProperty().set(qname);
	}

	public StringProperty valueProperty() {
		return this.value;
	}

	public String getValue() {
		return this.valueProperty().get();
	}

	public void setValue(final String value) {
		this.valueProperty().set(value);
	}

}
