package cn.edu.neu.MJ.SQLInject;

import java.util.ArrayList;

public class SqlInjectInfo {
	private boolean check = false;
	private boolean get = false;
	private boolean post = false;
	private boolean getSearch = false;
	private String dbtype;
	private ArrayList<TableInfo> tiList = new ArrayList<TableInfo>();
	
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	public boolean isGet() {
		return get;
	}
	public void setGet(boolean get) {
		this.get = get;
	}
	public boolean isPost() {
		return post;
	}
	public void setPost(boolean post) {
		this.post = post;
	}
	public boolean isGetSearch() {
		return getSearch;
	}
	public void setGetSearch(boolean getSearch) {
		this.getSearch = getSearch;
	}
	public ArrayList<TableInfo> getTiList() {
		return tiList;
	}
	public void setTiList(ArrayList<TableInfo> tiList) {
		if (this.tiList.size() == 0)
			this.tiList = tiList;
		for (int j=0;j<tiList.size();j++){
			boolean re = false;
			for (int i=0;i<this.tiList.size();i++)
				if (tiList.get(j).getTablename().equals(this.tiList.get(i).getTablename())){
					re = true;
					break;
				}
			if (re)
				continue;
			this.tiList.add(tiList.get(j));
		}
	}
	public String getDbtype() {
		return dbtype;
	}
	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
	
	public SqlInjectInfo clone(){
		SqlInjectInfo sii = null;
		try {
			sii = (SqlInjectInfo)(super.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sii;
	}
}
