/********************************
 *	프로젝트 : VisualFxVoEditor
 *	패키지   : com.kyj.fx.voeditor.visual.component.grid
 *	작성일   : 2015. 10. 14.
 *	프로젝트 : SOS 미어캣 프로젝트
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.fx.controls.grid;

/**
 * 버튼 활성화 정보
 * 
 * @author KYJ
 *
 */
public class Buttons {

	public static final int ADD = 0x001;
	public static final int DELETE = 0x002;
	public static final int UPDATE = 0x004;
	public static final int SAVE = 0x008;

	public static final int OK = 0x010;
	public static final int NO = 0x020;
	public static final int YES = 0x040;
	public static final int CANCEL = 0x080;

	/**
	 * 추가, 삭제, 저장, 수정 버튼
	 * 
	 * @최초생성일 2015. 10. 14.
	 */
	private static final int ADD_DELETE_UPDATE_SAVE = Buttons.ADD | Buttons.DELETE | Buttons.SAVE | Buttons.UPDATE;

	public static final boolean isAdd(int mod) {
		return (mod & ADD) != 0;
	}

	public static final boolean isDelete(int mod) {
		return (mod & DELETE) != 0;
	}

	public static final boolean isUpdate(int mod) {
		return (mod & UPDATE) != 0;
	}

	public static final boolean isSave(int mod) {
		return (mod & SAVE) != 0;
	}

	public static final int useCudButtons() {
		return ADD_DELETE_UPDATE_SAVE;
	}

}
