/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.memory
 *	작성일   : 2018. 6. 27.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

import java.io.File;
import java.io.IOException;
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
 * 클래스패스에 참조되는 자바 코어 라이브러리를 로드하기 위한 클래스 <br/>
 * 
 * @author KYJ
 *
 */
public class RtClasspathLoader {

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 9.
	 * @param cast
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> load(Function<String, T> cast) throws Exception {
		return loadClasses(ClassLoader.getSystemClassLoader(), cast);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 9.
	 * @param loader
	 * @param cast
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> loadClasses(ClassLoader loader, Function<String, T> cast) throws Exception {
		String javaHome = System.getProperty("java.home");
		String[] locations = new String[] { javaHome + File.separator + "lib" + File.separator + "rt.jar" };
		return load(loader, locations, cast);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 9.
	 * @param loader
	 * @param locations
	 * @param cast
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> load(ClassLoader loader, String[] locations, Function<String, T> cast) throws Exception {
		return stream(loader, locations, cast).collect(Collectors.toList());
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 9.
	 * @param cast
	 * @return
	 * @throws Exception
	 */
	public <T> Stream<T> stream(Function<String, T> cast) throws Exception {
		return streamClasses(ClassLoader.getSystemClassLoader(), cast);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @param loader
	 * @param cast
	 * @return
	 * @throws Exception
	 */
	public <T> Stream<T> streamClasses(ClassLoader loader, Function<String, T> cast) throws Exception {
		String javaHome = System.getProperty("java.home");
		// String[] locations = bootClassPath.split(";");

		// String bootClassPath = System.getProperty("sun.boot.class.path");
		// String[] locations = bootClassPath.split(";");

		String[] locations = new String[] { javaHome + File.separator + "lib" + File.separator + "rt.jar" };
		return stream(loader, locations, cast);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @param loader
	 * @param locations
	 * @param cast
	 * @return
	 * @throws Exception
	 */
	public <T> Stream<T> stream(ClassLoader loader, String[] locations, Function<String, T> cast) throws Exception {
		if (locations == null)
			return Stream.empty();

		if (cast == null)
			throw new RuntimeException("cast can't be null.");

		return Stream.of(locations).parallel().flatMap(baseJar -> {
			List<String> collect = collect(new ArrayList<>(), loader, new File(baseJar));
			return collect.stream();
		}).filter(filter()).map(cast);
	}

	/**
	 * default method return, start with 'com.sun' or 'sun.' packages. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 14.
	 * @return
	 */
	protected Predicate<? super String> filter() {
		return pack -> {

			if (pack.startsWith("sun.")) {
				return false;
			}
			if (pack.startsWith("com.sun")) {
				return false;
			}
			return true;
		};
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 9.
	 * @param items
	 * @param loader
	 * @param jarFile
	 * @return
	 */
	public List<String> collect(List<String> items, ClassLoader loader, File jarFile) {
		if (!jarFile.exists())
			return items;
		try {
			JarFile j = new JarFile(jarFile);
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
	 * @param jarFile
	 * @return
	 */
	public List<String> collect(ClassLoader loader, File jarFile) {
		List<String> items = new ArrayList<>();
		return collect(items, loader, jarFile);
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

}
