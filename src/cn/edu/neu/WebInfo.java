package cn.edu.neu.MJ.SQLInject;


public class WebInfo {
	private String url;
	private boolean spider = false;
	private SqlInjectInfo sii = null;//sql注入
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isSpider() {
		return spider;
	}
	public void setSpider(boolean spider) {
		this.spider = spider;
	}
	public SqlInjectInfo getSii() {
		return sii;
	}
	public void setSii(SqlInjectInfo sii) {
		this.sii = sii;
	}
	
}
