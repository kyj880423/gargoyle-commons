/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.word
 *	작성일   : 2016. 12. 27.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mime;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ
 *
 */
public class MimeToHtmlAdapter extends AbstractMimeAdapter {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MimeToHtmlAdapter.class);

	private byte[] content;

	/**
	 * @param mimeFile
	 */
	private MimeToHtmlAdapter() {
		super(null);
	}

	public MimeToHtmlAdapter(String content, String charset) throws UnsupportedEncodingException {
		this(content.getBytes(charset));
	}

	public MimeToHtmlAdapter(String content) throws UnsupportedEncodingException {
		this(content, "UTF-8");
	}

	public MimeToHtmlAdapter(byte[] content) {
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

			ByteArrayInputStream instream = new ByteArrayInputStream(this.content);

			sb.append("<html>");
			final DefaultMessageBuilder builder = new DefaultMessageBuilder();
			builder.setContentDecoding(false);
			/*
			 * 2017-06-14 Maximum line length limit exceeded 이라는 에러가 발생하는 케이스가
			 * 존재함. 데이터가 너무 크면 파싱이 불가능? 어떻게 해결하지? -> html 문자 섞인경우 파싱할때 문제가 발생
			 * Mime값은 컬럼 너비 길이가 일정한 데이터여야 정상적인 데이터임
			 */
			Message message = builder.parseMessage(instream);
			Body body = ((Entity) message).getBody();

			Queue<? super Body> queue = new LinkedList<>();
			queue.add(body);

			while (!queue.isEmpty()) {
				Object poll = queue.poll();
				if (poll instanceof TextBody) {
					TextBody textBody = (TextBody) poll;
					try {
						InputStream is = textBody.getInputStream();
						String charset = textBody.getMimeCharset();
						Charset forName = Charset.forName("UTF-8");
						if (ValueUtil.isNotEmpty(charset)) {
							forName = Charset.forName(charset);
						} else {
							forName = Charset.forName(charset);
						}
						StringBuilder buffer = new StringBuilder();
						try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, forName))) {
							sb.append("<div>");
							String temp = null;
							while ((temp = reader.readLine()) != null) {
								buffer.append(temp);
							}
							byte[] decode = Base64.getMimeDecoder().decode(buffer.toString());
							String string = new String(decode);
							sb.append(string);
							sb.append("</div>");
						}

					} catch (IOException ex) {
						ex.printStackTrace();
					}

				} else if (poll instanceof BinaryBody) {
					BinaryBody bBody = (BinaryBody) poll;

					// example : image/jpeg , image/png
					String mimeType = bBody.getParent().getMimeType();
					// String contentTransferEncoding =
					// bBody.getParent().getContentTransferEncoding();

					String charset = bBody.getParent().getCharset();
					String mimeValue = "";
					if (ValueUtil.isNotEmpty(charset)) {
						Charset forName = Charset.forName(charset);
						// ByteArrayOutputStream out = new
						// ByteArrayOutputStream();
						// bBody.writeTo(out);
						InputStream is = bBody.getInputStream();
						BufferedReader r = new BufferedReader(new InputStreamReader(is, forName));
						StringBuilder buffer = new StringBuilder();
						String temp = null;
						while ((temp = r.readLine()) != null) {
							buffer.append(temp);
						}
						// char[] charArray =
						// IOUtils.toCharArray(bBody.getInputStream(), forName);
						// String string = new String(charArray);
						// mimeValue = new
						// String(IOUtils.toByteArray(bBody.getInputStream()),
						// charset);
						// mimeValue =
						// ValueUtil.toString(bBody.getInputStream(), forName);
						mimeValue = buffer.toString();
						// mimeValue = out.toString();
						// mimeValue = out.toString(charset);
					} else {
						mimeValue = ValueUtil.toString(bBody.getInputStream());
					}

					String imgeformat = String.format("<image src=\"data:%s/;%s,%s \"> </image> ", mimeType, "base64", mimeValue); /*   */
					sb.append(imgeformat);
				} else if (poll instanceof Multipart) {
					Multipart mbody = (Multipart) poll;
					for (Entity part : mbody.getBodyParts()) {
						Body bBody = to(part);
						queue.add(bBody);
					}
				}

				/*
				 * Ignore Fields </br>
				 * 
				 * ContentTypeField,AddressListField,DateTimeField
				 * UnstructuredField, Field
				 * 
				 */
				else {
					LOGGER.debug("{}", body);
					sb.append(body.toString());
				}
			}

			// append(sb, body);

		} catch (IOException e) {
			e.printStackTrace();
		}

		sb.append("</html>");
		return sb.toString();
	}

	/***************************************************************************/
	/* sb에 번역된 텍스트데이터를 덧붙임 */
	/***************************************************************************/
	// private void append(StringBuilder sb, Body body) {
	// if (body instanceof TextBody) {
	// /*
	// * A text body. Display its contents.
	// */
	// TextBody textBody = (TextBody) body;
	// try {
	// Reader r = textBody.getReader();
	// int c;
	// while ((c = r.read()) != -1) {
	// sb.append((char) c);
	// }
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	//
	// } else if (body instanceof BinaryBody) {
	// BinaryBody bBody = (BinaryBody) body;
	// append(sb, bBody);
	// } else if (body instanceof Multipart) {
	// Multipart mbody = (Multipart) body;
	// for (Entity part : mbody.getBodyParts()) {
	// Body bBody = to(part);
	// append(sb, part);
	// }
	// }
	//
	// /*
	// * Ignore Fields </br>
	// *
	// * ContentTypeField,AddressListField,DateTimeField UnstructuredField,
	// * Field
	// *
	// */
	// else {
	// LOGGER.debug("{}" , body);
	// sb.append(body.toString());
	// }
	// }

	private Body to(Entity body) {
		return body.getBody();
	}

	// static class CustomMessageBuilder extends DefaultMessageBuilder {
	//
	// public Header parseHeader(final InputStream is) throws IOException,
	// MimeIOException {
	// // final MimeConfig cfg = config != null ? config : new MimeConfig();
	// final MimeConfig cfg = new MimeConfig();
	// boolean strict = cfg.isStrictParsing();
	//
	// // final DecodeMonitor mon = monitor != null ? monitor :
	// // strict ? DecodeMonitor.STRICT : DecodeMonitor.SILENT;
	//
	// final DecodeMonitor mon = DecodeMonitor.SILENT;
	//
	// // final FieldParser<? extends ParsedField> fp = fieldParser != null ?
	// fieldParser :
	// // strict ? DefaultFieldParser.getParser() :
	// LenientFieldParser.getParser();
	//
	// final FieldParser<? extends ParsedField> fp =
	// LenientFieldParser.getParser();
	//
	// final HeaderImpl header = new HeaderImpl();
	// final MimeStreamParser parser = new MimeStreamParser();
	// parser.setContentHandler(new AbstractContentHandler() {
	// @Override
	// public void endHeader() {
	// parser.stop();
	// }
	//
	// @Override
	// public void field(Field field) throws MimeException {
	// ParsedField parsedField;
	// if (field instanceof ParsedField) {
	// parsedField = (ParsedField) field;
	// } else {
	// parsedField = fp.parse(field, mon);
	// }
	// header.addField(parsedField);
	// }
	// });
	// try {
	// parser.parse(is);
	// } catch (MimeException ex) {
	// throw new MimeIOException(ex);
	// }
	// return header;
	// }
	// }
	//
	// static class CustomMimeStreamParser extends MimeStreamParser {
	// public CustomMimeStreamParser() {
	// super(new MimeTokenStream(new MimeConfig(), null, null));
	// }
	// }

}
