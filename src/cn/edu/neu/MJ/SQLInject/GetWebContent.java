package cn.edu.neu.MJ.SQLInject;

import cn.edu.neu.PublicClass.WebTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class GetWebContent {
	public boolean timeout = false;
	public ArrayList<String> GetContent(String urlString){
		ArrayList<String> list = new ArrayList<String>();
		URL url;
		try {
			url = new URL(urlString);
			
			URLConnection urlConnection;
			urlConnection = url.openConnection();
			urlConnection.setReadTimeout(5000);//设置连接超时
			InputStreamReader isr;//从标准输入读取数据
			isr = new InputStreamReader(urlConnection.getInputStream());//返回从此打开的连接读取的输入流
			BufferedReader br = new BufferedReader(isr);
//			System.out.println("+++++++++++++++++++++++++++");
			String str;
			str = br.readLine();//读取一个文本行
			while(str!=null){
				list.add(str);
				str=br.readLine();
			}
		}catch (SocketTimeoutException e){
			System.out.println("TimeOUT:  "+urlString);//连接网页超时异常
			timeout = true;
			return null;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		//for(String aText : list){System.out.println(aText);}
		//System.out.println("\n");
		return list;
	}

	public boolean RepareContent(String urltop,String urlback){
		//System.out.println(urltop);
		//System.out.println(urlback);
		ArrayList<String> topcontent = GetContent(urltop);//获取原页面的内容
		ArrayList<String> backcontent = GetContent(urlback);//获取注入后页面的内容
		if (timeout)
			return false;
		
		WebTools wt=new WebTools();
		//for (String a : topcontent){System.out.println(a);}
		//System.out.println("11111\n");
		//for (String a : backcontent){System.out.println(a);}
		if(wt.Compare(topcontent, backcontent)){//比较原页面与注入后的页面内容是否相似，相似度达到指定要求返回真
			return true;
		}
		else{
			return false;
		}
	}

}
