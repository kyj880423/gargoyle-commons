/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.text
 *	작성일   : 2016. 10. 6.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.text;

import java.util.List;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.fx.controls.tree.JavaMethodTreeView;
import com.kyj.fx.commons.memory.StageStore;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * 코드 처리 관련 Helper 클래스 <br/>
 * 
 * CodeArea클래스와 연관된 모든 공통처리내용이 구현된다. <br/>
 *
 *
 * 2016-10-13 kyj 라인이동 기능 추가. <br/>
 * 
 * 
 * 2019-02-19 PMD Not Support Anymore. KYJ <br/>
 * @author KYJ
 *
 */
public class JavaCodeAreaHelper extends CodeAreaHelper<CodeArea> implements EventHandler<ActionEvent> {
	private static Logger LOGGER = LoggerFactory.getLogger(JavaCodeAreaHelper.class);

	private Menu menuPmd;
	private MenuItem menuRunPmd;
	private MenuItem menuAutoComment;
	private MenuItem menuMethodTree;

	public JavaCodeAreaHelper(CodeArea codeArea) {
		super(codeArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kyj.fx.commons.fx.controls.text.CodeAreaHelper#codeAreaKeyClick(
	 * javafx.scene.input.KeyEvent)
	 */
	@Override
	public void codeAreaKeyClick(KeyEvent e) {

		if (!e.isAltDown() && !e.isControlDown() && !e.isShiftDown() && e.getCode() == KeyCode.F4) {
			if (e.isConsumed())
				return;
			showMethodTree();
			e.consume();
		} else {
			super.codeAreaKeyClick(e);
		}

	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 9.
	 */
	protected void showMethodTree() {
		String javaCode = codeArea.getText();
		JavaMethodTreeView<Object> view = new JavaMethodTreeView<>();
		FxUtil.createStageAndShow(view, stage -> {
			stage.focusedProperty().addListener((oba, o, n) -> {
				if (!n) {
					stage.close();
				}
			});
		});
		view.setValue(javaCode);
	}

	/**
	 * 메뉴 생성
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 6.
	 */
	@Override
	public void createMenus() {
		super.createMenus();
		menuPmd = new Menu("PMD");
		menuRunPmd = new MenuItem("Run PMD");
		menuAutoComment = new MenuItem("Auto Comment");
		menuMethodTree = new MenuItem("Method Tree");

		menuRunPmd.setOnAction(this);
		menuAutoComment.setOnAction(this);
		menuMethodTree.setOnAction(this);

		menuPmd.getItems().addAll(menuRunPmd);
		getMenu().getItems().addAll(menuPmd, new SeparatorMenuItem(), menuAutoComment, menuMethodTree);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
	 */
	@Override
	public void handle(ActionEvent event) {
//		if (event.getSource() == menuRunPmd) {
//			doPmd();
//		}

		if (event.getSource() == menuAutoComment) {
			doAutoComment();
		}

		else if (event.getSource() == menuMethodTree) {
			showMethodTree();
		}
	}

	/**
	 * @최초생성일 2016. 10. 12.
	 */
	public static final String appendLineKeyword = "+";

	/**
	 * 주석 자동화 처리
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 12.
	 */
	private void doAutoComment() {
		String javaCode = codeArea.getText();

		List<String> autoCommentedList = ValueUtil.toAutoCommentedList(javaCode, appendLineKeyword);

		JavaTextAreaForAutoComment area = new JavaTextAreaForAutoComment();
		area.setContent(autoCommentedList);

		FxUtil.createStageAndShow(area, stage -> {
			stage.setTitle(" Auto - Comment ");
			stage.initOwner(StageStore.getPrimaryStage());
			stage.setWidth(1200d);
			stage.setHeight(800d);
		});
	}

	/**
	 * PMD 처리
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2016. 10. 12.
	 */
//	private void doPmd() {
//		PMDCheckedListComposite pmdCheckedListComposite = new PMDCheckedListComposite(null) {
//
//			@Override
//			public void run() {
//				simpleFilePmd(null);
//			}
//
//			@Override
//			protected void simpleFilePmd(File file) {
//
//				try {
//					GargoylePMDParameters params = new GargoylePMDParameters();
//					String sourceCode = codeArea.getText();
//					params.setSourceFileName("Java");
//					params.setSourceText(sourceCode);
//
//					// if (!FileUtil.isJavaFile(file)) {
//					// String fileExtension = FileUtil.getFileExtension(file);
//					// try {
//					// Field declaredField =
//					// PMDParameters.class.getDeclaredField("language");
//					// if (declaredField != null) {
//					// declaredField.setAccessible(true);
//					// declaredField.set(params, fileExtension);
//					// }
//					// } catch (Exception e) {
//					// e.printStackTrace();
//					// }
//					// }
//
//					// transformParametersIntoConfiguration(params);
//					long start = System.nanoTime();
//
//					doPMD.doPMD(transformParametersIntoConfiguration(params), reportListenerPropertyProperty().get(),
//							violationCountingListenerProperty().get());
//					long end = System.nanoTime();
//					Benchmarker.mark(Benchmark.TotalPMD, end - start, 0);
//
//					TextReport report = new TextReport();
//					try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//						report.generate(Benchmarker.values(), new PrintStream(out));
//						out.flush();
//						LOGGER.debug(out.toString("UTF-8"));
//					}
//
//					updateStatus(sourceCode);
//
//				} catch (IOException e) {
//					LOGGER.error(ValueUtil.toString(e));
//				}
//			}
//
//		};
//
//		pmdCheckedListComposite.run();
//
//		CloseableParent<BorderPane> closa = pmdCheckedListComposite;
//		Consumer<Stage> option = stage -> {
//			stage.setTitle("PMD Check.");
//			// Owner를 Root로 지정.
//			stage.initOwner(StageStore.getPrimaryStage());
//			stage.setWidth(1200d);
//			stage.setHeight(800d);
//		};
//
//		FxUtil.createStageAndShow(closa, option);
//	}
}