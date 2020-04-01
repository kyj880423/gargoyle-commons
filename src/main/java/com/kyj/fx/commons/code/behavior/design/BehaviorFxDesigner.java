/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 6. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.code.behavior.exec.BehaviorRunner;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;

/**
 * 
 * BehaviorObjectVO 파라미터 인수에 대응되는 오브젝트 리턴 <br/>
 * 
 * @author KYJ
 *
 */
public class BehaviorFxDesigner {
	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorFxDesigner.class);
	private BehaviorVo behaviorVo;
	private AbstractFxDrawAdapter<Node> adapter;

	public BehaviorFxDesigner(BehaviorVo behaviorVo) {
		this.behaviorVo = behaviorVo;
		// default adapter.
		adapter = new AnchorPaneFxDrawAdapter(this);
	}

	public ContextMenu getContextMenu() {
		return this.adapter.getContextMenu();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 20.
	 * @param adapter
	 */
	public void setAdapter(AbstractFxDrawAdapter<Node> adapter) {
		this.adapter = adapter;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 18.
	 * @return
	 */
	public Node draw() {
		return adapter.draw();
	}

	/**
	 * 사용자 정의 핸들러 처리
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 20.
	 * @param customHandler
	 */
	public void setItemHandler(BiConsumer<Node, BehaviorPropertyVo> customHandler) {
		if (this.adapter != null && this.adapter instanceof IUserCustomFxDrawAdapter) {
			((AnchorPaneFxDrawAdapter) this.adapter).setItemHandler(customHandler);
		}
	}

	/**
	 * 툴팁 텍스트를 보여줄지 유무 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 2.
	 * @param flag
	 */
	public void showTooltopText(boolean flag) {
		this.adapter.showTooltipText(flag);
	}

	/**
	 * @return the behaviorObject
	 */
	public BehaviorVo getBehaviorVo() {
		return this.behaviorVo;
	}

	/**
	 * @return the behaviorObject
	 */
	public BehaviorObjectVO getBehaviorObject() {
		return this.behaviorVo.getDesignerLayoutVo().getBehaviorObjectVO();
	}

	/**
	 * behavior value xml 을 리턴 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 17.
	 * @return
	 */
	public String getBehaviorValues() {
		return this.adapter.getBehaviorValues();
	}

	/**
	 * Execute Behavior <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 12.
	 * @param run
	 */
	public void executeBehavior(BehaviorRunner run) {
		this.adapter.executeBehavior(run);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 14.
	 * @return
	 */
	public List<BehaviorPannelable> getControls() {
		return adapter.getControls();
	}

	/**
	 * @return the isShowTooltipLabel
	 */
	public Predicate<BehaviorPannelable> getIsShowTooltipLabel() {
		return this.adapter.getIsShowTooltipLabel();
	}

	/**
	 * @param isShowTooltipLabel
	 *            the isShowTooltipLabel to set
	 */
	public void setIsShowTooltipLabel(Predicate<BehaviorPannelable> isShowTooltipLabel) {
		this.adapter.setIsShowTooltipLabel(isShowTooltipLabel);
	}
}
