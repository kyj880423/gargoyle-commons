package com.kyj.fx.commons.code.java.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

/**
 * 2018.12.15 KYJ
 * jar 파일과 src 파일을 링크를 걸 수 있는 좋은 방법을 마련해야함.
 * 이 문제를 해결하기 위해 SrcLinkStore.java 라는 파일을 이용한 방법을 마련하고자함.
 * 해당 클래스는 Gargoyle.db안에 테이블을 정의하고 정의된 테이블에 링크 위치를 저장하여
 * 사용자별로 설정 및 관리하는 Location을 지정.
 * 다행히 Gargoyle에 Configuration 메뉴에 Java 환경을 링크할 수 있는 
 * 기능은 어느정도 구성은해놓음.
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class GargoyleJavaParserTest {

	private String javaHome;
	private File lookupSrcZip;

	@Before
	public void init() {
		// javaHome = System.getProperty("java.home");
		// File file = new File(javaHome);
		// lookupSrcZip = GargoyleJavaParser.lookupSrcZip(file);
		// System.out.println("prepared.");
	}

	@Test
	public void testFromSrcZip() throws IOException {
		// if (lookupSrcZip == null)
		// Assert.fail();

		
		String javaHome = System.getenv("JAVA_HOME");
		System.out.println(javaHome);
		String srcZip = javaHome + "\\src.zip";
		lookupSrcZip = new File(srcZip);
		// new ZipFile(srcZip).stream().forEach(ent -> {
		// System.out.println(ent.getName());
		// });
		CompilationUnit unit = GargoyleJavaParser.fromSrcZip(lookupSrcZip, "java/util/HashMap.java");
		unit.accept(new GenericVisitorAdapter<Void, Void>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com
			 * .github.javaparser.ast.body.ConstructorDeclaration,
			 * java.lang.Object)
			 */
			@Override
			public Void visit(ConstructorDeclaration n, Void arg) {

				if ("HashMap".equals(n.getName())) {
					System.out.println(n.getName() );
					List<Parameter> parameters = n.getParameters();
					for (Parameter param : parameters) {
						String name = param.toString();
						System.out.print(name + " ," );
					}
					System.out.println();
					
				}

				// if (n.contains(new ClassOrInterfaceType("HashMap"))) {
				// System.out.println("contains.....");
				// return super.visit(n, arg);
				// }
				// if (Modifier.isPublic(n.getModifiers()))
				return super.visit(n, arg);
//				return null;
			}

		}, null);
		unit.accept(new MethodVisitor(md -> {
			System.out.println(md.getName());
		}) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.github.javaparser.ast.visitor.VoidVisitorAdapter#visit(com.
			 * github.javaparser.ast.body.ConstructorDeclaration,
			 * java.lang.Object)
			 */
			@Override
			public void visit(ConstructorDeclaration cd, Object arg) {
				super.visit(cd, arg);

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.kyj.fx.commons.code.java.parser.MethodVisitor#visit(com.
			 * github.javaparser.ast.body.MethodDeclaration, java.lang.Object)
			 */
			@Override
			public void visit(MethodDeclaration md, Object arg) {
				// super.visit(n, arg);

			}

		}, null);

	}

}
