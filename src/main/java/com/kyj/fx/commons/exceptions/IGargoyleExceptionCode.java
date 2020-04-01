/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.exceptions
 *	작성일   : 2016. 7. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.exceptions;

/***************************
 * 
 * @author KYJ
 *
 ***************************/
public interface IGargoyleExceptionCode {

	/***************************
	 * 에러코드 정의.
	 * 
	 * @author KYJ
	 *
	 ***************************/
	/***************************
	 * 
	 * @author KYJ
	 *
	 ***************************/
	enum ERROR_CODE {

		EMPTY(""),

		/**
		 * 값이 NULL인경우.
		 * 
		 * @최초생성일 2016. 7. 18.
		 */
		NULL("NULL"),
		/**
		 * File 타입이 일치하지않는경우 .
		 * 
		 * @최초생성일 2016. 7. 14.
		 */
		FILE_DOES_NOT_MATCH("FILE_DOES_NOT_MATCH"),

		/**
		 * 파일이 존재하지않음.
		 * 
		 * @최초생성일 2016. 7. 18.
		 */
		FILE_NOT_FOUND("FILE_NOT_FOUND"),

		/**
		 * 파일이 이미 존재함.
		 * 
		 * @최초생성일 2016. 7. 18.
		 */
		FILE_ALREADY_EXISTS("FILE_ALREADY_EXISTS"),

		/**
		 * 파라미터 값이 빔.
		 * 
		 * @최초생성일 2016. 7. 14.
		 */
		PARAMETER_EMPTY("PARAMETER_EMPTY"),

		/**
		 * 접속불가 상태.
		 * 
		 * @최초생성일 2016. 7. 21.
		 */
		CONNECTION_FAIL("CONNECTION_FAIL");

		String code;

		ERROR_CODE(String code) {
			this.code = code;
		}

		public String getCodeMessage() {
			return code;
		}

	}

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 * 에러코드 리턴.
	 * 
	 * @return
	 ********************************/
	public ERROR_CODE getErrorCode();

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 *
	 * @return
	 ********************************/
	public default String getCodeMessage() {
		return getErrorCode().getCodeMessage();
	}
}
