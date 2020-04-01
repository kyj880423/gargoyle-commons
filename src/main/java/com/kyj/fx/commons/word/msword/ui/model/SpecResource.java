/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.resources
 *	작성일   : 2016. 8. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.ui.model;

import java.io.File;

import com.kyj.fx.commons.word.msword.vo.ProgramSpecSVO;

/**
 * @author KYJ
 *
 */
public class SpecResource {

	private File projectFile;
	private File targetFile;
	private ProgramSpecSVO svo;

	public SpecResource(File projectFile, File javaFile) {
		this.projectFile = projectFile;
		this.targetFile = javaFile;
	}

	public SpecResource(ProgramSpecSVO svo) {
		this.svo = svo;
	}

	/**
	 * @return the svo
	 */
	public final ProgramSpecSVO getSvo() {
		return svo;
	}

	/**
	 * @param svo
	 *            the svo to set
	 */
	public final void setSvo(ProgramSpecSVO svo) {
		this.svo = svo;
	}

	/**
	 * @return the projectFile
	 */
	public final File getProjectFile() {
		return projectFile;
	}

	/**
	 * @return the targetFile
	 */
	public final File getTargetFile() {
		return targetFile;
	}

	/**
	 * @param projectFile
	 *            the projectFile to set
	 */
	public final void setProjectFile(File projectFile) {
		this.projectFile = projectFile;
	}

	/**
	 * @param targetFile
	 *            the targetFile to set
	 */
	public final void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}

}
