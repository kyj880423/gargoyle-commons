/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.functions
 *	작성일   : 2016. 1. 3.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.memory.ResourceLoader;
import com.kyj.fx.commons.models.BigDataDVO;
import com.kyj.fx.commons.utils.ValueUtil;

/**
 * ResultSet 결과 데이터를 Map형태로 반환함.
 *
 *
 * 17.11.17 findFirst 라는 속성 추가. 첫번째 행만 처리하고 리턴. by kyj.
 * 
 * @author KYJ
 *
 */
public class ResultSetToMapConverter implements BiFunction<ResultSetMetaData, ResultSet, List<Map<String, Object>>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResultSetToMapConverter.class);

	/**
	 * 컬럼 크기가 큰 경우 데이터 맵핑을 생략할건지 유무
	 *
	 * @최초생성일 2016. 2. 11.
	 */
	public static final String SKIP_BIG_DATA_COLUMN = ResourceLoader.SKIP_BIG_DATA_COLUMN;

	public static final String START_ROW = "start.row";

	public static final String FIND_FIRST_YN = "findFirst.yn";

	/**
	 * Mapping처리할때 필요한 속성이 정의된다.
	 *
	 * @최초생성일 2016. 2. 12.
	 */
	protected Properties prop;

	protected boolean isBigDataColumnSkip;

	protected int startRow = -1;

	/**
	 * 첫번째 행만 리턴할지 유무
	 * 
	 * @최초생성일 2017. 11. 17.
	 */
	protected boolean isFindFirst;

	protected Consumer<Exception> exceptionHandler;

	public ResultSetToMapConverter(Properties prop) {
		if (prop != null)
			this.prop = prop;
		else
			this.prop = getDefaultProperties();
		initialize();
	}

	public ResultSetToMapConverter() {
		this.prop = getDefaultProperties();
		initialize();
	}

	public Consumer<Exception> getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(Consumer<Exception> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public Properties getDefaultProperties() {
		return new Properties();
	}

	private void initialize() {
		if (this.prop.containsKey(SKIP_BIG_DATA_COLUMN)) {
			Object value = this.prop.get(SKIP_BIG_DATA_COLUMN);
			if (value != null)
				isBigDataColumnSkip = "true".equals(this.prop.get(SKIP_BIG_DATA_COLUMN).toString());
		}

		if (this.prop.containsKey(START_ROW)) {
			Object startRow = this.prop.get(START_ROW);
			if (startRow != null) {

				try {
					if (startRow instanceof Integer) {
						this.startRow = (int) startRow;
					} else {
						this.startRow = Integer.parseInt(startRow.toString());
					}
				} catch (NumberFormatException e) {
					/* Nothing. */}

			}
		}

		if (this.prop.containsKey(FIND_FIRST_YN)) {
			Object findFirst = this.prop.get(FIND_FIRST_YN);
			if ("Y".equals(findFirst)) {
				this.isFindFirst = true;
			} else {
				this.isFindFirst = false;
			}
		}
	}

	private BiFunction<ResultSetMetaData, ResultSet, List<Map<String, Object>>> deligator = (t, u) -> {

		List<Map<String, Object>> arrayList = Collections.emptyList();
		try {

			if (startRow != -1)
				u.absolute(startRow);

			ResultSetMetaData metaData = u.getMetaData();
			int columnCount = metaData.getColumnCount();
			arrayList = new ArrayList<Map<String, Object>>();
			boolean firstRow = true;
			while (u.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int c = 1; c <= columnCount; c++) {

					int columnType = metaData.getColumnType(c);

					// 컬럼 크기가 큰 경우 데이터 맵핑을 생략할건지 유무

					String value = u.getString(c);

					boolean isEmptyValue = value == null || value.isEmpty();
					// String tmpColumnLabel = metaData.getColumnLabel(c);
					String columnLabel = metaData.getColumnLabel(c);

					// 2016-08-04 중복되는 컬럼이 씹혀 없어지지않도록 컬럼이름이 중복되면 인덱스를 붙임.
					int nextNameIdx = 1;
					while (map.containsKey(columnLabel) && /* 무한루핑 방지 */nextNameIdx < 1000) {
						columnLabel = String.format("%s_%d", metaData.getColumnLabel(c), nextNameIdx);
						nextNameIdx++;
					}

					if (isBigDataColumnSkip) {
						switch (columnType) {

						case Types.BLOB:
							// map.put(metaData.getColumnLabel(c), new
							// BigDataDVO("BLOB", value));
							map.put(columnLabel, isEmptyValue ? new BigDataDVO("{data.blob}", "") : new BigDataDVO("{DATA.BLOB}", value));
							break;
						case Types.CLOB:
							// map.put(metaData.getColumnLabel(c), new
							// BigDataDVO("CLOB", value));
							map.put(columnLabel,
									isEmptyValue ? new BigDataDVO("{data.clob}", value) : new BigDataDVO("{DATA.CLOB}", value));
							break;
						default:
							String columnTypeName = metaData.getColumnTypeName(c);
							int cType = metaData.getColumnType(c);

							// 17.11.2 if the length over 3000 character replace
							// by kyj.
							if (value != null && value.length() > 3000) {
								map.put(columnLabel,
										isEmptyValue ? new BigDataDVO("{data.text}", value) : new BigDataDVO("{DATA.TEXT}", value));
								break;
							}

							// mysql big data type.
							if ("text".equals(columnTypeName)) {
								// map.put(metaData.getColumnLabel(c), new
								// BigDataDVO("TEXT", value));
								map.put(columnLabel,
										isEmptyValue ? new BigDataDVO("{data.text}", value) : new BigDataDVO("{DATA.TEXT}", value));
								break;
							}
							// postgre big data type.
							else if ("bytea".equals(columnTypeName)) {
								// map.put(metaData.getColumnLabel(c), new
								// BigDataDVO("BYTEA", value));
								map.put(columnLabel,
										isEmptyValue ? new BigDataDVO("{data.bytea}", value) : new BigDataDVO("{DATA.BYTEA}", value));
								break;
							}
							// else if ("xml".equals(columnTypeName)) {
							// map.put(columnLabel,
							// isEmptyValue ? new BigDataDVO("{data.bytea}",
							// value) : new BigDataDVO("{DATA.BYTEA}", value));
							// break;
							// }

							else if (java.sql.Types.LONGNVARCHAR == cType) {
								map.put(columnLabel,
										isEmptyValue ? new BigDataDVO("{data.bytea}", value) : new BigDataDVO("{DATA.BYTEA}", value));
								break;
							}
							map.put(columnLabel, value);
							break;
						}
						if (firstRow) {
							LOGGER.debug(String.format("column : %s type %s (%d)", columnLabel, metaData.getColumnTypeName(c),
									metaData.getColumnType(c)));
						}
					} else {
						map.put(columnLabel, value);
					}

				}
				arrayList.add(map);
				firstRow = false;

				if (isFindFirst)
					break;
			}
		} catch (SQLException e) {
			LOGGER.error(ValueUtil.toString(e));
			if (exceptionHandler != null)
				exceptionHandler.accept(e);
		}
		return arrayList;

	};

	/**
	 * @return the deligator
	 */
	public BiFunction<ResultSetMetaData, ResultSet, List<Map<String, Object>>> getDeligator() {
		return deligator;
	}

	/**
	 * @param deligator
	 *            the deligator to set
	 */
	public void setDeligator(BiFunction<ResultSetMetaData, ResultSet, List<Map<String, Object>>> deligator) {
		this.deligator = deligator;
	}

	@Override
	public List<Map<String, Object>> apply(ResultSetMetaData t, ResultSet u) {
		
		if (deligator == null)
			throw new RuntimeException("deligator is null");
		
		return deligator.apply(t, u);
	}

}
