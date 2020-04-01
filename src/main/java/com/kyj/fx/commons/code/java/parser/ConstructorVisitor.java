/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework.parser
 *	작성일   : 2018. 3. 31.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.code.java.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * @author KYJ
 *
 */
public class ConstructorVisitor<V> extends VoidVisitorAdapter<V> {

	private Predicate<ConstructorDeclaration> filter;
	private List<ConstructorDeclaration> items;

	public ConstructorVisitor(Predicate<ConstructorDeclaration> filter) {
		this.filter = filter;
		items = new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com
	 * .github.javaparser.ast.body.ConstructorDeclaration, java.lang.Object)
	 */
	@Override
	public void visit(ConstructorDeclaration n, V arg) {
		if (filter.test(n)) {
			items.add(n);
		}
	}

	/**
	 * @return the items
	 */
	public List<ConstructorDeclaration> getItems() {
		return items;
	}

}
