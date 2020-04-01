/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.biz
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.biz;

import java.util.List;
import java.util.regex.Pattern;

import com.kyj.fx.commons.word.msword.vo.SourceAnalysisDVO;

public interface SourceCodeAnalysis {

	public void compile(Pattern p);

	public List<SourceAnalysisDVO> findStatement();
}
