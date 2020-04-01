/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * 기본적인 Crud를 처리해주는 공통그리드 콤포넌트
 *
 * @author KYJ
 *
 */
public class CrudBaseGridView<T extends AbstractDVO> extends BorderPane {
	private static Logger LOGGER = LoggerFactory.getLogger(CrudBaseGridView.class);
	/**
	 * 버튼정보가 담긴 컴퍼넌트로서 추가적인 버튼은 Hbox를 받아서 처리하도록한다. KYJ
	 */
	private HBox buttonBox;

	private AnnotationOptions<T> options;

	private CommonsBaseGridView<T> gridview;
	private Button btnAdd = new Button("추가");
	private Button btnUpdate = new Button("수정");
	private Button btnDelete = new Button("삭제");
	private Button btnSave = new Button("저장");

	public CrudBaseGridView(Class<T> clazz, AnnotationOptions<T> options) {
		this.options = options;
		gridview = new BaseGridView<T>(clazz, options);

		buttonBox = new HBox();
		buttonBox.setSpacing(5);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.setPadding(new Insets(5, 5, 5, 0));
		deleteItems = FXCollections.observableArrayList();

		ObservableList<Button> activeButtons = activeButtons(options.useButtons());
		buttonBox.getChildren().addAll(activeButtons);

		// 추가버튼 클릭 이벤트 핸들러
		btnAdd.setOnMouseClicked(setBtnClickHandler(clazz));
		// 삭제버튼 클릭 이벤트 핸들러
		btnDelete.setOnMouseClicked(deleteBtnClickHandler());

		btnUpdate.setOnMouseClicked(updateBtnClickHandler());

		// 상태값 업데이트 이벤트 핸들러
		gridview.addEventHandler(GridBaseTableCellValueChangeEvent.ACTION, updateStatusHandler());

		// 저장버튼 클릭시 이벤트 핸들러
		btnSave.setOnMouseClicked(saveBtnClickHandler(saveClickCallbackProperty));
		this.setCenter(gridview);
		this.setTop(buttonBox);

	}

	/**
	 * 아이템 요소가 삭제된 경우 임시적으로 아래 변수에 담기게된다. KYJ
	 */
	private List<T> deleteItems;
	private ObjectProperty<Consumer<List<T>>> saveClickCallbackProperty = new SimpleObjectProperty<Consumer<List<T>>>();

	private Consumer<String> errorMsgCallback = str -> {
		LOGGER.error(str);
	};

	/**
	 * 실제 사용되는 그리드 뷰 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 18.
	 * @return
	 */
	public CommonsBaseGridView<T> getRealGrid() {

		return this.gridview;
	}

	public boolean isEditable(int row) {
		T selectedItem = this.gridview.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return false;
		return selectedItem.get_status() != null;
	}

	public Consumer<List<T>> getSaveClickCallback() {
		return saveClickCallbackProperty.get();
	}

	public void setSaveClickCallback(Consumer<List<T>> saveClickCallback) {
		this.saveClickCallbackProperty.set(saveClickCallback);
	}

	public CrudBaseGridView(Class<T> clazz) {
		this(clazz, new AnnotationOptions<>(clazz));
	}

	public CrudBaseGridView(Class<T> clazz, ObservableList<T> list) {
		this(clazz, list, new AnnotationOptions<>(clazz));
	}

	public CrudBaseGridView(Class<T> clazz, ObservableList<T> list, AnnotationOptions<T> options) {
		this(clazz, options);
		addItems(list);
	}

	/**
	 * 버튼 활성화 정보를 설정한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 14.
	 * @param buttons
	 * @return
	 */
	private ObservableList<Button> activeButtons(int buttons) {
		ObservableList<Button> list = FXCollections.observableArrayList();

		if (Buttons.isAdd(buttons)) {
			list.add(btnAdd);
		}

		if (Buttons.isUpdate(buttons)) {
			list.add(btnUpdate);
		}

		if (Buttons.isDelete(buttons)) {
			list.add(btnDelete);
		}

		if (Buttons.isSave(buttons)) {
			list.add(btnSave);
		}

		return list;
	}

	private EventHandler<? super MouseEvent> updateBtnClickHandler() {

		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				ObservableList<T> items = gridview.getItems();
				for (int i = 0; i < items.size(); i++) {
					T t = items.get(i);
					if (t.getClicked()) {
						t.set_status(CommonConst._STATUS_UPDATE);
					}
				}

			}
		};
	}

	/**
	 * 저장버튼 클릭 핸들러
	 *
	 * @param saveClickCallbackProperty2
	 *
	 * @Date 2015. 10. 10.
	 * @return
	 * @User KYJ
	 */
	private EventHandler<MouseEvent> saveBtnClickHandler(ObjectProperty<Consumer<List<T>>> saveClickCallbackProperty) {
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				try {
					Consumer<List<T>> callback = saveClickCallbackProperty.get();
					if (callback == null) {
						errorMsgCallback.accept("saveClickCallback 함수에 에러메세지 콜백을 등록하세요.");
						return;
					}

					gridview.fireEvent(event);

					List<T> items = getItems();

					//필수값 검증로직 추가. 2016.12.08
					AbstractVoNullChecker<T> nullCheckHandler = new DefaultVoNullChecker<>(CrudBaseGridView.this);
					nullCheckHandler.setList(items);
					Optional<Field> findFirst = nullCheckHandler.findFirst();
					boolean present = findFirst.isPresent();
					if (present) {
						String msgFieldName = nullCheckHandler.getMsgNameByfield();
						//						String message = ValueUtil.getMessage("MSG_W_000001", msgFieldName);

						int emptyIndex = nullCheckHandler.getEmptyIndex();



						Set<Node> findAllByNodes = CrudBaseGridView.this.lookupAll("TableRow");
						findAllByNodes.stream().map(n -> (TableRow) n).filter(r -> {
							return emptyIndex == r.getIndex();
						}).findFirst().ifPresent(n -> {

							Timeline timeline = new Timeline();
							timeline.setCycleCount(10);
							timeline.setAutoReverse(true);

							KeyFrame keyFrame = new KeyFrame(Duration.millis(500),
									new KeyValue(n.styleProperty(), "-fx-border-color : red ; -fx-border-width : 1px"));
							KeyFrame keyFrame2 = new KeyFrame(Duration.millis(500),
									new KeyValue(n.styleProperty(), ""));

							KeyValue keyValueX = new KeyValue(n.styleProperty(), "-fx-border-color : red ; -fx-border-width : 1px");
					        KeyValue keyValueY = new KeyValue(n.styleProperty(), "");

							KeyFrame keyFrame3 = new KeyFrame(Duration.seconds(2), "", keyValueX, keyValueY );
							timeline.getKeyFrames().add(keyFrame3);

							timeline.play();
//							n.setStyle("-fx-border-color : red ; -fx-border-width : 1px");

						});

						getSelectionModel().select(emptyIndex);

//						DialogUtil.showMessageDialog(SharedMemory.getPrimaryStage(), msgFieldName + " Field is empty.");
						return;
					}

					List<T> arrayList = new ArrayList<T>(items);
					arrayList.addAll(deleteItems);
					callback.accept(arrayList);

					// 사용자 정의 로직이 이상없으면 deleteItems 항목도 비운다.
					deleteItems.clear();
				} catch (Exception e) {
					throw e;
				}

			}
		};
	}

	/**
	 * 그리드의 상태값이 변화될때 처리하기 위한 핸들러
	 *
	 * @Date 2015. 10. 10.
	 * @return
	 * @User KYJ
	 */
	private EventHandler<? super ActionEvent> updateStatusHandler() {
		return event -> {
			if (event instanceof GridBaseTableCellValueChangeEvent) {
				GridBaseTableCellValueChangeEvent _event = (GridBaseTableCellValueChangeEvent) event;
				AbstractDVO item = (AbstractDVO) _event.getItem();

				if (Objects.equals(CommonConst._STATUS_CREATE, item.get_status())) {
					// 새롭게 추가된 항목(CREATE는) UPDATE로 바꾸지않는다.

					/* Nothing.. */
				} else {
					// _status가 NULL상태인 데이터는 수정시 UPDATE로 바꾼다.
					item.set_status(CommonConst._STATUS_UPDATE);
				}
				_event.consume();

			}

		};
	}

	/**
	 * 삭제버튼 클릭 핸들러
	 *
	 * @Date 2015. 10. 10.
	 * @return
	 * @User KYJ
	 */
	private EventHandler<? super MouseEvent> deleteBtnClickHandler() {
		return event -> {
			ObservableList<T> items = gridview.getItems();
			int SIZE = items.size() - 1;
			for (int i = SIZE; i >= 0; i--) {

				T t = items.get(i);
				if (t.commonsClickedProperty().get()) {

					if (Objects.equals(CommonConst._STATUS_CREATE, t.get_status())) {
						// UI에서 새롭게 추가된 항목은 처리항목없음
						/* Nothing.. */
					} else {
						// 데이터베이스 삭제처리를 위해 플래그 처리한다.
						t.set_status(CommonConst._STATUS_DELETE);
						deleteItems.add(t);
					}
					items.remove(i);
				}

			}
		};
	}

	/**
	 * 마우스 클릭 이벤트 등록
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 18.
	 * @param e
	 */
	public void setMouseClicked(EventHandler<? super MouseEvent> e) {
		gridview.setOnMouseClicked(e);
	}

	/**
	 * 추가버튼 클릭 핸들러
	 *
	 * @Date 2015. 10. 10.
	 * @param clazz
	 * @return
	 * @User KYJ
	 */
	private EventHandler<? super MouseEvent> setBtnClickHandler(Class<T> clazz) {
		return event -> {
			try {
				T newInstance = clazz.newInstance();
				newInstance.set_status(CommonConst._STATUS_CREATE);
				addItems(newInstance);

				// gridview.getColumnMapper().cellEditable(gridview.getItems().size()
				// - 1, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	public HBox getButtonBox() {
		return buttonBox;
	}

	public void addItems(T... items) {

		for (T item : items) {
			gridview.addItems(item);
		}
	}

	public void addItems(List<T> items) {
		gridview.getItems().addAll(items);
	}

	public ObservableList<T> getItems() {
		return gridview.getItems();
	}

	public ObservableList<TableColumn<T, ?>> getColumns() {
		return gridview.getColumns();
	}

	/**
	 * name에 일치하는 컬럼인덱스를 반환
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 12.
	 * @param name
	 * @return
	 */
	public int getColumnIndex(String name) {
		return gridview.getColumnIndex(name);
	}

	/**
	 * Selection Model 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2015. 11. 18.
	 * @return
	 */
	public TableViewSelectionModel<T> getSelectionModel() {
		return gridview.getSelectionModel();
	}

	/**
	 * Crud기반 그리드 공통 테이블뷰
	 *
	 * @author KYJ
	 *
	 * @param <T>
	 */
	class BaseGridView<T extends AbstractDVO> extends CommonsBaseGridView<T> {
		public BaseGridView(Class<T> clazz) {
			super(clazz);
		}

		public BaseGridView(Class<T> clazz, IOptions iColumnNaming) {
			super(clazz, iColumnNaming);
		}

		public BaseGridView(Class<T> clazz, List<T> items, IOptions iColumnNaming) {
			super(clazz, items, iColumnNaming);
		}

		public BaseGridView(Class<T> clazz, List<T> items, List<String> columns, IOptions columnNaming) {
			super(clazz, items, columns, columnNaming);
		}

		public BaseGridView(Class<T> clazz, List<T> items) {
			super(clazz, items);
		}

		@Override
		public IColumnMapper<T> createColumnMapper() {
			return new CrudBaseColumnMapper<>();
		}
	}

	public AnnotationOptions<T> getOptions() {
		return options;
	}
}
