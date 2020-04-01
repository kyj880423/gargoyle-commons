/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

public class TableDVO {

	private String tableId;
	private String tableName;
	private String crud;

	public TableDVO() {
		super();
	}

	public TableDVO(String tableId, String tableName, String crud) {
		super();
		this.tableId = tableId;
		this.tableName = tableName;
		this.crud = crud;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCrud() {
		return crud;
	}

	public void setCrud(String crud) {
		this.crud = crud;
	}

	@Override
	public String toString() {
		return tableId;
	}

}
