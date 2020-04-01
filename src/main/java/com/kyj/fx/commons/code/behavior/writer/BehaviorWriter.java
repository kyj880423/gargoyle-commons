/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.writer
 *	작성일   : 2018. 5. 16.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.writer;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author KYJ
 *
 */
public class BehaviorWriter implements Closeable {

	private File outFile;
	private String cont;
	private InputStream in;

	public enum Mode {
		NEW, MODIFY
	}

	/**
	 * wib or bfm 파일 <br/>
	 * 
	 * @param f
	 * @throws FileNotFoundException
	 */
	public BehaviorWriter(File outFile, String cont) throws FileNotFoundException {
		this.outFile = outFile;
		this.cont = cont;
		this.in = new FileInputStream(outFile);
	}

	public BehaviorWriter(File outFile) throws FileNotFoundException {
		this.outFile = outFile;
		this.in = new FileInputStream(outFile);
	}

	public BehaviorWriter(InputStream in) {
		this.in = in;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @return
	 * @throws IOException
	 */
	public void writeBehavior() throws IOException {
		this.writeBehavior(this.outFile, this.cont);
	}

	/**
	 * Behavior 파읽을 읽는 작업을 수행한다. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @param wib
	 * @return
	 * @throws IOException
	 */
	private void writeBehavior(File wib, String cont) throws IOException {
		try (FileWriter writer = new FileWriter(wib)) {
			writer.write(cont);
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 3.
	 * @return
	 */
	public File getFile() {
		return this.outFile;
	}

	@Override
	public void close() throws IOException {
		if (this.in != null)
			this.in.close();
	}

}
