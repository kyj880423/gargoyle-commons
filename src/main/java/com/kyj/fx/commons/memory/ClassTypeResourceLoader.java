/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2015. 10. 15.
 *	프로젝트 : VisualFxVoEditor
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.voeditor.core.model.meta.FieldMeta;

import kyj.Fx.dao.wizard.memory.IFileBaseConfiguration;

/**
 * VO 에디터 Type에서 타입이름에 대해 변환할 표준 클래스타입으로 변환하는 정보를 관리
 *
 * ReadOnly속성
 *
 * @author KYJ
 *
 */
public class ClassTypeResourceLoader implements IFileBaseConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassTypeResourceLoader.class);

	private static Properties properties;
	private static ClassTypeResourceLoader loader;
	private static final String FILE_NAME = "META-INF/typeMapping.properties";

	public static final String[] KEYWORDS = new String[] { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
			"class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto",
			"if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected",
			"public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient",
			"try", "void", "volatile", "while" };

	public static ClassTypeResourceLoader getInstance() {
		if (loader == null) {
			loader = new ClassTypeResourceLoader();
			loader.initialize();
		}
		return loader;
	}

	private ClassTypeResourceLoader() {
		initialize();
	}

	private void initialize() {
		properties = new Properties();
		try {
			properties.load(ClassTypeResourceLoader.class.getClassLoader().getResourceAsStream(FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public FieldMeta get(String key, Consumer<Exception> errorCallback) {
		FieldMeta f = null;
		try {
			f = get(key);
		} catch (Exception e) {
			errorCallback.accept(e);
		}
		return f;
	}

	public FieldMeta get(String key) throws ClassNotFoundException {
		String property = properties.getProperty(key);
		FieldMeta fieldMeta = null;

		if (property == null) {
			// LOGGER.error("Key Value not found!");
			// return null;
			throw new IllegalArgumentException("Key Value not found!");
		}

		if (property.indexOf('!') < 0) {
			Class<?> instanceClass = Class.forName(property);
			fieldMeta = new FieldMeta(instanceClass);

			if (fieldMeta == null) {
				String msg = String.format("Could not create meta instance... type : %s instance %s", instanceClass);
				throw new IllegalStateException(msg);
			}
		} else {
			String[] split = property.split("!");
			String _typeClass = split[0];
			String _instanceClass = split[1];
			Class<?> typeClass = Class.forName(_typeClass);
			Class<?> instanceClass = Class.forName(_instanceClass);
			fieldMeta = new FieldMeta(typeClass, instanceClass);

			if (fieldMeta == null) {
				String msg = String.format("Could not create meta instance... type : %s instance %s", _typeClass, _instanceClass);
				throw new IllegalStateException(msg);
			}
		}

		return fieldMeta;
	}

	public List<String> getKeyList() {
		return properties.stringPropertyNames().stream().collect(Collectors.toList());
	}

	@Override
	public String getFileName() {
		return FILE_NAME;
	}

	@Override
	public Set<Entry<Object, Object>> getEntry() {
		return properties.entrySet();
	}
}
