/********************************
 *	프로젝트 : sos-client
 *	패키지   : com.samsung.sds.sos.client.component.grid
 *	작성일   : 2016. 12. 8.
 *	프로젝트 : OPERA
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ
 *
 */
public abstract class AbstractVoNullChecker<T extends AbstractDVO> implements VoNullCheckable<T> {

	private List<T> list;

	private Field field;

	private String fieldName;

	private String msgNameByfield;

	private Object value;

	private CrudBaseGridView<T> view;

	private T emptyDataset;

	private int emptyIndex = -1;

	public AbstractVoNullChecker() {

	}

	public AbstractVoNullChecker(List<T> list) {
		this.list = list;
	}

	public AbstractVoNullChecker(CrudBaseGridView<T> view) {
		this();
		this.view = view;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public boolean isViolation(Policy p, Field field, Object value) {

		boolean flag = false;
		switch (p) {
		case NotNull:
			flag = (p == null);
			break;
		case NotEmpty:
			flag = ValueUtil.isEmpty(value);
			break;
		default:
			break;
		}
		return flag;

	}

	@Override
	public Optional<Field> findFirst() {

		if (list == null)
			return Optional.empty();

		for (int i=0; i<list.size(); i++ ) {
			T t  = list.get(i);

			for (Field f : getFields(t)) {
				Object value = null;

				if (!f.isAccessible())
					f.setAccessible(true);

				NotNull annotation = f.getAnnotation(getCheckAnnotationClass());
				if (annotation == null)
					continue;

				Policy policy = annotation.policy();
				try {

					if (!f.isAccessible()) {
						f.setAccessible(true);
					}

					value = getValue(t, f);

					if (isViolation(policy, f, value)) {

						this.value = value;
						this.field = f;
						this.msgNameByfield = getMessage(f);
						this.fieldName = f.getName();
						this.emptyDataset = t;
						this.emptyIndex = i;

						return Optional.of(f);
					}

				} catch (Exception e) {
					if (!isContinue(e))
						break;
				}
			}
		}

		return Optional.empty();
	}

	public final Field getField() {
		return field;
	}

	public final String getFieldName() {
		return fieldName;
	}

	public final Object getValue() {
		return value;
	}

	public String getMsgNameByfield() {
		return msgNameByfield;
	}

	public T getEmptyDataset(){
		return emptyDataset;
	}

	public int getEmptyIndex(){
		return emptyIndex;
	}
	protected String getMessage(Field f) {
		AnnotationOptions<T> options = view.getOptions();
		return options.getColumnHeader(f.getName());

	}
}
