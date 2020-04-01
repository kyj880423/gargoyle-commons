/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2017. 12. 8.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.reader;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.kyj.fx.commons.utils.AbstractStringFileContentConversion;
import com.kyj.fx.commons.utils.BehaviorFileContentConversion;
import com.kyj.fx.commons.utils.PreferencesUtil;

/**
 * behavior 파일을 읽어오는 기능을 수행 <br/>
 * 
 * @author KYJ
 *
 */
public class BehaviorReader implements Closeable {

	public static final String BEHAVIOR_BASE_DIR = "behavior.base.dir";
	private File wib;
	private String name;
	private InputStream in;

	/**
	 * wib or bfm 파일 <br/>
	 * 
	 * @param f
	 * @throws FileNotFoundException
	 */
	public BehaviorReader(File f) throws FileNotFoundException {
		this.wib = f;
		this.name = f.getName();
		this.in = new FileInputStream(f);
	}

	/**
	 * @param in
	 * @throws FileNotFoundException
	 */
	public BehaviorReader(InputStream in) throws FileNotFoundException {
		this(null, in);
	}

	public BehaviorReader(String name, InputStream in) {
		this.name = name;
		this.in = in;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @return
	 * @throws Exception
	 */
	public String readBehavior() throws Exception {
		try (InputStream in = this.in) {
			return this.readBehavior(in);
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @return
	 * @throws Exception
	 */
	public byte[] readBehaviorByte() throws Exception {
		return this.readByte(this.in);
	}

	/**
	 * 데이터 컨버젼 객체를 리턴함 <br/>
	 * 해당 클래스는 이진파일읅 사람이 읽을 수 있는 텍스트형으로 바꿔주는 역할을 수행 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @param wib
	 * @return
	 */
	protected AbstractStringFileContentConversion createConversion() {
		return new BehaviorFileContentConversion();
	}

	/**
	 * Behavior 파읽을 읽는 작업을 수행한다. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @param wib
	 * @return
	 * @throws Exception
	 */
	private String readBehavior(InputStream in) throws Exception {
		AbstractStringFileContentConversion conversion = createConversion();
		conversion.in(in);
		return conversion.conversion();
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param wib
	 * @return
	 * @throws Exception
	 */
	private byte[] readByte(InputStream in) throws Exception {
		AbstractStringFileContentConversion createConversion = createConversion();
		if (createConversion instanceof BehaviorFileContentConversion) {
			BehaviorFileContentConversion conversion = (BehaviorFileContentConversion) createConversion;
			conversion.in(in);
			return conversion.byteConversion();
		}

		throw new RuntimeException("there is no matched conversion item.");
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @return
	 */
	public static String getBehaviorBaseDir() {
		String path = PreferencesUtil.getDefault().get(BEHAVIOR_BASE_DIR, null);
		return path;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 23.
	 * @param location
	 * @return
	 */
	public static void setBehaviorBaseDir(String location) {
		PreferencesUtil.getDefault().put(BEHAVIOR_BASE_DIR, location);
	}

	/**
	 * @return the wib
	 */
	public File getWib() {
		return wib;
	}

	@Override
	public void close() throws IOException {
		if (this.in != null)
			in.close();
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
