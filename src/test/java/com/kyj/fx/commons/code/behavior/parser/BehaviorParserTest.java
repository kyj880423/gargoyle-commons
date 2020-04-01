package com.kyj.fx.commons.code.behavior.parser;

import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.code.behavior.reader.BehaviorReaderWrapper;
import com.kyj.fx.commons.utils.ValueUtil;

public class BehaviorParserTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorParserTest.class);

	@Test
	public void bfmReadTest() throws Exception {
		String code = "";
		// Read Script. From File.

		try (InputStream in = BehaviorParserTest.class.getResourceAsStream("FunctionsExamples.bfm")) {
			BehaviorReaderWrapper r = new BehaviorReaderWrapper(in);
			r.load();
			code = r.readScript();
		}

		// tray vb parse.
		BehaviorParser behaviorParser = new BehaviorParser(code);

		behaviorParser.setMethodVisitor(new BehaviorFunctionVisitor() {

			@Override
			public void visite(String code) {

				// Print MethodNames.
				String name = ValueUtil.regexMatch("(?i)Function[ ]+.+\\([.]{0,}\\)", code);
				LOGGER.debug("Method Name : {} ", name);
			}
		});

		behaviorParser.setSubVisitor(new BehaviorSubProcedureVisitor() {

			@Override
			public void visite(String code) {
				// Print MethodNames.
				String name = ValueUtil.regexMatch("(?i)Sub[ ]+.+\\([.]{0,}\\)", code);
				LOGGER.debug("Sub Name : {} ", name);

			}
		});
		behaviorParser.parse();
	}

	@Test
	public void wibReadTest() throws Exception {

		String code = "";
		// Read Script. From File.

		try (InputStream in = getClass().getResourceAsStream("FunctionsExamples.bfm")) {
			BehaviorReaderWrapper r = new BehaviorReaderWrapper(in);
			r.load();
			code = r.readScript();

		}

		// tray vb parse.
		BehaviorParser behaviorParser = new BehaviorParser(code);
		// behaviorParser.setMethodVisitor(new AbstractBehaviorVisitor() {
		//
		// @Override
		// public void visite(String code) {
		//
		// // Print MethodNames.
		// String name = ValueUtil.regexMatch("(?i)Function[ ]+.+\\([.]{0,}\\)",
		// code);
		// LOGGER.debug("Method Name : {} ", name);
		// }
		// });
		//
		// behaviorParser.setSubVisitor(new AbstractBehaviorVisitor() {
		//
		// @Override
		// public void visite(String code) {
		// // Print MethodNames.
		// String name = ValueUtil.regexMatch("(?i)Sub[ ]+.+\\([.]{0,}\\)",
		// code);
		// LOGGER.debug("Sub Name : {} ", name);
		//
		// }
		// });

		behaviorParser.setClassVisitor(new AbstractBehaviorVisitor() {

			@Override
			public void visite(String code) {

				LOGGER.info("Class Fully Code  : \n{} ", code);

			}
		});

		behaviorParser.parse();

	}
}
