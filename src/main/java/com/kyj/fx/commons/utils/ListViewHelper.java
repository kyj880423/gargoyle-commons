/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2017. 6. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.scene.control.skin.VirtualScrollBar;

import javafx.scene.control.ListView;

/**
 * @author KYJ
 *
 */
public class ListViewHelper {

	public static VirtualFlow getVirtualFlow(ListView lv) {
		return (VirtualFlow) lv.lookup(".virtual-flow");
	}

	public static VirtualScrollBar getVbar(ListView lv) {
		VirtualFlow lookup = (VirtualFlow) lv.lookup(".virtual-flow");
		if (lookup != null)
			return getVbar(lookup);
		return null;
	}

	public static VirtualScrollBar getVbar(VirtualFlow vf) {
		try {
			final Method method = VirtualFlow.class.getDeclaredMethod("getVbar");
			method.setAccessible(true);
			return (VirtualScrollBar) method.invoke(vf);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static VirtualScrollBar getHbar(VirtualFlow vf) {
		try {
			final Method method = VirtualFlow.class.getDeclaredMethod("getHbar");
			method.setAccessible(true);
			return (VirtualScrollBar) method.invoke(vf);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}

}
