/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component
 *	작성일   : 2016. 07. 04.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

import java.util.Collections;
import java.util.List;

import com.kyj.fx.commons.utils.FxUtil;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

/**
 * 자바프로젝트 트리Item 처리 기반 TreeItem.. 이름을 수정할 필요성이 있음.
 *
 * @author KYJ
 *
 */
public class FileTreeItem<T extends FileWrapper> extends TreeItem<T> {

	public FileTreeItem(T fileWrapper) {
		super(fileWrapper);
		updateGraphics(fileWrapper);
	}

	/********************************
	 * 작성일 : 2016. 7. 10. 작성자 : KYJ
	 *
	 * UI에 보여질 Dispay 처리.
	 *
	 * @param fileWrapper
	 ********************************/
	private void updateGraphics(T fileWrapper) {
		Node value = createGraphcis(fileWrapper);
		value.getStyleClass().add(graphicsCssId());
		setGraphic(value);
	}

	/********************************
	 * 작성일 : 2016. 7. 11. 작성자 : KYJ
	 *
	 * 이 함수에서 리턴되는 노드가 트리를 구성하는 주 Node가 된다.
	 *
	 * @param fileWrapper
	 * @return
	 ********************************/
	protected Node createGraphcis(T fileWrapper) {
		String meta = converter.toString(fileWrapper); // getMetadata();

		ImageView createImageView = getImage(fileWrapper);
		HBox value = new HBox(createImageView, new Label(fileWrapper.getFile().getName()), new Label(meta));
		List<Node> createAttachLabels = createAttachNodes();
		if (createAttachLabels != null && !createAttachLabels.isEmpty())
			value.getChildren().addAll(createAttachLabels);
		return value;
	}

	/**
	 * @최초생성일 2019. 1. 14.
	 */
	private StringConverter<T> converter = new StringConverter<T>() {

		@Override
		public String toString(T fileWrapper) {
			return getMetadata(fileWrapper);
		}

		@Override
		public T fromString(String string) {
			/* NA */
			return null;
		}
	};

	/**
	 * @param converter
	 *            the converter to set
	 */
	public void setConverter(StringConverter<T> converter) {
		this.converter = converter;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 14.
	 * @return
	 */
	public StringConverter<T> getConverter() {
		return converter;
	}

	/********************************
	 * 작성일 : 2016. 7. 27. 작성자 : KYJ
	 *
	 * 추가적으로 덧붙일 노드정보가 있으면 오버라이드해서 사용할 수 있도록한다.
	 *
	 * @return
	 ********************************/
	protected List<Node> createAttachNodes() {
		return Collections.emptyList();
	}

	/********************************
	 * 작성일 : 2016. 7. 27. 작성자 : KYJ
	 *
	 * 파일 이미지 리턴.
	 *
	 * @param fileWrapper
	 * @return
	 ********************************/
	protected ImageView getImage(FileWrapper fileWrapper) {
		ImageView createImageView = FxUtil.createImageIconView(fileWrapper.getFile());
		return createImageView;
	}

	/********************************
	 * 작성일 : 2016. 7. 27. 작성자 : KYJ
	 *
	 * 노드의 css명을 리턴
	 * 
	 * @return
	 ********************************/
	protected String graphicsCssId() {
		return "fiile-tree-item";
	}

	/********************************
	 * 작성일 : 2016. 7. 27. 작성자 : KYJ
	 *
	 * UI에 Diplay되는 텍스트를 리턴. 이클립스로 예를들어 svn이 연결되면 뒤에붙는 메타정보를 표현.
	 *
	 * @return
	 ********************************/
	@Deprecated
	protected String getMetadata() {
		return getMetadata(super.getValue());
	}
	
	
	/**
	 * UI에 Diplay되는 텍스트를 리턴. 이클립스로 예를들어 svn이 연결되면 뒤에붙는 메타정보를 표현.
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 14. 
	 * @param fileWrapper
	 * @return
	 */
	protected String getMetadata(T fileWrapper) {
		if(fileWrapper!=null)
			return fileWrapper.toString();
		return "<< null >>";
	}
	
}