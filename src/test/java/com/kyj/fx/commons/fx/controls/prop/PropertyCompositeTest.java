package com.kyj.fx.commons.fx.controls.prop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PropertyCompositeTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * @author KYJ (callakrsos@naver.com)
	 *
	 */
	public static class PropertyCompositeTestModel {
		private String name;
		private int age;
		private String sex;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the age
		 */
		public int getAge() {
			return age;
		}

		/**
		 * @param age
		 *            the age to set
		 */
		public void setAge(int age) {
			this.age = age;
		}

		/**
		 * @return the sex
		 */
		public String getSex() {
			return sex;
		}

		/**
		 * @param sex
		 *            the sex to set
		 */
		public void setSex(String sex) {
			this.sex = sex;
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		PropertyComposite<PropertyCompositeTestModel> p = new PropertyComposite<>(PropertyCompositeTestModel.class);
		root.setCenter(p);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

		PropertyCompositeTestModel item = new PropertyCompositeTestModel();
		item.setName("KYJ");
		item.setAge(30);
		item.setSex("Man");
		p.setItem(item);

	}

}
