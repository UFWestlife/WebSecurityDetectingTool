package cn.edu.neu.HJ.Scrawler;

/**
 * Created by AzureHJ on 2015/7/29.
 */
public class WebInfo{                                                          //保存网页信息
    public WebInfo(String URL, int webLinkNumber, String webSource, String links[], int layer){
        this.URL = URL;
        this.links = links;
        this.webSource = webSource;
        this.webLinkNumber = webLinkNumber;
        this.layer = layer;
    }
    public WebInfo(){}
    public int layer;                                                           //该URL处于第几层
    public String URL;                                                          //URL
    public int webLinkNumber;                                                   //改页面中包含的URL数
    public String webSource;                                                    //网页源码
    public String links[] = new String[webLinkNumber];                          //该页面包含的URL

}
