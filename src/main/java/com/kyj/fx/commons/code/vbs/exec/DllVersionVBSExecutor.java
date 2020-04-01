/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.vbs.exec
 *	작성일   : 2018. 8. 25.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.vbs.exec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;

/**
 * @author KYJ
 *
 */
public class DllVersionVBSExecutor extends AbstractVBSExecutor<String> {

	public DllVersionVBSExecutor(File dllFileName) throws Exception {
		super();
		setScriptCode("Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\r\n" + "Wscript.Echo objFSO.GetFileVersion(\""
				+ dllFileName + "\")");
		Function<Process, String> resultHandler = p -> {

			try (BufferedReader rd = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				String s = null;
				while (null != (s = rd.readLine())) {
					if (s.matches("^[\\d\\.]+$")) {
						return s;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		};

		setResultHandler(resultHandler);

	}

}
