/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.color
 *	작성일   : 2018. 10. 8.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.color;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.fxloader.FXMLController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
@FXMLController(value = "LinearGradientPickerView.fxml", isSelfController = true)
public class LinearGradientPickerComposite extends HBox {

	private static final Logger LOGGER = LoggerFactory.getLogger(LinearGradientPickerComposite.class);
	@FXML
	private Rectangle rec;

	public LinearGradientPickerComposite() {
		FxUtil.loadRoot(LinearGradientPickerComposite.class, this, err -> {
			LOGGER.error(ValueUtil.toString(err));
		});
	}

	@FXML
	public void initialize() {

		int SIZE = 255 * 6;
		int step = 0;
		List<Stop> stops = new ArrayList<>(SIZE);
		int initR = 255;
		int initG = 0;
		int initB = 0;

		// G 를 올림.
		for (int r = initG; r < 255; r++) {
			stops.add(new Stop((double) step / (double) SIZE, Color.rgb(initR, initG, initB)));
			initG = r;
			step++;
		}
		// R 을 내림.
		for (int r = initG; r > 0; r--) {
			stops.add(new Stop((double) step / (double) SIZE, Color.rgb(r, initG, initB)));
			initR = r;
			step++;
		}
		// B를 올림.
		for (int r = initB; r < 255; r++) {
			stops.add(new Stop((double) step / (double) SIZE, Color.rgb(initR, initG, r)));
			initB = r;
			step++;
		}
		// G를 내림
		for (int r = initG; r > 0; r--) {
			stops.add(new Stop((double) step / (double) SIZE, Color.rgb(initR, r, initB)));
			initG = r;
			step++;
		}
		// R을 올림
		for (int r = initR; r < 255; r++) {
			stops.add(new Stop((double) step / (double) SIZE, Color.rgb(r, initG, initB)));
			initR = r;
			step++;
		}
		// B를 내림
		for (int r = initB; r > 0; r--) {
			Stop e = new Stop((double) step / (double) SIZE, Color.rgb(initR, initG, r));
			stops.add(e);
			initB = r;
			step++;
		}
		LinearGradient lg2 = new LinearGradient(0, 0, 1000, 200, false, CycleMethod.NO_CYCLE, stops);
		rec.setFill(lg2);

		rec.setOnMouseClicked(ev -> {
			double x = ev.getX();
			double y = ev.getY();

			Node intersectedNode = ev.getPickResult().getIntersectedNode();

		});
	}

}
