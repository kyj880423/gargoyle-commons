/**
 * KYJ
 * 2015. 10. 13.
 */
package com.kyj.fx.commons.fx.controls.tree;

import java.io.File;

/**
 * 파일트리를 표현하기 위한 기본단위 래퍼클래스
 *
 * @author KYJ
 *
 */
public class JavaProjectFileWrapper extends FileWrapper {

	/**
	 * @최초생성일 2017. 6. 15.
	 */
	private static final long serialVersionUID = -8511619052170433254L;
	/**
	 * 자바 프로젝트인지 유무
	 *
	 * @최초생성일 2016. 7. 10.
	 */
	private boolean isJavaProjectFile;
	/**
	 * svn 연결이 되었는지 유무
	 *
	 * @최초생성일 2016. 7. 10.
	 */
	private boolean isSVNConnected;

	/**
	 * SVN 연결시 연계되는 SQLIte DB 파일.
	 * @최초생성일 2016. 7. 18.
	 */
	private File wcDbFile;

	public JavaProjectFileWrapper(File file) {
		super(file);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isFile() {
		return this.file.isFile();
	}

	@Override
	public String toString() {
		/*2016.07.10 set empty toString*/
		return "";
		//		return file.getName();
	}

	public boolean isJavaProjectFile() {
		return isJavaProjectFile;
	}

	public void setJavaProjectFile(boolean isJavaProjectFile) {
		this.isJavaProjectFile = isJavaProjectFile;
	}

	public boolean isSVNConnected() {
		return isSVNConnected;
	}

	public void setSVNConnected(boolean isSVNConnected) {
		this.isSVNConnected = isSVNConnected;
	}

	public void setWcDbFile(File wcDbFile) {
		this.wcDbFile = wcDbFile;
	}

	public File getWcDbFile() {
		return wcDbFile;
	}

}
