package com.kyj.fx.commons.code.behavior.reader;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BehaviorReaderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorReaderTest.class);

	@Test
	public void test() throws Exception {

		// BehaviorReader original = new BehaviorReader(new
		// File("C:\\SVN_WORKSPACE\\wwwroot\\WF\\bin\\behaviors\\EMR_COMET_COMPONENT_DISPENSE.wib"));
		// LOGGER.debug("{}", original.readBehavior());
		//
		// LOGGER.debug("########################################################");
		// LOGGER.debug("########################################################");
		
		try (BehaviorReader reader = new BehaviorReader(BehaviorReaderTest.class.getResourceAsStream("ZTEST_Gargoyle-Behavior-Test.wib"))) {
			LOGGER.debug("{}", reader.readBehavior());
		}

	}

}
