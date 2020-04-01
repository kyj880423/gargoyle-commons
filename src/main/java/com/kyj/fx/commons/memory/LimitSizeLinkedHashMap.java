/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.collections
 *	작성일   : 2017. 11. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.memory;

import java.util.LinkedHashMap;

/**
 * @author KYJ
 *
 */
public class LimitSizeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

	/**
	 * @최초생성일 2017. 11. 5.
	 */
	private static final long serialVersionUID = 5624581547697218320L;
	
	private int maxSize = 100;

	public LimitSizeLinkedHashMap(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return size() > maxSize;
	}

}
