/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.parser
 *	작성일   : 2016. 7. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.java.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.kyj.fx.commons.functions.FileCheckHandler;
import com.kyj.fx.commons.utils.ZipUtil;

/***************************
 * 
 * @author KYJ
 *
 ***************************/
public class GargoyleJavaParser {

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 *
	 * @param javaFile
	 * @param converter
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 ********************************/
	public static String getPackageName(File javaFile, FileCheckHandler<String> converter)
			throws FileNotFoundException, IOException, ParseException {
		String packageName = null;

		if (javaFile == null) {
			packageName = converter.ifNull();
		} else if (!javaFile.exists())
			packageName = converter.notExists();
		else if (javaFile.isFile()) {

			CompilationUnit cu = getCompileUnit(javaFile);
			packageName = getPackageName(cu);

		}
		return packageName;

	}

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 *
	 * @param cu
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 ********************************/
	public static String getPackageName(CompilationUnit cu) throws FileNotFoundException, IOException, ParseException {
		PackageDeclaration packageDeclaration = cu.getPackage();
		return packageDeclaration.getPackageName();
	}

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 *
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 ********************************/
	public static CompilationUnit getCompileUnit(File f) throws FileNotFoundException, IOException, ParseException {

		CompilationUnit cu = null;
		try (FileInputStream in = new FileInputStream(f)) {
			// parse the file
			cu = getCompileUnit(in);
		}

		return cu;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 3.
	 * @param str
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static CompilationUnit getCompileUnit(String str) throws FileNotFoundException, IOException, ParseException {
		CompilationUnit cu = JavaParser.parse(new ByteArrayInputStream(str.getBytes()));
		return cu;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 3.
	 * @param in
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static CompilationUnit getCompileUnit(InputStream in) throws FileNotFoundException, IOException, ParseException {
		CompilationUnit cu = JavaParser.parse(in);
		return cu;
	}

	public static String toStringVisibility(int modifier) {

		// 접근지정자 추출.
		String accessModifiers = "";

		switch (modifier) {
		case Modifier.PUBLIC:
			accessModifiers = "public";
			break;
		case Modifier.PRIVATE:
			accessModifiers = "private";
			break;
		case Modifier.PROTECTED:
			accessModifiers = "protected";
			break;
		default:
			accessModifiers = "default";
			break;
		}
		return accessModifiers;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @param zip
	 * @param entryName
	 * @return
	 */
	public static CompilationUnit fromSrcZip(File zip, String entryName) {
		CompilationUnit compileUnit = null;
		try {
			ZipFile zipFile = ZipUtil.toZipFile(zip);
			ZipEntry zipEntry = ZipUtil.getZipEntry(zipFile, entryName);
			InputStream in = zipFile.getInputStream(zipEntry);
			compileUnit = getCompileUnit(in);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return compileUnit;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @param dir
	 * @param entryName
	 * @return
	 */
	public static File lookupSrcZip(File dir) {
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.getName().equals("src.zip");
				}
			});
			if (listFiles == null || listFiles.length == 0)
				return null;
			return listFiles[0];
		} else {
			return "src.zip".equals(dir.getName()) ? dir : null;
		}
	}

}
