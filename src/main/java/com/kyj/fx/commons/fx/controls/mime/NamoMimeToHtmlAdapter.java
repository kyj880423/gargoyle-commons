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
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.util.ByteSequence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.ValueUtil;

/**
 * @author KYJ
 *
 */
public class NamoMimeToHtmlAdapter extends AbstractMimeAdapter {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NamoMimeToHtmlAdapter.class);

	private byte[] content;

	public NamoMimeToHtmlAdapter(String content) {
		this(content.getBytes());
	}

	public NamoMimeToHtmlAdapter(byte[] content) {
		super(null);
		this.content = content;
	}

	private Map<String, LinkedTag> binaryMap = new HashMap<>();

	private class LinkedTag {

		String mimeType;
		String contentId;
		String val;

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
							String element = new String(decode);
							sb.append(element);

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

					Header header = bBody.getParent().getHeader();
					Field fContentId = header.getField("Content-ID");

					ByteSequence raw = fContentId.getRaw();
					String key = fContentId.getName();
					String val = new String(raw.toByteArray());
					String contentId = val.replaceAll(key + "[ ]{0,}:", "").trim();
					contentId = "cid:" + contentId.replace("<", "").replace(">", "");

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

					if (ValueUtil.isNotEmpty(key)) {
						LinkedTag linkedTag = new LinkedTag();
						linkedTag.contentId = contentId;
						linkedTag.mimeType = mimeType;
						linkedTag.val = mimeValue;
						binaryMap.put(contentId, linkedTag);
						// System.out.println();
					}

					// else
					// {
					// String imgeformat = String.format("<image
					// src=\"data:%s/;%s,%s \"> </image> ", mimeType, "base64",
					// mimeValue); /* */
					// sb.append(imgeformat);
					// }

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

		} catch (IOException e) {
			e.printStackTrace();
		}

		sb.append("</html>");

		Document parse = Jsoup.parse(sb.toString());
		Elements select = parse.select("img");
		for (Element e : select) {
			Tag tag = e.tag();
			// ex cid:EWXXYKENUIXH@namo.co.kr
			String id = e.attr("src");
			if (binaryMap.containsKey(id)) {
				LinkedTag tagdata = binaryMap.get(id);
				e.attr("src", String.format("data:%s/;%s,%s", tagdata.mimeType, "base64", tagdata.val));
			}
		}
		return parse.html();
		// String attr = select.attr("src");

		// return sb.toString();
	}

	private Body to(Entity body) {
		return body.getBody();
	}

}
