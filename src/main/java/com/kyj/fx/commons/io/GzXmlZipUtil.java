/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.io
 *	작성일   : 2020. 1. 30.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
class GzXmlZipUtil {

	private static final String ZIP_ENTRY_NAME = "gzxml";
	private static final int BUFFER_SIZE = 1024 * 2;

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 30.
	 * @param outFile
	 * @param data
	 * @throws Exception
	 */
	public static void zip(File outFile, byte[] data) throws Exception {
		zip(outFile, new ByteArrayInputStream(data));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 30.
	 * @param outFile
	 * @param data
	 * @throws Exception
	 */
	public static void zip(File outFile, InputStream data) throws Exception {

		if (data == null)
			throw new Exception(" targets is null.");

		if (outFile.isDirectory())
			throw new Exception(" zipFile param must be file");

		try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)))) {

			ZipEntry zentry = new ZipEntry(ZIP_ENTRY_NAME);
			zentry.setTime(System.currentTimeMillis());
			out.putNextEntry(zentry);
			byte[] buffer = new byte[BUFFER_SIZE];
			int cnt = 0;
			while ((cnt = data.read(buffer, 0, BUFFER_SIZE)) != -1) {
				out.write(buffer, 0, cnt);
			}
			out.closeEntry();
		}
	}

	public static void zip(OutputStream out, byte[] data) throws Exception {
		if (data == null)
			throw new Exception(" targets is null.");
		try (ZipOutputStream zos = new ZipOutputStream(out)) {
			ZipEntry zentry = new ZipEntry(ZIP_ENTRY_NAME);
			zentry.setTime(System.currentTimeMillis());
			zos.putNextEntry(zentry);
			zos.write(data);
			zos.closeEntry();
		}
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
	protected static void unzipEntry(ZipInputStream zis, OutputStream out) throws Exception {
		byte[] buffer = new byte[BUFFER_SIZE];
		int len = 0;
		while ((len = zis.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2020. 1. 30.
	 * @param zipFile
	 * @param out
	 * @throws Exception
	 */
	public static void unzip(File zipFile, OutputStream out) throws Exception {
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry zentry = null;

		try {
			fis = new FileInputStream(zipFile); // FileInputStream
			zis = new ZipInputStream(fis); // ZipInputStream

			while ((zentry = zis.getNextEntry()) != null) {
				if (ZIP_ENTRY_NAME.contentEquals(zentry.getName())) {
					unzipEntry(zis, out);
					break;
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

}
