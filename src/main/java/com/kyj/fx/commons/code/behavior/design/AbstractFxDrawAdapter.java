/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 6. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.code.behavior.exec.BehaviorRunner;
import com.kyj.fx.commons.code.behavior.reader.BehaviorReader;
import com.kyj.fx.commons.threads.ExecutorDemons;
import com.kyj.fx.commons.utils.DateUtil;
import com.kyj.fx.commons.utils.DialogUtil;
import com.kyj.fx.commons.utils.ExceptionHandler;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.utils.XMLFormatter;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;

/**
 * @author KYJ
 *
 */
public abstract class AbstractFxDrawAdapter<T extends Node> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFxDrawAdapter.class);

	/**
	 * return root node <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 12.
	 * @return
	 */
	public abstract T getRoot();

	/**
	 * Behavior draw <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 18.
	 * @return
	 */
	public abstract T draw();

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 12.
	 * @return
	 */
	public abstract ContextMenu getContextMenu();

	/**
	 * 툴팁 텍스트를 보여줄지 유무 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 2.
	 * @param flag
	 */
	public abstract void showTooltipText(boolean flag);

	/**
	 * behavior value xml 을 리턴 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 17.
	 * @return
	 */
	public abstract String getBehaviorValues();

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 12.
	 * @return
	 */
	public abstract BehaviorVo getBehaviorVo();

	/**
	 * return control items <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 14.
	 * @return
	 */
	public abstract List<BehaviorPannelable> getControls();

	/**
	 * @최초생성일 2018. 10. 1.
	 */
	private Predicate<BehaviorPannelable> isShowTooltipLabel = node -> {

		if (node instanceof BehaviorDesignGroupPane) {
			return false;
		}

		if (node instanceof BehaviorDesignImageView) {
			return false;
		}

		if (node instanceof BehaviorDesignLabel) {
			return false;
		}

		if (node instanceof BehaviorBorderPane) {
			return false;
		}
		return true;
	};

	/**
	 * @return the isShowTooltipLabel
	 */
	public Predicate<BehaviorPannelable> getIsShowTooltipLabel() {
		return isShowTooltipLabel;
	}

	/**
	 * @param isShowTooltipLabel
	 *            the isShowTooltipLabel to set
	 */
	public void setIsShowTooltipLabel(Predicate<BehaviorPannelable> isShowTooltipLabel) {
		this.isShowTooltipLabel = isShowTooltipLabel;
	}

	/**
	 * Execute Behavior <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 11.
	 * @param BehaviorRunner
	 */
	public void executeBehavior(BehaviorRunner run) {
		ExecutorDemons.getGargoyleSystemExecutorSerivce().execute(run);
		LOGGER.debug("Start Execute Behavior");
	}

	static final String EWI_VARIABLES_XML = "EWIVariables.xml";

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 10. 2.
	 * @param behaviorExe
	 * @return
	 */
	public static boolean downloadBehaviorExe(File parent) {
		if (parent == null) {
			throw new RuntimeException("ExecuteBehavior.exe does not exists.");
		}

		if (parent.exists()) {

			File w = new File(parent, "ExecuteBehavior.zip");
			try (InputStream inBehaviorExe = BehaviorRunner.class.getResourceAsStream("ExecuteBehavior.zip")) {
				FileUtil.writeFile(w, inBehaviorExe);
				return true;
			} catch (IOException e) {
				LOGGER.error(ValueUtil.toString(e));
			}
		}
		return false;

	}

	/**
	 * Execute Behavior <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 12.
	 */
	public void executeBehavior() {

		String dir = BehaviorReader.getBehaviorBaseDir();
		if (dir == null) {
			DialogUtil.showMessageDialog(EWI_VARIABLES_XML + " not yet setup behavior value. ");
		}

		File file = new File(dir, EWI_VARIABLES_XML);
		if (!file.exists()) {
			DialogUtil.showMessageDialog(EWI_VARIABLES_XML + " File. does not exists.");
		}

		File _wib = getBehaviorVo().getMeta().getWib();
		String wib = _wib.getName();
		String behaviorValues = "";
		String variableValues = "";

		behaviorValues = getBehaviorValues();
		variableValues = "<VariableValuesXml/>";

		BehaviorRunner command = new BehaviorRunner(wib, behaviorValues, variableValues);
		command.setErrorHandler(new ExceptionHandler() {

			@Override
			public void handle(Exception t) {
				LOGGER.debug(ValueUtil.toString(t));
			}
		});

		command.setBeforeExecution(mills -> {
			LOGGER.debug(DateUtil.getDateString(mills));
		});

		command.setAfterExecution(mills -> {
			LOGGER.debug(DateUtil.getDateString(mills));
		});
		command.setOnErrorStream(msg -> {
			LOGGER.debug(msg);
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
			LOGGER.debug(sb.toString());
		});

		executeBehavior(command);
	}
}
