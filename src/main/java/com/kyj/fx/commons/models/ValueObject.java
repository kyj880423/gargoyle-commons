package com.kyj.fx.commons.models;

class ValueObject {

	private String modules;

	private String fullName;

	private String simpleName;

	private String storyName;

	private String writer;

	private String desc;

	public ValueObject() {
	}

	public ValueObject(String modules, String fullName, String simpleName, String storyName, String writer) {

		this.modules = modules;
		this.fullName = fullName;
		this.simpleName = simpleName;
		this.storyName = storyName;
		this.writer = writer;
		desc = "";
	}

	public ValueObject(String modules, String fullName, String simpleName, String storyName, String writer, String desc) {

		this.modules = modules;
		this.fullName = fullName;
		this.simpleName = simpleName;
		this.storyName = storyName;
		this.writer = writer;
		this.desc = desc;
	}

	public String getModules() {
		return modules;
	}

	public void setModules(String modules) {
		this.modules = modules;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getStoryName() {
		return storyName;
	}

	public void setStoryName(String storyName) {
		this.storyName = storyName;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
