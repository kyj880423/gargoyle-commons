/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

import java.util.ArrayList;
import java.util.List;

public class ImportsDVO {
	private List<String> imports;

	public ImportsDVO() {
		super();
		imports = new ArrayList<String>();
	}

	public List<String> getImports() {
		return imports;
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
	}

	public boolean addImport(String importStatement) {
		return this.imports.add(importStatement);
	}

	public String toImportStatement() {
		if (imports == null || imports.isEmpty())
			return "";

		StringBuffer sb = new StringBuffer();
		for (String i : imports) {
			sb.append(i);
		}
		return sb.toString();
	}

}
