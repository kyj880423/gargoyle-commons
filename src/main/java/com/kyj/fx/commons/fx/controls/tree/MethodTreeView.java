/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.tree
 *	작성일   : 2018. 3. 31.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

import java.io.File;
import java.io.InputStream;

import com.github.javaparser.ast.body.MethodDeclaration;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * @author KYJ
 *
 */
public abstract class MethodTreeView<T extends MethodItem> extends TreeView<MethodItem> {

	private ObjectProperty<Object> value = new SimpleObjectProperty<>();

	public MethodTreeView() {
		value.addListener(javaFileChangeListener);
	}

	/**
	 * ChangeListener
	 * 
	 * @최초생성일 2018. 3. 31.
	 */
	private ChangeListener<Object> javaFileChangeListener = (oba, o, n) -> {
		if(n instanceof File)
			onUpdate((File)n);
		else if(n instanceof InputStream)
			onUpdate((InputStream)n);
		else if(n instanceof String)
			onUpdate((String)n);
		
	};

	/**
	 * 파일이 변경되었을떄 처리할 내용 기술. <br>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 31.
	 * @param f
	 */
	public abstract void onUpdate(File f);

	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 3. 
	 * @param in
	 */
	public abstract void onUpdate(InputStream in);
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 3. 
	 * @param in
	 */
	public abstract void onUpdate(String in);
	
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 31.
	 * @param n
	 * @return
	 */
	protected TreeItem<MethodItem> toMethodItem(MethodDeclaration n) {
		MethodItem methodItem = new MethodItem();
		methodItem.setReference(n);
		methodItem.setName(n.getName());

		TreeItem<MethodItem> treeItem = new TreeItem<>(methodItem);
		treeItem.setExpanded(true);
		return treeItem;
	}

	public final ObjectProperty<Object> valueProperty() {
		return this.value;
	}

	public final Object getValue() {
		return this.valueProperty().get();
	}

	public final void setValue(final Object value) {
		this.valueProperty().set(value);
	}

}
