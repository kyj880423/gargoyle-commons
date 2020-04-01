/********************************
 *	프로젝트 : Gagoyle
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.auto.msword.biz
 *	작성일   : 2016. 2. 15.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.exceptions.ProgramSpecSourceErrException;
import com.kyj.fx.commons.exceptions.ProgramSpecSourceException;
import com.kyj.fx.commons.exceptions.ProgramSpecSourceNullException;
import com.kyj.fx.commons.word.msword.vo.BlockDVO;

public class InspectorSourceMeta {
	private static Logger LOGGER = LoggerFactory.getLogger(InspectorSourceMeta.class);

	public String getErrorMsg() {

		return errorMsg;
	}

	private static final String START_BRACE = "{";

	private static final String END_BRACE = "}";

	/**
	 * 소스내에 존재하는 문자열 포함문자가 존재하면 제거
	 */
	private static final String EXCEPT_STING = "\"[\\{\\}]{0,}\"";

	/* SVN 소스 List */
	private List<String> svnCatList;

	private List<String> importList = new ArrayList<String>();

	/* 블록구간 결과 리스트 */
	private List<BlockDVO> resultDVOList;

	private String fileName;

	private String errorMsg;

	/* List로 입력받은 소스를 하나의 소스로 만들기 위한 변수 */
	private StringBuilder sb = new StringBuilder();

	public InspectorSourceMeta(String fileName, List<String> svnCatList) throws Exception {
		this.fileName = fileName;
		this.svnCatList = svnCatList;
		try {
			doAnalysis();
		} catch (ProgramSpecSourceException e) {
			errorMsg = e.getMessage();
		}
	}

	/**
	 * 소스코드내 import문반환
	 * 
	 * @return
	 */
	public List<String> getImportList() {
		return importList;
	}

	public String getFileName() {
		return fileName;
	}

	public List<String> getSourceCodeList() {
		return svnCatList;
	}

	/**
	 * 2014. 6. 29. KYJ
	 * 
	 * @return
	 * @처리내용 : 소스내 블록구간의 정보를 반환
	 */
	public List<BlockDVO> listBlock() {
		return resultDVOList;
	}

	/**
	 * 2014. 6. 29. KYJ
	 * 
	 * @return
	 * @처리내용 : 소스코드 스트링반환
	 */
	public String getSourceCode() {
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		ArrayList<String> arrayList = new ArrayList<String>();
		System.out.println("if(weekFrom < \"10\") { weekFrom=\"0\"+weekFrom; }".contains("{"));
		arrayList.add("if(weekFrom < \"10\") { weekFrom=\"0\"+weekFrom; }");

		InspectorSourceMeta inspectorSourceMeta = new InspectorSourceMeta("", arrayList);
		System.out.println(inspectorSourceMeta);

	}

	Pattern startBracePattern = Pattern.compile("\\{");

	Pattern endBracePattern = Pattern.compile("\\}");

	/**
	 * 2014. 6. 28. KYJ
	 * 
	 * @throws InspectSourceErrException
	 *             브레이스 숫자가 다를경우 발생
	 * @처리내용 : Brace위치 조정 및
	 */
	private void doAnalysis() throws Exception {
		/* brace 시작부 */
		List<BraceDVO> startBraceDVOList;

		/* brace 종료부 */
		List<BraceDVO> endBraceDVOList;

		if (svnCatList != null && !svnCatList.isEmpty()) {
			/* 배열의 삭제가 빠르도록 LinkedList로 생성 */
			startBraceDVOList = new LinkedList<BraceDVO>();
			endBraceDVOList = new LinkedList<BraceDVO>();
			Matcher matcher = null;
			for (int index = 0; index < svnCatList.size(); index++) {

				String txt = svnCatList.get(index);
				sb.append(txt).append("\n");
				/* if ~ elseif로 가지 않는다. 한 라인내에 브레이스의 시작과 종료부가 같이 있을경우도 있기 때문 */

				/* 만약 소스내 문자열이 포함되있는경우는 없애고 작업함 문자열내에 브레이스문이 있을가능성이 있기때문 */
				txt = txt.replaceAll(EXCEPT_STING, "");
				txt = txt.trim();

				/* '//' 로 시작하는 주석부분은 제외 */
				if (txt.startsWith("//")) {
					continue;
				}

				// import문일경우 importList에 add
				if (txt.startsWith("import ")) {
					this.importList.add(txt);
				}
				/* brace시작부일경우 + 주석부분에 해당하는 브레이스의 경우 제외 */
				if (txt.contains(START_BRACE)) {
					matcher = startBracePattern.matcher(txt);
					boolean result = matcher.find();
					while (result) {
						startBraceDVOList.add(new BraceDVO(START_BRACE, index));
						result = matcher.find();
					}
				}
				/* brace종료부 + 주석부분에 해당하는 브레이스 경우 제외 */
				if (txt.contains(END_BRACE)) {
					matcher = endBracePattern.matcher(txt);
					boolean result = matcher.find();
					while (result) {
						endBraceDVOList.add(new BraceDVO(END_BRACE, index));
						result = matcher.find();
					}
				}

			}

			resultDVOList = new LinkedList<BlockDVO>();
			/* 배열이 비어있지않고 브레이스의 숫자가 같은경우 진행 */
			if (!startBraceDVOList.isEmpty() && !endBraceDVOList.isEmpty() && (startBraceDVOList.size() == endBraceDVOList.size())) {

				int startIndex = -1;
				int endIndex = -1;

				/* 시작브레이스의 끝부분 인덱스부터 시작 */
				for (int sbIdx = startBraceDVOList.size() - 1; sbIdx >= 0; sbIdx--) {
					startIndex = startBraceDVOList.get(sbIdx).getIndex();
					/* 종료브레이스는 처음인덱스부터 시작 */
					int endSize = endBraceDVOList.size();
					for (int ebIdx = 0; ebIdx < endSize; ebIdx++) {
						/**/

						endIndex = endBraceDVOList.get(ebIdx).getIndex();
						if (startIndex <= endIndex) {

							resultDVOList.add(new BlockDVO(startIndex + 1, endIndex + 1));
							endBraceDVOList.remove(ebIdx);
							endSize = endBraceDVOList.size();
							break;
						}
					}
				}

				Collections.reverse(resultDVOList);

				System.out.println("############");
				int seq = 0;
				for (BlockDVO b : resultDVOList) {
					LOGGER.debug(String.format("seq : %d start : %d  end : %d \n", seq++, b.getStartindex(), b.getEndindex()));
				}
				System.out.println("############");

			} else if (startBraceDVOList.isEmpty() && endBraceDVOList.isEmpty()) {
				// N/A
			} else {
				throw new ProgramSpecSourceErrException("Brace Count is Wrong.");
			}
		} else {
			throw new ProgramSpecSourceNullException("Source is null");
		}
	}

	class BraceDVO {

		private String brace;

		private int index;

		public BraceDVO(String brace, int index) {
			super();
			this.brace = brace;
			this.index = index;
		}

		public String getBrace() {
			return brace;
		}

		public void setBrace(String brace) {
			this.brace = brace;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

	}
}
