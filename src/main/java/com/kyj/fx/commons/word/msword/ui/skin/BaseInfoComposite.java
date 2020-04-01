/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.words.spec.ui.skin
 *	작성일   : 2016. 2. 18.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.word.msword.ui.skin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.kyj.fx.commons.code.java.parser.GargoyleJavaParser;
import com.kyj.fx.commons.fx.controls.tree.JavaProjectFileTreeItem;
import com.kyj.fx.commons.fx.controls.tree.JavaProjectFileWrapper;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;
import com.kyj.fx.commons.word.msword.ui.model.SpecResource;
import com.kyj.fx.commons.word.msword.ui.tabs.AbstractSpecTab;
import com.kyj.fx.commons.word.msword.vo.MethodDVO;
import com.kyj.fx.commons.word.msword.vo.MethodParameterDVO;
import com.kyj.fx.fxloader.FXMLController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;

/**
 * @author KYJ
 *
 */
@FXMLController(value = "BaseInfoApp.fxml", isSelfController = true)
public class BaseInfoComposite extends BorderPane {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseInfoComposite.class);

	@FXML
	private TextField lblProjectName, lblRealPath, lblFileName, lblPackage, lblImports, lblUserName;

	private ObservableList<MethodDVO> methodData = FXCollections.observableArrayList();

	private AbstractSpecTab projectInfoBaseInfoTab;

	private File projectFile;
	private File targetFile;

	/**
	 * Construnctor.
	 *
	 * @param projectInfoBaseInfoTab
	 *
	 * @param projectDir
	 * @param targetFile
	 * @throws Exception
	 */
	public BaseInfoComposite(AbstractSpecTab projectInfoBaseInfoTab) throws Exception {
		this.projectInfoBaseInfoTab = projectInfoBaseInfoTab;
		FxUtil.loadRoot(getClass(), this);

	}

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 * JavaParser
	 *
	 * @param javaFile
	 * @param converter
	 * @param errorHandler
	 ********************************/
	public static void consumeJavaParser(File javaFile, Consumer<CompilationUnit> converter, Consumer<Exception> errorHandler) {

		try {
			if (javaFile != null && javaFile.exists()) {
				CompilationUnit cu = GargoyleJavaParser.getCompileUnit(javaFile);
				converter.accept(cu);
			}
		} catch (Exception e) {
			errorHandler.accept(e);
		}
	}
	
	/**
	 *
	 * 코드 분석 시작 처리.
	 * @작성자 : KYJ
	 * @작성일 : 2017. 2. 3.
	 */
	public void start() {

		// 동적처리에 따라 API 함수 수정.
		consumeJavaParser(targetFile, cu -> {

			NameExpr name = cu.getPackage().getName();
			lblPackage.setText(name.toString());
			String importStatement = cu.getImports().stream().map(im -> im.getName().toString()).collect(Collectors.joining(","));
			lblImports.setText(importStatement);

			Service<Void> service = new Service<Void>() {
				@Override
				protected Task<Void> createTask() {

					return new Task<Void>() {

						@Override
						protected Void call() throws Exception {
							new MethodVisitor(v -> {

								methodData.add(v);

							}).visit(cu, null);
							return null;
						}
					};
				}
			};
			service.start();

		} , err ->{
			LOGGER.error(ValueUtil.toString(err));
		});

	}

	@FXML
	public void initialize() {

		SpecResource specResource = projectInfoBaseInfoTab.getSpecResource();
		//TODO SVO형태로 받은경우는 없을 수 있음 처리해야함.

		projectFile = specResource.getProjectFile();
		targetFile = specResource.getTargetFile();

		String projectFileName = projectFile.getName();

		lblProjectName.setText(projectFileName);
		lblRealPath.setText(targetFile.getAbsolutePath());
		lblFileName.setText(targetFile.getName());
		lblUserName.setText(SystemUtils.USER_NAME);


		//2017-02-03 by kyj 사양서 분석처리를 start() 함수로 분리.
	}
	//	}

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 * JavaProjectTree 반환
	 *
	 * @param treeItem
	 * @return
	 ********************************/
	public JavaProjectFileTreeItem getJavaProjectFileTreeItem(TreeItem<JavaProjectFileWrapper> treeItem) {

		if (treeItem != null) {

			if (treeItem instanceof JavaProjectFileTreeItem) {
				return (JavaProjectFileTreeItem) treeItem;
			}

			return getJavaProjectFileTreeItem(treeItem.getParent());
		}

		return null;
	}

	/**
	 * 메타정보를 콜렉팅
	 *
	 * @author KYJ
	 *
	 */
	private static class MethodVisitor extends VoidVisitorAdapter<Object> {

		private Consumer<MethodDVO> onMethodDVOVisite;

		public MethodVisitor(Consumer<MethodDVO> onMethodDVOVisite) {
			this.onMethodDVOVisite = onMethodDVOVisite;
		}

		public void visit(MethodDeclaration n, Object arg) {

			MethodDVO methodDVO = new MethodDVO();
			methodDVO.setMethodName(n.getName());
			methodDVO.setVisivility(GargoyleJavaParser.toStringVisibility(n.getModifiers()));
			methodDVO.setDescription(n.getComment() != null ? n.getComment().toString() : "");
			ArrayList<MethodParameterDVO> methodParameterDVOList = new ArrayList<>();
			methodDVO.setMethodParameterDVOList(methodParameterDVOList);
			List<Parameter> parameters = n.getParameters();
			for (Parameter p : parameters) {
				//				String string2 = p.toString();
				VariableDeclaratorId id2 = p.getId();
				String varName = id2.getName();
				Type type = p.getType();
				String typeName = type.toString();
				Comment comment = p.getComment();
				methodParameterDVOList.add(new MethodParameterDVO(varName, typeName, "", comment == null ? "" : comment.toString()));
			}

			onMethodDVOVisite.accept(methodDVO);

			super.visit(n, arg);
		}

	}

	public ObservableList<MethodDVO> getMethodData() {
		return methodData;
	}

	/********************************
	 * 작성일 : 2016. 8. 14. 작성자 : KYJ
	 *
	 * package List 리턴
	 *
	 * @return
	 ********************************/
	public List<String> getImports() {
		return this.getImports(t -> t);
	}

	public <T> List<T> getImports(Function<String, T> convert) {
		String text = this.lblImports.getText();
		if (text != null && !text.isEmpty()) {
			return Stream.of(text.split(",")).map(convert).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	public String getPackage() {
		return lblPackage.getText();
	}

	public String getProjectName() {
		return lblProjectName.getText();
	}

	public String getUserName() {
		return lblUserName.getText();
	}
}
