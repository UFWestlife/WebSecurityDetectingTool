package cn.edu.neu.MJ.SQLInject;

import java.util.ArrayList;

public class TableInfo {
	private String tablename;
	private ArrayList<String> fieldname = new ArrayList<String>();
	private ArrayList<String> content = new ArrayList<String>();
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public ArrayList<String> getFieldname() {
		return fieldname;
	}
	public void setFieldname(ArrayList<String> fieldname) {
		this.fieldname = fieldname;
	}
	public ArrayList<String> getContent() {
		return content;
	}
	public void setContent(ArrayList<String> content) {
		this.content = content;
	}
}
