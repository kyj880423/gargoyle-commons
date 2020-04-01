/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.relez
 *	작성일   : 2019. 11. 4.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.magt.scripts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;
import com.kyj.fx.fxloader.FxPostInitialize;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@FXMLController(value = "ReleaseScriptView.fxml", isSelfController = true)
public class ReleaseScriptComposite extends BorderPane {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseScriptComposite.class);
	@FXML
	private MenuBar mbRoot;
	@FXML
	private Button btnNew, btnDelete, btnSave;
	@FXML
	private TreeView<EventScriptDVO> tvControlTreebox;
	@FXML
	private TabPane tpScript;

	/**
	 */
	public ReleaseScriptComposite() {
		FxUtil.loadRoot(ReleaseScriptComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	ObjectProperty<AbstractEventManager> eventManager = new SimpleObjectProperty<>();
	ObjectProperty<AbstractScriptPaneManager> scriptGuiManager = new SimpleObjectProperty<>(new DefaultScriptPaneManager());

	ObjectProperty<ReleaseScriptEvent> onLoadEvent = new SimpleObjectProperty<>(new OnLoadEvent());

	ObjectProperty<OnSaveClickEvent> onSaveClickEvent = new SimpleObjectProperty<>(new OnSaveClickEvent());
	ObjectProperty<ReleaseScriptEvent> onDeleteClickEvent = new SimpleObjectProperty<>(new OnDeleteClickEvent());
	ObjectProperty<OnNewClickEvent> onNewClickEvent = new SimpleObjectProperty<>(new OnNewClickEvent());

	/**
	 * 사용자 정의가 필요없이 충분히 구현 가능. <br/>
	 * 스크립트 내용을 읽어 처리할 행위를 기술 <br/>
	 * 코드는 AbstractScriptPaneManager 를 구현하여 읽을 컨텐츠를 구현할 것 <br/>
	 * 그외 액션에 관한 내용은 OnReadEvent 클래스 참조 <br/>
	 * 
	 * @최초생성일 2019. 11. 18.
	 */
	private ObjectProperty<ReleaseScriptEvent> onReadEvent = new SimpleObjectProperty<>(new OnReadEvent());

	TreeItem<EventScriptDVO> treeRoot = new TreeItem<>(new EventScriptDVO("", "Root", "/"));

	@FXML
	public void initialize() {
		tvControlTreebox.setRoot(treeRoot);
		tvControlTreebox.setOnMouseClicked(ev -> {
			if (ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2) {
				readScriptOnAction();
			}
		});
		tvControlTreebox.setCellFactory(TextFieldTreeCell.forTreeView(new StringConverter<EventScriptDVO>() {

			@Override
			public String toString(EventScriptDVO object) {
				return object == null ? "" : object.getScriptPath();
			}

			@Override
			public EventScriptDVO fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		}));

		// tvControlTreebox.setCellFactory(new
		// Callback<TreeView<EventScriptDVO>, TreeCell<EventScriptDVO>>() {
		//
		// @Override
		// public TreeCell<EventScriptDVO> call(TreeView<EventScriptDVO> param)
		// {
		// TreeCell<EventScriptDVO> cell = new TreeCell<EventScriptDVO>() {
		//
		// /* (non-Javadoc)
		// * @see javafx.scene.control.Cell#updateItem(java.lang.Object,
		// boolean)
		// */
		// @Override
		// protected void updateItem(EventScriptDVO item, boolean empty) {
		// super.updateItem(item, empty);
		// if(empty)
		// setText("");
		// else
		// if(item ==null)
		// setText("");
		// else
		// setText(item.getScriptPath());
		// }
		//
		// };
		//
		// return cell;
		// }
		// });
	}

	@FxPostInitialize
	public void after() {

		reloadOnAction();

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 18.
	 */
	public void readScriptOnAction() {
		ReleaseScriptEvent releaseScriptEvent = onReadEvent.get();
		if (releaseScriptEvent != null)
			releaseScriptEvent.onAceept(EventMeta.create(this, this, eventManager.get()));
	}

	@FXML
	public void reloadOnAction() {
		ReleaseScriptEvent releaseScriptEvent = onLoadEvent.get();
		if (releaseScriptEvent != null)
			releaseScriptEvent.onAceept(EventMeta.create(this, this, eventManager.get()));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 15.
	 */
	@FXML
	public void btnNewOnAction() {
		ReleaseScriptEvent e = onNewClickEvent.get();
		if (e != null) {
			e.onAceept(EventMeta.create(this, btnNew, eventManager.get()));
		}

	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 15.
	 */
	@FXML
	public void btnDeleteOnAction() {
		ReleaseScriptEvent e = onDeleteClickEvent.get();
		if (e != null)
			e.onAceept(EventMeta.create(this, btnDelete, eventManager.get()));
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 15.
	 */
	@FXML
	public void btnSaveOnAction() {
		ReleaseScriptEvent e = onSaveClickEvent.get();
		if (e != null)
			e.onAceept(EventMeta.create(this, btnSave, eventManager.get()));
	}

	public final ObjectProperty<OnSaveClickEvent> onSaveClickEventProperty() {
		return this.onSaveClickEvent;
	}

	public final ReleaseScriptEvent getOnSaveClickEvent() {
		return this.onSaveClickEventProperty().get();
	}

	public final void setOnSaveClickEvent(final OnSaveClickEvent onSaveClickEvent) {
		this.onSaveClickEventProperty().set(onSaveClickEvent);
	}

	public final ObjectProperty<ReleaseScriptEvent> onDeleteClickEventProperty() {
		return this.onDeleteClickEvent;
	}

	public final ReleaseScriptEvent getOnDeleteClickEvent() {
		return this.onDeleteClickEventProperty().get();
	}

	public final void setOnDeleteClickEvent(final ReleaseScriptEvent onDeleteClickEvent) {
		this.onDeleteClickEventProperty().set(onDeleteClickEvent);
	}

	public final ObjectProperty<OnNewClickEvent> onNewClickEventProperty() {
		return this.onNewClickEvent;
	}

	public final OnNewClickEvent getOnNewClickEvent() {
		return this.onNewClickEventProperty().get();
	}

	public final void setOnNewClickEvent(final OnNewClickEvent onNewClickEvent) {
		this.onNewClickEventProperty().set(onNewClickEvent);
	}

	/**
	 * @return the tvControlTreebox
	 */
	public final TreeView<EventScriptDVO> getControlTreeView() {
		return tvControlTreebox;
	}

	/**
	 * @return the treeRoot
	 */
	public final TreeItem<EventScriptDVO> getTreeRoot() {
		return treeRoot;
	}

	public final ObjectProperty<ReleaseScriptEvent> onLoadEventProperty() {
		return this.onLoadEvent;
	}

	public final ReleaseScriptEvent getOnLoadEvent() {
		return this.onLoadEventProperty().get();
	}

	public final void setOnLoadEvent(final ReleaseScriptEvent onLoadEvent) {
		this.onLoadEventProperty().set(onLoadEvent);
	}

	public final ObjectProperty<AbstractEventManager> eventManagerProperty() {
		return this.eventManager;
	}

	public final AbstractEventManager getEventManager() {
		return this.eventManagerProperty().get();
	}

	public final void setEventManager(final AbstractEventManager eventManager) {
		this.eventManagerProperty().set(eventManager);
	}

	public final ObjectProperty<AbstractScriptPaneManager> scriptGuiManagerProperty() {
		return this.scriptGuiManager;
	}

	public final AbstractScriptPaneManager getScriptGuiManager() {
		return this.scriptGuiManagerProperty().get();
	}

	public final void setScriptGuiManager(final AbstractScriptPaneManager scriptGuiManager) {
		this.scriptGuiManagerProperty().set(scriptGuiManager);
	}

	/**
	 * @return the tpScript
	 */
	public TabPane getTpScript() {
		return tpScript;
	}

	/**
	 * @param tpScript
	 *            the tpScript to set
	 */
	public void setTpScript(TabPane tpScript) {
		this.tpScript = tpScript;
	}

	final ObjectProperty<ReleaseScriptEvent> onReadEventProperty() {
		return this.onReadEvent;
	}

	final ReleaseScriptEvent getOnReadEvent() {
		return this.onReadEventProperty().get();
	}

	final void setOnReadEvent(final ReleaseScriptEvent onReadEvent) {
		this.onReadEventProperty().set(onReadEvent);
	}

	/**
	 * @return the mbRoot
	 */
	public final MenuBar getMbRoot() {
		return mbRoot;
	}

}
