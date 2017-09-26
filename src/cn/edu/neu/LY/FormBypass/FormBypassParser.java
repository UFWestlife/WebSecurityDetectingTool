package cn.edu.neu.LY.FormBypass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class FormBypassParser {
	private String html;
	private String action=null;
	private String p_name;
	private String n_name;
	private String charset;
	ArrayList<String> type=new ArrayList<String>();
	ArrayList<String> name =new ArrayList<String>();
	public FormBypassParser(String urls) throws IOException{
		Document doc =Jsoup.connect(urls).get();
		Elements eles=doc.select("form");
		if(eles!=null){
			this.html=doc.html();
			this.action=doc.select("form").attr("action");
			this.charset=doc.charset().name();
		}
		URL url =new URL(urls);
		URLConnection urlConnection = url.openConnection();// 打开url连接
		InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());//返回从此打开的连接读取的输入流
		BufferedReader br = new BufferedReader(isr);
		String all="";
		String str = br.readLine();
		while (str!=null){//将所以网页内容存到all里
			all+=str;
			str=br.readLine();
		}
		String s=new String(all);
//		System.out.println(s);
		String sub=null;
		while (true) {
			int begin = s.toLowerCase().indexOf("<form");
//			System.out.println(begin);
			if (begin != -1) {
				int end = s.toLowerCase().indexOf("</form>");
//				System.out.println(end);
				if(end==-1){
					end=s.toLowerCase().lastIndexOf(">");
				}
				else{
					end+=7;
				}
				sub = s.substring(begin, end);//<form...></form>
				System.out.println(sub);
				parseForm(sub);
				s = s.substring(end);
			} else {                 //如果没有form表单则不存在绕过
				break;
			}
		}
	}

	public void parseForm(String form){
		String sub;
		String sub1;
		String type;
		String name;
		int index;
		while (true) {
			int begin = form.toLowerCase().indexOf("<input");//找到第一个input标签 输入用户名的位置
			if(begin==-1){
				break;
			}
			sub = form.substring(begin);
			int end = sub.toLowerCase().indexOf(">") + 1;
			sub1 = sub.substring(0, end);
//			System.out.println(sub1);
			type = getContent(sub1, "type");
//			System.out.println(type);
			this.type.add(type);
			name = getContent(sub1, "name");
			this.name.add(name);
			form=sub.substring(end);
		}
		index=this.type.indexOf("password");
//		System.out.println(index);
		if(index==-1){
			this.n_name=null;
			this.p_name=null;
		}
		else{
			this.p_name=this.name.get(index);
			int i=0;
			i=this.type.indexOf("text");
			if(i==-1){
				this.n_name=this.name.get(0);
			}else{
				this.n_name=this.name.get(i);
			}
		}
	}
    public String getContent(String element,String s){//从input element中找到s(name,type)的值
        int t;
        int t1;
        int t2;
        String sub;
        String str="\"";
        t=element.indexOf(s);
        if (t!=-1) {
            t1 = t + s.length()+1;  //等号后面
            sub = element.substring(t1);
            int index=sub.indexOf(str);
            if (index!=-1) {  			 //有引号
//				System.out.println("true");
                t1=1;//引号后面

                if (sub.indexOf(" ") != -1) {
                    t2 = sub.indexOf(" ") - 1;			//属性后面是空格“ ”
                } else {
                    t2 = sub.indexOf(">") - 1;  		 //属性后面直接是结束符“>”
                }
            }
            else{                    //没引号
                t1=0;
                if (sub.indexOf(" ") != -1) {
                    t2 = sub.indexOf(" ");
                } else {
                    t2 = sub.indexOf(">");
                }
            }
            sub = sub.substring(t1, t2);
            return sub;
        }
        else{
            return null;
        }

    }
	public String getN_name(){
		return this.n_name;
	}
	public String getP_name(){
		return this.p_name;
	}
	public String getAction(){
		return this.action;
	}
	public String getCharset(){
		return this.charset;
	}
	/*	获取网页中所有链接
	 * try {
			Document doc = Jsoup.connect("http://www.neu.edu.cn").get();

			Elements links= doc.select("a[href]");
			System.out.println("links: "+links.size());
			for(Element link:links){
				 String str=link.attr("abs:href");
				 System.out.println(str);
			}
//			System.out.println(title);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}



