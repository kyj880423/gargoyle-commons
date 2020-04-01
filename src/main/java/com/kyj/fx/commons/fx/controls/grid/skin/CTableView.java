/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.javafx.scene.control.skin
 *	작성일   : 2017. 7. 6.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid.skin;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author KYJ
 *
 */
public class CTableView<T> extends TableView<T> {

//	private final BooleanProperty fixingColumnsAllowedProperty = new SimpleBooleanProperty(true);

	/**
	* Return whether change to Fixed columns are allowed.
	*
	* @return whether change to Fixed columns are allowed.
	*/
//	public boolean isFixingColumnsAllowed() {
//		return fixingColumnsAllowedProperty.get();
//	}

//	public BooleanProperty fixingColumnsAllowedProperty() {
//		return fixingColumnsAllowedProperty;
//	}

//	private ObservableList<TableColumn<T, ?>> fixedColumns = FXCollections.observableArrayList();
//
//	public ObservableList<TableColumn<T, ?>> getFixedColumns() {
//		return fixedColumns;
//	}
	
	private IntegerProperty fixedColumnIndex = new SimpleIntegerProperty(-1);

	public int getFixedColumnIndex(){
		return fixedColumnIndex.get();
	}
	
	public void setFixedColumnIndex(int colIndex){
		fixedColumnIndex.set(colIndex);
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return new CTableViewSkin<>(this);
	}



}
