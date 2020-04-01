/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.parser
 *	작성일   : 2018. 3. 31.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.java.parser;

import java.util.function.Consumer;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * @author KYJ
 *
 */
public class MethodVisitor extends VoidVisitorAdapter<Object> {

	Consumer<MethodDeclaration> visiter;

	public MethodVisitor(Consumer<MethodDeclaration> visiter) {
		this.visiter = visiter;
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		this.visiter.accept(n);
		super.visit(n, arg);
	}

}
