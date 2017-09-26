package cn.edu.neu.LY.WebTro;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class urlReader {
	public static String getHTML(String tempurl)  //获取页面内容
	{
		  URL url = null;
		  BufferedReader breader = null ;
		  InputStream is = null ;
		  StringBuilder resultBuffer = new StringBuilder();  //页面内容
		  
		 //读取页面内容 存入resultBuffer
		  try {
		   url = new URL(tempurl);
		   HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		   is = conn.getInputStream();
		   breader = new BufferedReader(new InputStreamReader(is));
		   String line = "";
		   while((line = breader.readLine()) != null){
		    resultBuffer.append(line);
		   }
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
		   try {
			breader.close();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  }
		  return resultBuffer.toString();   //返回String类型

}
}
