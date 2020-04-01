/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.functions
 *	작성일   : 2015. 10. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.kyj.fx.commons.memory.ClassTypeResourceLoader;
import com.kyj.fx.voeditor.core.model.meta.FieldMeta;

import kyj.Fx.dao.wizard.core.model.vo.TableModelDVO;

/**
 * @author KYJ
 *
 */
public class FieldMetaFunction implements Function<List<TableModelDVO>, List<FieldMeta>> {

	@Override
	public List<FieldMeta> apply(List<TableModelDVO> models) {
		List<FieldMeta> fileMetaList = new ArrayList<>(models.size());

		for (TableModelDVO tableModel : models) {
			try {
				String fieldName = tableModel.getName();
				String type = tableModel.getType();
				FieldMeta fieldMeta = ClassTypeResourceLoader.getInstance().get(type);
				fieldMeta.setName(fieldName);
				fieldMeta.setModifier(Modifier.PRIVATE);
				fieldMeta.setDesc(tableModel.getDesc());
				if ("Y".equals(tableModel.getPk())) {
					fieldMeta.setPrimarykey(true);
				}
				fileMetaList.add(fieldMeta);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return fileMetaList;
	}
}