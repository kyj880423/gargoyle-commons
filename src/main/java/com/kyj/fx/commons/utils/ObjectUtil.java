/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

import com.kyj.fx.commons.fx.controls.grid.AbstractDVO;
import com.kyj.fx.commons.models.KeyPair;

/**
 * @author KYJ
 *
 */
public class ObjectUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtil.class);

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 31.
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> toMap(Object obj) {
		return toMap(obj, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 19.
	 * @param collection
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> toMap(Supplier<Map<String, Object>> collection, Object obj) {
		return toMap(collection, obj, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 31.
	 * @param obj
	 * @param filter
	 * @return
	 * @throws IntrospectionException
	 */
	public static Map<String, Object> toMap(Object t, Predicate<String> fieldNameFilter) {
		return toMap(() -> new HashMap<>(), t, fieldNameFilter);
	}

	/**
	 * 2019.03.04 t가 null인경우 Suppier 객체어세 빈 Map 리턴 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 19.
	 * @param collection
	 * @param t
	 * @param fieldNameFilter
	 * @return
	 */
	public static Map<String, Object> toMap(Supplier<Map<String, Object>> collection, Object t, Predicate<String> fieldNameFilter) {

		if (t == null)
			return collection.get();

		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(t.getClass());
		} catch (IntrospectionException e1) {
			throw new RuntimeException(e1);
		}

		Map<String, Object> hashMap = collection.get(); // new HashMap<String,
														// Object>();
		// Iterate over all the attributes
		for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

			// write메소드와 read메소드가 존재할때만.
			Method writeMethod = descriptor.getWriteMethod();
			Method readMethod = descriptor.getReadMethod();
			if (ValueUtil.isEmpty(writeMethod) || ValueUtil.isEmpty(readMethod)) {
				continue;
			}
			// Class<?> returnType = readMethod.getReturnType();

			String methodName = ValueUtil.getSimpleMethodName(readMethod.getName());

			if (fieldNameFilter != null) {
				if (!fieldNameFilter.test(methodName))
					continue;
			}

			Object originalValue = null;
			try {
				originalValue = readMethod.invoke(t);
			} catch (Exception e) {
			}

			if (ValueUtil.isNotEmpty(originalValue)) {
				hashMap.put(methodName, originalValue);
			} else {
				hashMap.put(methodName, null);
			}
		}
		return hashMap;
	}

	/**
	 * find only key <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 8.
	 * @param collection
	 * @param t
	 * @param fieldNameFilter
	 * @return
	 */
	public static Map<String, Object> getKeys(Supplier<Map<String, Object>> collection, Class<?> t, Predicate<String> fieldNameFilter) {

		Map<String, Object> hashMap = collection.get();

		if (t == null)
			return hashMap;

		Field[] fields = t.getDeclaredFields();

		// Iterate over all the attributes
		for (Field field : fields) {

			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			String name = field.getName();
			if (fieldNameFilter != null) {
				if (!fieldNameFilter.test(name))
					continue;
			}
			hashMap.put(name, null);

		}
		return hashMap;

	}

	/**
	 * reference only getter method.
	 * 
	 * @author KYJ
	 *
	 */
	public static class GetterOnly {
		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 7. 10.
		 * @param t
		 * @return
		 */
		public static Map<String, Object> toMap(Object t) {
			return toMap(() -> new HashMap<>(), t, null);
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 7. 10.
		 * @param t
		 * @param fieldNameFilter
		 * @return
		 */
		public static Map<String, Object> toMap(Object t, Predicate<String> fieldNameFilter) {
			return toMap(() -> new HashMap<>(), t, fieldNameFilter);
		}

		/**
		 * @작성자 : KYJ
		 * @작성일 : 2018. 6. 19.
		 * @param collection
		 * @param t
		 * @param fieldNameFilter
		 * @return
		 */
		public static Map<String, Object> toMap(Supplier<Map<String, Object>> collection, Object t, Predicate<String> fieldNameFilter) {
			BeanInfo beanInfo = null;
			try {
				beanInfo = Introspector.getBeanInfo(t.getClass());
			} catch (IntrospectionException e1) {
				throw new RuntimeException(e1);
			}

			Map<String, Object> hashMap = collection.get(); // new
															// HashMap<String,
															// Object>();
			// Iterate over all the attributes
			for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

				// write메소드와 read메소드가 존재할때만.
				// Method writeMethod = descriptor.getWriteMethod();
				Method readMethod = descriptor.getReadMethod();
				if (ValueUtil.isEmpty(readMethod)) {
					continue;
				}
				// Class<?> returnType = readMethod.getReturnType();

				String methodName = ValueUtil.getSimpleMethodName(readMethod.getName());

				if (fieldNameFilter != null) {
					if (!fieldNameFilter.test(methodName))
						continue;
				}

				Object originalValue = null;
				try {
					originalValue = readMethod.invoke(t);
				} catch (Exception e) {
				}

				if (ValueUtil.isNotEmpty(originalValue)) {
					hashMap.put(methodName, originalValue);
				} else {
					hashMap.put(methodName, null);
				}
			}
			return hashMap;
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 7. 5.
	 * @param value
	 * @param name
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getDeclaredFieldValue(Object value, String name) {

		Object val = null;
		try {
			Field field = value.getClass().getDeclaredField(name);
			if (field == null)
				return null;

			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			val = field.get(value);
		} catch (Exception e) {
			LOGGER.error(ValueUtil.toString(e));
		}
		return val;

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 28.
	 * @param col
	 * @param duplication
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unused")
	private static <T> Collection<T> populate(Collection<T> col, boolean duplication, T[] data) {
		if (ValueUtil.isEmpty(data)) {
			return col;
		}
		for (T d : data) {
			if (!duplication) {
				if (col.contains(d)) {
					continue;
				}
			}
			col.add(d);
		}
		return col;
	}

	/**
	 * from객체 타입으로 to 객체를 변환시킨후 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 14.
	 * @param toClass
	 * @param fromList
	 * @return
	 * @throws Exception
	 */
	public static <T, K> List<K> populate(Class<K> toClass, List<T> fromList) throws Exception {
		List<K> arrayList = new ArrayList<K>();

		for (T from : fromList) {
			K to = toClass.newInstance(); 
			arrayList.add(populate(from, to));
		}
		return arrayList;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 28.
	 * @param toClass
	 * @param fromList
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public static <T, K> List<K> populate(Class<K> toClass, List<T> fromList, String... columns) throws Exception {
		List<K> arrayList = new ArrayList<K>();
		List<String> props = new ArrayList<String>();
		for (String prop : columns) {
			props.add(prop);
		}

		for (T from : fromList) {
			K to = toClass.newInstance();
			arrayList.add(populate(from, to, props));
		}
		return arrayList;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 12. 28.
	 * @param from
	 * @param to
	 * @return
	 */
	public static <T, K> K populate(final T from, K to) {
		return populate(from, to, Collections.emptyList());
	}

	/**
	 * from객체에서 to 객체로 값을 바인드한다.
	 *
	 * @주처리내용 setter와 getter로 처리되며 접근지정자는 public
	 * @예외 static이나 from객체에서 [final로 선언된 함수는 바인드 처리안함.] -> final은 처리함 field처랑
	 *     헷깔린듯.
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 6.
	 * @param from
	 * @param to
	 * @param from에서
	 *            특정변수만 바인드처리.
	 * @return 바인드된 결과 객체
	 */
	public static <T, K> K populate(final T from, K to, final List<String> props) {
		ReflectionUtils.doWithMethods(from.getClass(), new MethodCallback() {
			@Override
			public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {

				// 특정접근자인경우 바인드 처리안함.
				if (((method.getModifiers() & Modifier.PUBLIC) != Modifier.PUBLIC)
						|| (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC
				/* || method.getModifiers() == Modifier.FINAL */)
					return;

				try {
					Class<? extends Object> toClass = to.getClass();
					String simpleMethodName = ValueUtil.getSimpleMethodName(method.getName());
					if (props != null && !props.isEmpty()) {
						if (!props.contains(simpleMethodName)) {
							return;
						}
					}

					String simpleName = method.getName().substring(3);
					String setMethodName = "set" + simpleName;
					Object valueObject = method.invoke(from);
					// 값이 null인경우 처리안함.
					if (valueObject == null)
						return;

					Method targetSetterMethod = null;
					try {
						targetSetterMethod = toClass.getDeclaredMethod(setMethodName, valueObject.getClass());
					} catch (NoSuchMethodException e) {
						targetSetterMethod = getAbstractDVOMethod(toClass, setMethodName, valueObject.getClass());
					}
					if (targetSetterMethod == null)
						return;
					targetSetterMethod.setAccessible(true);
					targetSetterMethod.invoke(to, valueObject);

				} catch (InvocationTargetException | SecurityException e) {
					e.printStackTrace();
				}

			}
		}, new MethodFilter() {
			@Override
			public boolean matches(Method method) {
				return method.getName().startsWith("get") || method.getName().startsWith("is");
			}
		});
		return to;
	}

	/**
	 * abstract멤버 클래스 메소드를 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 15.
	 * @param toClass
	 * @param method
	 * @param valueType
	 * @return
	 */
	private static Method getAbstractDVOMethod(Class<?> toClass, String method, Class<?> valueType) {

		// AbstractDVO멤버클래스인지 확인후 아니면 NULL 리턴
		if (AbstractDVO.class.isAssignableFrom(toClass)) {
			try {
				Class<?> superclass = toClass.getSuperclass();
				Method declaredMethod = superclass.getDeclaredMethod(method, valueType);
				return declaredMethod;
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 19. 
	 * @param o
	 * @return
	 */
	public static LinkedList<KeyPair<String, Object>> toKeyPairs(Object o) {
		return toKeyPairs(toMap(o, a -> true));
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 19. 
	 * @param o
	 * @return
	 */
	public static LinkedList<KeyPair<String, Object>> toKeyPairs(Object o, Predicate<String> fieldNameFilter) {
		return toKeyPairs(toMap(o, fieldNameFilter));
	}
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 19. 
	 * @param map
	 * @return
	 */
	public static LinkedList<KeyPair<String, Object>> toKeyPairs(Map<String, Object> map) {
		LinkedList<KeyPair<String, Object>> collect = map.entrySet().stream().collect(LinkedList::new, (a, b) -> {
			a.add(new KeyPair<>(b.getKey(), b.getValue()));
		}, (a, b) -> {
			a.addAll(b);
		});
		return collect;
	}
}
