/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.util
 *	작성일   : 2015. 10. 16.
 *	프로젝트 : SOS 미어캣 프로젝트
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.kyj.fx.commons.functions.DefaultGenerateVoEditorFunction;
import com.kyj.fx.commons.functions.FieldMetaFunction;
import com.kyj.fx.voeditor.core.VoEditor;
import com.kyj.fx.voeditor.core.model.meta.ClassMeta;
import com.kyj.fx.voeditor.core.model.meta.FieldMeta;

import kyj.Fx.dao.wizard.core.model.vo.TableModelDVO;

/**
 * @author KYJ VoEditor Converter
 *
 */
public class VoEditorConverter {

	private ClassMeta classMeta;
	private List<FieldMeta> fields;
	/**
	 * 생성자
	 */
	public VoEditorConverter(ClassMeta classMeta, List<TableModelDVO> tableModesl) {
		fields = defaultModelConvertFunction().apply(tableModesl);
		this.classMeta = classMeta;
	}

	/**
	 * Vo를 생성하기 위한 기본 모델을 반환
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 15.
	 * @return
	 */
	private Function<List<TableModelDVO>, List<FieldMeta>> defaultModelConvertFunction() {
		return new FieldMetaFunction();
	}

	/**
	 * Vo를 생성하기 위한 기본 모델을 반환
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 15.
	 * @return
	 */
	private BiFunction<ClassMeta, List<FieldMeta>, VoEditor> defaultGenertateVoEditorBiConsumer() {
		return new DefaultGenerateVoEditorFunction();
	}

	/**
	 * 변환처리
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2015. 10. 16.
	 * @return
	 */
	public VoEditor convert() {
		return defaultGenertateVoEditorBiConsumer().apply(classMeta, fields);
	}

}
