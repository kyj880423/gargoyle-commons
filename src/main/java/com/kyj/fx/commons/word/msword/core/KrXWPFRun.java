/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.core
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.core;

import org.apache.poi.xwpf.usermodel.IRunBody;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class KrXWPFRun extends XWPFRun {

	public KrXWPFRun(XWPFRun run) {
		this(run.getCTR(), run.getParent());
	}

	public KrXWPFRun(CTR r, IRunBody p) {
		super(r, p);
	}

	@Override
	public void setFontFamily(String fontFamily) {
		CTR run = getCTR();
		CTRPr pr = run.isSetRPr() ? run.getRPr() : run.addNewRPr();
		CTFonts fonts = pr.isSetRFonts() ? pr.getRFonts() : pr.addNewRFonts();
		fonts.setHAnsi(fontFamily);
		fonts.setEastAsia(fontFamily);
		fonts.setAscii(fontFamily);
		
		
		super.setFontFamily(fontFamily);

	}
	
	

}
