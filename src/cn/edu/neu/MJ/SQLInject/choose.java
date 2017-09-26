package cn.edu.neu.MJ.SQLInject;



public class choose {
	public boolean timeout = false;
    public boolean truth(String url,String sqlurl){
    	GetWebContent gwc=new GetWebContent();
		
//		System.out.println(url);
//		System.out.println(sqlurl);
	
		if(gwc.RepareContent(url, sqlurl))//比较原页面与注入后的页面内容，匹配度达到指定高度则为真
			return true;
		else{
			if (gwc.timeout)
				timeout = true;
			return false;
		}
    }
}
