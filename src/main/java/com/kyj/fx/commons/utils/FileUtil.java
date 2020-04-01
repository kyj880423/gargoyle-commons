/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.functions.LoadFileOptionHandler;
import com.kyj.fx.commons.memory.CachedMap;
import com.kyj.fx.commons.threads.CloseableCallable;
import com.kyj.fx.commons.threads.DemonThreadFactory;

/**
 * @author KYJ
 *
 */
public class FileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

	/********************************
	 * 작성일 : 2016. 7. 13. 작성자 : KYJ
	 *
	 * SnapShot 임시 디렉토리 리턴.
	 *
	 * @return
	 ********************************/
	public static File getTempSnapShotDir() {
		File file = new File("SnapShot");
		if (!file.exists())
			file.mkdir();
		return file;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 1.
	 * @param pathName
	 * @return
	 */
	public static boolean openFile(String pathName) {
		return openFile(new File(pathName));
	}

	/**
	 * openFile
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 2. 18.
	 * @param file
	 * @return
	 */
	public static boolean openFile(File file) {
		return openFile(file, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 10.
	 * @param file
	 * @param errorHandler
	 * @return
	 */
	public static boolean openFile(File file, Consumer<Exception> errorHandler) {
		return fileOpenAction(file, errorHandler);
	}

	/**
	 * @최초생성일 2017. 10. 10.
	 */
	private static BiFunction<File, Consumer<Exception>, Boolean> openAction = (file, errhandler) -> {
		boolean isSuccess = false;
		try {
			Desktop.getDesktop().open(file);
			isSuccess = true;
		} catch (IOException e) {

			String osName = SystemUtils.OS_NAME.toLowerCase();
			LOGGER.debug(osName);
			if (osName.contains("window")) {
				RuntimeClassUtil.simpleExeAsynchLazy(Arrays.asList("explorer", file.getAbsolutePath()), errhandler);
			} else
				errhandler.accept(e);
		}
		return isSuccess;
	};

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 10.
	 * @param file
	 * @param errorHandler
	 * @return
	 */
	private static boolean fileOpenAction(File file, Consumer<Exception> errorHandler) {
		return fileAction(file, openAction, errorHandler);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 10.
	 * @param file
	 * @param action
	 * @param errorHandler
	 * @return
	 */
	private static boolean fileAction(File file, BiFunction<File, Consumer<Exception>, Boolean> action, Consumer<Exception> errorHandler) {
		if (Desktop.isDesktopSupported()) {
			if (file.exists()) {
				return action.apply(file, errorHandler);
			}
		}
		return false;
	}

	/**
	 * 파일확장자 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param extension
	 */
	public static String getFileExtension(File fileName) {
		return getFileExtension(fileName.getName());
	}

	/**
	 * 파일확장자 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param fileName
	 * @return
	 */
	public static String getFileExtension(String fileName) {
		int dotIndex = -1;
		int length = fileName.length();

		for (int i = length - 1; i >= 0; i--) {
			if (fileName.charAt(i) == '.') {
				dotIndex = i;
				break;
			}
		}
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}

	private static final Map<File, String> cacheReadFile = new CachedMap<>(60000);

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 18.
	 * @param file
	 * @return
	 */
	public static String readFile(File file) {
		return readFile(file, false, LoadFileOptionHandler.getDefaultHandler());
	}

	/**
	 * 파일 읽기 처리.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 19.
	 * @param file
	 * @param options
	 * @return
	 */
	public static String readFile(File file, LoadFileOptionHandler options) {
		return readFile(file, false, options);
	}

	/**
	 * 파일 읽기 처리.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 26.
	 * @param file
	 * @param useCache
	 *            메모리에 읽어온 파일의 컨텐츠를 임시저장함.
	 * @param options
	 * @return
	 */
	public static String readFile(File file, boolean useCache, LoadFileOptionHandler options) {
		String content = "";

		if (useCache && cacheReadFile.containsKey(file)) {
			LOGGER.debug(" file -> {} read from cache.  ", file);
			return cacheReadFile.get(file);
		}

		try {

			if (file != null && options == null) {
				content = FileUtils.readFileToString(file, "UTF-8");
				if (useCache)
					cacheReadFile.put(file, content);

				return content;
			}

			if (file == null) {

				if (options == null)
					return null;

				Function<File, String> fileNotFoundThan = options.getFileNotFoundThan();
				if (fileNotFoundThan != null) {
					content = fileNotFoundThan.apply(file);
					return content;
				}
				return null;
			}

			List<String> fileNameLikeFilter = options.getFileNameLikeFilter();
			boolean isMatch = false;
			if (fileNameLikeFilter == null) {
				isMatch = true;
			} else {
				for (String ext : fileNameLikeFilter) {
					if (file.getName().endsWith(ext)) {
						isMatch = true;
						break;
					}
				}
			}

			if (isMatch && file.exists()) {

				// 인코딩이 존재하지않는경우 UTF-8로 치환.
				String encoding = options.getEncoding();
				if (ValueUtil.isEmpty(encoding))
					encoding = "UTF-8";

				try (FileInputStream in = new FileInputStream(file)) {
					// FileUtils.readFileToString(file,encoding);
					content = FileUtil.readToString(in, encoding);
				}

			} else {

				// 만약 파일이 존재하지않는다면 option파라미터에서 제공되는 처리내용을 반영.
				Function<File, String> fileNotFoundThan = options.getFileNotFoundThan();
				if (fileNotFoundThan != null) {
					content = fileNotFoundThan.apply(file);
				}
			}

		} catch (Exception e) {
			if (options != null)
				options.setException(e);
			LOGGER.error(ValueUtil.toString(e));
		}

		if (useCache)
			cacheReadFile.put(file, content);

		return content;

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 19.
	 * @param is
	 * @return
	 */
	public static String readToString(InputStream is) {
		return readToString(is, "UTF-8");
	}

	/**
	 * READ TO String
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @param f
	 * @param charset
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readToString(File f, String charset) throws FileNotFoundException, IOException {
		return readToString(f, Charset.forName(charset));
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 9. 
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readToString(File f) throws FileNotFoundException, IOException {
		return readToString(f, "utf-8");
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 9. 
	 * @param f
	 * 		reading target.
	 * @param charset
	 * 		encoding.
	 * @param handler
	 * @return 
	 * 			if on error return null.
	 */
	public static String readToString(File f, String charset, ExceptionHandler handler) {
		try {
			return readToString(f, Charset.forName(charset));
		} catch (IOException e) {
			handler.handle(e);
		}
		return null;
	}

	/**
	 * READ TO String
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @param f
	 * @param encoding
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readToString(File f, Charset encoding) throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(f)) {
			return readToString(fis, encoding);
		}
	}

	/**
	 * READ TO String
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 3.
	 * @param is
	 * @param encoding
	 * @return
	 */
	public static String readToString(InputStream is, Charset encoding) {
		return readToString(is, encoding == null ? "UTF-8" : encoding.name());
	}

	/**
	 * READ TO String
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 1.
	 * @param is
	 * @param encoding
	 * @return
	 */
	public static String readToString(InputStream is, String encoding) {
		String result = "";
		if (is != null) {
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			String temp = null;
			try {
				br = new BufferedReader(new InputStreamReader(is, encoding));
				while ((temp = br.readLine()) != null) {
					sb.append(temp).append(System.lineSeparator());
				}
			} catch (Exception e) {

				try {
					if (br != null)
						br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				result = sb.toString();
			}
		}
		return result;
	}

	/**
	 * 파일내용에 인코딩 정보가 존재하면 그 인코딩을 리턴합니다. </br>
	 * 만약에 인코딩정보가 없다면 UTF-8을 리턴합니다 </br>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 1.
	 * @param f
	 *            인코딩을 찾을 파일.
	 * @return 인코딩
	 * @throws IOException
	 */
	public static String findEncoding(File f) {
		return new FileEncodingFinder(f).getEncoding();
	}

	/**
	 * 파일 타입에 따라 적절하게 내용을 <br/>
	 * 사람이 읽을 수 있는 텍스트로 변환하는 API <br/>
	 * 
	 * gargoyle.cont.conversion.properties파일에 등록된 내용을 참조 <br/>
	 * Conversion 클래스는 AbstractStringFileContentConversion.java와 상속관계여야만 함 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @param f
	 * @param encoding
	 * @return
	 */
	public static String readConversion(File f, Charset encoding) {
		return FileContentConversionUtil.conversion(f, encoding);
	}

	/**
	 * refer readConversion(File f, Charset encoding) <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 24.
	 * @param f
	 * @return
	 */
	public static String readConversion(File f) {
		return FileContentConversionUtil.conversion(f);
	}

	/**
	 * 이클립스의 경우 프로젝트 디렉토리라는 걸 인식하기 위한 파일 확장자. 이 확장자에는 참조되는 라이브러리에 대한 메타데이터를 정의함.
	 *
	 * @최초생성일 2016. 5. 5.
	 */
	private static final String PROJECT = ".project";
	/**
	 * 자바파일 확장자
	 *
	 * @최초생성일 2016. 5. 5.
	 */
	private static final String JAVA = ".java";
	/**
	 * pdf파일 확장자
	 *
	 * @최초생성일 2016. 5. 5.
	 */
	private static final String PDF = ".pdf";
	/**
	 * FXML파일 확장자
	 *
	 * @최초생성일 2016. 5. 5.
	 */
	private static final String FXML = ".fxml";

	/**
	 * XML 파일 확장자
	 *
	 * @최초생성일 2016. 8. 19.
	 */
	private static final String XML = ".xml";

	private static final String ZIP = ".zip";

	private static final String JAR = ".jar";

	private static final String RAX = ".rax";

	/**
	 * 이미지 파일 확장자들을 정의
	 *
	 * @최초생성일 2016. 5. 5.
	 */
	public static String[] IMAGES_FILES = new String[] { ".jpg", ".png", ".bmp", ".jpeg", ".gif" };

	public static boolean isJavaFile(String fileName) {
		return fileName.endsWith(JAVA);
	}

	/**
	 * 자바파일인지 확인
	 *
	 * @Date 2015. 10. 18.
	 * @param file
	 * @return
	 * @User KYJ
	 */
	public static boolean isJavaFile(File file) {
		if (file.exists()) {
			return isJavaFile(file.getName());
		}
		return false;
	}

	/**
	 * FXML파일여부
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 16.
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static boolean isFXML(File file) {
		if (file != null && file.exists()) {
			return isFXML(file.getName());
		}
		return false;
	}

	/**
	 * FXML파일여부
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 16.
	 * @param fileName
	 * @return
	 */
	public static boolean isFXML(String fileName) {
		return ValueUtil.isEmpty(fileName) ? false : fileName.endsWith(FXML);
	}

	/********************************
	 * 작성일 : 2016. 8. 19. 작성자 : KYJ
	 *
	 * XML 파일 여부
	 *
	 * @param file
	 * @return
	 ********************************/
	public static boolean isXML(File file) {
		if (file != null && file.exists()) {
			return isXML(file.getName());
		}
		return false;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 6. 15.
	 * @param file
	 * @return
	 */
	public static boolean isZip(File file) {
		if (file != null && file.exists()) {
			return isZip(file.getName());
		}
		return false;
	}

	public static boolean isRax(File file) {
		if (file != null && file.exists()) {
			return isRax(file.getName());
		}
		return false;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 6. 15.
	 * @param file
	 * @return
	 */
	public static boolean isJar(File file) {
		if (file != null && file.exists()) {
			return isJar(file.getName());
		}
		return false;
	}

	/********************************
	 * 작성일 : 2016. 8. 19. 작성자 : KYJ
	 *
	 * XML 파일 여부
	 *
	 * @param fileName
	 * @return
	 ********************************/
	public static boolean isXML(String fileName) {
		return ValueUtil.isEmpty(fileName) ? false : fileName.endsWith(XML);
	}

	private static boolean isZip(String fileName) {
		return ValueUtil.isEmpty(fileName) ? false : fileName.endsWith(ZIP);
	}

	private static boolean isJar(String fileName) {
		return ValueUtil.isEmpty(fileName) ? false : fileName.endsWith(JAR);
	}

	private static boolean isRax(String fileName) {
		return ValueUtil.isEmpty(fileName) ? false : fileName.endsWith(RAX);
	}

	/**
	 * str 내용을 file로 write처리함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param file
	 * @param str
	 * @param charset
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean writeFile(File file, byte[] bytes, Charset charset) throws FileNotFoundException, IOException {

		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), charset)) {

			for (byte b : bytes) {
				writer.write(b);
			}

			writer.flush();
			return true;
		}
	}

	/**
	 * is 내용을 filr로 write처리함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param file
	 * @param is
	 * @throws IOException
	 */
	public static void writeFile(File file, InputStream is, Charset charset) throws IOException {

		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), charset)) {
			try (InputStreamReader reader = new InputStreamReader(is, charset)) {
				int read = -1;
				while ((read = reader.read()) != -1) {
					writer.write(read);
				}
				writer.flush();
			}
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 10. 2.
	 * @param outFile
	 * @param is
	 * @throws IOException
	 */
	public static void writeFile(File outFile, InputStream is) throws IOException {

		if (outFile == null || is == null || is.available() == -1)
			return;

		if (outFile.getParentFile() != null && !outFile.getParentFile().exists()) {
			outFile.getParentFile().mkdirs();
		}

		if (!outFile.exists()) {
			outFile.createNewFile();
		}

		byte[] byteArray = IOUtils.toByteArray(is);

		try (FileOutputStream out = new FileOutputStream(outFile, false)) {
			IOUtils.write(byteArray, out);

			// int read = -1;
			// byte[] b = new byte[4096];
			// while ((read = is.read(b, 0, 4096)) != -1) {
			// out.write(b, 0, read);
			// }
		}

	}

	/**
	 * str 내용을 file로 write처리함, 디폴트 인코딩 UTF-8 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 26.
	 * @param file
	 * @param str
	 * @throws IOException
	 */
	public static boolean writeFile(File file, String str) throws IOException {
		return writeFile(file, str, StandardCharsets.UTF_8);
	}

	/**
	 * str 내용을 file로 write처리함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param file
	 * @param str
	 * @param charset
	 * @throws IOException
	 */
	public static boolean writeFile(File file, String str, Charset charset) throws IOException {
		return writeFile(file, str, charset, false);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 10. 11.
	 * @param file
	 * @param str
	 * @param charset
	 * @param append
	 * @return
	 * @throws IOException
	 */
	public static boolean writeFile(File file, String str, Charset charset, boolean append) throws IOException {

		if (!file.exists()) {
			file.createNewFile();
		}

		if (!file.canWrite())
			return false;

		boolean flag = false;
		try (FileOutputStream out = new FileOutputStream(file, append)) {
			try (OutputStreamWriter writer = new OutputStreamWriter(out, charset)) {
				writer.write(str);
				writer.flush();
				flag =  true;
			}
		}
		return flag;
	}

	/**
	 * str 내용을 file로 write처리함.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 27.
	 * @param file
	 * @param str
	 * @param charset
	 * @param errorHandler
	 */
	public static boolean writeFile(File file, String str, Charset charset, Consumer<Exception> errorHandler) {
		try {
			return writeFile(file, str, charset);
		} catch (Exception e) {
			if (errorHandler != null)
				errorHandler.accept(e);
			else
				LOGGER.error(ValueUtil.toString(e));
		}
		return false;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 30.
	 * @param file
	 * @param str
	 * @param charset
	 * @param errorHandler
	 */
	public static boolean writeFile(File file, String str, Charset charset, ExceptionHandler errorHandler) {
		try {
			return writeFile(file, str, charset);
		} catch (Exception e) {
			if (errorHandler != null)
				errorHandler.handle(e);
			else
				LOGGER.error(ValueUtil.toString(e));
		}
		return false;
	}

	/**
	 * str 내용을 file로 write처리함. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 30.
	 * @param file
	 * @param str
	 * @param errorHandler
	 */
	public static boolean writeFile(File file, String str, Consumer<Exception> errorHandler) {
		return writeFile(file, str, StandardCharsets.UTF_8, errorHandler);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 30.
	 * @param file
	 * @param str
	 * @param errorHandler
	 */
	public static boolean writeFile(File file, String str, ExceptionHandler errorHandler) {
		return writeFile(file, str, StandardCharsets.UTF_8, errorHandler);
	}

	/**
	 * 시스템 Temp 파일 위치 리턴.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @return
	 */
	public static File getTempFileSystem() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * 임시파일을 만든다. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 14.
	 * @param name
	 * @param cont
	 * @return file 만약 임시파일 생성에 실패할시 null. <br/>
	 * @throws IOException
	 */
	public static File createTempFile(String name, String cont) throws IOException {
		File temp = new File(getTempFileSystem(), name);
		boolean writeFile = writeFile(temp, cont);
		if (!writeFile)
			return null;
		return temp;
	}

	/**
	 * Gagoyle에서 사용중인 Temp 파일 디렉토리 리턴.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 */
	public static File getTempGagoyle() {
		File file = new File(getTempFileSystem(), "Gagoyle");
		if (!file.exists())
			file.mkdirs();
		return file;
	}

	/********************************
	 * 작성일 : 2016. 5. 5. 작성자 : KYJ
	 *
	 * 디렉토리 삭제.
	 *
	 * 일반 delete 함수로는 디렉토리안에 파일들이 존재하는 삭제하는 허용되지않음. 그래서 하위파일들을 먼저 삭제하고 디렉토리삭제를
	 * 처리해야함. 이 함수는 그런 디렉토리 삭제를 지원해주기 위한 함수.
	 *
	 * @param path
	 ********************************/
	public static boolean deleteDir(File path) {
		if (!path.exists()) {
			return false;
		}

		File[] files = path.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				deleteDir(file);
			} else {
				file.delete();
			}
		}

		return path.delete();
	}

	public static void asynchReadLine(String text, BiPredicate<Integer, String> stop) {
		DemonThreadFactory<String> newInstance = DemonThreadFactory.newInstance();
		Thread newThread = newInstance.newThread(new CloseableCallable<String>() {

			@Override
			public String call() throws Exception {

				try (BufferedReader br = new BufferedReader(new StringReader(text))) {

					String temp = null;
					int line = 0;
					while ((temp = br.readLine()) != null) {
						line++;
						if (!stop.test(line, temp)) {
							return null;
						}
					}
				}
				return null;
			}
		}, str -> {
		} /* Nothing. */, err -> {
			throw new RuntimeException(err);
		});

		newThread.start();
	}

	public static <T> void asynchRead(Path path, Function<byte[], T> handler) throws IOException {

		AsynchronousFileChannel open = AsynchronousFileChannel.open(path, StandardOpenOption.READ);

		// ByteBuffer 크기를 8k로 축소
		ByteBuffer byteBuffer = ByteBuffer.allocate(8 * 1024);
		long position = 0L;
		Long attachment = 0L;
		long fileSize = open.size();

		open.read(byteBuffer, position, attachment, new CompletionHandler<Integer, Long>() {

			@Override
			public void completed(Integer result, Long attachment) {

				if (result == -1)
					close();

				// 읽기 쓰기 병행시 flip을 호출해줘야함.
				byteBuffer.flip();
				byteBuffer.mark();
				handler.apply(byteBuffer.array());
				byteBuffer.reset();

				// 읽어들인 바이트수가
				// 파일사이즈와 같거나(버퍼 크기와 파일 크기가 같은 경우)
				// 버퍼 사이즈보다 작다면 파일의 끝까지 읽은 것이므로 종료 처리
				if (result == fileSize || result < byteBuffer.capacity()) {

					//// asyncFileChannel 닫기
					close();

					return;
				}
				// 읽을 내용이 남아있으므로 반복 회수를 증가 시키고 다시 읽는다.
				attachment++;
				open.read(byteBuffer, result * attachment, attachment, this);

			}

			@Override
			public void failed(Throwable exc, Long attachment) {
				exc.printStackTrace();
				close();

			}

			public void close() {
				try {
					if (open != null)
						open.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		});

	}

	/**
	 * 파일을 비동기로 읽어들인다. Predicate값이 false를 리턴하면 읽기를 중단한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 10.
	 * @param file
	 * @param stop
	 */
	public static void asynchReadLine(File file, BiPredicate<Integer, String> stop) {
		DemonThreadFactory<String> newInstance = DemonThreadFactory.newInstance();
		Thread newThread = newInstance.newThread(new CloseableCallable<String>() {

			@Override
			public String call() throws Exception {

				try (BufferedReader br = new BufferedReader(new FileReader(file))) {

					String temp = null;
					int line = 0;
					while ((temp = br.readLine()) != null) {
						line++;
						if (!stop.test(line, temp)) {
							return null;
						}
					}
				}
				return null;
			}
		}, str -> {
		} /* Nothing. */, err -> {
			throw new RuntimeException(err);
		});

		newThread.start();
	}

	/**
	 * 비동기 텍스트 리더 </br>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 11. 3.
	 * @param location
	 * @param conCompleted
	 * @throws IOException
	 */
	public static <T> void asynchRead(File location, Consumer<String> conCompleted) {
		asynchRead(location, StandardCharsets.UTF_8, conCompleted);
	}

	/**
	 * 비동기 텍스트 리더 </br>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 24.
	 * @param location
	 * @param charset
	 * @param conCompleted
	 */
	public static <T> void asynchRead(File location, Charset charset, Consumer<String> conCompleted) {

		DemonThreadFactory<String> newInstance = DemonThreadFactory.newInstance();
		Thread newThread = newInstance.newThread(new CloseableCallable<String>() {

			@Override
			public String call() throws Exception {
				return FileUtil.readFile(location, LoadFileOptionHandler.getDefaultHandler());
			}
		}, conCompleted, err -> {
			throw new RuntimeException(err);
		});

		newThread.start();
	}

	/**
	 * 대소문자를 무시한 <br/>
	 * 파일 확장자 일치 여부 <br/>
	 * 
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 5.
	 * @param fileName
	 * @param expect
	 * @return
	 */
	public static boolean equalsIgnoreCaseFileExtension(File fileName, String expect) {
		String extension = getFileExtension(fileName);
		return extension.equalsIgnoreCase(expect);
	}

	/**
	 * explorer file.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 13.
	 * @param file
	 * @throws Exception
	 */
	public static void explorer(File file) throws Exception {

		if (SystemUtils.IS_OS_WINDOWS) {

			// LOGGER.debug("explorer /select,{}", file.getAbsolutePath());

			// 여러 과정을 테스트해본결과 아래와 같은 형태로 명령어를 보내야 , 공백이 포함된 경로도 explorer가 가능.
			List<String> asList = Arrays.asList("explorer", "/select", ",", file.getAbsolutePath());
			LOGGER.debug("{}", asList);
			RuntimeClassUtil.simpleExec(asList);
		}

		else
			LOGGER.error("Not yet impled this os.");
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 13.
	 * @param absoltePath
	 * @throws Exception
	 */
	public static void explorer(String absoltePath) throws Exception {

		File file = new File(absoltePath);
		if (!file.exists())
			throw new FileNotFoundException("can't found path : " + absoltePath);

		explorer(file);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 27.
	 * @param uri
	 * @throws IOException
	 */
	public static void browse(URI uri) throws IOException {
		Desktop.getDesktop().browse(uri);

	}

	/**
	 * 이미지 파일 유형인지 판단 <br/>
	 * 확장자만 살피고 체크한다. <br/>
	 *
	 * @param file
	 * @return
	 */
	public static boolean isImageFile(File file) {
		if (file.exists()) {
			return Stream.of(IMAGES_FILES).filter(str -> file.getName().endsWith(str)).findFirst().isPresent();
		}
		return false;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 16.
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(File f) throws IOException {
		return toByteArray(f);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 14.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(File file) throws IOException {
		return FileUtils.readFileToByteArray(file);
	}

	/**
	 * find all files <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 21.
	 * @param root
	 * @return
	 */
	public static List<File> recursive(File root) {
		return new FileSearcher(root).find();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 25.
	 * @param root
	 * @param filter
	 * @return
	 */
	public static List<File> recursive(File root, String[] extensions) {
		return new FileSearcher(root, -1, extensions).find();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 9.
	 * @param root
	 * @param extensions
	 * @return
	 */
	public static List<File> recursive(File root, List<String> extensions) {
		return new FileSearcher(root, -1, extensions, a -> true).find();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 15.
	 * @param start
	 * @param maxDepth
	 * @param matcher
	 * @param options
	 * @return
	 * @throws IOException
	 */
	public static Stream<Path> find(File start, int maxDepth, BiPredicate<Path, BasicFileAttributes> matcher, FileVisitOption... options)
			throws IOException {
		return Files.find(start.toPath(), maxDepth, matcher, options);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @param file
	 * @throws IOException
	 */
	public static final void mkDirs(File file) throws IOException {

		if (file.exists())
			return;

		File parentFile = null;
		if (file.isFile()) {
			parentFile = file.getParentFile();
		}

		if (parentFile == null || !parentFile.exists()) {
			Path path = Paths.get(file.getAbsolutePath());
			Files.createDirectories(path.getParent());
		}
	}

	/**
	 * byte코드를 확인하여 인코딩을 리턴. 만약 찾지 못할경우 <br/>
	 * UTF-8 리턴 <br/>
	 * <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 16.
	 * @param file
	 * @return
	 */
	public static String getFindEncoding(File file) {
		FileEncodingFinder encodingFinder = new FileEncodingFinder(file);
		return encodingFinder.getEncoding();
	}

	/**
	 * Temp파일을 생성한다. 상위 디렉토리가 존재하지않으면 디렉토리도 만든다. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 7.
	 * @param file
	 * @throws IOException
	 */
	public static void createDummyFile(File file) throws IOException {
		if (file == null)
			return;
		if (file.isFile()) {
			File parentFile = file.getParentFile();
			if (parentFile.mkdirs())
				file.createNewFile();
		} else {
			file.mkdirs();
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 24.
	 * @param is
	 *            source
	 * @param file
	 *            dst. file
	 * @throws IOException
	 */
	public static void copy(InputStream is, File file) throws IOException {

		// try (FileWriter writer = new FileWriter(file, false)) {
		// byte[] b = new byte[1024];
		// while (is.read(b) != -1) {
		// writer.write(b, 0, b.length);
		// }
		// writer.flush();
		// }
		copy2(is, file);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 18.
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void copy(File src, File dst) throws IOException {
		try (FileInputStream is = new FileInputStream(src)) {
			copy2(is, dst);
		}
	}

	public static void copy2(InputStream is, File file) throws IOException {

		try (FileOutputStream out = new FileOutputStream(file, false)) {
			int temp = -1;
			byte[] b = new byte[4096];
			while ((temp = is.read(b)) != -1) {
				out.write(b, 0, temp);
			}
			out.flush();
		}

	}
}
