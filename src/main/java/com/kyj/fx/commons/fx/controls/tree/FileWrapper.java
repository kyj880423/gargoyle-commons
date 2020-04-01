/**
 * KYJ
 * 2015. 10. 13.
 */
package com.kyj.fx.commons.fx.controls.tree;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 파일트리를 표현하기 위한 기본단위 래퍼클래스
 *
 * @author KYJ
 *
 */
public class FileWrapper implements Serializable {
	/**
	 * @최초생성일 2016. 7. 18.
	 */
	private static final long serialVersionUID = -6959924416500023356L;
	/**
	 * 파일
	 *
	 * @최초생성일 2015. 10. 21.
	 */
	protected File file;
	/**
	 * 숨김파일을 보여줄지 유무. (숨김파일의 기준은 파일명이 '.'으로 시작하는경우 숨김파일이라 가정한다.)
	 *
	 * @최초생성일 2015. 10. 21.
	 */
	private boolean showHiddenFile = true;

	public FileWrapper(File file) {
		super();
		this.file = file;
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

	/**
	 * @return the showHiddenFile
	 */
	public boolean isShowHiddenFile() {
		return showHiddenFile;
	}

	/**
	 * @param showHiddenFile
	 *            the showHiddenFile to set
	 */
	public void setShowHiddenFile(boolean showHiddenFile) {
		this.showHiddenFile = showHiddenFile;
	}

	public File[] listFiles() {

		File[] listFiles = this.file.listFiles((dir, name) -> {
			if (!isShowHiddenFile()) {
				return !name.startsWith(".");
			}
			return true;
		});

		Arrays.sort(listFiles, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {

				if (o1.isFile() && !o2.isFile())
					return 1;
				else if (!o1.isFile() && o2.isFile())
					return -1;

				return o1.getName().compareTo(o2.getName());
			}
		});
		return listFiles;
	}

}
