/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component
 *	작성일   : 2016. 5. 24.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import org.springframework.util.ReflectionUtils;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TreeItem;

/***************************
 *
 * @author KYJ
 *
 ***************************/
public class FilterableTreeItem<T> extends TreeItem<T> {
	private final ObservableList<TreeItem<T>> sourceChildren = FXCollections.observableArrayList();

	private final ObjectProperty<TreeItemPredicate<T>> predicate = new SimpleObjectProperty<>();

	public FilterableTreeItem(T value) {
		super(value);

		FilteredList<TreeItem<T>> filteredChildren = new FilteredList<>(sourceChildren);
		filteredChildren.predicateProperty().bind(Bindings.createObjectBinding(() -> {
			Predicate<TreeItem<T>> p = child -> {
				if (child instanceof FilterableTreeItem) {
					((FilterableTreeItem<T>) child).predicateProperty().set(predicate.get());
				}
				if (predicate.get() == null || !child.getChildren().isEmpty()) {
					return true;
				}
				return predicate.get().test(this, child.getValue());
			};
			return p;
		}, predicate));

		setHiddenFieldChildren(filteredChildren);

	}

	/**
	 * Set the hidden private field {@link TreeItem#children} through reflection
	 * and hook the hidden {@link ListChangeListener} in
	 * {@link TreeItem#childrenListener} to the list
	 *
	 */
	protected void setHiddenFieldChildren(ObservableList<TreeItem<T>> list) {
		Field children = ReflectionUtils.findField(getClass(), "children");
		children.setAccessible(true);
		ReflectionUtils.setField(children, this, list);

		Field childrenListener1 = ReflectionUtils.findField(getClass(), "childrenListener");
		childrenListener1.setAccessible(true);
		Object childrenListener = ReflectionUtils.getField(childrenListener1, this);

		list.addListener((ListChangeListener<? super TreeItem<T>>) childrenListener);
	}

	public ObservableList<TreeItem<T>> getSourceChildren() {
		return sourceChildren;
	}

	public ObjectProperty<TreeItemPredicate<T>> predicateProperty() {
		return predicate;
	}

}
