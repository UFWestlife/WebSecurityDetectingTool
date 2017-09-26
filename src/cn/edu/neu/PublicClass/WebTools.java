package cn.edu.neu.PublicClass;

import cn.edu.neu.WZ.XSS.StringTools;
import info.monitorenter.cpdetector.io.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class WebTools {
	//判断网页编码
	public String charSet(URL url) throws IOException{
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		Map<String, List<String>> map = connection.getHeaderFields();
		Set<String> keys = map.keySet();
		Iterator<String> iterator = keys.iterator();

		// 遍历,查找字符编码
		String key = null;
		String tmp = null;
		while (iterator.hasNext()) {
		    key = iterator.next();
		    tmp = map.get(key).toString().toLowerCase();
		    // 获取content-type charset
		    if (key != null && key.equals("Content-Type")) {
		        int m = tmp.indexOf("charset=");
		        if (m != -1) {
		            String strencoding = tmp.substring(m + 8).replace("]", "");
		            return strencoding;
		        }
		    }
		}
		
		CodepageDetectorProxy codepageDetectorProxy = CodepageDetectorProxy.getInstance();
	    codepageDetectorProxy.add(JChardetFacade.getInstance());
	    codepageDetectorProxy.add(ASCIIDetector.getInstance());
	    codepageDetectorProxy.add(UnicodeDetector.getInstance());
	    codepageDetectorProxy.add(new ParsingDetector(false));
	    codepageDetectorProxy.add(new ByteOrderMarkDetector());
	    Charset charset = codepageDetectorProxy.detectCodepage(url);
	    return charset.name();
	}
	
	//比较网页内容，匹配度高于95%则判为相同页面
	public boolean Compare(ArrayList<String> l1,ArrayList<String> l2){
		ArrayList[] List = {new ArrayList<String>(l1), new ArrayList<String>(l2)};
		int longer;
		if (List[0].size()>List[1].size())
			longer = 0;
		else 
			longer = 1;
		int shorter = 1-longer;
		//System.out.println((double)(List[shorter].size())/(double)(List[longer].size()));
		if ((double)(List[shorter].size())/(double)(List[longer].size())<0.9)
			return false;
		for (int i = 0; i <  List[longer].size(); i++) {
			String line = (String)(List[longer].get(i));
			Iterator<String> it=List[shorter].iterator(); 
			while(it.hasNext()){
				String temp=it.next();
				if(line.equals(temp)){
					it.remove();
					break;
				}
			}
		}
		if ((double)(List[shorter].size())/(double)(List[longer].size())>0.05)
			return false;
//		System.out.println((double)(List[shorter].size())/(double)(List[longer].size()));
		return true;
	}
	
	public static boolean isValid(String strLink) {//**********************************************
		String strurl=new StringTools().startsWithHTTP(strLink);
		URL url;
		try {
		url = new URL(strurl);
		HttpURLConnection connt = (HttpURLConnection)url.openConnection();
		connt.setRequestMethod("HEAD");
		String strMessage = connt.getResponseMessage();
		if (strMessage.compareTo("Not Found") == 0) {
		    return false;
		}
		connt.disconnect();
		} catch (Exception e) {
		    return false;
		}
		return true;
	}
}
