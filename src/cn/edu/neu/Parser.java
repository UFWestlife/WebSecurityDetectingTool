package cn.edu.neu.PublicClass;

import cn.edu.neu.HJ.ProcedureOrientedJAVA.TheParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;

public class Parser {
	private ArrayList<FormInfo> fiList = new ArrayList<FormInfo>();//存form内容的list
	private String n_name;//用户名的name
	private String p_name;//口令的name
	private int index;//用户名和口令所在的form在fiList中的下标
//	private String method=null;
	
	public void start(String urlstr) throws IOException{
		URL url =new URL(urlstr);
		URLConnection urlConnection = url.openConnection();// 打开url连接
		InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
		//返回从此打开的连接读取的输入流
				
		BufferedReader br = new BufferedReader(isr);
		String all="";
		String s = br.readLine();
		while (s!=null){//将所有网页内容存到all里
			all+=s;
			s=br.readLine();
		}
		parse(all);//解析所有网页内容
	}
	private void parse(String all){
		String s = all;
		String sub;
		int begin;
		int end;
		while (true){
			begin = s.toLowerCase().indexOf("<form");
			end = s.toLowerCase().indexOf("</form>");
			if (end==-1)
				end=s.length()-8;
			if (begin == -1)
				break;
			sub = s.substring(begin,end+7);//找到第一个<form ...... </form>
			parseForm(sub);//解析这个form
			if (s.length()>end+7){//将这个form截掉
				s = s.substring(end+7);
			}else
				break;
//			System.out.println(sub);//输出的是<form ...... </form>
		}
		
		
	}
	private void parseForm(String s){
		FormInfo fi = new FormInfo();
		String form = new String(s);
		String temp;
		String method;
		
		int method_index =form.toLowerCase().indexOf("method");
//		System.out.println(method_index);
		if (method_index!=-1){
			String msub = form.substring(method_index+6);
			if (msub.indexOf("\"")>msub.indexOf("=")+2){
				msub = msub.substring(msub.indexOf("=")+1);
				while (msub.indexOf(" ")==0)
					msub.substring(1);
				msub = msub.substring(0,msub.indexOf(" "));
			}else{
				msub = msub.substring(msub.indexOf("\"")+1);
				msub = msub.substring(0,msub.indexOf("\""));
			}
			if (msub.toLowerCase().contains("javascript") || msub.toLowerCase().contains("mailto"))
				return;
			method = msub.toLowerCase();
			fi.method2 = method;
		}else
			method = null;
		
		int action_index =form.toLowerCase().indexOf("action");//获取action内容
		if (action_index!=-1){
			String sub = form.substring(action_index+6);
			sub = sub.substring(sub.indexOf("\"")+1);
			sub = sub.substring(0,sub.indexOf("\""));
			if (sub.toLowerCase().contains("javascript") || sub.toLowerCase().contains("mailto"))
				return;
			temp = sub;
			System.out.println("该表单的action类型为：" + sub);//本行注释
			if (TheParam.getParams().SelectedModule[1] == 1) {
                mf.printSQLInjectProcess("该表单的action类型为："+sub + "\n");
            }
            if (TheParam.getParams().SelectedModule[2] == 1) {
                mf.setXSSProcessText("该表单的action类型为：" + sub + "\n");
                mf.setActionAttribution(sub + "\n");
            }
		}else{
			temp = null;
		}
		int begin = 0;
		int end;
		int t1,t2;
		String sub;
		String tag;
		String name;
		String type;
		while (begin != -1){
			t1 = form.toLowerCase().indexOf("<input");
			//找到最靠前的input标签语句或者textarea标签语句，即用户输入部分
			t2 = form.toLowerCase().indexOf("<textarea");
			if (t1==-1 && t2==-1)
				break;
			if (t1==-1 || t2==-1)
				begin = t2==-1?t1:t2;
			else
				begin = t1<t2?t1:t2;
			if (begin!=0)
				form = form.substring(begin);
			if (form.toLowerCase().startsWith("<input"))
				tag = "input";
			else
				tag = "textarea";
			end = form.toLowerCase().indexOf(">");
			sub = form.substring(tag.length() + 2, end);
			name = getInfo(sub, "name");//在这个标签里解析出name的值
            if (TheParam.getParams().SelectedModule[1] == 1) {
                mf.printSQLInjectProcess("input标签中name值为：" + name + "\n");
            }
			type = getInfo(sub,"type");//在这个标签里解析出type的值
            if (TheParam.getParams().SelectedModule[1] == 1) {
                mf.printSQLInjectProcess("input标签中type值为：" + type + "\n");
            }
			fi.text_name.add(name);
			if (type==null || type.toLowerCase().equals("text")){
				//如果type是text的话表示它是输入框，或者textarea没有type它也是输入框
				fi.u_Name.add(name);
				fi.action = temp;//有输入框才把action存起来
				fi.method = method;
				System.out.println(temp+"的提交方式是"+method+"类型");//本行注释
                if (TheParam.getParams().SelectedModule[2] == 1) {
                    mf.setXSSProcessText(temp + "的提交方式是" + method + "类型\n");
                }
                if (TheParam.getParams().SelectedModule[1] == 1) {
                    mf.printSQLInjectProcess("提交方式：" + method + "\n");
                }

			}else
				if (type.equals("password")){
					//如果type是password，表示它是口令输入处，而前一个输入框就是用户名输入处了
					this.p_name = new String(name);
					name = new String(fi.u_Name.get(fi.u_Name.size()-1));
					//取出前一个name，就是用户名输入处的name
					this.n_name = new String(name);
					fi.u_Name.remove(name);
					this.index = fiList.size();
				}
			form = form.substring(end);//截掉已经处理过的
		}
//		fi.tryit();
		
		form = new String(s);//恢复form
		begin = form.toLowerCase().indexOf("<select");
		//找select标签，用类似的方法找出下拉框的name和value
		while (begin!=-1){
			end = form.toLowerCase().indexOf("</select>");
			sub = form.substring(begin + 7, end);
			fi.s_Name.add(getInfo(sub,"name"));
            if (TheParam.getParams().SelectedModule[1] == 1) {
                mf.printSQLInjectProcess("select标签中name值为：" + getInfo(sub, "name") + "\n");
            }
			SelectValue sv = new SelectValue();
			sv.v = getValue(sub,"value");
            if (TheParam.getParams().SelectedModule[1] == 1) {
                mf.printSQLInjectProcess("select标签中value值为：" + getValue(sub, "value") + "\n");
            }
			fi.s_value.add(sv);
			form = form.substring(form.toLowerCase().indexOf("</select>") + 9);
			begin = form.toLowerCase().indexOf("<select");;
		}
		fiList.add(fi);
	}
	private String getInfo(String s1,String s2){//从s1语句里取到代表s2的值
		String s = new String(s1);
		String sub = new String(s2);
		int index = s.indexOf(sub);
		if (index == -1)
			return null;
//		System.out.println(s2.length());//本行注释
		s = s.substring(index+s2.length());
//		System.out.println(s);//本行注释
		String result;
		if (s.indexOf("\"")==-1){
			s = s.substring(s.indexOf("=")+1);
			if (s.indexOf(" ")==-1)
				result = s;
			else
				result = s.substring(0,s.indexOf(" "));
		}else{
			s = s.substring(s.indexOf("\"")+1);	
			result = s.substring(0,s.indexOf("\""));
		}
		return result;
	}
	
	private ArrayList<String> getValue(String s1,String s2){
		//同上，不同的是下拉框有多个值，所以这里返回ArrayList
		ArrayList<String> value = new ArrayList<String>();
		String s = new String(s1);
		String sub = new String(s2);
		String result;
		int t1,t2;
		String temp;
		while(true){
			int index = s.indexOf(sub);
			if (index == -1)
				return value;
			s = s.substring(index+5);
			t1 = s.indexOf("'");
			t2 = s.indexOf("\"");
			if (t1==-1)
				temp = "\"";
			else
				if (t2==-1)
					temp = "'";
				else
					temp = t1<t2?"'":"\"";
			s = s.substring(s.indexOf(temp)+1);
			result = s.substring(0,s.indexOf(temp));
			value.add(result);
			s = s.substring(s.indexOf(temp)+1);
			
		}
	}
	
	public ArrayList<FormInfo> getFIList(){
		return fiList;
	}
	
	public int u_NameNum(){//返回拿到多少个name
		int r = 0;
		for (int i=0;i<fiList.size();i++){
			r+=fiList.get(i).u_Name.size();
		}
		return r;
	}
	
	public String getN_name(){
		return this.n_name;
	}
	
	public String getP_name(){
		return this.p_name;
	}
	
	public int getIndex(){
		return this.index;
	}
//	public String getMethod(){
//		return method;     //获取时比较不能用equals，用==。因method定义为null，注意大小写
//	}
}
