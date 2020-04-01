/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.code.behavior.design
 *	작성일   : 2018. 6. 19.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.behavior.design;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * @author KYJ
 *
 */
public class GroupPane extends AnchorPane {
	public GroupPane() {
		setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	}
}
