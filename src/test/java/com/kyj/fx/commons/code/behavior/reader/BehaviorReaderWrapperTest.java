package com.kyj.fx.commons.code.behavior.reader;

import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kyj.fx.commons.code.behavior.design.BehaviorVo;

public class BehaviorReaderWrapperTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorReaderWrapperTest.class);

	@Test
	public void readDesignTest() throws Exception {
		try (InputStream in = BehaviorReaderWrapperTest.class.getResourceAsStream("Inbound.wib")) {
			
			if(in == null)
			{
				LOGGER.info("WIB File does not exists.");
				return;
			}
				
			BehaviorReaderWrapper wrapper = new BehaviorReaderWrapper(in);
			wrapper.load();
			BehaviorVo readDesignLayout = wrapper.readDesignLayout();
			
			
			LOGGER.debug("{}", readDesignLayout.getDesignerLayoutVo());

		}
	}

}
