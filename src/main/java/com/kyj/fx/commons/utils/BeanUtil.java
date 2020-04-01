package com.kyj.fx.commons.utils;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 객체 생성관리.
 * 
 * @author KYJ
 *
 */
public class BeanUtil {

	private static ClassPathXmlApplicationContext ac;

	private static ClassPathXmlApplicationContext getContext() {
		if (ac == null) {
			ac = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
		}
		return ac;
	}

	/**
	 * 2014. 12. 25. KYJ
	 * 
	 * @param t
	 *            반환받기 원하는 타입
	 * @return 타입과 일치하는 생성된 객체
	 * @throws Exception
	 * @처리내용 : class타입에 해당하는 객체를 생성후 반환
	 */
	public static <T> T getBean(Class<T> t) throws Exception {

		if (ValueUtil.isNotEmpty(t)) {
			getContext();
			String name = t.getPackage().getName() + "." + t.getName();
			if (ac.containsBean(name)) {
				return ac.getBean(t);
			} else {
				return ac.getAutowireCapableBeanFactory().createBean(t);
				// ac.registerSingleton(name, t);
			}

		}
		return ac.getBean(t);
	}

	/**
	 * 2014. 12. 25. KYJ
	 * 
	 * @param t
	 *            반환받기 원하는 타입
	 * @return 타입과 일치하는 생성된 객체
	 * @throws Exception
	 * @처리내용 : class타입에 해당하는 객체를 생성후 반환
	 */
	public static <T> T getBean(String beanName, Class<T> requireType) throws Exception {
		getContext();
		if (ac.containsBean(beanName)) {
			return ac.getBean(beanName, requireType);
		} else {
			return getBean(requireType);
		}

	}

}
