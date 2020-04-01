/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.chart
 *	작성일   : 2018. 2. 20.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.chart;

import java.util.function.Consumer;

import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;

/**
 * @author KYJ
 *
 */
public class AttachedTextValueLineChart<X, Y> extends LineChart<X, Y> {

	public AttachedTextValueLineChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
		super(xAxis, yAxis);
	}

	/**
	 * Construct a new LineChart with the given axis and data.
	 *
	 * @param xAxis
	 *            The x axis to use
	 * @param yAxis
	 *            The y axis to use
	 * @param data
	 *            The data to use, this is the actual list used so any changes
	 *            to it will be reflected in the chart
	 */
	public AttachedTextValueLineChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis,
			@NamedArg("data") ObservableList<Series<X, Y>> data) {
		super(xAxis, yAxis, data);
	}

	private Consumer<Node> custom;

	public void setNodeUpdate(Consumer<Node> custom) {
		this.custom = custom;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.scene.chart.LineChart#layoutPlotChildren()
	 */
	@Override
	protected void layoutPlotChildren() {

		if (custom != null) {
			getData().forEach(d -> {
				d.getData().forEach(x -> {

					Node node = x.getNode();
					custom.accept(node);

				});
			});
		}

		super.layoutPlotChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.scene.chart.XYChart#getPlotChildren()
	 */
	@Override
	protected ObservableList<Node> getPlotChildren() {
		// TODO Auto-generated method stub
		return super.getPlotChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.scene.chart.Chart#layoutChildren()
	 */
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
	}

}
