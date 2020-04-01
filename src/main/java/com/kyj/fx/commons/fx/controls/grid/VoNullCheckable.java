/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2016. 12. 8.
 *	프로젝트 : OPERA
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.value.ObservableValue;

/**
 * @author KYJ
 *
 */
public interface VoNullCheckable<T extends AbstractDVO> {

	public default Class<NotNull> getCheckAnnotationClass() {
		return NotNull.class;
	}

	public default List<Field> getFields(T obj) {

		Class<? extends AbstractDVO> class1 = obj.getClass();
		Field[] fields = class1.getDeclaredFields();
		List<Field> array = new ArrayList<>(fields.length);
		for (Field f : fields) {

			if (!f.isAccessible())
				f.setAccessible(true);

			NotNull annotation = f.getAnnotation(getCheckAnnotationClass());
			if (annotation == null)
				continue;

			array.add(f);
		}

		return array;
	}

	public default Object getValue(Object obj, Field f) {
		Object value = null;

		try {
			value = f.get(obj);
		} catch (Exception e) {
			//
		}

		if (value != null) {
			if (value instanceof ObservableValue) {
				value = ((ObservableValue<?>) value).getValue();
			}
		}
		return value;
	}

	public default boolean isContinue(Exception e) {
		return false;
	}

	public void setList(List<T> list);

	public Optional<Field> findFirst();

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 8.
	 * @param policy
	 * @param value
	 * @param field
	 * @return
	 */
	public boolean isViolation(Policy policy, Field field, Object value);

}
