/**
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	date      : 2015. 11. 11.
 *	user      : KYJ
 */
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 자동으로 콤보박스 처리를 지원하는 테이블 컬럼
 * 
 * @author KYJ
 *
 */
public class CommboBoxTableColumn<T, K> extends TableColumn<T, K> {

	private static Logger LOGGER = LoggerFactory.getLogger(CommboBoxTableColumn.class);
	private Supplier<ChoiceBoxTableCell<T, K>> cellSupplier;

	private String columnName;
	private ObservableList<K> items;
	private String kCommCode, kColumnName;

	/**
	 * 생성자
	 * 
	 * @param columnName
	 *            T 형태에 맵핑되는 VO컬럼명
	 * @param items
	 *            <K> 콤보박스로 사용할 데이터셋 리스트
	 * @param kCommCode
	 *            K 콤보박스로 사용할 데이터셋의 코드부(실제 값에 바인드되는 컬럼명을 기술)
	 * @param kColumnName
	 *            K 콤보박스로 사용할 데이터셋의 코드명부(디스플레이되는 컬럼명을 기술)
	 */

	public CommboBoxTableColumn(String columnName, ObservableList<K> items, String kCommCode, String kColumnName) {
		this.cellSupplier = () -> new ChoiceBoxTableCell<T, K>(items);
		this.columnName = columnName;
		this.items = items;
		this.kCommCode = kCommCode;
		this.kColumnName = kColumnName;
		initialize();
	}

	public CommboBoxTableColumn(Supplier<ChoiceBoxTableCell<T, K>> cellSupplier, String columnName, ObservableList<K> items,
			String kCommCode, String kColumnName) {
		this.cellSupplier = cellSupplier;
		this.columnName = columnName;
		this.items = items;
		this.kCommCode = kCommCode;
		this.kColumnName = kColumnName;
		initialize();
	}

	public void initialize() {

		Connector connector = new Connector();
		this.setCellFactory(cell -> {
			ChoiceBoxTableCell<T, K> choiceBoxTableCell;
			if (cellSupplier == null || cellSupplier.get() == null) {
				choiceBoxTableCell = new ChoiceBoxTableCell<T, K>(items);
			} else {
				choiceBoxTableCell = cellSupplier.get();
			}

			return choiceBoxTableCell;
		});

		this.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, K>, ObservableValue<K>>() {

			@Override
			public ObservableValue<K> call(javafx.scene.control.TableColumn.CellDataFeatures<T, K> param) {
				try {
					T value = param.getValue();
					ItemAdapter<T, K> adapter = connector.getAdapter(value);
					K getterCommboObj = adapter.getterValue(value, columnName);

					// [시작]콤보박스 데이터와 일치하는 데이터가 존재할때 프로퍼티를 새로 생성한후 반환한다.
					Optional<K> findFirst = items.stream().filter(vo -> {
						try {
							return getterCommboObj.equals(connector.getDataSetCommboBoxAdapter().getterValue(vo, kCommCode));
						} catch (Exception e) {
							return false;
						}
					}).findFirst();
					if (findFirst.isPresent()) {
						K k = findFirst.get();
						return newProperty(value, k);
					}
					// [끝]

					/*
					 * 코드와 일치하지 않는 데이터가 존재하는경우 처리하는 부분이다.
					 * 
					 * 이 코드가 있음으로인해 UI에는 코드와 일치하지않는 경우 데이터라도 출력되고, 값까지 바인드 될 수
					 * 있게함.
					 */
					return notFoundCodeProperty(value, columnName, getterCommboObj);
				} catch (Exception e) {
					LOGGER.error(exceptionString(e));
				}
				return new SimpleObjectProperty<>();
			}

			/**
			 * 코드부와 일치하지 않는 초기 데이터 처리
			 * 
			 * [콤보박스 데이터와 일치하지않는 데이터
			 * 
			 * @param t
			 * @param columnName
			 * @param getterCommboObj
			 * @return
			 */
			private ObservableValue<K> notFoundCodeProperty(T t, String columnName, K getterCommboObj) {
				SimpleObjectProperty<K> simpleObjectProperty = new SimpleObjectProperty<>(getterCommboObj);
				simpleObjectProperty.addListener((oba, oldval, newval) -> {
					try {
						ItemAdapter<T, K> adapter = connector.getAdapter(t);
						adapter.setValue(t, columnName, newval, kCommCode);
					} catch (Exception e) {
						LOGGER.error(exceptionString(e));
					}
				});
				return simpleObjectProperty;
			}

			/**
			 * 코드부와 일치하는 경우 사용
			 * 
			 * [콤보박스 데이터와 일치]
			 * 
			 * @param t
			 * @param bol
			 * @return
			 */
			private SimpleObjectProperty<K> newProperty(T t, K bol) {

				SimpleObjectProperty<K> simpleObjectProperty = new SimpleObjectProperty<>(bol);
				simpleObjectProperty.addListener((oba, oldval, newval) -> {
					try {
						ItemAdapter<T, K> adapter = connector.getAdapter(t);
						adapter.setValue(t, columnName, newval, kCommCode);
					} catch (Exception e) {
						LOGGER.error(exceptionString(e));
					}
				});

				return simpleObjectProperty;
			}
		});

	}

	/**
	 * 추상화된 아이템 어댑터
	 * 
	 * @author KYJ
	 *
	 * @param <T>
	 * @param <K>
	 */
	interface ItemAdapter<T, K> {
		<M, V> V getterValue(M param, String column) throws Exception;

		void setValue(T param, String tcolumnName, K commboObj, String kCommCode) throws Exception;
	}

	/**
	 * 아이템값에 따른 어댑터를 연결해주는 커넥터
	 * 
	 * @author KYJ
	 *
	 */
	class Connector {
		private ItemAdapter<T, K> adapter;

		public ItemAdapter<T, K> getAdapter(T item) {
			if (item instanceof Map) {
				if (adapter == null) {
					adapter = new MapItemAdapter();
				}
			} else {
				if (adapter == null) {
					adapter = new DataSetItemAdapter();
					dataSetItemAdapter = adapter;
				}
			}
			return adapter;
		}

		private ItemAdapter<T, K> dataSetItemAdapter;

		public ItemAdapter<T, K> getDataSetCommboBoxAdapter() {
			if (dataSetItemAdapter == null)
				dataSetItemAdapter = new DataSetItemAdapter();
			return dataSetItemAdapter;
		}

	}

	/**
	 * 데이터셋 처리 어댑터
	 * 
	 * @author KYJ
	 *
	 */
	class DataSetItemAdapter implements ItemAdapter<T, K> {
		/**
		 * M 객체에 존재하는 column명에 일치하는 값을 반환한다.
		 * 
		 * @param param
		 * @param column
		 * @return
		 * @throws Exception
		 */
		public <M, V> V getterValue(M param, String column) throws Exception {

			String getterMethodName = "get" + getIndexUppercase(column, 0);
			Method declaredField = param.getClass().getDeclaredMethod(getterMethodName);
			if (declaredField != null) {
				declaredField.setAccessible(true);
				@SuppressWarnings("unchecked")
				V bol = (V) declaredField.invoke(param);
				return bol;
			}
			LOGGER.warn(String.format("can't not found  check me... %s class,  %s field....", param.getClass().getName(), column));
			return null;
		}

		/**
		 * 콤보박스 객체인 K의 kCommCode 컬럼명에 일치하는 데이터를
		 * 
		 * T객체에 tcolumnName 필드에 값을 입력한다.
		 * 
		 * @param param
		 * @param tcolumnName
		 * @param commboObj
		 * @param kCommCode
		 * @throws Exception
		 */
		public void setValue(T param, String tcolumnName, K commboObj, String kCommCode) throws Exception {

			// 실제 값이 입력될 setter 메소드 객체를 받음.
			String tSetterMethodName = "set" + getIndexUppercase(tcolumnName, 0);

			// UI에서 선택된 콤보박스 실 데이터를 받음.
			Object getterValue = getterValue(commboObj, kCommCode);

			// setter객체에 실제 값을 입력
			Method declaredMethod = param.getClass().getDeclaredMethod(tSetterMethodName, getterValue.getClass());
			if (declaredMethod != null) {
				declaredMethod.setAccessible(true);
				declaredMethod.invoke(param, getterValue);
			} else {
				LOGGER.warn(String.format("can't not found check me... %s class,  %s field....", param.getClass().getName(), kCommCode));
			}
		}
	}

	/**
	 * 맵 데이터 아이템 어댑터
	 * 
	 * @author KYJ
	 *
	 */
	class MapItemAdapter extends DataSetItemAdapter {
		/**
		 * M 객체에 존재하는 column명에 일치하는 값을 반환한다.
		 * 
		 * @param param
		 * @param column
		 * @return
		 * @throws Exception
		 */
		public <M, V> V getterValue(M param, String column) throws Exception {

			String getterMethodName = "get";
			Method declaredField = param.getClass().getDeclaredMethod(getterMethodName, Object.class);
			if (declaredField != null) {
				declaredField.setAccessible(true);
				@SuppressWarnings("unchecked")
				V bol = (V) declaredField.invoke(param, column);
				return bol;
			}
			LOGGER.warn(String.format("can't not found  check me... %s class,  %s field....", param.getClass().getName(), column));
			return null;
		}

		/**
		 * 콤보박스 객체인 K의 kCommCode 컬럼명에 일치하는 데이터를
		 * 
		 * T객체에 tcolumnName 필드에 값을 입력한다.
		 * 
		 * @param param
		 * @param tcolumnName
		 * @param commboObj
		 * @param kCommCode
		 * @throws Exception
		 */
		public void setValue(T param, String tcolumnName, K commboObj, String kCommCode) throws Exception {

			// 실제 값이 입력될 setter 메소드 객체를 받음.
			String tSetterMethodName = "put";

			// UI에서 선택된 콤보박스 실 데이터를 받음.
			Object getterValue = super.getterValue(commboObj, kCommCode);

			// setter객체에 실제 값을 입력
			Method declaredMethod = param.getClass().getDeclaredMethod(tSetterMethodName, Object.class, Object.class);
			if (declaredMethod != null) {
				declaredMethod.setAccessible(true);
				declaredMethod.invoke(param, tcolumnName, getterValue);
			} else {
				LOGGER.warn(String.format("can't not found check me... %s class,  %s field....", param.getClass().getName(), kCommCode));
			}
		}
	}

	/**
	 * get메소드 이름 패턴의 명에서 get을 제거하고 앞글자는 소문자로 바꾼 글자를 반환
	 * 
	 * @param methodName
	 * @return
	 */
	public static String getSimpleMethodName(final String methodName) {
		String getMethodName = methodName;
		// validation
		char[] charArray = getMethodName.replaceFirst("get", "").toCharArray();
		String lowerCase = String.valueOf(charArray[0]).toLowerCase();
		charArray[0] = lowerCase.charAt(0);
		getMethodName = String.valueOf(charArray);
		return getMethodName;
	}

	public static String exceptionString(Throwable e) {
		StackTraceElement[] stackTrace = e.getStackTrace();
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage()).append("\n");
		for (StackTraceElement s : stackTrace) {
			sb.append(s.getClassName()).append(".").append(s.getMethodName()).append("[").append(s.getLineNumber()).append("]\n");
		}
		return sb.toString();
	}

	public enum IndexCaseTypes {
		UPPERCASE, LOWERCASE
	}

	/**
	 * str 문자열에서 index에 해당하는 부분만 대문자, 혹은 소문자로 고친다. (IndexCaseTypes에 따른 설정)
	 * 
	 * @param str
	 *            target
	 * @param index
	 *            character index
	 * @param type
	 *            lower or upper
	 * @return
	 */
	public static String getIndexcase(String str, int index, IndexCaseTypes type) {
		StringBuffer sb = new StringBuffer();

		// DEFAULT UPPERCASE
		char indexChar = Character.toUpperCase(str.charAt(index));
		if (type == IndexCaseTypes.LOWERCASE) {
			indexChar = Character.toLowerCase(str.charAt(index));
		}

		switch (index) {
		case 0:
			sb.append(indexChar).append(str.substring(index + 1));
			break;
		default:
			sb.append(str.substring(0, index)).append(indexChar).append(str.substring(index + 1));
			break;
		}
		return sb.toString();
	}

	/**
	 * str 문자열에서 index에 해당하는 부분만 대문자로 고친다.
	 * 
	 * @param str
	 * @param index
	 * @return
	 */
	public static String getIndexUppercase(String str, int index) {
		return getIndexcase(str, index, IndexCaseTypes.UPPERCASE);
	}

	/**
	 * str 문자열에서 index에 해당하는 부분만 소문자로 고친다.
	 * 
	 * @param str
	 * @param index
	 * @return
	 */
	public static String getIndexLowercase(String str, int index) {
		return getIndexcase(str, index, IndexCaseTypes.LOWERCASE);
	}
}
