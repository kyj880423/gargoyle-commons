/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.mail
 *	작성일   : 2017. 10. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.mail;

import java.io.File;

import javax.activation.FileDataSource;

/**
 * 메일 -파일 첨부
 * 
 * @author KYJ
 *
 */
public class FileAttachmentItem extends AttachmentItem {

	public FileAttachmentItem(String displayName, File attachmentItem) {
		super(displayName, new FileDataSource(attachmentItem));
	}

}
