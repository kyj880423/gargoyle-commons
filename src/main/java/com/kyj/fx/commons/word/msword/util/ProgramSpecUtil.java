/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2016. 2. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.exceptions.ProgramSpecSourceException;
import com.kyj.fx.commons.word.msword.biz.InspectorSourceMeta;
import com.kyj.fx.commons.word.msword.model.AbstractJavaProgramSpecFile;
import com.kyj.fx.commons.word.msword.model.AbstractXframeProgramSpecFile;
import com.kyj.fx.commons.word.msword.model.IProgramSpecFile;
import com.kyj.fx.commons.word.msword.model.IProgramSpecFile.FILE_TYPE;
import com.kyj.fx.commons.word.msword.model.IProgramSpecFile.SOURCE_FILE_TYPE;
import com.kyj.fx.commons.word.msword.template.ProgramSpecWordTemplate;
import com.kyj.fx.commons.word.msword.vo.ImportsDVO;
import com.kyj.fx.commons.word.msword.vo.MethodDVO;
import com.kyj.fx.commons.word.msword.vo.ProgramSpecSVO;
import com.kyj.fx.commons.word.msword.vo.SourceAnalysisDVO;
import com.kyj.fx.commons.word.msword.vo.TableDVO;
import com.kyj.fx.commons.word.msword.vo.UserSourceMetaDVO;

/**
 * @author KYJ
 *
 */
public class ProgramSpecUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProgramSpecUtil.class);

	public static boolean createDefault(File sourceFile) {
		return createDefault(sourceFile, null);
	}

	public static boolean createDefault(File sourceFile, File targetFile) {
		boolean result = false;
		try {
			IProgramSpecFile newInstance = ProgramSpecFileUtil.newInstance(sourceFile);

			ProgramSpecSVO svo = new ProgramSpecSVO();

			if (newInstance instanceof AbstractJavaProgramSpecFile) {
				svo = doJavaFile("sampleJavaProject", sourceFile.getName(), (AbstractJavaProgramSpecFile) newInstance);
			} else if (newInstance instanceof AbstractXframeProgramSpecFile) {
				svo = doJsFile("sampleXframeProject", sourceFile.getName(), (AbstractXframeProgramSpecFile) newInstance);
			}

			IProgramSpecFile abPFile = svo.getFile();
			// 확장자까지 포함된 파일명
			String simpleFileName = svo.getUserSourceMetaDVO().getSimpleFileName();
			// 확장자를 제거하고 워드문서가 생성될 파일명 작성
			String fileName = "(" + abPFile.getSourceFileType().toString() + ")(사양서)"
					+ simpleFileName.substring(0, simpleFileName.indexOf('.')) + ".docx";
			// 풀 경로 설정.( 유저의 데스크탑에 저장됨.)
			String docFile = "";
			if (targetFile == null)
				docFile = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + fileName;
			else
				docFile = targetFile.getAbsolutePath();

			ProgramSpecWordTemplate word = new ProgramSpecWordTemplate(docFile, svo);
			word.write();
			word.close();
			LOGGER.debug("사양서 생성 완료.[ " + sourceFile + " ]");
			result = true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}

	public static boolean createDefault(ProgramSpecSVO source, File targetFile) {

		boolean result = false;
		try {

			//			IProgramSpecFile abPFile = source.getFile();
			//			// 확장자까지 포함된 파일명
			//			String simpleFileName = source.getUserSourceMetaDVO().getSimpleFileName();
			//			// 확장자를 제거하고 워드문서가 생성될 파일명 작성
			//			String fileName = "(" + abPFile.getSourceFileType().toString() + ")(사양서)"
			//					+ simpleFileName.substring(0, simpleFileName.indexOf('.')) + ".docx";
			//			// 풀 경로 설정.( 유저의 데스크탑에 저장됨.)
			//			String docFile = "";
			//			if (targetFile == null)
			//				docFile = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + fileName;
			//			else
			//				docFile = targetFile.getAbsolutePath();

			IProgramSpecFile newInstance = new IProgramSpecFile() {

				@Override
				public SOURCE_FILE_TYPE getSourceFileType() {
					return SOURCE_FILE_TYPE.BIZ;
				}

				@Override
				public InspectorSourceMeta getInspectorSourceMeta() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getFileName() {
					return targetFile.getName();
				}

				@Override
				public FILE_TYPE getFileType() {
					// TODO Auto-generated method stub
					return FILE_TYPE.JAVA;
				}

				@Override
				public List<TableDVO> getTableList() {
					// TODO Auto-generated method stub
					return Collections.emptyList();
				}

			};

			source.setFile(newInstance);

			ProgramSpecWordTemplate word = new ProgramSpecWordTemplate(targetFile.getAbsolutePath(), source);
			word.write();
			word.close();

			LOGGER.debug("사양서 생성 완료.[ " + source + " ]");

			result = true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;

	}

	public static ProgramSpecSVO doJsFile(String projectName, String fileName, AbstractXframeProgramSpecFile newInstance) {

		ProgramSpecSVO svo = new ProgramSpecSVO();

		newInstance.setProjectName(projectName);
		svo.setFile(newInstance);

		String userName = System.getProperty("user.name");

		List<SourceAnalysisDVO> listStatement = newInstance.listStatement();

		UserSourceMetaDVO userSourceMetaDVO = new UserSourceMetaDVO();
		userSourceMetaDVO.setProjectName(projectName);
		userSourceMetaDVO.setSimpleFileName(fileName);
		userSourceMetaDVO.setRealFilePath(newInstance.getFullFileName());
		userSourceMetaDVO.setUserPcName(userName);
		userSourceMetaDVO.setPackages("");

		svo.setUserSourceMetaDVO(userSourceMetaDVO);

		/* 시작 import문 처리 */
		svo.setImportsDVO(new ImportsDVO());
		/* 끝 import문 처리 */

		// 테이블 데이터 바인드
		List<MethodDVO> methodDVOList = new ArrayList<MethodDVO>();
		for (SourceAnalysisDVO dvo : listStatement) {
			try {
				// 소스내에 존재하는 메소드명.. 접근지 정자 + static + void 등의 잡다한 정보가 담겨있다.
				String methodName = dvo.getMethodName();
				String methodDescription = newInstance.getMethodDescription(methodName);
				MethodDVO methodDVO = null;
				methodDVO = AbstractXframeProgramSpecFile.toMethodDVO(methodName);
				methodDVO.setDescription(methodDescription);
				methodDVOList.add(methodDVO);
			} catch (ProgramSpecSourceException e) {
				e.printStackTrace();
			}

		}

		svo.setMethodDVOList(methodDVOList);
		return svo;
	}

	public static ProgramSpecSVO doJavaFile(String projectName, String fileName, AbstractJavaProgramSpecFile newInstance) {

		ProgramSpecSVO svo = new ProgramSpecSVO();

		String packageNames = newInstance.getPackage();
		newInstance.setPackage(packageNames);
		newInstance.setProjectName(projectName);
		svo.setFile(newInstance);
		// svo.getUserSourceMetaDVO().setProjectName(projectName);
		// svo.getUserSourceMetaDVO().setPackages(packageNames);

		String userName = System.getProperty("user.name");

		List<SourceAnalysisDVO> listStatement = newInstance.listStatement();

		UserSourceMetaDVO userSourceMetaDVO = new UserSourceMetaDVO();
		userSourceMetaDVO.setProjectName(projectName);
		userSourceMetaDVO.setSimpleFileName(fileName);
		userSourceMetaDVO.setRealFilePath(newInstance.getFullFileName());
		userSourceMetaDVO.setUserPcName(userName);
		userSourceMetaDVO.setPackages(packageNames);
		svo.setUserSourceMetaDVO(userSourceMetaDVO);

		/* 시작 import문 처리 */
		List<String> imports = newInstance.getImports();
		ImportsDVO importsDVO = new ImportsDVO();
		importsDVO.setImports(imports);
		svo.setImportsDVO(importsDVO);

		/* 끝 import문 처리 */

		// 테이블 데이터 바인드
		List<MethodDVO> methodDVOList = new ArrayList<MethodDVO>();
		for (SourceAnalysisDVO dvo : listStatement) {
			try {
				// 소스내에 존재하는 메소드명.. 접근지 정자 + static + void 등의 잡다한 정보가 담겨있다.
				String methodName = dvo.getMethodName();
				String methodDescription = newInstance.getMethodDescription(methodName);
				MethodDVO methodDVO = null;
				methodDVO = AbstractJavaProgramSpecFile.toMethodDVO(methodName);
				methodDVO.setDescription(methodDescription);
				methodDVOList.add(methodDVO);
			} catch (ProgramSpecSourceException e) {
				e.printStackTrace();
			}

		}

		svo.setMethodDVOList(methodDVOList);
		return svo;
	}
}
