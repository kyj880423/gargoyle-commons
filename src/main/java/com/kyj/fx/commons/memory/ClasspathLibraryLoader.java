/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.memory
 *	작성일   : 2019. 01. 11.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

/**
 * 클래스패스에 참조되는 자바 코어 라이브러리를 로드하기 위한 클래스
 * 
 * @author KYJ
 *
 */
public class ClasspathLibraryLoader extends CoreClasspathLoader {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.commons.memory.CoreClasspathLoader#getLibraryPaths()
	 */
	@Override
	protected String[] getLibraryPaths() {
		String bootClassPath = System.getProperty("java.class.path");
		String[] locations = bootClassPath.split(";");
		return locations;
	}

}
