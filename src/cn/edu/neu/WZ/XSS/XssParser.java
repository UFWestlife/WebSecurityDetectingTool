package cn.edu.neu.WZ.XSS;

import cn.edu.neu.PublicClass.FormInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;


public class XssParser {
	String nameValue = "<script>alert(\"跨站\")</script>";//检测跨站的输入文本
	String aimUrl = null;
	private boolean xss = false;
	public XssParser(String url, FormInfo temp,String charset) {
		if(temp.action==null||temp.action.contains("http:"))
		{
			this.aimUrl=temp.action;
		}
		else 
		{
			this.aimUrl = url.substring(0,url.lastIndexOf("/")+1);
			this.aimUrl += temp.action;	
			System.out.println("正在向   " + this.aimUrl + " 中写入跨站脚本...");
			mf.setXSSProcessText("正在向   " + this.aimUrl + " 中写入跨站脚本...\n");
		}
		
		if (temp.method2.toLowerCase().equals("post")) {
			
			postParser(temp,charset);
		} else {
			getParser(temp,charset);
		}
	}

	public void getParser(FormInfo temp,String charset) {//get 方式提交
		ArrayList<String> text_Name=new ArrayList<String>();
		text_Name=temp.text_name;
		try {
			StringBuffer formDataItems = new StringBuffer();
			for (int j = 0; j < text_Name.size(); j++) 
			{
				formDataItems.append(text_Name.get(j));
				formDataItems.append("=");
				formDataItems.append(nameValue);
				if (j != (text_Name.size() - 1))
					formDataItems.append("&");
			}
			URL url=new URL(aimUrl+"?"+formDataItems);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));
			String str="";
			String total="";
			while((str=br.readLine())!=null){
//			System.out.println(str); //网页源码
              total+=str;
			}
			if(total.contains("<script>alert(\"跨站\")</script>")){
				System.out.println("成功get  <script>alert(\"跨站\")</script>");
				mf.setXSSProcessText("成功get  <script>alert(\"跨站\")</script>\n");
				mf.setVector("<script>alert(\"跨站\")</script>\n");
				mf.setSubmitType("get\n");
				xss = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}//end get
	public void postParser(FormInfo temp,String charset) //post方式提交
	{
		try{
			ArrayList<String> text_Name=new ArrayList<String>();
			text_Name=temp.text_name;
	        URL url = new URL(this.aimUrl);   
	        URLConnection connection = url.openConnection();      
	        connection.setDoOutput(true);        
	        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), charset);     
	        StringBuffer formDataItems = new StringBuffer();
	        for (int j = 0; j < text_Name.size(); j++) 
			{
				formDataItems.append(text_Name.get(j));
				formDataItems.append("=");
				formDataItems.append(nameValue);
				if (j != (text_Name.size() - 1))
					formDataItems.append("&");
			}
	        out.write(formDataItems.toString());
	        out.flush();     
	        out.close();   
	        /**   
	         * 这样就可以发送一个看起来象这样的POST：    
	         * POST /jobsearch/jobsearch.cgi HTTP 1.0 ACCEPT:   
	         * text/plain Content-type: application/x-www-form-urlencoded   
	         * Content-length: 99 username=bob password=someword   
	         */    
	       
	        String sCurrentLine;     
	        String sTotalString;     
	        sCurrentLine = "";     
	        sTotalString = "";     
	        InputStream l_urlStream;     
	        l_urlStream = connection.getInputStream();     
	        // 传说中的三层包装阿！     
	        BufferedReader l_reader = new BufferedReader(new InputStreamReader(     
	                l_urlStream));     
	        connection.connect();
	        while ((sCurrentLine = l_reader.readLine()) != null) {     
	            sTotalString += sCurrentLine + "\r\n";     
	    
	        }     
	        if(sTotalString.contains("<script>alert(\"跨站\")</script>")){
				System.out.println("成功post <script>alert(\"跨站\")</script>");
				mf.setXSSProcessText("成功post <script>alert(\"跨站\")</script>\n");
				mf.setSubmitType("post\n");
				mf.setVector("<script>alert(\"跨站\")</script>\n");
				xss = true;
			}
			}
			catch(Exception e)
			{
		
			}
	}
	public boolean isXss() {
		return xss;
	}
	

}
