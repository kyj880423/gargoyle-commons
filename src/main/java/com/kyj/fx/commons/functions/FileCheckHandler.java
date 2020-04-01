/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.framework
 *	작성일   : 2016. 7. 14.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

import java.io.File;
import java.io.FileNotFoundException;

/***************************
 * 
 * @author KYJ
 *
 ***************************/
public interface FileCheckHandler<R> {

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 * 존재하지않는 경우
	 * 
	 * @return
	 ********************************/
	public R notExists();

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 * Null인경우
	 * 
	 * @return
	 ********************************/
	public R ifNull();

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 * File이 일치하는지 여부
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 ********************************/
	public boolean isMatch(File file) throws Exception;

	/********************************
	 * 작성일 : 2016. 7. 14. 작성자 : KYJ
	 *
	 *
	 * @return
	 ********************************/
	public R notMatchThan();
}
