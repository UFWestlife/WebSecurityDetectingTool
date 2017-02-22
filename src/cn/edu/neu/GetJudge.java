package cn.edu.neu.MJ.SQLInject;


import cn.edu.neu.HJ.ProcedureOrientedJAVA.TheParam;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;

public class GetJudge {
	public String sqltype;//记录sql注入的类型
	public boolean Judge(String SqlInjectionUrl){
		if (SqlInjectionUrl.indexOf('?')==-1)//如果指定url中不存在参数则不可能存在普通的get注入，返回false
			return false;
		
		String str=SqlInjectionUrl+"%20and%201=1";// and 1=1
		String str1=SqlInjectionUrl+"%20and%201=2";// and 1=2
		
		String str2=SqlInjectionUrl+"'%20and%20'1'='1";// ’ and ‘1’=‘1
		String str3=SqlInjectionUrl+"'%20and%20'1'='2";// ' and '1'='2
		
		String str4=SqlInjectionUrl+"%'%20and%201=1%20and%20'%'='";
		String str5=SqlInjectionUrl+"%'%20and%201=2%20and%20'%'='";
		
		choose c =new choose();
		
		if(c.truth(SqlInjectionUrl, str)&&(!c.truth(SqlInjectionUrl, str1)))//检测是否为int型的get注入
		{//检测原网页与注入后网页的匹配度
			this.sqltype="intGet";
			System.out.println("intGet");
			mf.printSQLInjectProcess("存在get注入\n");
			mf.printSQLInjectProcess("注入类型为：intGet" + "\n");
			TheParam.getParams().isSQL = true;
			TheParam.getParams().sqlInjectType.add("intGet");
			mf.printSQLInjectTypeResult("整型Get注入\n");
			return true;
		}
		else if(c.truth(SqlInjectionUrl, str2)&&(!c.truth(SqlInjectionUrl, str3)))//检测是否char型的get注入
		{
			this.sqltype="charGet";
			System.out.println("charGet");
			mf.printSQLInjectProcess("存在get注入\n");
			mf.printSQLInjectProcess("注入类型为：charGet" + "\n");
            TheParam.getParams().isSQL = true;
            TheParam.getParams().sqlInjectType.add("charGet");
			mf.printSQLInjectTypeResult("字符型Get注入\n");
			return true;
		}else if(c.truth(SqlInjectionUrl, str4)&&(!c.truth(SqlInjectionUrl, str5)))//检测是否char型的get注入
		{
			this.sqltype="searchGet";
			System.out.println("searchGet");
			mf.printSQLInjectProcess("存在get注入\n");
			mf.printSQLInjectProcess("注入类型为：searchGet" + "\n");
            TheParam.getParams().isSQL = true;
            TheParam.getParams().sqlInjectType.add("searchGet");
			mf.printSQLInjectTypeResult("搜索Get注入\n");
			return true;
		}
		else
		{
			System.out.println("noGet");
			mf.printSQLInjectProcess("不存在get注入" + "\n");
			return false;
		}
			
	}
	
	//返回sql注入的注入类型
	public String getType(){
		return this.sqltype;
	}

}
