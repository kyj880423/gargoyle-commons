/********************************
 *	프로젝트 : sos-client
 *	패키지   : com.samsung.sds.sos.client.util
 *	작성일   : 2016. 9. 6.
 *	프로젝트 : OPERA 
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.excel;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TableColumn;

/**
 * 엑셀처리시 필요한 메타데이터.
 * 
 * @author KYJ
 *
 */
public class ExcelColumnExpression {

	/**
	 * 부모 컬럼의 메타데이터를 가리킴.
	 * @최초생성일 2016. 9. 20.
	 */
	ExcelColumnExpression parent;

	/**
	 * 실제 뷰에 맵핑된값
	 * @최초생성일 2016. 9. 6.
	 */
	String realText;

	/**
	 * 뷰에 매핑된 다른 텍스를 표현하는경우 사용. 예측 : 다국어별 텍스트등
	 * @최초생성일 2016. 9. 6.
	 */
	String displayText;
	/**
	 * 해당 클래스에 매핑된 테이블컬럼을 가리킴.
	 * @최초생성일 2016. 9. 6.
	 */
	TableColumn<?, ?> tableColumn;
	/**
	 * 뷰 순서대로 표현되는 인덱스 순서.
	 * @최초생성일 2016. 9. 6.
	 */
	int index;
	/**
	 * 계층형 컬럼을 표현할떄의 레벨순서. 가장위에 있는컬럼은 0
	 * @최초생성일 2016. 9. 6.
	 */
	int level;
	/**
	 * 계층형 컬럼에서 자식노드들을 일컬음.
	 * @최초생성일 2016. 9. 6.
	 */
	List<ExcelColumnExpression> childrens = new ArrayList<>();

	/**
	 * 보임속성인지여부
	 * @최초생성일 2016. 9. 7.
	 */
	boolean isVisible;

	public ExcelColumnExpression getParent() {
		return parent;
	}

	public void setParent(ExcelColumnExpression parent) {
		this.parent = parent;
	}

	public String getRealText() {
		return realText;
	}

	public void setRealText(String realText) {
		this.realText = realText;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public TableColumn<?, ?> getTableColumn() {
		return tableColumn;
	}

	public void setTableColumn(TableColumn<?, ?> tableColumn) {
		this.tableColumn = tableColumn;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<ExcelColumnExpression> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<ExcelColumnExpression> childrens) {
		this.childrens = childrens;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	public String toString() {
		return "ExcelColumnExpression [realText=" + realText + ", displayText=" + displayText + ", index=" + index + ", level=" + level
				+ ", isVisible=" + isVisible + "]";
	}

}
