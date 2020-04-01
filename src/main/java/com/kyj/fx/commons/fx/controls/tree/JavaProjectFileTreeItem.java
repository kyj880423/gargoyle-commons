/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component
 *	작성일   : 2016. 07. 11
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

import java.io.InputStream;

import com.kyj.fx.commons.utils.FxUtil;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * 자바프로젝트 트리Item 처리 기반 Tree
 *
 * @author KYJ
 *
 */
public class JavaProjectFileTreeItem extends FileTreeItem<JavaProjectFileWrapper> {

	public JavaProjectFileTreeItem(JavaProjectFileWrapper fileWrapper) {
		super(fileWrapper);
	}

	@Override
	protected Node createGraphcis(JavaProjectFileWrapper fileWrapper) {
		InputStream imageStream = ClassLoader.getSystemClassLoader().getResourceAsStream("META-INF/images/eclipse/project.gif");
		ImageView createImageView = FxUtil.createImageView(imageStream);
		//		String meta = String.format("[%b][%b]", fileWrapper.isJavaProjectFile(), fileWrapper.isSVNConnected());
		return new HBox(createImageView, new Label(fileWrapper.getFile().getName()), new Label(getMetadata()));
	}

//	@Override
//	protected String getMetadata() {
//		File wcDbFile = getValue().getWcDbFile();
//		try {
//			SVNWcDbClient client = new SVNWcDbClient(wcDbFile);
//			return String.format(" [%s]", client.getUrl());
//		} catch (Exception e) {
//			//e.printStackTrace();
//		}
//		return "";
//		//		return String.format("[%b][%b]", getValue().isJavaProjectFile(), getValue().isSVNConnected());
//
//	}

}