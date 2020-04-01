/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.scanner
 *	작성일   : 2019. 12. 6.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.scanner;

import java.util.List;

import org.junit.Test;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class ResourceScannerTest {

	@Test
	public void test() {

		ResourceScanner resourceScanner = new ResourceScanner(ResourceScannerTest.class);
		List<a> createInstances = resourceScanner.createInstances(a.class, c -> {

			try {
				return c.newInstance();

			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		});

		createInstances.forEach(a -> a.print());
	}

	public interface a {
		public void print();
	}

	public static class b implements a {

		@Override
		public void print() {
			System.out.println("b");
		}

	}

	public static class c implements a {

		@Override
		public void print() {
			System.out.println("c");

		}

	}

	public static class d implements a {

		@Override
		public void print() {
			System.out.println("d");

		}

	}

	public static class e implements a {

		@Override
		public void print() {
			System.out.println("e");

		}

	}
}
