/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.magt.scripts
 *	작성일   : 2019. 11. 15.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class EventScriptDVO {
	/**
	 * 다른 데이터셋을 구분하기 위한 값으로 유일값이 되어야 합니다. <br/>
	 * 
	 * @최초생성일 2019. 11. 18.
	 */
	private String scriptId;
	private String scriptTitle;
	private StringProperty scriptContent = new SimpleStringProperty();
	private String name;
	private String scriptPath;
	private boolean isDir;

	public EventScriptDVO(String scriptId, String scriptTitle, String scriptPath, String name) {
		this();
		this.scriptId = scriptId;
		this.scriptTitle = scriptTitle;
		this.scriptPath = scriptPath;
	}

	public EventScriptDVO(String scriptId, String scriptTitle, String scriptPath) {
		this();
		this.scriptId = scriptId;
		this.scriptTitle = scriptTitle;
		this.scriptPath = scriptPath;
	}

	public EventScriptDVO() {
		super();
	}

	/**
	 * @return the scriptId
	 */
	public String getScriptId() {
		return scriptId;
	}

	/**
	 * @param scriptId
	 *            the scriptId to set
	 */
	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	/**
	 * @return the scriptTitle
	 */
	public String getScriptTitle() {
		return scriptTitle;
	}

	/**
	 * @param scriptTitle
	 *            the scriptTitle to set
	 */
	public void setScriptTitle(String scriptTitle) {
		this.scriptTitle = scriptTitle;
	}

	/**
	 * @return the scriptPath
	 */
	public String getScriptPath() {
		return scriptPath;
	}

	/**
	 * @param scriptPath
	 *            the scriptPath to set
	 */
	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return scriptPath + (name == null ? "" : name);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isDir
	 */
	public boolean isDir() {
		return isDir;
	}

	/**
	 * @param isDir
	 *            the isDir to set
	 */
	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((scriptId == null) ? 0 : scriptId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventScriptDVO other = (EventScriptDVO) obj;
		if (scriptId == null) {
			if (other.scriptId != null)
				return false;
		} else if (!scriptId.equals(other.scriptId))
			return false;
		return true;
	}

	public final StringProperty scriptContentProperty() {
		return this.scriptContent;
	}

	public final String getScriptContent() {
		return this.scriptContentProperty().get();
	}

	public final void setScriptContent(final String scriptContent) {
		this.scriptContentProperty().set(scriptContent);
	}

}
