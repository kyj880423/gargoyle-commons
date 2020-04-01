/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.file.conversion
 *	작성일   : 2018. 3. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author KYJ
 *
 */
public class BehaviorFileContentConversion extends AbstractStringFileContentConversion {

	private ByteArrayOutputStream out;

	/**
	 * @param size
	 *            buffer size
	 */
	public BehaviorFileContentConversion(int size) {
		out = new ByteArrayOutputStream(size);
	}

	public BehaviorFileContentConversion() {
		out = new ByteArrayOutputStream();
	}

	/*
	 * @Deprecated this class not used. <br/>
	 * 
	 */
	@Deprecated
	@Override
	public void out(OutputStream out) {
		// Nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.voeditor.visual.framework.file.conversion.
	 * AbstractFileContentConversion#getOut()
	 */
	@Override
	public OutputStream getOut() {
		return this.out;
	}

	/**
	 * 압축해제 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @param compressed
	 * @return
	 * @throws DataFormatException
	 * @throws IOException
	 */
	public String decompress(byte[] compressed) throws DataFormatException, IOException {
		if (compressed == null || compressed.length == 0)
			return "";

		Inflater decompresser = new Inflater(true);

		byte[] buf = new byte[4096];
		String text = "";
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			decompresser.setInput(compressed);
			int count = -1;
			while (!decompresser.finished()) {
				count = decompresser.inflate(buf);
				if(count== -1)
					break;
				out.write(buf, 0, count);
			}
			text = out.toString(getEncoding().name());
		
		} finally {
			decompresser.end();
		}
		return text;
	}

	/**
	 * 압축 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param data
	 * @return
	 * @throws DataFormatException
	 * @throws IOException
	 */
	public static byte[] compress(byte[] data) throws Exception {
		Deflater compresser = new Deflater(Deflater.BEST_SPEED, true);

		byte[] buf = new byte[4096];
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			compresser.setInput(data);
			compresser.finish();
			while (!compresser.finished()) {
				int length = compresser.deflate(buf);
				out.write(buf, 0, length);
			}
			compresser.end();
			return out.toByteArray();
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @param compressed
	 * @return
	 * @throws DataFormatException
	 * @throws IOException
	 */
	public static byte[] byteDecompress(byte[] compressed) throws DataFormatException, IOException {
		Inflater decompresser = new Inflater(true);

		byte[] result = new byte[1024];

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			decompresser.setInput(compressed);
			while (!decompresser.finished()) {
				int count = decompresser.inflate(result);
				out.write(result, 0, count);
			}
			decompresser.end();
			return out.toByteArray();
		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 16.
	 * @return
	 * @throws Exception
	 */
	public byte[] byteConversion() throws Exception {
		byte[] str = null;
		try {
			InputStream in = getIn();
			ByteArrayOutputStream out = this.out;
			int c = 0;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			str = byteDecompress(out.toByteArray());
		} finally {
			out.close();
		}
		return str;
	}

	/**
	 * 데이터 변환 처리 <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 6.
	 * @return String 변환된 데이터
	 * @throws Exception
	 */
	@Override
	public String conversion() throws Exception {
		String str = null;
		try {
			InputStream in = getIn();
			ByteArrayOutputStream out = this.out;
			int c = 0;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			str = decompress(out.toByteArray());
		} finally {
			out.close();
		}
		return str;
	}
}
