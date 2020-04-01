/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.chart
 *	작성일   : 2016. 10. 4.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.chart;

import java.util.Optional;
import java.util.function.BiConsumer;

import com.kyj.fx.commons.utils.FxUtil;
import com.sun.javafx.charts.Legend;

import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

/**
 * javafx에서 제공되지않는 기능에 대한 처리를 도와주는 PieChart
 *
 * 라벨링.
 * @author KYJ
 *
 */
/**
 * @author KYJ
 *
 */
public class AttachedTextValuePieChart extends PieChart {

	public AttachedTextValuePieChart() {

	}

	public Legend getChartLegend() {
		return (Legend) super.getLegend();
	}

	/**
	 * textlabel 텍스쳐 처리
	 * 
	 * @최초생성일 2016. 10. 4.
	 */
	private StringConverter<Data> labelConverter;

	/**
	 * 그래프에서 툴팁 처리
	 * 
	 * @최초생성일 2016. 10. 4.
	 */
	private StringConverter<Data> tooltipConverter;

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param labelConverter
	 */
	public void setLabelConverter(StringConverter<Data> labelConverter) {
		this.labelConverter = labelConverter;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param tooltipConverter
	 */
	public void setTooltipConverter(StringConverter<Data> tooltipConverter) {
		this.tooltipConverter = tooltipConverter;
	}

	private BiConsumer<Data, Node> customAction;

	/**
	 * 사용자 특화 Action처리 지원
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 19.
	 * @param customAction
	 */
	public void setChartGraphicsCustomAction(BiConsumer<Data, Node> customAction) {
		this.customAction = customAction;
	}

	/* (non-Javadoc)
	 * @see javafx.scene.chart.PieChart#layoutChartChildren(double, double, double, double)
	 */
	@Override
	protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
		if (getLabelsVisible()) {

			getData().forEach(d -> {

				if (tooltipConverter != null || customAction != null) {
					d.getNode().lookupAll(".chart-pie").stream().forEach(v -> {

						if (tooltipConverter != null)
							FxUtil.installTooltip(v, tooltipConverter.toString(d));

						if (customAction != null) {
							customAction.accept(d, v);
						}
					});
				}

				//				System.out.println(d.getNode().lookupAll(".chart-legend-item"));

				if (labelConverter != null) {
					Optional<Node> opTextNode = this.lookupAll(".chart-pie-label").stream()
							.filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName())).findAny();
					if (opTextNode.isPresent()) {
						Text text = (Text) opTextNode.get();
						text.setText(labelConverter.toString(d));
					}
				}
			});

			//			Legend legend = (Legend) getLegend();
			//			legend.getItems().forEach(item->{
			//				
			//				item.
			//				
			//			});
			//			if (isLegendVisible() && seriesLegendLabelCustomAction != null) {
			//				this.lookupAll(".chart-legend-item").stream().forEach(v -> {
			//					System.out.println(v);
			//				});
			//			}

		}
		super.layoutChartChildren(top, left, contentWidth, contentHeight);

	}

}
