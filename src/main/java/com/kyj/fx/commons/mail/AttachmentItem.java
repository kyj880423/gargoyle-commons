/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.mail
 *	작성일   : 2017. 10. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.mail;

import java.io.File;

import javax.activation.DataSource;

/**
 * 
 * 메일 전송시 첨부파일 정보
 * 
 * @author KYJ
 *
 */
public class AttachmentItem {

	/**
	 * 첨부파일이름
	 * 
	 * @최초생성일 2017. 10. 14.
	 */
	private String displayName;

	/**
	 * 첨부파일
	 * 
	 * @최초생성일 2017. 10. 14.
	 */
	private DataSource dataSource;

	public AttachmentItem(String displayName, DataSource dataSource) {
		super();
		this.displayName = displayName;
		this.dataSource = dataSource;
	}

	public final String getDisplayName() {
		return displayName;
	}

	public final void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public final DataSource getDataSource() {
		return dataSource;
	}

	public final void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 14.
	 * @param displayName
	 * @param attachmentItem
	 * @return
	 */
	public static AttachmentItem createFileAttachmentItem(String displayName, File attachmentItem) {
		return new FileAttachmentItem(displayName, attachmentItem);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 14.
	 * @param attachmentItem
	 * @return
	 */
	public static AttachmentItem createFileAttachmentItem(File attachmentItem) {
		return new FileAttachmentItem(attachmentItem.getName(), attachmentItem);
	}
}
