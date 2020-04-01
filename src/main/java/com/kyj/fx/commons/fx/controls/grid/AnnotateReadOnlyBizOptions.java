/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/

package com.kyj.fx.commons.fx.controls.grid;

import com.kyj.fx.commons.utils.DateUtil;

import javafx.util.StringConverter;

/**
 * 어노테이션기반 텍스트헤더를 처리함.
 * 
 * DEL_YN, USE_YN은 콤보박스 처리함.
 * 
 * @COLUMN("텍스트")
 * 
 * @author KYJ
 *
 */
public class AnnotateReadOnlyBizOptions<T> extends AnnotationOptions<T> {

	public AnnotateReadOnlyBizOptions(Class<T> clazz) {
		super(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.samsung.sds.sos.client.component.grid.AnnotationOptions#editable(
	 * java.lang.String)
	 */
	@Override
	public boolean editable(String columnName) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.samsung.sds.sos.client.component.grid.AnnotationOptions#
	 * useCommonCheckBox ()
	 */
	@Override
	public boolean useCommonCheckBox() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.samsung.sds.sos.client.component.grid.IOptions#useButtons()
	 */
	@Override
	public int useButtons() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.samsung.sds.sos.client.component.grid.AnnotationOptions#columnSize
	 * (java.lang.String)
	 */
	@Override
	public int columnSize(String columnName) {
		if ("fstRegDt".equals(columnName) || "fnlUpdDt".equals(columnName)) {
			return 150;
		}
		return super.columnSize(columnName);
	}

	@Override
	public CommboInfo<?> comboBox(String columnName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.samsung.sds.sos.client.component.grid.IOptions#stringConverter(java
	 * .lang.String)
	 */
	@Override
	public StringConverter<Object> stringConverter(String columnName) {

		if ("fstRegDt".equals(columnName) || "fnlUpdDt".equals(columnName)) {
			return new StringConverter<Object>() {

				@Override
				public String toString(Object object) {
					if (object == null)
						return "";

					String dateString = object.toString();
					return DateUtil.getDateAsStr(dateString, DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSS,
							DateUtil.SYSTEM_DATEFORMAT_YYYY_MM_DD_HH_MM_SS);
				}

				@Override
				public Object fromString(String string) {

					if (string == null || string.isEmpty())
						return "";
					return DateUtil.getDateAsStr(string, DateUtil.SYSTEM_DATEFORMAT_YYYY_MM_DD_HH_MM_SS,
							DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSS);
				}
			};
		}
		return null;
	}
}
