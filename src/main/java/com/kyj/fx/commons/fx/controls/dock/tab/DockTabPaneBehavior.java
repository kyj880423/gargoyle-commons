/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.dock.tab
 *	작성일   : 2016. 10. 25.
 *	작성자   : KYJ
 *******************************/
/*
 * Copyright (c) 2011, 2014, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.kyj.fx.commons.fx.controls.dock.tab;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

import javafx.event.Event;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class DockTabPaneBehavior extends BehaviorBase<DockTabPane> {

	/**************************************************************************
	 *                          Setup KeyBindings                             *
	 *************************************************************************/
	private static final String HOME = "Home";
	private static final String END = "End";
	private static final String CTRL_PAGE_UP = "Ctrl_Page_Up";
	private static final String CTRL_PAGE_DOWN = "Ctrl_Page_Down";
	private static final String CTRL_TAB = "Ctrl_Tab";
	private static final String CTRL_SHIFT_TAB = "Ctrl_Shift_Tab";

	protected static final List<KeyBinding> TAB_PANE_BINDINGS = new ArrayList<>();

	static {
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.HOME, HOME));
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.END, END));
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, CTRL_PAGE_UP).ctrl());
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, CTRL_PAGE_DOWN).ctrl());
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_TAB).ctrl());
		TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_SHIFT_TAB).shift().ctrl());
	}

	@Override
	protected void callAction(String name) {
		boolean rtl = (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT);

		if (("TraverseLeft".equals(name) && !rtl) || ("TraverseRight".equals(name) && rtl) || "TraverseUp".equals(name)) {
			if (getControl().isFocused()) {
				selectPreviousTab();
			}
		} else if (("TraverseRight".equals(name) && !rtl) || ("TraverseLeft".equals(name) && rtl) || "TraverseDown".equals(name)) {
			if (getControl().isFocused()) {
				selectNextTab();
			}
		} else if (CTRL_TAB.equals(name) || CTRL_PAGE_DOWN.equals(name)) {
			selectNextTab();
		} else if (CTRL_SHIFT_TAB.equals(name) || CTRL_PAGE_UP.equals(name)) {
			selectPreviousTab();
		} else if (HOME.equals(name)) {
			if (getControl().isFocused()) {
				moveSelection(0, 1);
			}
		} else if (END.equals(name)) {
			if (getControl().isFocused()) {
				moveSelection(getControl().getTabs().size() - 1, -1);
			}
		} else {
			super.callAction(name);
		}
	}

	/***************************************************************************
	 *                                                                         *
	 * Mouse event handling                                                    *
	 *                                                                         *
	 **************************************************************************/

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		DockTabPane tp = getControl();
		tp.requestFocus();
	}

	/**************************************************************************
	 *                         State and Functions                            *
	 *************************************************************************/

	public DockTabPaneBehavior(DockTabPane tabPane) {
		super(tabPane, TAB_PANE_BINDINGS);
	}

	public void selectTab(DockTab tab) {
		getControl().getSelectionModel().select(tab);
	}

	public boolean canCloseTab(DockTab tab) {
		Event event = new Event(tab, tab, DockTab.DOCK_TAB_CLOSE_REQUEST_EVENT);
		Event.fireEvent(tab, event);
		return !event.isConsumed();
	}

	public void closeTab(DockTab tab) {
		DockTabPane tabPane = getControl();
		// only switch to another tab if the selected tab is the one we're closing
		int index = tabPane.getTabs().indexOf(tab);
		if (index != -1) {
			tabPane.getTabs().remove(index);
		}
		if (tab.getOnClosed() != null) {
			Event.fireEvent(tab, new Event(DockTab.DOCK_CLOSED_EVENT));
		}
	}

	// Find a tab after the currently selected that is not disabled. Loop around
	// if no tabs are found after currently selected tab.
	public void selectNextTab() {
		moveSelection(1);
	}

	// Find a tab before the currently selected that is not disabled.
	public void selectPreviousTab() {
		moveSelection(-1);
	}

	private void moveSelection(int delta) {
		moveSelection(getControl().getSelectionModel().getSelectedIndex(), delta);
	}

	private void moveSelection(int startIndex, int delta) {
		final DockTabPane tabPane = getControl();
		int tabIndex = findValidTab(startIndex, delta);
		if (tabIndex > -1) {
			final SelectionModel<DockTab> selectionModel = tabPane.getSelectionModel();
			selectionModel.select(tabIndex);
		}
		tabPane.requestFocus();
	}

	private int findValidTab(int startIndex, int delta) {
		final DockTabPane tabPane = getControl();
		final List<DockTab> tabs = tabPane.getTabs();
		final int max = tabs.size();

		int index = startIndex;
		do {
			index = nextIndex(index + delta, max);
			DockTab tab = tabs.get(index);
			if (tab != null && !tab.isDisable()) {
				return index;
			}
		} while (index != startIndex);

		return -1;
	}

	private int nextIndex(int value, int max) {
		final int min = 0;
		int r = value % max;
		if (r > min && max < min) {
			r = r + max - min;
		} else if (r < min && max > min) {
			r = r + max - min;
		}
		return r;
	}
}
