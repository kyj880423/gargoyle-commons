/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.biz
 *	작성일   : 2016. 2. 22.
 *	작성자   : KYJ
 *******************************/

package com.kyj.fx.commons.word.msword.biz;

import java.util.ArrayList;
import java.util.List;

import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.word.msword.vo.SourceAnalysisDVO;

/**
 * @author KYJ
 *
 */
public class InspectorBiz {
	private static InspectorBiz s;

	/**
	 * 2014. 7. 2. KYJ
	 *
	 * @return
	 * @처리내용 : 인스턴스 반환
	 */
	public static InspectorBiz getInstance() {
		if (s == null) {
			s = new InspectorBiz();
		}
		return s;
	}

	/**
	 * 2014. 6. 24. KYJ
	 *
	 * @return
	 * @처리내용 : 메소드 목록을구한다.
	 */
	public List<SourceAnalysisDVO> methodList(InspectorSourceMeta inspectorSourceMeta) {

		String fileName = inspectorSourceMeta.getFileName();
		List<SourceAnalysisDVO> findMethodStatement = new ArrayList<SourceAnalysisDVO>();
		if (FileUtil.isJavaFile(fileName)) {
			SourceCodeAnalysis sa = new JavaSourceAnalysis(inspectorSourceMeta);
			if (sa != null) {
				findMethodStatement = sa.findStatement();
			}
		}
		return findMethodStatement;
	}
}
