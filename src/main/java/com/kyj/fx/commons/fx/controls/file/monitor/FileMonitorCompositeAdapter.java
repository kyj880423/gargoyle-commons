/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.file.monitor
 *	작성일   : 2018. 8. 3.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.file.monitor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.fx.controls.CloseableParent;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FxLoader;

import javafx.scene.layout.BorderPane;

/**
 * @author KYJ
 *
 */

public class FileMonitorCompositeAdapter extends CloseableParent<BorderPane> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileMonitorCompositeAdapter.class);
	private FileMonitorComposite loadRoot;

	public FileMonitorCompositeAdapter(BorderPane parent) {
		super(new BorderPane());

		loadRoot = FxLoader.loadRoot(FileMonitorComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
		getParent().setCenter(loadRoot);
	}

	@Override
	public void close() throws IOException {
		loadRoot.close();
	}

}
