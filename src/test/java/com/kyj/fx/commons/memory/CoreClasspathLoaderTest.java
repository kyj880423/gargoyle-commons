package com.kyj.fx.commons.memory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class CoreClasspathLoaderTest {

	@Test
	public void test() throws Exception {
		long start = System.currentTimeMillis();
		List<String> load = new CoreClasspathLoader().load(jar -> {
			System.out.println(jar.getName());
			return true;
		}, m -> m);
		long count = load.stream().count();

		System.out.println(count);
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 + " sec");
	}

	/**
	 * 너무 많은 라이브러리 로드로 인해 로딩이 너무 오래 걸림
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 13.
	 * @throws Exception
	 */
	@Test
	public void classPath() throws Exception {
		long start = System.currentTimeMillis();
		List<String> load = new ClasspathLibraryLoader().load(jar -> {
			System.out.println(jar.getName());
			return true;
		}, m -> m);
		// 너무 많은 라이브러리 로드로 인해 로딩이 너무 오래 걸림
		long count = load.stream().count();
		// gargoyle 기준 1386119개 6 sec
		System.out.println(count);
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 + " sec");
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 13. 
	 * @throws Exception
	 */
	@Test
	public void rt() throws Exception {
		long start = System.currentTimeMillis();
		List<String> load = new RtClasspathLoader().stream(m -> m).sorted().collect(Collectors.toList());
		long count = load.stream().count();
		System.out.println(count);
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 + " sec");
	}

}
