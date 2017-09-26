package cn.edu.neu.WZ.XSS;

public class StringTools {
	//将boolean转为int 0 1
	public static int BTI(boolean b){
		if (b)
			return 1;
		else
			return 0;
	}
	
	//将boolean转为字符串"0" "1"
	public static String BTIS(boolean b){
		if (b)
			return "1";
		else 
			return "0";
	}
	
	//将boolean转为"N" "Y" 
	public static String BTISYN(boolean b){
		if (b)
			return "Y";
		else
			return "N";
	}
	
	//让没有以http://开头的url转为以其为开头的
	public static String startsWithHTTP(String s){
		if (s.toLowerCase().startsWith("http://")){
			return s;
		}
		return "http://"+s;
	}
	
	//比较两个url，在没有最后的"/"的情况下
	public static boolean equalsWithoutLD(String str1,String str2){
		String s1 = new String(str1);
		String s2 = new String(str2);
		while (s1.lastIndexOf("/")==s1.length()-1) s1=s1.substring(0,s1.length()-1);
		while (s2.lastIndexOf("/")==s2.length()-1) s2=s2.substring(0,s2.length()-1);
		return s1.equals(s2);
	}
	
	//找到文档里第二个http:// 或 https://，哪个靠前取哪个
	public static int findSecondHttpOrHttps(String s){
		int t1 = s.indexOf("\nhttp://");
		int t2 = s.indexOf("\nhttps://");
		if (t1 == -1 && t2 == -1)
			return -1;
		if (t1 == -1)
			t1 = s.length();	
		if (t2 == -1)
			t2 = s.length();
		return t1<t2?t1:t2;
	}
	
	//比较两个url，在不带参数的情况下
	public static boolean equalsWithoutParameter(String url1,String url2){
		String top = stringWithoutIM(url1);
		String back = stringWithoutIM(url2);
		if (top.equals(back))
			return true;
		else
			return false;
	}
	
	//将url尾部的？+参数删掉
	public static String stringWithoutIM(String s){
		if (s.indexOf('?') == -1)
			return s;
		else
			return s.substring(0,s.indexOf('?'));
	}
	
	//用于将string存入数据库中，如果string里有' 则用转义字符''代替
	public static String dealWithSQ(String s){
		String re = "";
		while (s!=null && s!=""){
			if (s.indexOf('\'')!=-1){
				re += s.substring(0,s.indexOf('\'')+1);
				re += "\'";
				s = s.substring(s.indexOf('\'')+1);
			}else{
				re += s;
				break;
			}
		}
		return re;
	}
}
