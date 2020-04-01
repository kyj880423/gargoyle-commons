/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component
 *	작성일   : 2016. 07. 11
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

import com.kyj.fx.commons.fx.controls.tree.FileTreeItem;
import com.kyj.fx.commons.fx.controls.tree.JavaProjectFileWrapper;

/**
 * 자바프로젝트안에 속하는 멤버파일인경우 사용.
 *
 * @author KYJ
 *
 */
public class JavaProjectMemberFileTreeItem extends FileTreeItem<JavaProjectFileWrapper> {

	public JavaProjectMemberFileTreeItem(JavaProjectFileWrapper fileWrapper) {
		super(fileWrapper);
	}

	@Override
	protected String getMetadata() {
		return "";
	}

	// @Override
	// protected List<Node> createAttachNodes() {
	// return Arrays.asList(new Label("Connected"));
	// }

}