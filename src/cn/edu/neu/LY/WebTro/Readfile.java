package cn.edu.neu.LY.WebTro;

import java.io.*;
import java.util.ArrayList;

public class Readfile {//读取文本文件内容，写入到集合al中
	ArrayList<String> al=new ArrayList<String>();
	public Readfile(String fname){
		String str;
		File f=new File("features.txt");
		FileReader fr;
		try {
			fr = new FileReader(f);
			BufferedReader br=new BufferedReader(fr);
			str=br.readLine();
			while(str!=null){
				al.add(str);
				str=br.readLine();
			}
			fr.close();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
