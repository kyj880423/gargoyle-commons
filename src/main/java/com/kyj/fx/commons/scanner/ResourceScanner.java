/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.main.scanning
 *	작성일   : 2016. 2. 4.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.scanner;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * 리소스 스캐너
 *
 * @author KYJ
 *
 */
/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ResourceScanner {

	private static final String PACKAGE_PREFIX = "com.kyj";
	private static ResourceScanner scanner;
	private Reflections reflections;

	/**
	 * 객체로딩
	 *
	 * @return
	 */
	@Deprecated
	public static ResourceScanner getInstance() {
		if (scanner == null) {
			scanner = new ResourceScanner();
		}
		return scanner;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 11.
	 * @param clazz
	 * @param loaders
	 * @return
	 */
	public static ResourceScanner newInstance(Class<?> clazz, ClassLoader... loaders) {
		return new ResourceScanner(clazz, loaders);
	}

	/**
	 * 가고일 프로젝트만 리소스 로딩처리한다.
	 *
	 * 객체생성을 방지하기위해 private로 선언함.
	 *
	 * getInstance()함수를 통해 접근할것
	 */
	protected ResourceScanner() {
		this(PACKAGE_PREFIX);
	}

	/**
	 * @param packageName
	 */
	public ResourceScanner(String packageName) {
		Set<URL> forPackage = ClasspathHelper.forPackage(packageName, ClassLoader.getSystemClassLoader());
		reflections = new Reflections(new ConfigurationBuilder().setUrls(forPackage));
	}

	/**
	 * @param clazz
	 */
	public ResourceScanner(Class<?> clazz) {
		reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClass(clazz, clazz.getClassLoader())));
	}

	/**
	 * @param clazz
	 * @param loaders
	 */
	public ResourceScanner(Class<?> clazz, ClassLoader... loaders) {
		reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClass(clazz, loaders)));
	}

	/**
	 * GagoyleInitializable어노테이션이 붙은항목 클래스를 찾으며 찾은 항목은 Initialable Consumer를 통해
	 * 작업처리를한다.
	 *
	 * @param consumer
	 */
	public void initialize(Consumer<Class<?>> consumer) {
		initialize(GagoyleInitializable.class, consumer);
	}

	private void initialize(Class<? extends Annotation> annotation, Consumer<Class<?>> consumer) {
		Set<Class<?>> types = reflections.getTypesAnnotatedWith(annotation, true);
		types.parallelStream().forEach(consumer);
	}

	/**
	 * GagoyleInitializable어노테이션이 붙은항목 클래스를 찾으며 찾은 항목은 Consumer를 통해 작업처리를한다.
	 *
	 * @param consume
	 */
	public void initialize(Annotation annotation, Consumer<Class<?>> consume) {
		Set<Class<?>> types = reflections.getTypesAnnotatedWith(annotation);
		types.parallelStream().forEach(consume);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 11. 30.
	 * @param subTypesOf
	 * @param consume
	 */
	public <T> void iterateWith(Class<T> subTypesOf, Consumer<Class<?>> consume) {
		Set<Class<? extends T>> subTypesOf2 = reflections.getSubTypesOf(subTypesOf);
		subTypesOf2.parallelStream().forEach(consume);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 6.
	 * @param <T>
	 * @param subTypesOf
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> createInstances(Class<T> subTypesOf, Function<Class<T>, T> newInstance) {
		Set<Class<? extends T>> subTypesOf2 = reflections.getSubTypesOf(subTypesOf);
		return subTypesOf2.stream().map(z -> newInstance.apply((Class<T>) z)).collect(Collectors.toList());
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 6.
	 * @param <T>
	 * @param subTypesOf
	 * @return
	 */
	public <T> List<T> createInstances(Class<T> subTypesOf) {
		return createInstances(subTypesOf, c -> {

			try {
				return c.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}

			return null;
		});
	}
}
