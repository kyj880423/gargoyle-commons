/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.functions
 *	작성일   : 2018. 10. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import com.kyj.fx.commons.fx.controls.grid.AbstractDVO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ
 *
 */
public class DabaseInfo extends AbstractDVO {
	StringProperty database = new SimpleStringProperty();
	StringProperty url = new SimpleStringProperty();
	StringProperty id = new SimpleStringProperty();
	StringProperty password = new SimpleStringProperty();
	StringProperty driver = new SimpleStringProperty();

	public final StringProperty databaseProperty() {
		return this.database;
	}

	public final java.lang.String getDatabase() {
		return this.databaseProperty().get();
	}

	public final void setDatabase(final java.lang.String database) {
		this.databaseProperty().set(database);
	}

	public final StringProperty urlProperty() {
		return this.url;
	}

	public final java.lang.String getUrl() {
		return this.urlProperty().get();
	}

	public final void setUrl(final java.lang.String url) {
		this.urlProperty().set(url);
	}

	public final StringProperty idProperty() {
		return this.id;
	}

	public final java.lang.String getId() {
		return this.idProperty().get();
	}

	public final void setId(final java.lang.String id) {
		this.idProperty().set(id);
	}

	public final StringProperty passwordProperty() {
		return this.password;
	}

	public final java.lang.String getPassword() {
		return this.passwordProperty().get();
	}

	public final void setPassword(final java.lang.String password) {
		this.passwordProperty().set(password);
	}

	public final StringProperty driverProperty() {
		return this.driver;
	}

	public final java.lang.String getDriver() {
		return this.driverProperty().get();
	}

	public final void setDriver(final java.lang.String driver) {
		this.driverProperty().set(driver);
	}

}
