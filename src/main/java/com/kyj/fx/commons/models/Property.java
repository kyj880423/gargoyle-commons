package com.kyj.fx.commons.models;

public class Property {
	private String Key;

	private Object value;

	public Property(String key, Object value) {
		super();
		Key = key;
		this.value = value;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Property [Key=" + Key + ", value=" + value + "]";
	}

}
