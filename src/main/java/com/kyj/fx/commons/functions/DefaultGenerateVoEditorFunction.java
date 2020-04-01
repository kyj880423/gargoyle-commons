/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.functions
 *	작성일   : 2015. 10. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import java.util.List;
import java.util.function.BiFunction;

import com.kyj.fx.voeditor.core.VoEditor;
import com.kyj.fx.voeditor.core.model.meta.ClassMeta;
import com.kyj.fx.voeditor.core.model.meta.FieldMeta;

/**
 * @author KYJ
 *
 */
public class DefaultGenerateVoEditorFunction implements BiFunction<ClassMeta, List<FieldMeta>, VoEditor> {

	@Override
	public VoEditor apply(ClassMeta t, List<FieldMeta> fields) {
		VoEditor voEditor = new VoEditor(t, fields);
		voEditor.build();
		return voEditor;
	}

}
