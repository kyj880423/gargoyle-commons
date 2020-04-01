/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.memory
 *	작성일   : 2018. 6. 27.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 클래스패스에 참조되는 자바 코어 라이브러리를 로드하기 위한 클래스
 * 
 * @author KYJ
 *
 */
/**
 * @author KYJ
 *
 */
public class CoreClasspathLoader {

	/**
	 * @작성일 : 2018. 6. 27.
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> load(Function<String, T> cast) throws Exception {
		return load(ClassLoader.getSystemClassLoader(), cast);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 28.
	 * @param loader
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> load(Predicate<JarFile> filter, Function<String, T> cast) throws Exception {
		String[] locations = getLibraryPaths();
		return load(ClassLoader.getSystemClassLoader(), locations, filter, cast);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 28.
	 * @param loader
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> load(ClassLoader loader, Function<String, T> cast) throws Exception {
		String[] locations = getLibraryPaths();
		return load(loader, locations, t -> true, cast);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 28.
	 * @param loader
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> load(ClassLoader loader, Predicate<JarFile> filter, Function<String, T> cast) throws Exception {
		String[] locations = getLibraryPaths();
		return load(loader, locations, filter, cast);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @return
	 */
	protected String[] getLibraryPaths() {
		String bootClassPath = System.getProperty("sun.boot.class.path");
		String[] locations = bootClassPath.split(";");
		return locations;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 28.
	 * @param loader
	 * @param locations
	 * @param cast
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> load(ClassLoader loader, String[] locations, Predicate<JarFile> filter, Function<String, T> cast) throws Exception {
		return stream(loader, locations, filter, cast).collect(Collectors.toList());
	}

	/**
	 * @작성일 : 2018. 6. 27.
	 * @return
	 * @throws Exception
	 */
	public <T> Stream<T> stream(Function<String, T> cast) throws Exception {
		String[] locations = getLibraryPaths();
		return stream(ClassLoader.getSystemClassLoader(), locations, t -> true, cast);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 15.
	 * @param loader
	 * @param locations
	 * @param filter
	 * @param cast
	 * @return
	 * @throws Exception
	 */
	public <T> Stream<T> stream(ClassLoader loader, String[] locations, Predicate<JarFile> filter, Function<String, T> cast)
			throws Exception {
		if (locations == null)
			return Stream.empty();

		if (cast == null)
			throw new RuntimeException("cast can't be null.");
		return Stream.of(locations)
				/*
				 * .peek(location -> { System.out.println(location); })
				 */
				.parallel().flatMap(baseJar -> {
					List<String> collect = collect(new ArrayList<>(), loader, filter, new File(baseJar));
					return collect.stream();
				}).map(cast).filter(v -> v != null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 27.
	 * @param loader
	 * @param jarFile
	 * @return
	 * @throws Exception
	 */
	public List<String> collect(ClassLoader loader, Predicate<JarFile> filter, File jarFile) {
		List<String> items = new ArrayList<>();
		return collect(items, loader, filter, jarFile);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 27.
	 * @param loader
	 * @param jarFile
	 * @return
	 * @throws Exception
	 */
	public List<String> collect(List<String> items, ClassLoader loader, Predicate<JarFile> filter, File jarFile) {
		if (!jarFile.exists())
			return items;
		try {
			JarFile j = new JarFile(jarFile);
			if (filter.test(j)) {

				Enumeration<JarEntry> entries = j.entries();
				while (entries.hasMoreElements()) {
					JarEntry nextElement = entries.nextElement();
					String name = nextElement.getName();

					// System.out.println(name);
					boolean directory = nextElement.isDirectory();
					// 디렉토리폴더 제외
					if (!directory) {
						// 이너클래스인경우 제외
						if (name.indexOf('$') < 0) {
							// 클래스확장자 파일만...
							if (name.endsWith(".class")) {
								String findClass = findClass(loader, name);
								if (findClass != null)
									items.add(findClass);
//								items.addAll(findMethods(loader, name));
							}
						}
					}
				}
			}

		} catch (IOException e) {
			// Nothing.
		}

		return items;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 9.
	 * @param loader
	 * @param _name
	 * @return
	 */
	protected String findClass(ClassLoader loader, String _name) {
		String name = _name;
		if (!name.endsWith(".class")) {
			return null;
		}
		name = name.replace(".class", "");
		name = name.replace('/', '.');

		return name;
	}
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 27.
	 * @param loader
	 * @param _name
	 * @return
	 * @throws Exception
	 */
	private List<Method> findMethods(ClassLoader loader, String _name) {
		String name = _name;
		if (!name.endsWith(".class")) {
			return null;
		}
		name = name.replace(".class", "");
		name = name.replace('/', '.');

		try {
			Class<?> forName = Class.forName(name, false, loader);
			Method[] methods = forName.getMethods();
			return Stream.of(methods).collect(Collectors.toList());
		} catch (Throwable e) {
			// Nothing.
		}
		return new ArrayList<Method>(1);// Collections.emptyList();
	}

}
