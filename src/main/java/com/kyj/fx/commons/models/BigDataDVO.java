/**
 * 
 */
package com.kyj.fx.commons.models;

/**
 * BigData
 * 
 * @author Hong
 *
 */
public class BigDataDVO {

	private String type;

	private String value;

	public BigDataDVO() {
		super();
	}

//	enum datatype {
//		clob, blob, text, bytea
//	}

	/**
	 * 
	 * @param type
	 *            데이터 타입
	 * @param value
	 *            값
	 */
	public BigDataDVO(String type, String value) {
		super();
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return type;
	}

}
