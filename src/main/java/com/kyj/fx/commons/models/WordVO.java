package com.kyj.fx.commons.models;

public class WordVO {
	private String item_kor, item_eng, item_shot, item_size, item_desc, gubun, use_yn;

	public WordVO(String item_kor, String item_eng, String item_shot, String item_size, String item_desc, String gubun, String use_yn) {
		super();
		this.item_kor = item_kor;
		this.item_eng = item_eng;
		this.item_shot = item_shot;
		this.item_size = item_size;
		this.item_desc = item_desc;
		this.gubun = gubun;
		this.use_yn = use_yn;
	}

	public WordVO() {
		super();
	}

	public String getItem_kor() {
		return item_kor;
	}

	public void setItem_kor(String item_kor) {
		this.item_kor = item_kor;
	}

	public String getItem_eng() {
		return item_eng;
	}

	public void setItem_eng(String item_eng) {
		this.item_eng = item_eng;
	}

	public String getItem_shot() {
		return item_shot;
	}

	public void setItem_shot(String item_shot) {
		this.item_shot = item_shot;
	}

	public String getItem_size() {
		return item_size;
	}

	public void setItem_size(String item_size) {
		this.item_size = item_size;
	}

	public String getItem_desc() {
		return item_desc;
	}

	public void setItem_desc(String item_desc) {
		this.item_desc = item_desc;
	}

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

}
