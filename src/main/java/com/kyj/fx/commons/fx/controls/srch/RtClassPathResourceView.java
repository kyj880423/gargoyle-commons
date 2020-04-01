/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.popup
 *	작성일   : 2018. 12. 14.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.srch;

import java.util.List;
import java.util.stream.Collectors;

import com.kyj.fx.commons.memory.ClasspathLibraryLoader;
import com.kyj.fx.commons.memory.RtClasspathLoader;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class RtClassPathResourceView extends ResourceView<String> {

	private static List<String> resources;

	@Override
	public boolean isMatch(String value, String text) {
		return value.contains(text);
	}

	@Override
	public StringConverter<String> stringConverter() {
		return new DefaultStringConverter();
	}

	@Override
	protected void init() {
		if (resources == null) {
			try {
				
				
//				RtClasspathLoader loader = new RtClasspathLoader();
//				resources = loader.stream(m -> m).sorted().collect(Collectors.toList());
				
				ClasspathLibraryLoader loader = new ClasspathLibraryLoader();
				resources = loader.stream(m -> m).sorted().collect(Collectors.toList());
				
			} catch (Exception e) {
				ValueUtil.toString(e);
			}
		}

		setResources(resources);
		lvResources.getItems().addAll(resources);
	}

	@Override
	public void btnRefleshOnMouseClick(MouseEvent event) {
		try {
			resources = new RtClasspathLoader().stream(m -> m).sorted().collect(Collectors.toList());
			lvResources.getItems().addAll(resources);
		} catch (Exception e) {
			ValueUtil.toString(e);
		}
	}

}
