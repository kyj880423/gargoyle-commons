/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.tree
 *	작성일   : 2018. 3. 31.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ
 *
 */
public class MethodItem {

	/**
	 * Object Reference.
	 * 
	 * @최초생성일 2018. 3. 31.
	 */
	private ObjectProperty<Object> reference = new SimpleObjectProperty<>();
	/**
	 * method name
	 * 
	 * @최초생성일 2018. 3. 31.
	 */
	private StringProperty name = new SimpleStringProperty();

	public final ObjectProperty<Object> referenceProperty() {
		return this.reference;
	}

	public final Object getReference() {
		return this.referenceProperty().get();
	}

	public final void setReference(final Object reference) {
		this.referenceProperty().set(reference);
	}

	public final StringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}

}
