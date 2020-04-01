/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.vbs.exec
 *	작성일   : 2018. 8. 25.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.vbs.exec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author KYJ
 *
 */
public class AbstractVBSExecutor<T> {

	private File tmpScript;
	private String scriptCode;
	private Function<Process, T> resultHandler;
	/*
	 * private Function<Process, T> resultHandler = p -> {
	 * 
	 * StringBuffer sb = new StringBuffer(); try (BufferedReader rd = new
	 * BufferedReader(new InputStreamReader(p.getInputStream()))) {
	 * 
	 * String temp = null; while ((temp = rd.readLine()) != null) {
	 * sb.append(temp); } } return sb.toString();
	 * 
	 * };
	 */

	public AbstractVBSExecutor() throws Exception {
		tmpScript = File.createTempFile("xxxxx", ".vbs");
	}

	public T run() throws Exception {

		Process p = createCommand();
		int ret = p.waitFor();
		if (ret != 0) {
			return null;
		} else {
			if (resultHandler != null)
				return resultHandler.apply(p);
		}
		return null;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 8. 25.
	 * @return
	 * @throws IOException
	 */
	public Process createCommand() throws IOException {
		FileWriter fwrt = new FileWriter(tmpScript);
		fwrt.append(this.scriptCode);
		fwrt.close();
		return Runtime.getRuntime().exec("cmd.exe /c" + "cscript " + tmpScript.getAbsolutePath());
	}

	/**
	 * @return the tmpScript
	 */
	public File getTmpScript() {
		return tmpScript;
	}

	/**
	 * @param tmpScript
	 *            the tmpScript to set
	 */
	public void setTmpScript(File tmpScript) {
		this.tmpScript = tmpScript;
	}

	/**
	 * @return the scriptCode
	 */
	public String getScriptCode() {
		return scriptCode;
	}

	/**
	 * @param scriptCode
	 *            the scriptCode to set
	 */
	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	/**
	 * @return the resultHandler
	 */
	public Function<Process, T> getResultHandler() {
		return resultHandler;
	}

	/**
	 * @param resultHandler
	 *            the resultHandler to set
	 */
	public void setResultHandler(Function<Process, T> resultHandler) {
		this.resultHandler = resultHandler;
	}

}
