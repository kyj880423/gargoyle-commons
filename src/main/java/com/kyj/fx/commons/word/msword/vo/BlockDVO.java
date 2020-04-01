/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.vo
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.vo;

public class BlockDVO {

	private int startindex;
	private int endindex;

	public BlockDVO(int startindex, int endindex) {
		super();
		this.startindex = startindex;
		this.endindex = endindex;
	}

	public int getStartindex() {
		return startindex;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}

	public int getEndindex() {
		return endindex;
	}

	public void setEndindex(int endindex) {
		this.endindex = endindex;
	}

	@Override
	public String toString() {
		return "BlockDVO [startindex=" + startindex + ", endindex=" + endindex + "]";
	}

}
