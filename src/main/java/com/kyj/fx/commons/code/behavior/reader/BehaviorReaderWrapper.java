/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2018. 4. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kyj.fx.commons.code.behavior.design.BehaviorFxDesigner;
import com.kyj.fx.commons.code.behavior.design.BehaviorMeta;
import com.kyj.fx.commons.code.behavior.design.BehaviorVo;
import com.kyj.fx.commons.code.behavior.exec.BehaviorRunner;
import com.kyj.fx.commons.models.BehaviorReferenceVO;
import com.kyj.fx.commons.utils.DateUtil;
import com.kyj.fx.commons.utils.ExceptionHandler;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLFormatter;
import com.kyj.fx.commons.utils.XMLUtils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * 
 * BehaviorReader를 통해 파일을 읽은후 <br/>
 * 읽은 내용을 바탕으로 <br/>
 * 구조적 분리를 처리해주는 클래스<br/>
 * 
 * @author KYJ
 *
 */
public class BehaviorReaderWrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorReaderWrapper.class);

	private BehaviorReader reader;
	private String fully;
	private boolean isCallLoad;

	/**
	 * @param simpleFileName
	 * @throws FileNotFoundException
	 */
	public BehaviorReaderWrapper(String simpleFileName) throws FileNotFoundException {
		this(new File(BehaviorReader.getBehaviorBaseDir(), simpleFileName));
	}

	public BehaviorReaderWrapper(File reader) throws FileNotFoundException {
		this.reader = new BehaviorReader(reader);
	}

	public BehaviorReaderWrapper(InputStream in) throws FileNotFoundException {
		this.reader = new BehaviorReader(in);
	}

	public BehaviorReaderWrapper(String name, InputStream in) throws FileNotFoundException {
		this.reader = new BehaviorReader(name, in);
	}

	public BehaviorReaderWrapper(BehaviorReader reader) {
		this.reader = reader;
	}

	public BehaviorReaderWrapper(String name, String readableText) throws FileNotFoundException, UnsupportedEncodingException {
		this.reader = new BehaviorTextReader(name, readableText);
	}

	/**
	 * if this file is null then this is loaded by stream. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 3. 20.
	 * @return
	 */
	public File getWib() {
		if (this.reader != null) {
			return this.reader.getWib();
		}
		return null;
	}

	/**
	 * 
	 * pre required action before separate xml structure
	 * 
	 * @throws Exception
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 2.
	 */
	public void load() throws Exception {

		if (this.reader == null) {
			throw new IOException("the behaviorReader is null.");
		}

		this.fully = this.reader.readBehavior();
		this.isCallLoad = true;
	}

	/**
	 * return Script Section.<br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 2.
	 * @exception RuntimeException
	 *                content is empty.
	 * 
	 * @return
	 */
	public String readScript() {
		return readScript(this.fully);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param fully
	 * @return
	 */
	public String readScript(String fully) {
		if (this.fully == null) {
			if (!isCallLoad)
				throw new RuntimeException("need to call load.");
			throw new RuntimeException("the text is empty.");
		}

		// LOGGER.debug(fully);

		String name = this.reader.getName();

		Optional<String> xpathText = null;
		String script = "";
		// 파일이 존재하는경우
		if (name != null) {

			String fileExtension = FileUtil.getFileExtension(name);
			if (fileExtension.equalsIgnoreCase("wib")) {
				xpathText = XMLUtils.toXpathText(this.fully, "//Script/text()");
			} else if (fileExtension.equalsIgnoreCase("bfm")) {
				xpathText = XMLUtils.toXpathText(this.fully, "//FunctionFile/text()");
			}

		}
		/* 파일이 존재하지않는경우 */
		else {

			Optional<NodeList> test = XMLUtils.toXpathNodes(this.fully, "//Script");
			if (test.isPresent()) {
				NodeList nodeList = test.get();
				if (nodeList.getLength() != 0) {
					Node item = nodeList.item(0);
					script = item.getTextContent();
				}
			}
			if (xpathText == null)
				xpathText = XMLUtils.toXpathText(this.fully, "//FunctionFile/text()");
		}

		if (xpathText != null && xpathText.isPresent())
			script = xpathText.get();

		return script;
	}

	/**
	 * 
	 * return references metadata.<br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 2.
	 * @return
	 */
	public List<BehaviorReferenceVO> readReferences() {
		Optional<NodeList> xpathNodes = XMLUtils.toXpathNodes(this.fully, "//Reference");
		List<BehaviorReferenceVO> items = new ArrayList<>();
		if (xpathNodes.isPresent()) {
			NodeList nodes = xpathNodes.get();

			int length = nodes.getLength();

			for (int i = 0; i < length; i++) {
				Node item = nodes.item(i);
				NamedNodeMap attributes = item.getAttributes();
				if (attributes == null)
					continue;

				Node namedItem = attributes.getNamedItem("FileName");
				String txtFileName = namedItem.getTextContent();

				if (ValueUtil.isNotEmpty(txtFileName)) {

					// File wib = this.reader.getFile();
					// if (wib == null || !wib.exists())
					// throw new RuntimeException("wib file is empty.");

					File refFile = new File(BehaviorReader.getBehaviorBaseDir(), txtFileName);
					if (refFile.exists()) {
						items.add(new BehaviorReferenceVO(refFile, txtFileName));
					}
				}
			}

		}
		return items;
	}

	/**
	 * Behavior파일에서 디자인 스콥을 읽어 리턴한다. <br/>
	 * 
	 * fix] <br/>
	 * 8.11 Behavior 전체를 읽는 형태로 변경 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 18.
	 * @return BehaviorVo
	 * 
	 * @throws Exception
	 */
	public BehaviorVo readDesignLayout() throws Exception {
		if (this.fully == null) {
			if (!isCallLoad)
				throw new RuntimeException("need to call load.");
			throw new RuntimeException("the text is empty.");
		}
		BehaviorVo load = getBehavior();

		return load;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 16.
	 * @return
	 * @throws Exception
	 */
	public BehaviorVo getBehavior() throws Exception {

		if (this.fully == null)
			throw new NullPointerException("The script is empty.");

		BehaviorVo load = XMLUtils.JaxbUtils.load(this.fully, BehaviorVo.class);
		BehaviorMeta meta = new BehaviorMeta();
		meta.setReader(reader);

		// set metadata
		load.setMeta(meta);
		return load;
	}

	/**
	 * Behaivor 파일을 읽고 <br/>
	 * 디자인 xml부분을 추출하여 javaFx 노드로 변환하는 기능을 수행하는 <br/>
	 * 디자이너 리턴 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 20.
	 * @return
	 * @throws Exception
	 */
	public BehaviorFxDesigner createBehaviorDesigner() throws Exception {
		BehaviorVo behaviorVo = readDesignLayout();

		BehaviorFxDesigner behaviorFxDesigner = new BehaviorFxDesigner(behaviorVo);

		return behaviorFxDesigner;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 12.
	 * @return
	 */
	public String getVariableValue() {
		//
		try {

			/* 코드 고도화는 추후에.. */
			// BehaviorVo behaviorVo = readDesignLayout();
			// BehaviorObjectVO object =
			// behaviorVo.getDesignerLayoutVo().getBehaviorObjectVO();
			// List<BehaviorObjectVO> objects = object.getObjects();
			// for (BehaviorObjectVO obj : objects) {
			// String name = obj.getName();
			// String type = obj.getType();
			// }

			// BehaviorVo behavior = this.getBehavior();
			//
			// DesignerLayoutVo designerLayoutVo =
			// behavior.getDesignerLayoutVo();
			// BehaviorObjectVO behaviorObjectVO =
			// designerLayoutVo.getBehaviorObjectVO();
			// List<BehaviorObjectVO> objects = behaviorObjectVO.getObjects();

			BehaviorFxDesigner createBehaviorDesigner = this.createBehaviorDesigner();
			createBehaviorDesigner.draw();
			String behaviorValues2 = createBehaviorDesigner.getBehaviorValues();

			return behaviorValues2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<VariableValuesXml>");
		// ObservableList<EwiVariableDVO> items =
		// this.createEwiValue.getTbCustomValue().getItems();
		// for (EwiVariableDVO item : items) {
		// sb.append("<Object name='").append(item.getName()).append("'>");
		// sb.append(item.getValue());
		// sb.append("</Object>");
		// }
		sb.append("</VariableValuesXml>");

		return sb.toString();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 12.
	 * @param behaviorFxDesigner
	 * @param _wib
	 * @param behaviorValues
	 * @param variableValues
	 */
	public void executeBehavior(BehaviorFxDesigner behaviorFxDesigner, File _wib, String behaviorValues, String variableValues) {

		String wib = _wib.getName();

		TextArea console = new TextArea();
		FxUtil.createStageAndShow(wib, console, true);

		console.setText("");
		BehaviorRunner command = new BehaviorRunner(wib, behaviorValues, variableValues);
		command.setErrorHandler(new ExceptionHandler() {

			@Override
			public void handle(Exception t) {
				Platform.runLater(() -> {
					console.appendText(ValueUtil.toString(t));
					console.appendText("\n");
				});
			}
		});

		command.setBeforeExecution(mills -> {
			Platform.runLater(() -> {
				console.appendText(DateUtil.getDateString(mills));
				console.appendText("\n");
			});
		});

		command.setAfterExecution(mills -> {
			Platform.runLater(() -> {
				console.appendText(DateUtil.getDateString(mills));
				console.appendText("\n");
			});
		});
		command.setOnErrorStream(msg -> {
			Platform.runLater(() -> {
				console.appendText(msg);
				console.appendText("\n");
			});
		});
		command.setOnSuccess(msg -> {
			String format = null;
			XMLFormatter newInstnace = new XMLFormatter();
			try {
				format = newInstnace.formatWithAttributes(msg.trim());
			} catch (Exception err) {
				format = msg;
			}

			StringBuffer sb = new StringBuffer();
			Date date = new Date(command.getStartMills());

			String start = DateUtil.getDateAsStr(date, "hh:mm:ss");
			String end = DateUtil.getDateAsStr(new Date(command.getEndMills()), "hh:mm:ss");
			sb.append("*******************************************************************").append("\n");
			sb.append("* ").append("File : ").append(_wib != null ? _wib.getName() : "??").append("\n");
			sb.append("* ").append("Start Time: ").append(start).append("\n");
			sb.append("* ").append("End Time: ").append(end).append("\n");
			sb.append("* ").append("Cost : ").append((command.getEndMills() - command.getStartMills())).append(" ( ms )").append("\n");

			String v = newInstnace.removeSpace(command.getBehaviorValue());
			sb.append("* ").append("behaviorValues : \n").append(v).append("\n");
			v = newInstnace.formatWithAttributes(command.getVariableValues());
			sb.append("* ").append("variableValues : \n").append(v).append("\n");

			sb.append("* ").append("Cost : ").append((command.getEndMills() - command.getStartMills())).append(" ( ms )").append("\n");

			sb.append("*******************************************************************").append("\n");
			sb.append(format);
			Platform.runLater(() -> {
				console.appendText(sb.toString());
				console.appendText("\n");
			});
		});

		behaviorFxDesigner.executeBehavior(command);
		LOGGER.debug("Start Execute Behavior");
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 12. 26.
	 * @return
	 */
	public String getName() {
		return this.reader.getName();
	}
}
