/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.chart
 *	작성일   : 2016. 5. 21.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.chart;

import java.util.function.Function;
import java.util.stream.Collectors;

import com.kyj.fx.commons.utils.FxCollectors;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.fxloader.FXMLController;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

/***************************
 *
 * @author KYJ
 *
 ***************************/
@FXMLController(value = "SliderLineChartView.fxml", isSelfController = true)
public abstract class SliderLineChartComposite<T> extends BorderPane implements ListChangeListener<T> {

	@FXML
	public LineChart<String, Number> chart;

	@FXML
	public Slider slider;

	//전체 데이터.
	private ObservableList<T> items = FXCollections.observableArrayList();

	private DoubleProperty displaySize = new SimpleDoubleProperty(15d);

	public SliderLineChartComposite() throws Exception {
		FxUtil.loadRoot(SliderLineChartComposite.class, this);

	}

	@FXML
	public void initialize() {
		items.addListener(this);
		//		chart.getData().add(chartSeries);

		slider.setMin(0D);
		slider.setMax(displaySize.get());
		this.displaySize.bind(slider.maxProperty());

		slider.valueProperty().addListener((oba, oldval, newval) -> {

			int start = newval.intValue();
			int end = (int) (start + displaySize.get());
			if (end > items.size())
				end = items.size();
			ObservableList<Data<String, Number>> subList = items.stream().skip(start).limit(5).map(v -> converter.apply(v))
					.collect(FxCollectors.toObservableList());
			Series<String, Number> chartSeries = new Series<>("", subList);
			chart.getData().remove(0);
			chart.getData().add(chartSeries);

		});
	}

	@Override
	public void onChanged(javafx.collections.ListChangeListener.Change<? extends T> c) {
		if (c.next()) {

			if (c.wasAdded()) {
				int start = (int) slider.getValue();
				int end = (int) (start + displaySize.get());
				Series<String, Number> chartSeries = new Series<>();
				chartSeries.getData().addAll(items.subList(start, end).stream().map(v -> converter.apply(v)).collect(Collectors.toList()));
				chart.getData().add(chartSeries);
			}

			else if (c.wasRemoved()) {

			}
			this.slider.setMax(items.size());
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
