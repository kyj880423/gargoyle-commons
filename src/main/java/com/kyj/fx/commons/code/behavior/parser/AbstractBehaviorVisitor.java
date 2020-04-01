/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.parser
 *	작성일   : 2018. 5. 9.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.parser;

/**
 * @author KYJ
 *
 */
public abstract class AbstractBehaviorVisitor implements BehaviorVisitable {

	private BehaviorParser parser;
	private int currentLineSeparator;
	private int startIdx;
	private int endIdx;
	private int count;
	private String name;

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 10. 
	 * @return
	 */
	public final String getCode() {
		return this.parser == null ? null : this.parser.getCode();
	}

	/**
	 * @return the parser
	 */
	public BehaviorParser getParser() {
		return parser;
	}

	/**
	 * @param parser
	 *            the parser to set
	 */
	void setParser(BehaviorParser parser) {
		this.parser = parser;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 8.
	 * @param code
	 */
	public abstract void visite(String code);

	public void setStartIdx(int idx) {
		this.startIdx = idx;
	}

	@Override
	public void setEndIdx(int idx) {
		this.endIdx = idx;
	}

	/**
	 * @return the startIdx
	 */
	public int getStartIdx() {
		return startIdx;
	}

	/**
	 * @return the endIdx
	 */
	public int getEndIdx() {
		return endIdx;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 9.
	 */
	public void addCount() {
		count++;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the currentLineSeparator
	 */
	public int getCurrentLineSeparator() {
		return currentLineSeparator;
	}

	/**
	 * @param currentLineSeparator
	 *            the currentLineSeparator to set
	 */
	public void setCurrentLineSeparator(int currentLineSeparator) {
		this.currentLineSeparator = currentLineSeparator;
	}

}
