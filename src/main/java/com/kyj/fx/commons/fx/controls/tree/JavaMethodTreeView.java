/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.tree
 *	작성일   : 2018. 3. 31.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.tree;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.kyj.fx.commons.code.java.parser.GargoyleJavaParser;
import com.kyj.fx.commons.fx.controls.text.JavaTextView;
import com.kyj.fx.commons.utils.FileUtil;
import com.kyj.fx.commons.utils.FxUtil;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 
 * 자바 파일을 파싱하여 그 결과로 <br/>
 * 메소드 목록을 조회하기 편하게 만든 TreeView <br/>
 * 
 * 
 * support value type :: File, InputStream, String <br/>
 * 
 * @author KYJ
 *
 */
public class JavaMethodTreeView<T> extends MethodTreeView<MethodItem> {

	// private ObjectProperty<File> javaFile = new SimpleObjectProperty<>();

	public JavaMethodTreeView() {
		super();
		setCellFactory(forTreeView);
		setShowRoot(false);
		addEventHandler(MouseEvent.MOUSE_CLICKED, this::tvOnMouseClick);
		FxUtil.installClipboardKeyEvent(this);
	}

	public void tvOnMouseClick(MouseEvent e) {
		if (e.getClickCount() == 2 && MouseButton.PRIMARY == e.getButton()) {
			TreeItem<MethodItem> item = this.getSelectionModel().getSelectedItem();
			if (item != null) {
				MethodItem value = item.getValue();
				Object reference = value.getReference();

				if (reference instanceof MethodDeclaration) {
					MethodDeclaration d = (MethodDeclaration) reference;
					FxUtil.showPopOver(this, new JavaTextView(d.toString()));
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kyj.fx.voeditor.visual.component.tree.MethodTreeView#isValid(java.io.
	 * File)
	 */
	public boolean isValid(File f) {
		if (FileUtil.isJavaFile(f))
			return true;
		return false;
	}

	public boolean isValid(InputStream in) {
		return in != null;
	}

	/**
	 * 파일이 새로 변경되면 파일을 읽은후 <br/>
	 * 파싱하여 트리를 구성한다. <br/>
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 3. 31.
	 * @param n
	 * @return
	 */
	@Override
	public void onUpdate(File n) {
		if (n != null && n.exists()) {
			if (!isValid(n))
				return;

			try {
				TreeItem<MethodItem> root = new TreeItem<>();
				CompilationUnit compileUnit = GargoyleJavaParser.getCompileUnit(n);
				compileUnit.accept(new VoidVisitorAdapter<Object>() {

					@Override
					public void visit(MethodDeclaration n, Object arg) {
						root.getChildren().add(toMethodItem(n));
						super.visit(n, arg);
					}

				}, null);

				setRoot(root);
				root.setExpanded(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpdate(String str) {
		onUpdate(new ByteArrayInputStream(str.getBytes()));
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 5. 3.
	 * @param in
	 */
	@Override
	public void onUpdate(InputStream in) {
		if (!isValid(in))
			return;
		try {
			TreeItem<MethodItem> root = new TreeItem<>();
			CompilationUnit compileUnit = GargoyleJavaParser.getCompileUnit(in);
			compileUnit.accept(new VoidVisitorAdapter<Object>() {

				@Override
				public void visit(MethodDeclaration n, Object arg) {
					root.getChildren().add(toMethodItem(n));
					super.visit(n, arg);
				}

			}, null);

			setRoot(root);
			root.setExpanded(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	Callback<TreeView<MethodItem>, TreeCell<MethodItem>> forTreeView = TextFieldTreeCell.forTreeView(new StringConverter<MethodItem>() {

		@Override
		public String toString(MethodItem item) {

			Object reference = item.getReference();
			if (reference instanceof MethodDeclaration) {
				MethodDeclaration d = (MethodDeclaration) reference;

				Optional<String> reduce = d.getParameters().stream().map(a -> a.getType().toString())
						/* .map(a -> a.getName()) */.reduce((a, b) -> {
							return a.concat(" ").concat(b);
						});
				String param = "";
				if (reduce.isPresent()) {
					param = reduce.get();
				}

				return String.format("%s (%s)", d.getName(), param);
			} else {
				return item.getName();
			}
		}

		@Override
		public MethodItem fromString(String string) {
			return null;
		}

	});

}
