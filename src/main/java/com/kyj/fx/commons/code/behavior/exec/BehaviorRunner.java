/********************************
 *	프로젝트 : gargoyle-rax
 *	패키지   : com.kyj.fx.behavior.text
 *	작성일   : 2018. 7. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.exec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.code.behavior.reader.BehaviorReader;
import com.kyj.fx.commons.utils.DateUtil;
import com.kyj.fx.commons.utils.ExceptionHandler;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.RuntimeClassUtil;
import com.kyj.fx.commons.utils.ZipUtil;

/**
 * 
 * Behavior 실행처리 담당 <br/>
 * 
 * @author KYJ
 *
 */
public class BehaviorRunner implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorRunner.class);

	private String wibFileName;
	private String behaviorValue;
	private String variableValues = "<VariableValuesXml></VariableValuesXml>";
	private ExceptionHandler errorHandler;
	private Consumer<String> onSuccess;

	/**
	 * @param wibFileName
	 * @param behaviorValue
	 */
	public BehaviorRunner(String wibFileName, String behaviorValue) {
		super();
		this.wibFileName = wibFileName;
		this.behaviorValue = behaviorValue;
	}

	/**
	 * @param wibFileName
	 * @param behaviorValue
	 * @param variableValues
	 */
	public BehaviorRunner(String wibFileName, String behaviorValue, String variableValues) {
		super();
		this.wibFileName = wibFileName;
		this.behaviorValue = behaviorValue;
		this.variableValues = variableValues;
	}

	/**
	 * @return the errorHandler
	 */
	public ExceptionHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * @param errorHandler
	 *            the errorHandler to set
	 */
	public void setErrorHandler(ExceptionHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/**
	 * @return the wibFileName
	 */
	public String getWibFileName() {
		return wibFileName;
	}

	/**
	 * @param wibFileName
	 *            the wibFileName to set
	 */
	public void setWibFileName(String wibFileName) {
		this.wibFileName = wibFileName;
	}

	/**
	 * @return the behaviorValue
	 */
	public String getBehaviorValue() {
		return behaviorValue;
	}

	/**
	 * @param behaviorValue
	 *            the behaviorValue to set
	 */
	public void setBehaviorValue(String behaviorValue) {
		this.behaviorValue = behaviorValue;
	}

	/**
	 * @return the variableValues
	 */
	public String getVariableValues() {
		return variableValues;
	}

	/**
	 * @param variableValues
	 *            the variableValues to set
	 */
	public void setVariableValues(String variableValues) {
		this.variableValues = variableValues;
	}

	/**
	 * @return the onSuccess
	 */
	public Consumer<String> getOnSuccess() {
		return onSuccess;
	}

	/**
	 * @param onSuccess
	 *            the onSuccess to set
	 */
	public void setOnSuccess(Consumer<String> onSuccess) {
		this.onSuccess = onSuccess;
	}

	long startMills;
	long endMills;

	/**
	 * @return the startMills
	 */
	public long getStartMills() {
		return startMills;
	}

	/**
	 * @param startMills
	 *            the startMills to set
	 */
	public void setStartMills(long startMills) {
		this.startMills = startMills;
	}

	/**
	 * @return the endMills
	 */
	public long getEndMills() {
		return endMills;
	}

	/**
	 * @param endMills
	 *            the endMills to set
	 */
	public void setEndMills(long endMills) {
		this.endMills = endMills;
	}

	private Consumer<Long> beforeExecution = date -> {
		LOGGER.debug(DateUtil.getDateString(date));
	};
	private Consumer<Long> afterExecution = date -> {
		LOGGER.debug(DateUtil.getDateString(date));
	};
	private Consumer<String> onErrorStream = msg -> {
		LOGGER.debug(msg);
	};

	/**
	 * @param onErrorStream
	 *            the onErrorStream to set
	 */
	public void setOnErrorStream(Consumer<String> onErrorStream) {
		this.onErrorStream = onErrorStream;
	}

	/**
	 * @param beforeExecution
	 *            the beforeExecution to set
	 */
	public void setBeforeExecution(Consumer<Long> beforeExecution) {
		this.beforeExecution = beforeExecution;
	}

	/**
	 * @param afterExecution
	 *            the afterExecution to set
	 */
	public void setAfterExecution(Consumer<Long> afterExecution) {
		this.afterExecution = afterExecution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			File parentDir = new File(BehaviorReader.getBehaviorBaseDir());
			parentDir = parentDir.getParentFile();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayOutputStream err = new ByteArrayOutputStream();
			File behaviorExeuctorFile = new File(parentDir, "ExecuteBehavior.exe");
			if (!behaviorExeuctorFile.exists()) {
				
				URL resource = BehaviorRunner.class.getResource("BehaviorExec.zip");
				InputStream in = resource.openStream();
				if(in == null)
					throw new RuntimeException("ExecuteBehavior.exe does not exists.");
				
				try {
					File copyZip = new File(parentDir, "BehaviorExec.zip");
					if(!copyZip.exists())
						throw new RuntimeException("ExecuteBehavior.zip write failed. ");	
					
					FileUtil.writeFile(copyZip, in);
					ZipUtil.unzip(copyZip, parentDir);
				}finally
				{
					in.close();
				}
			}

			if (!behaviorExeuctorFile.canExecute())
				behaviorExeuctorFile.setExecutable(true);

			List<String> arguments = Arrays.asList(behaviorExeuctorFile.getAbsolutePath(), 
					
					"\"" + this.wibFileName + "\"",
					/* Behavior Values */
					"\"" + this.behaviorValue + "\"",
					"\"" +  this.variableValues + "\""
					
//					this.wibFileName,
//					/* Behavior Values */
//					this.behaviorValue ,
//					this.variableValues

					);
			LOGGER.debug("Prepare Execution.");
			startMills = System.currentTimeMillis();
			beforeExecution.accept(startMills);
			RuntimeClassUtil.exe(arguments, out, err);
			endMills = System.currentTimeMillis();
			afterExecution.accept(endMills);
			LOGGER.debug("End Execution.");
			String msg = out.toString("euc-kr");
			if (onSuccess != null)
				onSuccess.accept(msg);

			if (onErrorStream != null)
				onErrorStream.accept(err.toString());

			LOGGER.debug(msg);
			LOGGER.debug(err.toString());

		} catch (Exception e) {
			if (this.errorHandler != null)
				this.errorHandler.handle(e);
		}

	}

}
