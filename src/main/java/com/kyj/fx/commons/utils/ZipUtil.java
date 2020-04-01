/********************************
 *	프로젝트 : idu-commons
 *	패키지   : com.kyj.fx.commons.utils;
 *	작성일   : 2018. 12. 14.
 *	작성자   : callakrsos@naver.com
 *
 *   base source from 
 *    [ http://egloos.zum.com/yeon97/v/1551569 ]
 *    
 *    modifer kyj.
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ZipUtil {

	private static final int COMPRESSION_LEVEL = 8;

	private static final int BUFFER_SIZE = 1024 * 2;

	/**
	 * 지정된 폴더를 Zip 파일로 압축한다.
	 * 
	 * @param sourcePath
	 *            - 압축 대상 디렉토리
	 * @param output
	 *            - 저장 zip 파일 이름
	 * @throws Exception
	 */
	public static void zip(String sourcePath, String output) throws Exception {

		// 압축 대상(sourcePath)이 디렉토리나 파일이 아니면 리턴한다.
		File sourceFile = new File(sourcePath);
		if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
			throw new Exception("can't found zip target.");
		}

		// output 의 확장자가 zip이 아니면 리턴한다.
		if (!(StringUtils.substringAfterLast(output, ".")).equalsIgnoreCase("zip")) {
			throw new Exception("plz check output param extension.");
		}

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;

		try {
			fos = new FileOutputStream(output); // FileOutputStream
			bos = new BufferedOutputStream(fos); // BufferedStream
			zos = new ZipOutputStream(bos); // ZipOutputStream
			zos.setLevel(COMPRESSION_LEVEL); // 압축 레벨 - 최대 압축률은 9, 디폴트 8
			zipEntry(sourceFile, sourcePath, zos); // Zip 파일 생성
			zos.finish(); // ZipOutputStream finish
		} finally {
			if (zos != null) {
				zos.close();
			}
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * 압축
	 * 
	 * @param sourceFile
	 * @param sourcePath
	 * @param zos
	 * @throws Exception
	 */
	private static void zipEntry(File sourceFile, String sourcePath, ZipOutputStream zos) throws Exception {
		// sourceFile 이 디렉토리인 경우 하위 파일 리스트 가져와 재귀호출
		if (sourceFile.isDirectory()) {

			File[] fileArray = sourceFile.listFiles(); // sourceFile 의 하위 파일 리스트
			for (int i = 0; i < fileArray.length; i++) {
				zipEntry(fileArray[i], sourcePath, zos); // 재귀 호출
			}
		} else { // sourcehFile 이 디렉토리가 아닌 경우
			BufferedInputStream bis = null;
			try {
				String sFilePath = sourceFile.getPath();
				String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());

				bis = new BufferedInputStream(new FileInputStream(sourceFile));
				ZipEntry zentry = new ZipEntry(zipEntryName);
				zentry.setTime(sourceFile.lastModified());
				zos.putNextEntry(zentry);

				byte[] buffer = new byte[BUFFER_SIZE];
				int cnt = 0;
				while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
					zos.write(buffer, 0, cnt);
				}
				zos.closeEntry();
			} finally {
				if (bis != null) {
					bis.close();
				}
			}
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 11.
	 * @param zipFile
	 * @param targets
	 * @throws Exception
	 */
	public static int zip(File zipFile, File[] targets) throws Exception {

		if (targets == null)
			throw new Exception(" targets is null.");

		if (zipFile.isDirectory())
			throw new Exception(" zipFile param must be file");

		FileUtil.mkDirs(zipFile);

		int result = 0;
		// BufferedInputStream bis = null;

		try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {

			for (File target : targets) {

				try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(target))) {

					ZipEntry zentry = new ZipEntry(target.getName());
					zentry.setTime(target.lastModified());
					out.putNextEntry(zentry);
					byte[] buffer = new byte[BUFFER_SIZE];
					int cnt = 0;
					while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
						out.write(buffer, 0, cnt);
					}
					out.closeEntry();
					result++;
				}

			}

		}

		return result;

	}

	/**
	 * extract zip file <br/>
	 *
	 * @param zipFile
	 *            - 압축 풀 Zip 파일
	 * @param targetDir
	 *            - 압축 푼 파일이 들어간 디렉토리
	 * @param fileNameToLowerCase
	 *            - 파일명을 소문자로 바꿀지 여부
	 * @throws Exception
	 */
	public static void unzip(File zipFile, File targetDir, boolean fileNameToLowerCase) throws Exception {
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry zentry = null;

		try {
			fis = new FileInputStream(zipFile); // FileInputStream
			zis = new ZipInputStream(fis); // ZipInputStream

			while ((zentry = zis.getNextEntry()) != null) {
				String fileNameToUnzip = zentry.getName();
				if (fileNameToLowerCase) { // fileName toLowerCase
					fileNameToUnzip = fileNameToUnzip.toLowerCase();
				}

				File targetFile = new File(targetDir, fileNameToUnzip);

				if (zentry.isDirectory()) {// case Directory
					targetFile.mkdirs();

				} else { // case File
					// parent Directory 생성
					targetFile.getParentFile().mkdirs();
					unzipEntry(zis, targetFile);
				}
			}
		} finally {
			if (zis != null) {
				zis.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @param zipFile
	 * @param targetDir
	 * @throws Exception
	 */
	public static void unzip(File zipFile, File targetDir) throws Exception {
		unzip(zipFile, targetDir, false);
	}

	/**
	 * Zip 파일의 한 개 엔트리의 압축을 푼다.
	 *
	 * @param zis
	 *            - Zip Input Stream
	 * @param filePath
	 *            - 압축 풀린 파일의 경로
	 * @return
	 * @throws Exception
	 */
	protected static File unzipEntry(ZipInputStream zis, File targetFile) throws Exception {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFile);

			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = zis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		return targetFile;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @param f
	 * @return
	 * @throws IOException
	 * @throws ZipException
	 */
	public static ZipFile toZipFile(File f) throws ZipException, IOException {
		return new ZipFile(f);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @param f
	 * @param entryName
	 * @return
	 * @throws IOException
	 * @throws ZipException
	 */
	public static ZipEntry getZipEntry(File f, String entryName) throws ZipException, IOException {
		return getZipEntry(toZipFile(f), entryName);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @param f
	 * @param entryName
	 * @return
	 */
	public static ZipEntry getZipEntry(ZipFile f, String entryName) {
		return f.getEntry(entryName);
	}

}
