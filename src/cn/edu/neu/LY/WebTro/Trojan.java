package cn.edu.neu.LY.WebTro;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Trojan {
	private String url;
	private String str;
//	ArrayList<String> str=new ArrayList<String>();
	public Trojan(String url){
		this.url=url;
		str=urlReader.getHTML(this.url);
	}
	
	public boolean test(){
		boolean temp=false;//false表示没有木马
		Readfile rft=new Readfile("features.txt");
//		System.out.println(rft.al.get(0));
//		Readfile rf=new Readfile("hhh.txt");//hhh.txt和features.txt内容一样
		String a[]=new String[rft.al.size()];
//		String s=null;
//		for(int j=0;j<str.size();j++){
//			s+=str.get(j);
////			System.out.println(s);
//		}
		for(int i=0;i<rft.al.size();i++){
			a[i]=(String)rft.al.get(i);
			Pattern p=Pattern.compile(a[i],Pattern.CASE_INSENSITIVE);
			Matcher m=p.matcher(str);
			if(m.find()){
				System.out.println("此网站存在木马");
				temp=true;
				break;
			}
		}
		if(!temp){
		System.out.println("此网站不存在木马");}
		return temp;
	}
	/**
	 * @param args
	 * @throws IOException 
	 */

}
