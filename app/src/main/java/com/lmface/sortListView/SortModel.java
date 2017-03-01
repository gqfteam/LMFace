package com.lmface.sortListView;

public class SortModel {
	
	private String name;//��ʾ������
	private String sortLetters;//��ʾ����ƴ��������ĸ
	boolean isChecked;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}
}
