/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class AbstractDVO {

	private BooleanProperty commonsClicked;

	private String _status;

	public AbstractDVO() {
		commonsClicked = new SimpleBooleanProperty();
	}

	public boolean getClicked() {
		return commonsClicked.get();
	}

	public void setClicked(boolean clicked) {
		this.commonsClicked.set(clicked);
	}

	public BooleanProperty commonsClickedProperty() {
		return commonsClicked;
	}

	public String get_status() {
		return _status;
	}

	public void set_status(String _status) {
		this._status = _status;
	}

}
