/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 4. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.table.TableFilter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import com.kyj.fx.commons.excel.IExcelScreenHandler;
import com.kyj.fx.commons.exceptions.GargoyleException;
import com.kyj.fx.commons.functions.FxXmlSupplier;
import com.kyj.fx.commons.fx.controls.CloseableParent;
import com.kyj.fx.commons.fx.controls.bar.GargoyleLoadBar;
import com.kyj.fx.commons.fx.controls.bar.GargoyleSynchLoadBar;
import com.kyj.fx.commons.fx.controls.console.WebViewConsole;
import com.kyj.fx.commons.fx.controls.dock.pane.DockNode;
import com.kyj.fx.commons.fx.controls.grid.AbstractDVO;
import com.kyj.fx.commons.fx.controls.grid.AnnotationOptions;
import com.kyj.fx.commons.fx.controls.grid.IOptions;
import com.kyj.fx.commons.fx.controls.notify.GargoyleNotification;
import com.kyj.fx.commons.fx.controls.text.JavaTextArea;
import com.kyj.fx.commons.fx.controls.text.JavaTextView;
import com.kyj.fx.commons.fx.controls.text.SimpleTextView;
import com.kyj.fx.commons.fx.controls.text.XMLEditor;
import com.kyj.fx.commons.memory.StageStore;
import com.kyj.fx.commons.utils.ValueUtil.CodeType;
import com.kyj.fx.commons.utils.ValueUtil.IndexCaseTypes;
import com.kyj.fx.fxloader.FxLoader;
import com.kyj.fx.fxloader.GargoyleBuilderFactory;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotResult;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;

/**
 * Javafx UI 관련 유틸리티 클래스
 *
 * @author KYJ
 *
 */
/***************************
 *
 * @author KYJ
 *
 ***************************/
public class FxUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FxUtil.class);

	/**
	 * @author KYJ
	 *
	 */
	public static class ContextUtil {

		/**
		 * Create MenuItem Excel Export. <br/>
		 * 
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2019. 9. 17.
		 * @param <T>
		 * @param target
		 * @return
		 */
		public static <T> MenuItem createExcelExportMenuItem(TableView<T> target) {
			return FxTableViewUtil.EasyMenuItem.createExcelExportMenuItem(target);
		}

		/**
		 * XML 관련 컨텍스트 유틸 <br/>
		 * 
		 * @작성자 : KYJ
		 * @작성일 : 2017. 12. 5.
		 * @param parent
		 * @return
		 */
		public static ContextMenu createXmlContextMenu(FxXmlSupplier parent) {
			ContextMenu cm = new ContextMenu();
			cm.getItems().add(createXmlMenuItem(parent));
			return cm;
		}

		public static MenuItem createXmlMenuItem(FxXmlSupplier parent) {

			MenuItem miXpath = new MenuItem("Xpath");

			miXpath.setOnAction(ev -> {

				Optional<Pair<String, String>> showInputDialog = DialogUtil.showInputDialog(parent.getNode(), "Xpath", "Input XPath");
				showInputDialog.ifPresent(p -> {

					String value = p.getValue();
					if (ValueUtil.isNotEmpty(value)) {
						Optional<NodeList> xpathNodes = XMLUtils.toXpathNodes(parent.getXml(), value, err -> {
							FxUtil.createCodeAreaAndShow(String.format("Input XPath : %s\n\n\n%s", value, ValueUtil.toString(err)));
						});
						// Optional<String> xpathText =
						// XMLUtils.toXpathText();
						xpathNodes.ifPresent(r -> {
							int length = r.getLength();

							StringBuffer sb = new StringBuffer();
							for (int i = 0; i < length; i++) {
								org.w3c.dom.Node item = r.item(i);
								sb.append(item.toString()).append("\n");
							}
							FxUtil.createCodeAreaAndShow(String.format("Input XPath : %s\n\n\n%s", value, sb.toString()));

						});

					}
				});

			});

			return miXpath;

		}
	}

	/**
	 * 편의성 처리를 위해 완성코드 리턴.
	 *
	 * @author KYJ
	 *
	 */
	/**
	 * @author KYJ (callakrsos@naver.com)
	 *
	 */
	public static class EasyFxUtils {

		public static void showApplicationCode(String sql, Function<String, String> convert) {
			showApplicationCode(CodeType.JAVA_BUFFER, sql, convert);
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 11. 14.
		 * @param codeType
		 * @param sql
		 * @param convert
		 */
		public static void showApplicationCode(CodeType codeType, String sql, Function<String, String> convert) {
			createStageAndShow(new JavaTextView(getAppliationCode(codeType, sql, convert)), stage -> {
				stage.setWidth(1200d);
				stage.setHeight(800d);
			});

		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 11. 14.
		 * @param codeType
		 * @param sql
		 * @return
		 */
		public static String getAppliationCode(CodeType codeType, String sql) {
			return getAppliationCode(codeType, sql, smartDoubleDotConvert);
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 11. 14.
		 * @param codeType
		 * @param sql
		 * @param convert
		 * @return
		 */
		public static String getAppliationCode(CodeType codeType, String sql, Function<String, String> convert) {
			String[] split = sql.split("\n");
			StringBuilder sb = new StringBuilder();
			switch (codeType) {
			case JAVA_BUFFER:
				sb.append("StringBuffer sb = new StringBuffer();\n");
				for (String str : split) {
					sb.append("sb.append(\"").append(convert.apply(str)).append("\\n").append("\");\n");
				}
				sb.append("sb.toString();");
				break;
			case JAVA_LIST:
				sb.append("List<String> list = new java.util.ArrayList<String>();\n");
				for (String str : split) {
					sb.append("list.add(\"").append(convert.apply(str)).append("\\n").append("\");\n");
				}
				break;
			case DOT_NET:
				sb.append("StringBuilder sb = new StringBuilder();\n");
				for (String str : split) {
					sb.append("sb.Append(\"").append(convert.apply(str)).append("\\n").append("\");\n");
				}
				sb.append("sb.ToString();");
				break;
			case Nashorn:
				sb.append("var sb = new Packages.java.lang.StringBuilder();\n");
				for (String str : split) {
					sb.append("sb.append(\"").append(convert.apply(str)).append("\\n").append("\");\n");
				}
				sb.append("sb.ToString();");
				break;

			case VBS:
				sb.append("dim sb \n");
				sb.append("sb = \"\" & _ \n");

				for (int i = 0, max = split.length; i < max; i++) {
					String str = split[i];
					sb.append(" \" ").append(convert.apply(str)).append(" \" ");
					if (i != (max - 1))
						sb.append("   & _\n");
				}

				break;

			case JAVASCRIPT:
				sb.append("var sb \n");
				sb.append("sb = \"\"  \n");

				for (int i = 0, max = split.length; i < max; i++) {
					String str = split[i];
					sb.append(" sb +=\" ").append(convert.apply(str)).append(" \" ");
					if (i != (max - 1))
						sb.append("   \n");
				}
				break;
			}

			String string = sb.toString();
			LOGGER.debug(string);
			return string;
		}

		/**
		 * 본문에 double dot(")로 인해 발생되는 이슈를 해결함.
		 *
		 * @최초생성일 2016. 11. 10.
		 */
		private static final Function<String, String> smartDoubleDotConvert = str -> {

			if (str.indexOf("\"") != -1) {
				List<Integer> doubleDots = new ArrayList<>();
				int idx = 0;

				while (idx != -1) {

					int nextIdx = str.indexOf("\"", idx);

					if (nextIdx != -1) {

						/*
						 * ignore index check. character.
						 *
						 * 실제 텍스트 \ 기호가 포함되는 경우는 변환처리대상에서 제외.
						 *
						 * 텍스트에 \" 없이 단순히 "(double dot) 만 포함되는경우는
						 *
						 * \를 포함시킴.
						 */
						boolean b = nextIdx == 0;
						if (b || str.charAt(nextIdx - 1) != '\\') {
							doubleDots.add(nextIdx);
						}
					}

					if (nextIdx == -1)
						break;

					idx = nextIdx + 1;

				}

				if (!doubleDots.isEmpty()) {
					// 찾아낸 문자열들을 다시 재조합한후 리턴.
					StringBuffer sb = new StringBuffer();
					int arrayIdx = doubleDots.size();
					int stringIdx = 0;

					for (int i = 0; i < arrayIdx; i++) {
						int splitIndex = doubleDots.get(i).intValue();
						sb.append(str.substring(stringIdx, splitIndex)).append("\\\"");
						stringIdx = splitIndex + 1;
					}
					sb.append(str.substring(stringIdx));

					str = sb.toString();
				}

			}

			return str;
		};

		/**
		 * 어플리케이션 코드를 만들어주는 팝업을 보여준다. </br>
		 *
		 * @작성자 : KYJ
		 * @작성일 : 2016. 9. 23.
		 * @param sql
		 * @throws IOException
		 */
		public static void showJavaApplicationCode(String sql) {
			showApplicationCode(CodeType.JAVA_BUFFER, sql, smartDoubleDotConvert);
		}

		/**
		 * 입력받은 코드를 ArrayList에 담는 패턴으로 변환후 리턴 <br/>
		 * 
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 11. 23.
		 * @param sql
		 */
		public static void showJavaApplicationListCode(String sql) {
			showApplicationCode(CodeType.JAVA_LIST, sql, smartDoubleDotConvert);
		}

		/**
		 * 어플리케이션 코드를 만들어주는 팝업을 보여준다. </br>
		 * 
		 * @작성자 : KYJ
		 * @작성일 : 2017. 11. 2.
		 * @param sql
		 */
		public static void showApplicationCode(String sql) {
			showApplicationCode(CodeType.JAVA_BUFFER, sql, smartDoubleDotConvert);
		}

		/**
		 * @작성자 : KYJ (callakrsos@naver.com)
		 * @작성일 : 2018. 10. 17.
		 * @param code
		 */
		public static void showNashornCode(String code) {
			showApplicationCode(CodeType.Nashorn, code, smartDoubleDotConvert);
		}

		/**
		 * 어플리케이션 코드를 만들어주는 팝업을 보여준다. </br>
		 * 
		 * @작성자 : KYJ
		 * @작성일 : 2017. 11. 2.
		 * @param sql
		 */
		public static void showDotNetApplicationCode(String sql) {
			showApplicationCode(CodeType.DOT_NET, sql, smartDoubleDotConvert);
		}

		public static void showVbsApplicationCode(String text) {
			showApplicationCode(CodeType.VBS, text, smartDoubleDotConvert);

		}

		public static void showJavascriptApplicationCode(String text) {
			showApplicationCode(CodeType.JAVASCRIPT, text, smartDoubleDotConvert);

		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 26.
	 * @param p
	 * @param filter
	 * @return
	 */
	public static Optional<Node> findFirstNode(Parent p, Predicate<Node> filter) {
		return findFirstNode(p, true, filter);
	}

	/**
	 * Parent에서 filter의 조건에 맞는 노드들을 찾은후 리턴.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 26.
	 * @param p
	 * @param onlyVisible
	 *            visible 속성에 맞는 노드들 리턴
	 * @param filter
	 * @return
	 */
	public static Optional<Node> findFirstNode(Parent p, boolean onlyVisible, Predicate<Node> filter) {
		Stream<Node> flatMap = p.getChildrenUnmodifiable().stream().flatMap(v -> {

			// 화면에 visible true인 대상만.
			if (onlyVisible) {

				if (v.isVisible()) {

					if (filter.test(v)) {
						return Stream.of(v);
					}

					else if (v instanceof TableView) {

						Set<Node> lookupAll = ((TableView) v).lookupAll(".table-cell");
						return lookupAll.stream().map(n -> (TableCell) n).map(cell -> {
							return cell.getGraphic();
						}).filter(n -> filter.test(n));
						// return lookupAll.stream();
						// return Stream.empty();
						// return visibleLeafColumns.stream();
						// return findAllByNodes((Parent) v, onlyVisible,
						// filter).stream();
					} else if (v instanceof TitledPane) {

						Stream<Node> stream = findAllByNodes((Parent) v, onlyVisible, filter).stream();
						Node content = ((TitledPane) v).getContent();
						Stream<Node> stream2 = findAllByNodes((Parent) content, onlyVisible, filter).stream();
						return Stream.concat(stream, stream2);

					} else if (v instanceof Parent) {
						return findAllByNodes((Parent) v, onlyVisible, filter).stream();
					}

				}

			}
			// visible 상관없이 모두 찾음.
			else {

				if (filter.test(v)) {
					return Stream.of(v);
				}

				else if (v instanceof Parent) {
					return findAllByNodes((Parent) v, onlyVisible, filter).stream();
				}
			}

			return Stream.empty();
		});

		return flatMap.findFirst();
	}

	/**
	 * Parent에서 filter의 조건에 맞는 노드들을 찾은후 리턴.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 11.
	 * @param p
	 * @param onlyVisible
	 *            visible 속성에 맞는 노드들 리턴
	 * @param filter
	 * @return
	 */
	public static List<Node> findAllByNodes(Parent p, boolean onlyVisible, Predicate<Node> filter) {
		return p.getChildrenUnmodifiable().stream().flatMap(v -> {

			// 화면에 visible true인 대상만.
			if (onlyVisible) {

				if (v.isVisible()) {

					if (filter.test(v)) {
						return Stream.of(v);
					}

					else if (v instanceof TableView) {

						Set<Node> lookupAll = ((TableView) v).lookupAll(".table-cell");
						return lookupAll.stream().map(n -> (TableCell) n).map(cell -> {
							return cell.getGraphic();
						}).filter(n -> filter.test(n));
						// return lookupAll.stream();
						// return Stream.empty();
						// return visibleLeafColumns.stream();
						// return findAllByNodes((Parent) v, onlyVisible,
						// filter).stream();
					} else if (v instanceof TitledPane) {

						Stream<Node> stream = findAllByNodes((Parent) v, onlyVisible, filter).stream();
						Node content = ((TitledPane) v).getContent();
						Stream<Node> stream2 = findAllByNodes((Parent) content, onlyVisible, filter).stream();
						return Stream.concat(stream, stream2);

					} else if (v instanceof Parent) {
						return findAllByNodes((Parent) v, onlyVisible, filter).stream();
					}

				}

			}
			// visible 상관없이 모두 찾음.
			else {

				if (filter.test(v)) {
					return Stream.of(v);
				}

				else if (v instanceof Parent) {
					return findAllByNodes((Parent) v, onlyVisible, filter).stream();
				}
			}

			return Stream.empty();
		}).collect(Collectors.toList());
	}

	/********************************
	 * 작성일 : 2016. 5. 21. 작성자 : KYJ
	 *
	 * FXMLController에 정의된 내용을 기준으로 FXML을 로드한다.
	 *
	 * @param controllerClass
	 * @param instance
	 * @return
	 * @throws GargoyleException
	 * @throws NullPointerException
	 * @throws IOException
	 ********************************/
	public static <T, C> T loadRoot(Class<C> controllerClass, Object instance) throws Exception {
		return FxLoader.load(controllerClass, instance, null, null);
	}

	/********************************
	 * 작성일 : 2016. 5. 21. 작성자 : KYJ
	 *
	 * FXMLController에 정의된 내용을 기준으로 FXML을 로드한다.
	 *
	 * @param controllerClass
	 * @return
	 * @throws GargoyleException
	 * @throws NullPointerException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 ********************************/
	public static <T, C> T loadRoot(Class<C> controllerClass) throws Exception {
		return FxLoader.load(controllerClass, controllerClass.newInstance(), null, null);
	}

	/********************************
	 * 작성일 : 2016. 5. 28. 작성자 : KYJ
	 *
	 * ref loadRoot() method.
	 *
	 * 에러를 뱉지않고 핸들링할 수 있는 파라미터를 받음.
	 *
	 * @param controllerClass
	 * @param errorCallback
	 * @return
	 ********************************/
	private static <T, C> T loadRoot(Class<C> controllerClass, Consumer<Exception> errorCallback) {
		try {
			return FxLoader.load(controllerClass, controllerClass.newInstance(), null, null);
		} catch (Exception e) {
			errorCallback.accept(e);
		}
		return null;
	}

	/********************************
	 * 작성일 : 2016. 5. 29. 작성자 : KYJ
	 *
	 * ref loadRoot() method.
	 *
	 * 에러를 뱉지않고 핸들링할 수 있는 파라미터를 받음.
	 *
	 * @param controllerClass
	 * @param instance
	 * @param errorCallback
	 * @return
	 ********************************/
	public static <T, C> T loadRoot(Class<C> controllerClass, Object instance, Consumer<Exception> errorCallback) {
		try {
			return FxLoader.load(controllerClass, instance, null, null);
		} catch (Exception e) {
			errorCallback.accept(e);
		}
		return null;
	}

	/**
	 * 번거로운 테이블 value 맵핑을 간편하게 하기 위해 만듬. <br/>
	 * ㅠ
	 * 
	 * 조건 : fxml작성된 id와 vo 필드에 xxproperty 명이 일치하는 대상이 조건이며 <br/>
	 * 
	 * 틀린경우는 로그를 출력. <r/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 30.
	 * @param beanClass
	 * @param tb
	 * @param startPrefix
	 *            테이블 컬럼 preffix. <br/>
	 */
	public static <T> void tbAutoMapping(Class<T> beanClass, TableView<T> tb, String startPrefix) {
		tbAutoMapping(beanClass, tb, startPrefix, null);
	}

	/**
	 * 번거로운 테이블 value 맵핑을 간편하게 하기 위해 만듬. <br/>
	 * ㅠ
	 * 
	 * 조건 : fxml작성된 id와 vo 필드에 xxproperty 명이 일치하는 대상이 조건이며 <br/>
	 * 
	 * 틀린경우는 로그를 출력. <r/>
	 * 
	 * 2019.11.13 컬럼에 하위 항목도 존재하는 경우 대상에 추가하도록 수정.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 31.
	 * @param beanClass
	 * @param tb
	 * @param startPrefix
	 * @param filter
	 *            자동으로 지정하지않을 컬럼이 존재한경우
	 */
	public static <T> void tbAutoMapping(Class<T> beanClass, TableView<T> tb, String startPrefix, Predicate<TableColumn<T, ?>> filter) {
		// ObservableList<TableColumn<T, ?>> columns = tb.getColumns();
		LinkedList<TableColumn<T, ?>> columns = new LinkedList<TableColumn<T, ?>>();
		columns.addAll(tb.getColumns());
		while (!columns.isEmpty()) {
			TableColumn<T, ?> col = columns.pop();

			ObservableList<TableColumn<T, ?>> sub = col.getColumns();
			if (sub != null && !sub.isEmpty())
				columns.addAll(sub);

			if (filter != null) {
				if (!filter.test(col)) {
					continue;
				}
			}

			col.setCellValueFactory(callback -> {

				String id = col.getId();
				if (id.startsWith(startPrefix)) {

					String beanProperyName = ValueUtil.getIndexLowercase(id.replace(startPrefix, ""), 0) + "Property";
					boolean existsPropertyMethod = false;

					try {
						Method declaredMethod = null;
						try {
							declaredMethod = beanClass.getDeclaredMethod(beanProperyName);
							existsPropertyMethod = true;
						} catch (NoSuchMethodException e) {
							/* Nothing */}

						if (!existsPropertyMethod) {
							String getterMethod = "get" + ValueUtil.getIndexcase(id.replace(startPrefix, ""), 0, IndexCaseTypes.UPPERCASE);
							try {
								declaredMethod = beanClass.getDeclaredMethod(getterMethod);
							} catch (NoSuchMethodException e) {
								/* Nothing */}
						}

						if (declaredMethod == null)
							throw new RuntimeException("no suitable method found ");

						if (declaredMethod != null) {
							if (!declaredMethod.isAccessible()) {
								declaredMethod.setAccessible(true);
							}
						}

						if (existsPropertyMethod) {
							T value = callback.getValue();
							Object invoke = declaredMethod.invoke(value);
							if (invoke instanceof ObservableValue) {
								return (ObservableValue) invoke;
							}
						}
						// method is null.
						else {
							T value = callback.getValue();
							Object invoke = declaredMethod.invoke(value);
							return (ObservableValue) new ReadOnlyStringWrapper(invoke == null ? "" : invoke.toString());
						}

					} catch (Exception e) {
						LOGGER.error(ValueUtil.toString("fail to bind property. ", e));
					}

				}
				return null;
			});

		}

	}

	/**
	 * 화면에 있는값과 value 사이에 명명규칙에 따라 값을 바인딩한다. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 13.
	 * @param <T>
	 * @param clazz
	 * @param controllerInstance
	 * @param valueInstance
	 * @param textFieldPreffix
	 * @param filter
	 */
	public static <C, T> void textFieldSmartBinding(C controllerInstance, T valueInstance, String textFieldPreffix,
			Predicate<TextField> filter) {
		Field[] declaredFields = controllerInstance.getClass().getDeclaredFields();
		for (Field f : declaredFields) {
			if (!f.isAccessible())
				f.setAccessible(true);

			try {
				Object object = f.get(controllerInstance);
				if (object == null)
					continue;
				if (!(object instanceof TextField)) {
					continue;
				}

				TextField tf = (TextField) object;
				if (!filter.test(tf))
					continue;

				String id = tf.getId();
				if (!id.startsWith(textFieldPreffix))
					continue;

				String field = id.replace(textFieldPreffix, "");
				String fieldName = Character.toLowerCase(field.charAt(0)) + field.substring(1);
				Field declaredField = valueInstance.getClass().getDeclaredField(fieldName);
				if (!declaredField.isAccessible())
					declaredField.setAccessible(true);
				Object value = declaredField.get(valueInstance);
				if (value == null)
					tf.setText("");
				else {
					if (value instanceof ObservableValue) {
						ObservableValue b = (ObservableValue) value;
						if (b.getValue() == null)
							tf.setText("");
						else
							tf.setText(b.getValue().toString());
					} else
						tf.setText(value.toString());

				}

			} catch (Exception e) {
				LOGGER.info(ValueUtil.toString(e));
			}

		}
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 9. 18.
	 * @return
	 */
	public static MouseEvent mouseEventForDummy() {
		return mouseEventForDummy(1);
	}

	public static MouseEvent mouseEventForDummy(int clickCount) {
		return mouseEventForDummy(MouseButton.PRIMARY, clickCount);
	}

	public static MouseEvent mouseEventForDummy(MouseButton button, int clickCount) {
		return new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, button, clickCount, false, false, false, false, false, false, false,
				false, false, false, null);
	}

	/**
	 * 파일로부터 이미지를 그리기 위한 뷰를 반환한다.
	 *
	 * @Date 2015. 10. 14.
	 * @param file
	 * @return
	 * @User KYJ
	 */
	public static ImageView createImageView(InputStream inputStream) {
		Image fxImage = null;
		if (inputStream != null) {
			fxImage = new Image(inputStream);
		} else {
			return new ImageView();
		}
		return new ImageView(fxImage);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 10. 2.
	 * @param base64
	 * @return
	 */
	public static Image createImage(String base64) {
		byte[] decode = Base64.getDecoder().decode(base64);
		return new Image(new ByteArrayInputStream(decode));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 10. 2.
	 * @param base64
	 */
	public static ImageView createImageView(String base64) {
		Image createImage = createImage(base64);
		ImageView iv = new ImageView();
		iv.setImage(createImage);
		return iv;
	}

	public static <T> void sortAll(ObservableList<TreeItem<T>> children, Comparator<TreeItem<T>> compareator) {

		LinkedList<TreeItem<T>> queue = new LinkedList<>(children);

		while (!queue.isEmpty()) {
			TreeItem<T> poll = queue.poll();
			if (poll.getChildren().isEmpty())
				continue;
			poll.getChildren().sort(compareator);

			queue.addAll(poll.getChildren());
		}

	}

	/********************************
	 * 작성일 : 2016. 6. 29. 작성자 : KYJ
	 *
	 * 캡쳐
	 *
	 * out처리가 완료되면 stream은 자동 close처리됨.
	 *
	 * @param target
	 * @param out
	 * @param errorCallback
	 ********************************/
	public static boolean snapShot(Node target, OutputStream out, Consumer<Exception> errorCallback) {
		return snapShot(target, out, -1, -1, errorCallback);
	}

	public static boolean snapShot(Scene target, OutputStream out, Consumer<Exception> errorCallback) {
		return snapShot(target, out, -1, -1, errorCallback);
	}

	public static boolean snapShot(Scene target, OutputStream out, int requestWidth, int requestHeight, Consumer<Exception> errorCallback) {
		if (target == null)
			throw new NullPointerException("target Node is empty.");

		if (out == null)
			throw new NullPointerException("target Stream is empty.");

		SnapshotParameters params = new SnapshotParameters();
		params.setDepthBuffer(true);

		// params.setFill(Color.TRANSPARENT);

		WritableImage wi = null;
		if (requestWidth >= 0 || requestHeight >= 0) {
			wi = new WritableImage(requestWidth, requestHeight);
		}
		boolean isSuccess = false;
		WritableImage snapshot = target.snapshot(wi);
		try {
			isSuccess = snapShot(out, snapshot);
			LOGGER.debug("Write Image result {}", isSuccess);
		} catch (IOException e) {
			errorCallback.accept(e);
		}
		return isSuccess;
	}

	public static boolean snapShot(Node target, OutputStream out, int requestWidth, int requestHeight, Consumer<Exception> errorCallback) {

		if (target == null)
			throw new NullPointerException("target Node is empty.");

		if (out == null)
			throw new NullPointerException("target Stream is empty.");

		SnapshotParameters params = new SnapshotParameters();
		params.setDepthBuffer(true);

		// params.setFill(Color.TRANSPARENT);

		WritableImage wi = null;
		if (requestWidth >= 0 || requestHeight >= 0) {
			wi = new WritableImage(requestWidth, requestHeight);
		}
		boolean isSuccess = false;
		WritableImage snapshot = target.snapshot(params, wi);
		try {
			isSuccess = snapShot(out, snapshot);
			LOGGER.debug("Write Image result {}", isSuccess);
		} catch (IOException e) {
			errorCallback.accept(e);
		}
		return isSuccess;
	}

	/********************************
	 * 작성일 : 2016. 6. 29. 작성자 : KYJ
	 *
	 * 캡쳐 후 stream close처리.
	 *
	 * @param out
	 * @param image
	 * @return
	 * @throws IOException
	 ********************************/
	private static boolean snapShot(OutputStream out, WritableImage image) throws IOException {
		return ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out);
		// if (out != null)
		// out.close();
	}

	/**
	 * XMLController에 정의된 내용을 기준으로 FXML을 로드한다.
	 *
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param controllerClass
	 * @param controllerAction
	 * @return
	 * @throws GargoyleException
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static <N, C> N loadAndControllerAction(Class<C> controllerClass, Consumer<C> controllerAction) throws Exception {
		return FxLoader.load(controllerClass, null, null, controllerAction);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param parent
	 */
	public static void createStageAndShow(Node parent) {
		createStageAndShow(parent.getClass().getSimpleName(), new Scene(new BorderPane(parent)), false);
	}

	public static void createStageAndShow(String title, Node parent) {
		createStageAndShow(title, new Scene(new BorderPane(parent)), false);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param parent
	 * @param isModal
	 */
	public static <N extends Node> void createStageAndShow(String title, N parent, boolean isModal) {
		createStageAndShow(title, new Scene(new BorderPane(parent)), isModal);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 13.
	 * @param content
	 * @param option
	 */
	public static void createCodeAreaAndShow(String content, Consumer<Stage> option) {
		createCodeAreaAndShow(content, c -> {
			c.setParagraphGraphicFactory(LineNumberFactory.get(c));
		}, option);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 13.
	 * @param content
	 * @param add
	 * @param option
	 */
	public static void createCodeAreaAndShow(String content, Consumer<CodeArea> add, Consumer<Stage> option) {
		CodeArea parent = new CodeArea(content);

		if (add != null)
			add.accept(parent);

		createStageAndShow(parent, option);
	}

	public static <N extends Node> void createStageAndShow(String title, N parent, Consumer<Stage> option) {
		createStageAndShow(title, new Scene(new BorderPane(parent)), option);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param parent
	 * @param isModal
	 */
	public static void createStageAndShow(String title, Parent parent, boolean isModal) {
		createStageAndShow(title, new Scene(parent), isModal);
	}

	/**
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 30.
	 * @param parent
	 * @param option
	 * @exception RuntimeException
	 *                Not Support Parent Type.
	 * @return
	 */
	public static Stage createStageAndShow(Object parent, Consumer<Stage> option) {
		if (parent == null)
			throw new RuntimeException("parent can not be null");

		if (parent instanceof Parent) {
			return createStageAndShow((Parent) parent, option);

		} else if (parent instanceof CloseableParent) {
			return createStageAndShow((CloseableParent) parent, option);
		}

		throw new RuntimeException("Not Support Parent Type :  " + parent);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param parent
	 * @param option
	 */
	public static <P extends Parent> Stage createStageAndShow(P parent, Consumer<Stage> option) {

		Scene scene = null;
		boolean createNew = true;
		SingtonPopupStage annotation = parent.getClass().getAnnotation(SingtonPopupStage.class);
		if (annotation != null) {
			boolean singleton = annotation.singleton();
			if (singleton) {
				Scene checkScene = parent.getScene();
				if (checkScene != null) {
					createNew = false;
				}
			}
		}

		if (createNew) {
			scene = new Scene(parent);
		} else
			scene = parent.getScene();

		if (annotation != null) {
			Stage window = (Stage) scene.getWindow();
			if (window != null) {
				window.show();
				return window;
			}
		}

		return createStageAndShow(scene, option);
	}

	public static Stage createStageAndShow(CloseableParent<? extends Parent> cloableParent, Consumer<Stage> option) {

		Stage stage = craeteStage(cloableParent.getParent(), option);
		stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, ev -> {
			try {
				cloableParent.close();
			} catch (Exception e) {
				LOGGER.error(ValueUtil.toString(e));
			}
		});

		// remove setOnCloseRequest and then, addEventHandler
		// stage.setOnCloseRequest(ev -> {
		//
		// });
		stage.show();
		return stage;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 * @param scene
	 * @param isModal
	 */
	public static Stage createStageAndShow(String title, Scene scene, Consumer<Stage> option) {
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle(title);
		option.accept(stage);
		stage.show();
		return stage;
	}

	public static Stage createStageAndShow(String title, Scene scene, boolean isModal) {
		Consumer<Stage> option = null;
		if (isModal) {
			option = stage -> {
				stage.setTitle(title);
				stage.setAlwaysOnTop(true);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.initOwner(StageStore.getPrimaryStage());

			};
			return createStageAndShow(scene, option);
		} else {
			option = stage -> {
				stage.setTitle(title);
				stage.initOwner(StageStore.getPrimaryStage());
			};
			return createStageAndShow(scene, option);
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 6. 23.
	 */
	public static Stage createStageAndShow(Scene scene, Consumer<Stage> option) {
		Stage stage = craeteStage(scene, option);
		stage.show();
		return stage;
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 8. 28.
	 * @param scene
	 * @param option
	 * @return
	 */
	public static Stage createStageAndShowAndWait(Scene scene, Consumer<Stage> option) {
		Stage stage = craeteStage(scene, option);
		stage.showAndWait();
		return stage;
	}

	public static Stage craeteStage(Parent parent, Consumer<Stage> option) {
		return craeteStage(new Scene(parent), option);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 18.
	 * @param scene
	 * @param option
	 * @return
	 */
	public static Stage craeteStage(Scene scene, Consumer<Stage> option) {
		// Stage stage = new Stage();
		// stage.initOwner(StageStore.getPrimaryStage());
		// stage.setScene(scene);
		//
		// if (option != null)
		// option.accept(stage);

		Stage stage = craeteStage();
		stage.setScene(scene);

		if (option != null)
			option.accept(stage);

		return stage;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 12.
	 * @param option
	 * @return
	 */
	public static Stage craeteStage() {
		Stage stage = new Stage();
		stage.initOwner(StageStore.getPrimaryStage());
		return stage;
	}

	// /**
	// * @작성자 : KYJ
	// * @작성일 : 2016. 6. 23.
	// * @param parent
	// * @param isModal
	// */
	// public static <T extends Parent> void createStageLazyShow(Supplier<T>
	// lazyParent, Consumer<Stage> option) {
	// Stage stage = craeteStage(option);
	//
	// Platform.runLater(() -> {
	// BorderPane root = new BorderPane();
	//
	// Scene value = new Scene(root);
	// stage.setScene(value);
	// stage.show();
	//
	// root.setCenter(lazyParent.get());
	// });
	// }

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 11.
	 * @param treeView
	 */
	public static void expandTreeView(TreeView<?> treeView, boolean expand) {
		expandTreeView(treeView.getRoot(), expand);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 1. 11.
	 * @param item
	 */
	public static void expandTreeView(TreeItem<?> item, boolean expand) {
		if (item != null && !item.isLeaf()) {
			item.setExpanded(expand);
			for (TreeItem<?> child : item.getChildren()) {
				expandTreeView(child, expand);
			}
		}
	}

	/********************************
	 * 작성일 : 2016. 8. 23. 작성자 : KYJ
	 *
	 * Node의 Window 객체를 리턴함.
	 *
	 * @param node
	 * @return
	 ********************************/
	public static Window getWindow(Node node) {
		return getWindow(node, () -> StageStore.getPrimaryStage());
	}

	public static Window getWindow(Node node, Supplier<Window> emptyThan) {
		if (node != null) {
			Scene scene = node.getScene();
			if (scene != null) {
				return scene.getWindow();
			}
		}

		if (emptyThan != null)
			return emptyThan.get();

		return null;
	}

	/********************************
	 * 작성일 : 2016. 9. 3. 작성자 : KYJ
	 *
	 * TableView 키이벤트를 등록
	 *
	 * @param tb
	 ********************************/
	public static void installClipboardKeyEvent(TableView<?> tb) {
		// FxTableViewUtil.installCopyPasteHandler(tb);
		FxTableViewUtil.installCopyHandler(tb);
		// 2017.05.26 사용안함.
		// ClipboardKeyEventInstaller.install(tb);
	}

	public static void installClipboardKeyEvent(TreeView<?> tv) {
		// FxTableViewUtil.installCopyPasteHandler(tb);
		FxTreeViewClipboardUtil.installCopyPasteHandler(tv);
		// 2017.05.26 사용안함.
		// ClipboardKeyEventInstaller.install(tb);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 9.
	 * @param lv
	 * @param converter
	 */
	public static <T> void installClipboardKeyEvent(ListView<T> lv, StringConverter<T> converter) {
		// FxTableViewUtil.installCopyPasteHandler(tb);
		FxListViewUtil.installClipboardKeyEvent(lv, converter);
		// 2017.05.26 사용안함.
		// ClipboardKeyEventInstaller.install(tb);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 30.
	 * @param baseModel
	 * @param view
	 */
	public static <T extends AbstractDVO> void installCommonsTableView(Class<T> baseModel, TableView<T> view) {
		FxTableViewUtil.installCommonsTableView(baseModel, view, new AnnotationOptions<T>(baseModel) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.kyj.fx.commons.fx.controls.grid.AnnotationOptions#
			 * useCommonCheckBox()
			 */
			@Override
			public boolean useCommonCheckBox() {
				return false;
			}

		});
	}

	/**
	 * 테이블뷰의 컬럼모델을 쉽게 생성하기 위한 api.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 7. 17.
	 * @param baseModel
	 * @param view
	 * @param option
	 */
	public static <T extends AbstractDVO> void installCommonsTableView(Class<T> baseModel, TableView<T> view, IOptions option) {
		FxTableViewUtil.installCommonsTableView(baseModel, view, option);
	}

	public enum POPUP_STYLE {
		POP_OVER, POPUP
	}

	/**
	 * 테이블뷰에 더블클릭하면 팝업이 열리는 기능을 install 처리한다.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 28.
	 * @param tbMetadata
	 */
	@SuppressWarnings("rawtypes")
	public static <T> void installDoubleClickPopup(POPUP_STYLE style, TableView<T> tbMetadata) {
		installDoubleClickPopup(null, style, tbMetadata);
	}

	public static <T> void installDoubleClickPopup(Window _owner, POPUP_STYLE style, TableView<T> tbMetadata) {
		installDoubleClickPopup(_owner, style, tbMetadata, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 3.
	 * @param _owner
	 * @param style
	 * @param tbMetadata
	 * @param colFilter
	 *            적용하지않을 테이블
	 */
	public static <T> void installDoubleClickPopup(Window _owner, POPUP_STYLE style, TableView<T> tbMetadata,
			BiPredicate<TableColumn<T, ?>, Integer> colFilter) {
		tbMetadata.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
			Window owner = _owner;
			if (MouseButton.PRIMARY == ev.getButton() && ev.getClickCount() == 2) {
				if (ev.isConsumed())
					return;

				ObservableList<TablePosition> selectedCells = tbMetadata.getSelectionModel().getSelectedCells();
				if (selectedCells == null || selectedCells.isEmpty())
					return;

				TablePosition tablePosition = selectedCells.get(0);
				TableColumn<T, ?> tableColumn = tablePosition.getTableColumn();
				int column = tablePosition.getColumn();
				int row = tablePosition.getRow();

				if (column == -1)
					return;
				if (row == -1)
					return;

				// TableColumn<T, ?> tableColumn =
				// tbMetadata.getColumns().get(column);
				// ObservableList<TableColumn<T, ?>> columns =
				// tableColumn.getColumns();

				if (colFilter != null && !colFilter.test(tableColumn, row)) {
					return;
				}

				// tablePosition.getTableColumn().
				Object valueByConverter = FxTableViewUtil.getValue(tbMetadata, tableColumn, row);
				String value = "";
				if (ValueUtil.isNotEmpty(valueByConverter)) {
					value = valueByConverter.toString();
				}

				/**
				 * @최초생성일 2016. 11. 28.
				 */
				final double WIDTH = 500d;
				final double HEIGHT = 400d;

				switch (style) {
				case POP_OVER:
					JavaTextArea createJavaTextArea = createJavaTextArea(value);
					createJavaTextArea.setPrefSize(WIDTH, HEIGHT);
					FxUtil.showPopOver(tbMetadata, createJavaTextArea);
					break;
				case POPUP:

					createSimpleTextAreaAndShow(value, stage -> {
						stage.setTitle("Show Value");
						stage.setWidth(WIDTH);
						stage.setHeight(HEIGHT);
						stage.initStyle(StageStyle.UTILITY);
						stage.initOwner(owner);
						stage.focusedProperty().addListener((oba, o, n) -> {
							if (!n)
								stage.close();
						});
						stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
							if (KeyCode.ESCAPE == event.getCode()) {
								if (!event.isConsumed()) {
									stage.close();
								}
							}
						});
					});
					break;
				}

			}

		});
	}

	/**
	 * 테이블컬럼에서 화면에 보여주는 텍스트를 리턴한다.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 31.
	 * @param tc
	 * @param row
	 * @return
	 */

	public static String getDisplayText(TableColumn<?, ?> tc, int row) {
		return FxTableViewUtil.getDisplayText(tc, row, null).toString();
	}

	public static String getDisplayText(TableColumn<?, ?> tc, int row, BiFunction<TableColumn<?, ?>, Object, Object> stringconverter) {
		return FxTableViewUtil.getDisplayText(tc, row, stringconverter).toString();
	}

	public static Object getValue(TableColumn<?, ?> column, int rowIndex) {
		return FxTableViewUtil.getValue(column.getTableView(), column, rowIndex);
	}

	public static Object getValue(TableView<?> table, TableColumn<?, ?> column, int rowIndex) {
		return FxTableViewUtil.getValue(table, column, rowIndex);
	}

	/**
	 * excel Export 기능이 있는 메뉴 아이템을 리턴.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 7. 18.
	 * @param target
	 * @return
	 */
	public static <T> MenuItem createMenuItemExcelExport(TableView<T> target) {
		return FxTableViewUtil.EasyMenuItem.createExcelExportMenuItem(target);
	}

	/**
	 * TableView에 Filter 기능을 적용시킨다. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 21.
	 * @param tbEvents
	 * @return
	 */
	public static <T> TableFilter<T> installTableFilter(TableView<T> tbEvents) {
		return FxTableViewUtil.installTableFilter(tbEvents);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 12.
	 * @param tb
	 */
	public static <T> void installFindKeyEvent(TableView<T> tb) {
		installFindKeyEvent(StageStore.getPrimaryStage(), tb, null);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 12.
	 * @param owner
	 * @param tb
	 */
	public static <T> void installFindKeyEvent(Window owner, TableView<T> tb) {
		installFindKeyEvent(owner, tb, null);
	}

	public static <T> void installFindKeyEvent(Window owner, TableView<T> tb,
			BiFunction<TableColumn<?, ?>, Object, Object> customConverter) {
		FxTableViewUtil.installFindKeyEvent(owner, tb, customConverter);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 9.
	 * @param listview
	 * @param converter
	 */
	public static <T> void installFindKeyEvent(ListView<T> listview, StringConverter<T> converter) {
		FxListViewUtil.installFindKeyEvent(listview, converter);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 9.
	 * @param owner
	 * @param listview
	 * @param converter
	 */
	public static <T> void installFindKeyEvent(Stage owner, ListView<T> listview, StringConverter<T> converter) {
		FxListViewUtil.installFindKeyEvent(owner, listview, converter);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 2. 20.
	 * @param owner
	 * @param tb
	 * @param customConverter
	 */
	public static <T> void installCustomScriptEvent(Window owner, TableView<T> tb,
			BiFunction<TableColumn<?, ?>, Object, Object> customConverter) {
		FxTableViewUtil.installCustomScriptEvent(owner, tb, customConverter);
	}

	/**
	 * WEB 색상 리턴.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 19.
	 * @param color
	 * @return
	 */
	public static String toWebString(Color color) {
		if (color == null)
			return "BLACK";

		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	}

	/**
	 * RGB Color String
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 1.
	 * @param color
	 * @return
	 */
	public static String toRgbString(Color color) {
		if (color == null)
			return "BLACK";
		return String.format("rgba(%d, %d, %d, 1)", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}

	/**
	 * HSB Color String
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 12. 1.
	 * @param color
	 * @return
	 */
	public static String toHsbString(Color color) {
		if (color == null)
			return "BLACK";
		return String.format("hsl(%d, %d%%, %d%%)", (int) (color.getHue()), (int) (color.getSaturation() * 100),
				(int) (color.getBrightness() * 100));
	}

	/********************************
	 * 작성일 : 2016. 7. 19. 작성자 : KYJ
	 *
	 * Show PopOver
	 *
	 * @param root
	 * @param showingNode
	 ********************************/
	public static void showPopOver(Node root, Node showingNode) {
		showPopOver(root, showingNode, null);
	}

	public static void showPopOver(Node root, Node showingNode, Function<PopOver, PopOver> callback) {
		if (root == showingNode)
			return;

		PopOver popOver = null;
		if (callback != null) {
			popOver = callback.apply(createPopOver(showingNode));
		} else
			popOver = createPopOver(showingNode);

		popOver.show(root);
	}

	private static PopOver createPopOver(Node showingNode) {
		PopOver popOver = new PopOver(showingNode);
		popOver.setAnimated(false);
		popOver.setHideOnEscape(true);
		return popOver;
	}

	/**
	 * Show PopOver
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 28.
	 * @param root
	 * @param showingNode
	 * @param locationX
	 * @param locationY
	 */
	public static void showPopOver(Node root, Node showingNode, double locationX, double locationY) {
		if (root == showingNode)
			return;

		showPopOver(root, showingNode, pop -> {
			pop.setX(locationX);
			pop.setY(locationY);
			return pop;
		});
	}

	/**
	 * 로딩바가 뜨면서 액션 처리.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 31.
	 * @param action
	 *            사용자 처리 작업에 대한 코드 로직이 입력됨
	 * @return
	 */
	public static <K> K showLoading(Task<K> action) {
		return showLoading(StageStore.getPrimaryStage(), action);
	}

	/**
	 * 로딩바가 뜨면서 액션 처리.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 31.
	 * @param owner
	 *            primaryStage에 대한 데이터가 입력되야함. (사이즈 및 너비, 높이 조절에 대한 메타데이터를 참조함)
	 * @param action
	 *            사용자 처리 작업에 대한 코드 로직이 입력됨
	 * @return
	 */
	public static <K> K showLoading(Window owner, Task<K> action) {
		return showLoading(owner, action, null);
	}

	/**
	 * 로딩바가 뜨면서 액션 처리.
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 4. 9.
	 * @param owner
	 *            primaryStage에 대한 데이터가 입력되야함. (사이즈 및 너비, 높이 조절에 대한 메타데이터를 참조함)
	 * @param action
	 *            사용자 처리 작업에 대한 코드 로직이 입력됨
	 * @param customAction
	 *            사용자 정의 Stage 처리
	 * @return
	 */
	public static <K> K showLoading(Window owner, Task<K> action, Consumer<Stage> customAction) {

		// 비동기 로딩바
		GargoyleLoadBar<K> bar = new GargoyleSynchLoadBar<>(owner, action);
		// 비동기 로딩바
		// gargoyleSynchProgessPopup = new GargoyleASynchLoadBar<>(stage, task);
		return showLoading(bar, customAction);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 21.
	 * @param <K>
	 * @param customAction
	 * @param bar
	 * @return
	 */
	private static <K> K showLoading(GargoyleLoadBar<K> bar, Consumer<Stage> customAction) {
		Stage stage = bar.getStage();
		if (customAction != null)
			customAction.accept(stage);
		bar.setExecutor(GargoyleSynchLoadBar.newSingleThreadExecutor);
		bar.start();
		return bar.getValue();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 11. 21.
	 * @param <K>
	 * @param owner
	 * @param action
	 * @param customAction
	 * @return
	 */
	public static <K> void showWaitingLoading(Task<K> action) {
		// 비동기 로딩바
		GargoyleLoadBar<K> bar = new GargoyleSynchLoadBar<K>(StageStore.getPrimaryStage(), action) {

		};

		EventHandler<WorkerStateEvent> onAfterWorked = ev -> {
			bar.getStage().close();
		};
		action.setOnSucceeded(onAfterWorked);
		action.setOnFailed(onAfterWorked);
		showLoading(bar, stage -> {

		});
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 30.
	 * @param owner
	 * @param action
	 * @param customAction
	 * @param onComplate
	 */
	public static <K> void showLoading(Task<K> action, Consumer<K> onComplate) {
		showLoading(StageStore.getPrimaryStage(), action, null, onComplate);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 30.
	 * @param owner
	 * @param action
	 * @param customAction
	 * @param onComplate
	 */
	public static <K> void showLoading(Window owner, Task<K> action, Consumer<Stage> customAction, Consumer<K> onComplate) {

		// 비동기 로딩바
		GargoyleLoadBar<K> gargoyleSynchProgessPopup = new GargoyleSynchLoadBar<>(owner, action);
		// 비동기 로딩바
		// gargoyleSynchProgessPopup = new GargoyleASynchLoadBar<>(stage, task);

		Stage stage = gargoyleSynchProgessPopup.getStage();
		if (customAction != null)
			customAction.accept(stage);

		gargoyleSynchProgessPopup.setExecutor(GargoyleSynchLoadBar.newSingleThreadExecutor);
		gargoyleSynchProgessPopup.start();

		if (onComplate != null) {
			gargoyleSynchProgessPopup.setOnSucceeded(ev -> {
				K value = gargoyleSynchProgessPopup.getValue();
				onComplate.accept(value);
			});
		}

	}

	public static JavaTextArea createJavaTextArea(String content) {
		return createJavaTextArea(content, 1200, 800);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 7. 22.
	 * @param content
	 */
	public static JavaTextArea createJavaTextArea(String content, double width, double height) {
		JavaTextArea javaTextArea = new JavaTextArea();
		javaTextArea.setPrefSize(width, height);
		javaTextArea.setContent(content);
		return javaTextArea;
	}

	public static void createSimpleTextAreaAndShow(String content, Consumer<Stage> option) {
		createStageAndShow(new TextArea(content), option);
	}

	public static void createCodeAreaAndShow(String content) {
		SimpleTextView parent = new SimpleTextView(content);

		createStageAndShow(parent, stage -> {
			stage.setWidth(1200d);
			stage.setHeight(800d);
		});
	}

	/**
	 * 도킹기능이 제공되는 팝업을 로드함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 27.
	 * @param owner
	 * @param dockNode
	 *            메인화면이 되는 노드.
	 */
	public static void createDockStageAndShow(Window owner, DockNode dockNode) {
		createDockStageAndShow(owner, dockNode, null, true);
	}

	/**
	 * 도킹기능이 제공되는 팝업을 로드함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 27.
	 * @param owner
	 * @param dockNode
	 *            메인화면이 되는 노드.
	 * @param center
	 */
	public static void createDockStageAndShow(Window owner, DockNode dockNode, boolean center) {
		createDockStageAndShow(owner, dockNode, null, center);
	}

	/**
	 * 도킹기능이 제공되는 팝업을 로드함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 27.
	 * @param dockNode
	 */
	public static void createDockStageAndShow(Window owner, DockNode dockNode, Point2D initLocation) {
		createDockStageAndShow(owner, dockNode, initLocation, false);
	}

	/**
	 * 도킹기능이 제공되는 팝업을 로드함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 27.
	 * @param dockNode
	 *            메인화면이 되는 노드.
	 */
	public static void createDockStageAndShow(Window owner, DockNode dockNode, Point2D initLocation, boolean center) {
		createDockStageAndShow(owner, dockNode, initLocation, center, null);
	}

	/**
	 * 도킹기능이 제공되는 팝업을 로드함.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 27.
	 * @param dockNode
	 *            메인화면이 되는 노드.
	 */
	public static void createDockStageAndShow(Window owner, DockNode dockNode, Point2D initLocation, boolean center,
			Consumer<Parent> handler) {
		Platform.runLater(() -> {

			Parent p = (Parent) dockNode.getContents();

			if (handler != null) {
				handler.accept(p);
			}

			dockNode.setOwner(owner);
			dockNode.setFloating(true, initLocation);
			if (center)
				dockNode.getStage().centerOnScreen();

		});

	}

	/**
	 * 툴팁 처리.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param v
	 * @param string
	 */
	public static void installTooltip(Node node, String string) {
		Tooltip.install(node, new Tooltip(string));
	}

	/**
	 * 툴팁 처리.
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 4.
	 * @param v
	 * @param string
	 */
	public static void installTooltip(Labeled node) {
		installTooltip(node, node.getText());
	}

	/**
	 * 파일로부터 이미지를 그리기 위한 뷰를 반환한다.
	 *
	 * @Date 2015. 10. 14.
	 * @param file
	 * @return
	 * @User KYJ
	 */
	public static ImageView createImageIconView(File file) {
		Image fxImage = null;
		if (file.exists()) {
			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
			Icon icon = fileSystemView.getSystemIcon(file);

			BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
			fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
		} else {
			return new ImageView();
		}

		return new ImageView(fxImage);
	}

	private static final double BROWSER_WIDTH = 1400d;
	private static final double BROWSER_HEIGHT = 900d;

	/**
	 * 브라우저 창 오픈
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 18.
	 * @param content
	 * @return
	 */
	public static WebView openBrowser(Node parent, String content) {
		return openBrowser(parent, content, true);
	}

	public static WebView openBrowser(Node parent, String content, boolean contentIsUrl) {
		return openBrowser(parent, content, contentIsUrl, "text/html");
	}

	public static WebView openBrowser(Node parent, String content, boolean contentIsUrl, String contentType) {
		return openBrowser(parent, content, contentIsUrl, contentType, stage ->{} ) ;
	}
	
	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 12. 13. 
	 * @param parent
	 * @param content
	 * @param contentIsUrl
	 * @param contentType
	 * @param userAction
	 * @return
	 */
	public static WebView openBrowser(Node parent, String content, boolean contentIsUrl, String contentType, Consumer<Stage> userAction) {

		Stage newStage = new Stage();
		if (contentIsUrl)
			newStage.setTitle(content);
		newStage.initOwner(StageStore.getPrimaryStage());
		WebView view = createGargoyleWebView(newStage);
		WebEngine engine = view.getEngine();
		if (contentIsUrl)
			engine.load(content);
		else
			engine.loadContent(content, contentType);

		// return view;

		BorderPane root = new BorderPane(view);

		HBox linkGroup = new HBox(5);
		linkGroup.setAlignment(Pos.CENTER_LEFT);
		linkGroup.setPadding(new Insets(5));
		TextField txtLink = new TextField(content);
		HBox.setHgrow(txtLink, Priority.ALWAYS);
		txtLink.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if (KeyCode.ENTER == ev.getCode())
				engine.load(txtLink.getText());
		});

		linkGroup.getChildren().add(new Label("URL : "));
		linkGroup.getChildren().add(txtLink);

		root.setTop(linkGroup);

		engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				// if (newValue == State.SUCCEEDED) {
				String location = engine.getLocation();
				txtLink.setText(location);
				// }
			}
		});

		FxUtil.createStageAndShow(new Scene(root, BROWSER_WIDTH, BROWSER_HEIGHT), stage -> {

			stage.initOwner(newStage);
			stage.setOnCloseRequest(ev -> {

				// 메모리 릭 방지.
				engine.load("about:blank");
			});
			
			userAction.accept(stage);
		});

		return view;
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 10.
	 * @param parent
	 * @param content
	 * @param contentIsUrl
	 * @param contentType
	 * @return
	 */
	public static WebView createGargoyleWebView(Window parent) {
		WebView view = new WebView();
		view.setContextMenuEnabled(false);
		view.setCache(false);

		WebEngine engine = view.getEngine();

		engine.setOnError(err -> {
			String message = err.getMessage();
			DialogUtil.showMessageDialog(parent, message);
		});

		view.setOnKeyPressed(key -> {

			switch (key.getCode()) {
			case F5:
				if (key.isConsumed())
					return;
				view.getEngine().reload();
				break;

			case F12:
				if (key.isConsumed())
					return;

				FxUtil.createStageAndShow("Simple Web Console", new WebViewConsole(view));
				key.consume();
				break;

			default:
				break;
			}

		});

		view.setOnContextMenuRequested(ev -> {

			MenuItem miReload = new MenuItem("Reload");
			miReload.setOnAction(e -> {
				view.getEngine().reload();
			});

			MenuItem miSource = new MenuItem("Source");
			miSource.setOnAction(e -> {
				Object executeScript = view.getEngine().executeScript("document.documentElement.innerHTML");
				String htmlCoded = executeScript.toString();

				XMLEditor fxmlTextArea = new XMLEditor();
				fxmlTextArea.setContent(htmlCoded);
				FxUtil.createStageAndShow(fxmlTextArea, stage -> {
					stage.setWidth(1200d);
					stage.setHeight(800d);
				});
				// FxUtil.createStageAndShow("Source", new
				// WebViewConsole(view));

			});

			ContextMenu contextMenu = new ContextMenu(miReload, miSource);

			contextMenu.show(FxUtil.getWindow(view), ev.getScreenX(), ev.getScreenY());

		});

		engine.setJavaScriptEnabled(true);
		engine.setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {

			@Override
			public WebEngine call(PopupFeatures p) {
				WebView openBrowser = openBrowser(view, "", false);
				return openBrowser.getEngine();
			}
		});

		engine.setOnAlert(ev -> {
			DialogUtil.showMessageDialog(ev.getData());
		});

		engine.setConfirmHandler(new Callback<String, Boolean>() {

			@Override
			public Boolean call(String param) {
				Optional<Pair<String, String>> showYesOrNoDialog = DialogUtil.showYesOrNoDialog("Confirm.", param);
				if (showYesOrNoDialog.isPresent()) {
					Pair<String, String> pair = showYesOrNoDialog.get();
					if (pair == null)
						return false;
					return "Y".equals(pair.getValue());
				}
				return false;
			}
		});

		engine.setOnAlert((WebEvent<String> wEvent) -> {
			LOGGER.debug("Alert Event  -  Message: {}  ", wEvent.getData());
			DialogUtil.showMessageDialog(parent, wEvent.getData());
		});
		return view;
	}

	/**
	 * 다른이름으로 저장 처리에 대한 공통 API
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 1. 13. <br/>
	 *      2017. 10. 20 리턴값 추가.
	 * @param owner
	 * @param model
	 * @return
	 *
	 */
	public static <T> File saveAsFx(Window owner, SaveAsModel model) {
		if (model == null) {
			throw new NullPointerException("SaveAsModel is null");
		}

		File saveAs = DialogUtil.showFileSaveCheckDialog(owner, model.getFileChooserOption());
		if (saveAs == null)
			return null;

		if (!saveAs.exists()) {
			try {
				saveAs.createNewFile();
			} catch (IOException e) {
				LOGGER.error(ValueUtil.toString(e));
				Consumer<Exception> onError = model.onError();
				if (onError != null)
					model.onError().accept(e);
				else
					throw new RuntimeException(e);
			}
		}

		if (saveAs.exists()) {
			FileUtil.writeFile(saveAs, model.getContent(), model.getEncoding(), model.onError());
			model.onSuccess(saveAs);
		}

		return saveAs;

	}

	/**
	 * 다른이름으로 저장처리를 하기위한 형식을 정의한 클래스
	 * 
	 * @author KYJ
	 *
	 */
	public static interface SaveAsModel {
		public default Charset getEncoding() {
			return Charset.forName("UTF-8");
		}

		public abstract String getContent();

		public default Consumer<FileChooser> getFileChooserOption() {
			// FileChooset에 대한 정의처리.
			return chooser -> {

			};
		}

		public default void onSuccess(File f) {

		}

		/**
		 * 파일 쓰기 처리중 에러가 발생한경우
		 * 
		 * @작성자 : KYJ
		 * @작성일 : 2017. 1. 13.
		 * @return
		 */
		public default Consumer<Exception> onError() {
			return err -> LOGGER.error(ValueUtil.toString(err));
		}
	}

	/********************************
	 * 작성일 : 2016. 5. 21. 작성자 : KYJ
	 *
	 * FXMLController 어노테이션 에 정의된 내용을 기준으로 FXML을 로드한다. </br>
	 * 아래메소드를 활용하는 경우
	 *
	 * PostInitialize 어노테이션을 활용하여 initialize() 수행후 후처리를 지정할 수 있음.
	 *
	 * @param controllerClass
	 * @param option
	 * @return
	 * @throws GargoyleException
	 * @throws NullPointerException
	 * @throws IOException
	 ********************************/
	public static <N, C> N load(Class<C> controllerClass, Object rootInstance, Consumer<N> option, Consumer<C> controllerAction)
			throws Exception {
		return FxLoader.load(controllerClass, rootInstance, option, controllerAction);
	}

	/**
	 * TextField에 텍스트Auto Binding 설치
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 21.
	 * @param textField
	 * @param suggestions
	 */
	public static void installAutoTextFieldBinding(TextField textField, Supplier<Collection<String>> suggestions) {
		if (suggestions == null || suggestions.get() == null)
			return;

		AutoCompletionTextFieldBinding<String> autoCompletionTextFieldBinding = new AutoCompletionTextFieldBinding<>(textField, param -> {
			String userText = param.getUserText();
			return suggestions.get().stream().filter(v -> v.startsWith(userText)).collect(Collectors.toList());
		});
		autoCompletionTextFieldBinding.setVisibleRowCount(10);

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2016. 11. 28.
	 * @return
	 */
	public static FXMLLoader createNewFxmlLoader() {
		FXMLLoader loader = new FXMLLoader();
		loader.setBuilderFactory(GargoyleBuilderFactory.getInstance());
		return loader;
	}

	/**
	 * 픽포인트의 노드 리턴
	 *
	 * @작성자 : KYJ
	 * @작성일 : 2016. 1. 20.
	 * @param node
	 * @param sceneX
	 * @param sceneY
	 * @return
	 */
	public static Node pick(Node node, double sceneX, double sceneY) {
		Point2D p = node.sceneToLocal(sceneX, sceneY, true /* rootScene */);

		// check if the given node has the point inside it, or else we drop out
		if (!node.contains(p))
			return null;

		// at this point we know that _at least_ the given node is a valid
		// answer to the given point, so we will return that if we don't find
		// a better child option
		if (node instanceof Parent) {
			// we iterate through all children in reverse order, and stop when
			// we find a match.
			// We do this as we know the el ements at the end of the list have a
			// higher
			// z-order, and are therefore the better match, compared to children
			// that
			// might also intersect (but that would be underneath the element).
			Node bestMatchingChild = null;
			List<Node> children = ((Parent) node).getChildrenUnmodifiable();
			for (int i = children.size() - 1; i >= 0; i--) {
				Node child = children.get(i);
				p = child.sceneToLocal(sceneX, sceneY, true /* rootScene */);
				if (child.isVisible() && !child.isMouseTransparent() && child.contains(p)) {
					bestMatchingChild = child;
					break;
				}
			}

			if (bestMatchingChild != null) {
				return pick(bestMatchingChild, sceneX, sceneY);
			}
		}

		return node;
	}

	/********************************
	 * 작성일 : 2016. 6. 29. 작성자 : KYJ
	 *
	 * print 처리.
	 *
	 * @param window
	 * @param target
	 ********************************/
	public static void printJob(Window window, Node target) {
		Printer printer = Printer.getDefaultPrinter();
		// PrinterAttributes printerAttributes = printer.getPrinterAttributes();
		//
		Paper a4 = Paper.A4;

		// Paper a4 = PrintHelper.createPaper("Rotate A4", Paper.A4.getHeight(),
		// Paper.A4.getWidth(), Units.MM);
		PageLayout pageLayout = printer.createPageLayout(a4, PageOrientation.REVERSE_PORTRAIT, MarginType.DEFAULT);

		PrinterJob printerJob = PrinterJob.createPrinterJob();

		// JobSettings jobSettings = printerJob.getJobSettings();
		// jobSettings.setPrintSides(PrintSides.TUMBLE);
		ImageView imageView = new ImageView();
		// 화면 사이즈에 맞게 크기 조절.
		Callback<SnapshotResult, Void> callback = param -> {
			final WritableImage image = param.getImage();
			imageView.setImage(image);

			final double scaleX = pageLayout.getPrintableWidth() / imageView.getBoundsInParent().getWidth();
			final double scaleY = pageLayout.getPrintableHeight() / imageView.getBoundsInParent().getHeight();
			imageView.getTransforms().add(new Scale(scaleX, scaleY));

			return null;
		};

		target.snapshot(callback, null, null);

		if (printerJob.showPrintDialog(window) && printerJob.printPage(pageLayout, imageView))
			printerJob.endJob();
	}

	public static void printDefefaultJob(Window window, Node target) {
		printJob(window, target, (param, imageView) -> {

			// ImageView imageView = new ImageView();
			final WritableImage image = param.getImage();
			imageView.setImage(image);

			// final double scaleX = pageLayout.getPrintableWidth() /
			// imageView.getBoundsInParent().getWidth();
			// final double scaleY = pageLayout.getPrintableHeight() /
			// imageView.getBoundsInParent().getHeight();
			imageView.getTransforms().add(new Scale(1, 1));

		});
	}

	public static void printJob(Window window, Node target, BiConsumer<SnapshotResult, ImageView> draw) {
		Printer printer = Printer.getDefaultPrinter();
		// PrinterAttributes printerAttributes = printer.getPrinterAttributes();
		//
		Paper a4 = Paper.A4;

		// Paper a4 = PrintHelper.createPaper("Rotate A4", Paper.A4.getHeight(),
		// Paper.A4.getWidth(), Units.MM);
		PageLayout pageLayout = printer.createPageLayout(a4, PageOrientation.REVERSE_PORTRAIT, MarginType.DEFAULT);

		PrinterJob printerJob = PrinterJob.createPrinterJob();

		// JobSettings jobSettings = printerJob.getJobSettings();
		// jobSettings.setPrintSides(PrintSides.TUMBLE);
		ImageView imageView = new ImageView();

		Callback<SnapshotResult, Void> callback = param -> {

			draw.accept(param, imageView);

			// final WritableImage image = param.getImage();
			// imageView.setImage(image);

			// final double scaleX = pageLayout.getPrintableWidth() /
			// imageView.getBoundsInParent().getWidth();
			// final double scaleY = pageLayout.getPrintableHeight() /
			// imageView.getBoundsInParent().getHeight();
			// imageView.getTransforms().add(new Scale(scaleX, scaleY));

			return null;
		};

		target.snapshot(callback, null, null);

		if (printerJob.showPrintDialog(window) && printerJob.printPage(pageLayout, imageView))
			printerJob.endJob();
	}

	/**
	 * 컨텐스트 메뉴를 추가하기 위한 유틸리티성 클래스
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 4. 10.
	 * @param contextManager
	 */
	public static void installContextMenu(FxContextManager contextManager) {
		contextManager.install();
		LOGGER.debug(" contextmenu install success. ");
	}

	/**
	 * [start notifycation] 2017-06-08 kyj notifycation
	 */
	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 6. 8.
	 * @param cont
	 */
	public static void showNotification(final String cont) {
		showNotification("", cont);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 6. 8.
	 * @param title
	 * @param cont
	 */
	public static void showNotification(final String title, final String cont) {
		showNotification(null, title, cont, Pos.BOTTOM_RIGHT);
	}

	public static void showNotification(final String title, final String cont, Pos pos) {
		showNotification(null, title, cont, pos);
	}

	public static void showNotification(Node graphics, final String title, final String cont, Pos pos) {
		showNotification(() -> {
			return GargoyleNotification.create().darkStyle();
		}, n -> {

			if (graphics != null)
				n.graphic(graphics);

			n.text(cont);
			n.title(title);
			n.position(pos);
		});
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2017. 6. 8.
	 * @param suppl
	 * @param action
	 */
	public static void showNotification(Supplier<GargoyleNotification> suppl, Consumer<GargoyleNotification> action) {
		GargoyleNotification create = suppl.get();
		action.accept(create);
		create.show();
	}

	/**
	 * tableView의 item을 엑셀파일로 전환
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2017. 3. 31.
	 * @param saveFile
	 * @param tableView
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void exportExcelFile(File saveFile, TableView tableView) {

		// ObservableList<?> tvItems = tableView.getItems();
		// ObservableList<TableColumn> columns = tableView.getColumns();
		// int size = tvItems.size();
		// List<Map<String, Object>> items = Stream.iterate(0, a -> a +
		// 1).limit(size).map(rowIdx -> {
		//
		// Map<String, Object> map = new TreeMap<>();
		//
		// for (TableColumn c : columns) {
		// if (!c.isVisible())
		// continue;
		//
		// ObservableValue cellObservableValue =
		// c.getCellObservableValue(rowIdx);
		// if (cellObservableValue != null) {
		// Object value = cellObservableValue.getValue();
		// map.put(c.getText(), value);
		// continue;
		// }
		//
		// Callback cellValueFactory = c.getCellValueFactory();
		// if (cellValueFactory != null) {
		// Object param = tvItems.get(rowIdx);
		// Object call = cellValueFactory.call(param);
		// map.put(c.getText(), call);
		// continue;
		// }
		//
		// throw new RuntimeException("unexpected value property");
		// }
		//
		// return map;
		// }).collect(Collectors.toList());

		try {
			FxExcelUtil.createExcel(new IExcelScreenHandler() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.kyj.fx.commons.excel.IExcelScreenHandler#toSheetName(
				 * javafx.scene.control.TableView)
				 */
				@Override
				public String toSheetName(TableView<?> table) {
					return "Sheet1";
				}

			}, saveFile, Arrays.asList(tableView), true);
		} catch (Exception e) {
			DialogUtil.showExceptionDailog(e);
			// e.printStackTrace();
		}
		// List<Map<String, Object>> items = tableView.getItems().stream().map(v
		// -> {
		// if (v instanceof Map) {
		// return (Map<String, Object>) v;
		// }
		//
		// return ObjectUtil.toMap(() -> new TreeMap<>(), v);
		// }).collect(Collectors.toList());

		// List<Map<String, Object>> items = tableView.getItems().stream().map(v
		// -> {
		// if (v instanceof Map) {
		// return (Map<String, Object>) v;
		// }
		// return ObjectUtil.toMap( () -> new TreeMap<>(), v);
		// }).collect(Collectors.toList());

		// ObservableList<Map<String, Object>> items = this.tbResult.getItems();
		// ToExcelFileFunction toExcelFileFunction = new ToExcelFileFunction();
		// List<String> colStrings = columns.stream().filter(c ->
		// c.isVisible()).map(col ->
		// col.getText()).collect(Collectors.toList());
		//
		// toExcelFileFunction.generate0(saveFile, colStrings, items);
	}

	/**
	 * background color 객체 성성후 리턴
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 19.
	 * @param fill
	 * @return
	 */
	public static Background getBackgroundColor(Paint fill) {
		return new Background(new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 29.
	 * @param fill
	 * @param inserts
	 * @return
	 */
	public static Background getBackgroundColor(Paint fill, int inserts) {
		return getBackgroundColor(fill, getInserts(inserts));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 29.
	 * @param fill
	 * @param inserts
	 * @return
	 */
	public static Background getBackgroundColor(Paint fill, Insets inserts) {
		return new Background(new BackgroundFill(fill, CornerRadii.EMPTY, inserts));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 6. 29.
	 * @param i
	 * @return
	 */
	public static Insets getInserts(int i) {
		return new Insets(i);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2018. 10. 22.
	 * @param tb
	 * @param namedColumn
	 */
	public static <T> void installKeyMoveEvent(TableView<T> tb, TableColumn<T, ?> namedColumn) {
		FxTableViewUtil.installKeywordMoveEvent(tb, namedColumn);
	}

	/**
	 * 
	 * install keyClick event. <br/>
	 * 
	 * if user key click , this function find the matched preffix text. and then
	 * is selected the row <br/>
	 * 
	 * find value from toString method. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @param tv
	 * @param converter
	 */
	public static <T> void installKeyMoveEvent(TreeView<T> tv) {
		FxTreeViewUtil.installKeywordMoveEvent(tv);
	}

	/**
	 * install keyClick event. <br/>
	 * 
	 * if user key click , this function find the matched preffix text. and then
	 * is selected the row <br/>
	 * 
	 * find value by converter method. <br/>
	 * 
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 1. 11.
	 * @param tv
	 * @param converter
	 */
	public static <T> void installKeyMoveEvent(TreeView<T> tv, StringConverter<T> converter) {
		FxTreeViewUtil.installKeywordMoveEvent(tv, converter);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 9. 20.
	 * @param items
	 * @return
	 */
	public static TableView<Map<String, Object>> toTableView(List<Map<String, Object>> items) {
		return FxTableViewUtil.toTableView(items);
	}

}
