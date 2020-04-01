/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.tree
 *	작성일   : 2018. 3. 31.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableView;

/**
 * @author KYJ
 *
 */
public abstract class MethodTableView<T extends MethodItem> extends TableView<T> {

	private ObjectProperty<Object> value = new SimpleObjectProperty<>();
	private BooleanProperty autoSorting = new SimpleBooleanProperty(false);

	public MethodTableView() {
		value.addListener(sourceFileChangeListener);
	}

	/**
	 * ChangeListener
	 * 
	 * @최초생성일 2018. 3. 31.
	 */
	private ChangeListener<Object> sourceFileChangeListener = (oba, o, n) -> {
		if (n instanceof File)
			onUpdate((File) n);
		else if (n instanceof InputStream)
			onUpdate((InputStream) n);
		else if (n instanceof String)
			onUpdate((String) n);
		// List
		else if (n instanceof List) {

			List<?> items = (List<?>) n;
			items.forEach(v -> {
				if (v instanceof File)
					onUpdate((File) v);
				else if (v instanceof InputStream)
					onUpdate((InputStream) n);
				else if (v instanceof String)
					onUpdate((String) v);
			});

		}

		if (autoSorting.get()) {
			Collections.sort(getItems(), (a1, a2) -> {
				return ValueUtil.compare(a1.getName(), a2.getName());
			});
		}

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
	protected MethodItem toMethodItem(MethodDeclaration n) {
		MethodItem methodItem = new MethodItem();
		methodItem.setReference(n);
		methodItem.setName(n.getName());

		return methodItem;
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

	public final BooleanProperty autoSortingProperty() {
		return this.autoSorting;
	}

	public final boolean isAutoSorting() {
		return this.autoSortingProperty().get();
	}

	public final void setAutoSorting(final boolean autoSorting) {
		this.autoSortingProperty().set(autoSorting);
	}

}
