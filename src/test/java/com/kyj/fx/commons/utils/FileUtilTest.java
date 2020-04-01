package com.kyj.fx.commons.utils;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class FileUtilTest {

	/**
	 * 
	 * explorer pom.xml <br/>
	 * 
	 * 프로젝트 디렉토리의 pom.xml 경로 탐색.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 13.
	 * @throws Exception
	 */
	@Test
	public void explorerTest() throws Exception {

		// 같은 경로
		// FileUtil.explorer(new File("pom.xml"));

		// 다른 드라이브
		// FileUtil.explorer(new File("e:\\codetemplates.xml"));

		// 공백이 포함된 프로그램 파일
		// FileUtil.explorer(new File("C:\\Program
		// Files\\CPUID\\CPU-Z\\cpuz.exe"));

		// 공백이 포함된 다른 드라이브
		FileUtil.explorer(new File("E:\\virtual box - vm\\ubuntu\\Ubuntu.vdi"));

	}

	/**
	 * recursive method test <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 21.
	 */
	@Test
	public void recursiveTest() {
		List<File> recursive = FileUtil.recursive(new File("C:\\Users\\KYJ\\Desktop\\Source Code"));
		recursive.forEach(System.out::println);
	}
}
