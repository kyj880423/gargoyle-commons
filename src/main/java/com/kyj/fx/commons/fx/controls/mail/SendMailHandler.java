/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.mail
 *	작성일   : 2017. 10. 17.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mail;

import java.util.Collections;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.mail.Mail;
import com.kyj.fx.commons.utils.ExceptionHandler;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.MailUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.concurrent.Task;

/**
 * @author KYJ
 *
 */
public class SendMailHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendMailHandler.class);
	private MailViewComposite mailComposite;

	private ExceptionHandler errorHandler = ex -> {
		LOGGER.error(ValueUtil.toString(ex));
	};

	private Consumer<String> onSuccessHandler = id -> {

	};

	public SendMailHandler(MailViewComposite mailComposite) {
		this.mailComposite = mailComposite;
	}

	public ExceptionHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(ExceptionHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public Consumer<String> getOnSuccessHandler() {
		return onSuccessHandler;
	}

	public void setOnSuccessHandler(Consumer<String> onSuccessHandler) {
		this.onSuccessHandler = onSuccessHandler;
	}

	/**
	 * 메일보기
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 10. 17.
	 */
	public void sendMail() {

		FxUtil.showLoading(new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				Mail mail = mailComposite.getMail();

				try {
					/* String id = */MailUtil.sendMail(mail, Collections.emptyMap());
					onSuccessHandler.accept(/* id */ "");
				} catch (Exception e) {
					if (errorHandler != null)
						errorHandler.handle(e);
				}

				return null;
			}
		});

	}
}
