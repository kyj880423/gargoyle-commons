/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.mail
 *	작성일   : 2017. 10. 10.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.mail;

import java.io.Closeable;
import java.io.IOException;

import com.kyj.fx.commons.fx.controls.CloseableParent;

import javafx.scene.layout.BorderPane;

/**
 * @author KYJ
 *
 */
public class MailViewCompositeWrapper extends CloseableParent<BorderPane> {

	public static String getName() {
		return MailViewCompositeWrapper.class.getSimpleName();
	}

	public MailViewCompositeWrapper() {
		super(new MailViewComposite());
	}

	public MailViewCompositeWrapper(String initCont) {
		super(new MailViewComposite(initCont));
	}

	public void setText(String content) {
		MailViewComposite composite = (MailViewComposite) getParent();
		composite.setText(content);
	}

	@Override
	public void close() throws IOException {
		if (getParent() instanceof Closeable) {
			Closeable c = (Closeable) getParent();
			c.close();
		}
	}

}
