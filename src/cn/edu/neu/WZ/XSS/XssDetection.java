package cn.edu.neu.WZ.XSS;

import cn.edu.neu.PublicClass.FormInfo;
import cn.edu.neu.PublicClass.Parser;
import cn.edu.neu.PublicClass.WebTools;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class XssDetection {
	private boolean xss;//判断该url是否能够进行跨站
      Parser pForm=new Parser();//解析表单所用解析器
      private ArrayList<Integer> index = new ArrayList<Integer>();//记录可跨站的表单下标
  	  private ArrayList<String> action = new ArrayList<String>(); //记录可跨站的表单的action
      public XssDetection(String url)
      {   
    	  try {
			pForm.start(url);//从url解析表单
			FormInfo temp=new FormInfo();
			WebTools wts = new WebTools();
			for(int i=0;i<pForm.getFIList().size();i++)//分析每个表单，测试能否跨站
			{
				temp=pForm.getFIList().get(i);
				if(temp.text_name!=null)
				{
					XssParser xp = new XssParser(url,temp,wts.charSet(new URL(url)));
					if (!xss)
						xss = xp.isXss();
					if (xp.isXss()){
						index.add(i+1);
						action.add(temp.action);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
	public boolean isXss() {
		return xss;
	}
	public ArrayList<Integer> getIndex() {
		return index;
	}
	public ArrayList<String> getAction() {
		return action;
	}
     
	public static void main(String[] args) {
		XssDetection wz=new XssDetection("http://192.168.100.221/news/default.asp");
		//输入参数args[0]为要检测的网站
		if(wz.xss==true)
		{
			System.out.println("危险！！该网页存在跨站漏洞！");
		}
		else
		{
			System.out.println("安全~~该网页不存在跨站漏洞~");
		}
	}
}
