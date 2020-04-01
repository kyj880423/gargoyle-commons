/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.component.spreadsheets
 *	작성일   : 2016. 2. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.spreadsheet;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 * @author KYJ
 *
 */
public class DragDropWrapping implements Cloneable {

	private Node source;
	private BooleanProperty isFocused;
	private double initX;
	private double initY;
	private Point2D dragAnchor;
	private ContextMenu contextMenu;
	private GagoyleSpreadSheetView drawingPane;

	public DragDropWrapping(GagoyleSpreadSheetView drawingPane, Node source) {
		this.drawingPane = drawingPane;
		this.source = source;
		createContextMenu();

		isFocused = new SimpleBooleanProperty(false);
		source.setUserData("MovingItem");

		source.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				source.startDragAndDrop(TransferMode.COPY_OR_MOVE);
				event.consume();
			}
		});

		source.setOnMousePressed(event -> {
			Node intersectedNode = event.getPickResult().getIntersectedNode();

			if (intersectedNode instanceof ImageView) {
				isFocused.set(true);

				contextMenu.hide();
				initX = intersectedNode.getTranslateX();
				initY = intersectedNode.getTranslateY();
				dragAnchor = new Point2D(event.getSceneX(), event.getSceneY());
			}

		});
		source.setOnMouseDragged(event -> {

			if (isFocused.get()) {
				source.setOpacity(0.5);
				double dragX = event.getSceneX() - dragAnchor.getX();
				double dragY = event.getSceneY() - dragAnchor.getY();

				double newXPosition = initX + dragX;
				double newYPosition = initY + dragY;

				source.setTranslateX(newXPosition);
				source.setTranslateY(newYPosition);
				event.consume();
			}

		});

		source.setOnMouseReleased(event -> {
			if (isFocused.get()) {
				isFocused.set(false);
				source.setOpacity(1);

				event.consume();
			}
		});

		source.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				contextMenu.show(source, event.getScreenX(), event.getScreenY());
				event.consume();
			}
		});

	}

	private ContextMenu createContextMenu() {
		contextMenu = new ContextMenu();
		MenuItem menuSelectAll = new MenuItem("모두선택");
		MenuItem menuDelete = new MenuItem("선택 삭제");
		MenuItem menuTopProp = new MenuItem("위로");

		menuDelete.setOnAction(event -> {
			int indexOf = drawingPane.getChildren().indexOf(source);
			drawingPane.getChildren().remove(indexOf);
		});

		menuTopProp.setOnAction(event -> {
			int indexOf = drawingPane.getChildren().indexOf(source);
			try {
				drawingPane.getChildren().remove(indexOf);
				drawingPane.getChildren().add(getNode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		contextMenu.getItems().addAll(menuSelectAll, menuDelete, menuTopProp);
		return contextMenu;
	}

	public Node getNode() {
		return this.source;
	}

}
