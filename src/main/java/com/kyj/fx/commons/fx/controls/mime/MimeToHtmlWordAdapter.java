/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.word
 *	작성일   : 2016. 12. 27.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.message.DefaultMessageBuilder;

import com.kyj.fx.commons.utils.DateUtil;
import com.kyj.fx.commons.word.msword.core.MSWord;

/**
 * @author KYJ
 *
 */
public class MimeToHtmlWordAdapter extends AbstractMimeAdapter {

	private byte[] content;

	/**
	 * @param mimeFile
	 */
	private MimeToHtmlWordAdapter() {
		super(null);
	}

	public MimeToHtmlWordAdapter(String content) {
		this(content.getBytes());
	}

	public MimeToHtmlWordAdapter(byte[] content) {
		super(null);
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.word.AbstractMimeAdapter#getContent(
	 * )
	 */
	@Override
	public String getContent() {
		StringBuilder sb = new StringBuilder();
		try {
			final MessageBuilder builder = new DefaultMessageBuilder();
			Message message = builder.parseMessage(new ByteArrayInputStream(this.content));
			Body body = ((Entity) message).getBody();

			append(sb, body);
		} catch (MimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.word.AbstractMimeAdapter#tmpFileName
	 * ()
	 */
	@Override
	protected String tmpFileName() {
		String currentDateString = DateUtil.getCurrentDateString(DateUtil.SYSTEM_DATEFORMAT_YYYYMMDDHHMMSSS);
		String simpleTemplFileName = String.format("_%s.docx", currentDateString);
		return simpleTemplFileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.framework.word.AbstractMimeAdapter#toTempFile(
	 * )
	 */
	@Override
	public File toTempFile() {
		String content = getContent();

		MSWord msWord = new MSWord();
		msWord.addText(content);

		File mimeFile = createNewTempFile();

		try {
			msWord.close(mimeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mimeFile;
	}

	/***************************************************************************/
	/* sb에 번역된 텍스트데이터를 덧붙임 */
	/***************************************************************************/
	private void append(StringBuilder sb, Body body) {
		if (body instanceof TextBody) {
			/*
			 * A text body. Display its contents.
			 */
			TextBody textBody = (TextBody) body;
			try {
				Reader r = textBody.getReader();
				int c;
				while ((c = r.read()) != -1) {
					sb.append((char) c);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		} else if (body instanceof BinaryBody) {
			BinaryBody bBody = (BinaryBody) body;
			append(sb, bBody);
		} else if (body instanceof Multipart) {
			Multipart mbody = (Multipart) body;
			for (Entity part : mbody.getBodyParts()) {
				append(sb, part);
			}
		}

		/*
		 * Ignore Fields </br>
		 * 
		 * ContentTypeField,AddressListField,DateTimeField UnstructuredField,
		 * Field
		 * 
		 */
		else {
			sb.append(body.toString());
		}
	}

	private void append(StringBuilder sb, Entity body) {
		append(sb, body.getBody());
	}
}
