/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.chart
 *	작성일   : 2016. 5. 21.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.chart;

import java.util.function.Function;

import org.controlsfx.control.RangeSlider;

import com.kyj.fx.commons.utils.FxCollectors;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.BorderPane;

/***************************
 *
 * @author KYJ
 *
 ***************************/

public abstract class RangeSliderLineChartComposite<T> extends BorderPane implements ListChangeListener<T> {

	/**
	 * @최초생성일 2016. 10. 18.
	 */
	private NumberAxis yAxis;

	/**
	 * @최초생성일 2016. 10. 18.
	 */
	private CategoryAxis xAxis;

	public LineChart<String, Number> chart;

	public RangeSlider slider = new RangeSlider();

	//전체 데이터.
	private ObservableList<T> items = FXCollections.observableArrayList();

	private DoubleProperty displaySize = new SimpleDoubleProperty(15d);

	public RangeSliderLineChartComposite() throws Exception {
		yAxis = createNumberAxis();
		xAxis = createCategoryAxis();
		xAxis.setTickLabelsVisible(true);
		chart = new LineChart<>(xAxis, yAxis);
		this.setCenter(chart);
		this.setBottom(slider);

		initialize();
	}

	//	@FXML
	public void initialize() {
		chart.setAnimated(false);

		items.addListener(this);

		slider.setMajorTickUnit(3d);
		slider.setMajorTickUnit(3d);

		slider.setMin(0D);
		slider.setMax(0D);
		slider.setLowValue(0D);
		slider.setHighValue(0D);

		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setBlockIncrement(10);

		this.displaySize.bind(slider.maxProperty());

		slider.lowValueProperty().addListener((oba, oldval, newval) -> {

			if (items.isEmpty())
				return;

			int start = newval.intValue();
			ObservableList<Data<String, Number>> subList = items.stream().skip(start).limit(displaySize.intValue())
					.map(v -> converter.apply(v)).collect(FxCollectors.toObservableList());
			Series<String, Number> chartSeries = createNewSeries("", subList);
			chart.getData().clear();
			chart.getData().add(chartSeries);
		});

		slider.highValueProperty().addListener((oba, oldval, newval) -> {

			if (items.isEmpty())
				return;

			int start = (int) slider.getLowValue();
			int size = Math.abs(start - newval.intValue());
			ObservableList<Data<String, Number>> subList = items.stream().skip(start).limit(size).map(v -> converter.apply(v))
					.collect(FxCollectors.toObservableList());

			chart.getData().clear();
			if (!subList.isEmpty()) {
				Series<String, Number> chartSeries = createNewSeries("", subList);
				chart.getData().add(chartSeries);
			}

		});

	}

	protected NumberAxis createNumberAxis() {
		return new NumberAxis();
	}

	protected CategoryAxis createCategoryAxis() {
		return new CategoryAxis();
	}
	//	public Series<String, Number> createNewSeries() {
	//		return createNewSeries("", FXCollections.observableArrayList());
	//	}

	protected Series<String, Number> createNewSeries(String seriesName, ObservableList<Data<String, Number>> subList) {
		Series<String, Number> series = new Series<>(subList);
		series.setName(seriesName);
		return series;
	}

	@Override
	public void onChanged(javafx.collections.ListChangeListener.Change<? extends T> c) {
		if (c.next()) {

			if (c.wasPermutated() || c.wasUpdated() || c.wasReplaced())
				return;

			int end = 5;

			ObservableList<Data<String, Number>> collect = items.stream().limit(end).map(v -> converter.apply(v)).filter(v -> v != null)
					.collect(FxCollectors.toObservableList());

			Series<String, Number> chartSeries = createNewSeries("", collect);

			//			if (c.wasAdded()) {
			//
			//
			//
			//			}
			//
			//			else if (c.wasRemoved()) {
			//
			//			}

			//			chartSeries.getData().addAll(collect);
			chart.getData().clear();
			chart.getData().add(chartSeries);

			int size = this.items.size();
			if (size != 0) {
				this.slider.setMax(size - 1);
				this.slider.setHighValue(size - 1);
			}

		}

	}

	private Function<T, Data<String, Number>> converter = new Function<T, XYChart.Data<String, Number>>() {

		@Override
		public Data<String, Number> apply(T t) {
			return converter(t);
		}
	};

	public abstract Data<String, Number> converter(T t);

	public ObservableList<T> getItems() {
		return this.items;
	}

	public void setSliderSize(Double size) {
		displaySize.set(size);
	}
}
