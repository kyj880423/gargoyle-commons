/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2017. 12. 8.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.models;

import java.io.File;

/**
 * @author KYJ
 *
 */
public class BehaviorReferenceVO {
	private File wib;
	private String txtFileName;

	public BehaviorReferenceVO(File wib, String txtFileName) {
		this.wib = wib;
		this.txtFileName = txtFileName;
	}

	public File getWib() {
		return wib;
	}

	public void setWib(File wib) {
		this.wib = wib;
	}

	public String getTxtFileName() {
		return txtFileName;
	}

	public void setTxtFileName(String txtFileName) {
		this.txtFileName = txtFileName;
	}

}
