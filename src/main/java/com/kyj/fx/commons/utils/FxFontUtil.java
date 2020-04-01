/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.utils
 *	작성일   : 2018. 6. 11.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * @author KYJ
 *
 */
public class FxFontUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FxFontUtil.class);

	/**
	 * @최초생성일 2018. 6. 11.
	 */
	public static final String DEFAULT_FONT_SIMPLE = "NANUMBARUNGOTHIC";
	public static final String DEFAULT_FONT = DEFAULT_FONT_SIMPLE + ".TTF";
	public static final String FONTS_NANUMBARUNGOTHIC_TTF = "fonts/" + DEFAULT_FONT;

	public static Font getBoldFont() {
		return getBoldFont(12d);
	}

	public static Font getBoldFont(double fontSize) {
		return Font.font(DEFAULT_FONT_SIMPLE, FontWeight.BOLD, FontPosture.ITALIC, fontSize);
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 5. 13.
	 * @param fontFile
	 */
	private static void loadFont(File fontFile) {
		try {

			// always overwrite font.
			try (InputStream is = ClassLoader.getSystemResource(FONTS_NANUMBARUNGOTHIC_TTF).openStream()) {
				FileUtil.copy(is, fontFile);
			}

			try (FileInputStream in = new FileInputStream(fontFile)) {
				Font loadFont = Font.loadFont(in, 12);
				LOGGER.debug("load Font : {} ", loadFont);
			}

//			printFont();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 5. 13.
	 */
	public static void printFont() {
		LOGGER.debug("### font names ###");
		Font.getFontNames().forEach(str -> {
			LOGGER.debug(str);
		});
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 5. 13.
	 */
	public static void loadFont() {

		/*
		 * 2017-04-24 Font가 jar파일안에 압축되어있는경우 Temp 폴더에 임시 파일이 계속 쌓임. 관련된 버그수정을 위해
		 * Font를 임시디렉토리로 복사한후 읽어옴.
		 */
		File parentFile = new File(FileUtil.getTempGagoyle(), "font");
		if (!parentFile.exists())
			parentFile.mkdirs();

		File fontFile = new File(parentFile, DEFAULT_FONT);

		loadFont(fontFile);

	}
}
