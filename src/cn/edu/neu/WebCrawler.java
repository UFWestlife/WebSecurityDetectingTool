package cn.edu.neu.HJ.Scrawler; /**
 * Created by AzureHJ on 2015/7/29.
 */
import cn.edu.neu.HJ.GUI.GUIStart;
import cn.edu.neu.HJ.ProcedureOrientedJAVA.TheParam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class WebCrawler {

    public WebInfo getWebInfo(String url, int layer, int option){
        GUIStart.mf.printProcess("Fetching " + url);

        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ie) {
            //ie.printStackTrace();
            String[] error = new String[1];
            error[0] = url;
            return new WebInfo(url, 0, "该URL无法打开", error, layer);
        }
        assert doc != null;
        Elements links = doc.select("a[href]");
        String Source = doc.html();
        String link[] = new String[links.size()];
        int linkNumber = 0;

        GUIStart.mf.printProcess("\nLinks: " + links.size());
        for (Element eachLink : links) {
            if (eachLink.attr("abs:href").replaceAll("/", "").equals(url.replaceAll("/", "")) || eachLink.attr("abs:href").length() < 5){}
            else{
                link[linkNumber] = eachLink.attr("abs:href");
                linkNumber++;
            }
        }

        //我是真的SB 才会咎由自取写这段代码

        List<String> templeList = new ArrayList<>();
        Collections.addAll(templeList, link);
        templeList = arrangeUrlList(templeList, option, TheParam.getParams().crawlerURL);
       // GUIStart.mf.printProcess("ActuallyListlLinks: " + templeList.size() + "\n");
        String newLink[] = new String[templeList.size()];
        for (int i = 0; i < templeList.size(); i++){
            newLink[i] = templeList.get(i);
        }
        int newLinkNumber = newLink.length;
        GUIStart.mf.printProcess("ActualLinks: " + newLinkNumber + "\n");
        return new WebInfo(url, newLinkNumber, Source, newLink, layer);

    }

    public List<WebInfo> Crawler(String url, int layers, int option) {                           //option == 1 爬取所有URL，option == 0，爬取顶级域名下的URL
        //GUI中记得对layers进行检查
        List<WebInfo> webInfoList = new ArrayList<>();
        List<WebInfo> temlpList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        webInfoList.add(getWebInfo(url, 1, option));
        for (int layer = 2;layer <= layers;layer++){
            temlpList.clear();
            urlList.clear();
            for (WebInfo aWebInfoList : webInfoList) {                                         //进行第n层爬取时，将第n-1层爬取到的所有url存在一个list中
                if (aWebInfoList.layer == layer-1) {
                    urlList.addAll(Arrays.asList(aWebInfoList.links).subList(0, aWebInfoList.webLinkNumber));
                    System.out.println(urlList.size());
                }
            }
            urlList = arrangeUrlList(urlList, option, url);                                         //爬取之前对URL们进行整理，重复和无效的URL被剔除，URL过滤（可选）
            System.out.println(urlList.size());
            for (String anUrlList : urlList) {
                temlpList.add(getWebInfo(anUrlList, layer, option));
                //System.out.println(getWebInfo(anUrlList, layer, option).webLinkNumber);
            }
            webInfoList.addAll(temlpList);
        }
        webInfoList.addAll(temlpList);

        return webInfoList;
    }

    public static List<String> arrangeUrlList(List<String> list, int option, String firstURL) {                //对URL进行整理，重复和无效的URL被剔除，URL过滤（可选）
        Set<String> set = new HashSet<>();
        List<String> newList = new ArrayList<>();
        if (option == 1)
            newList.addAll(list.stream().filter(aURL -> set.add(aURL) && aURL != null).collect(Collectors.toList()));
        else {
            newList.addAll(list.stream().filter(aURL -> set.add(aURL) && aURL != null && domianTell(firstURL, aURL)).collect(Collectors.toList()));
        }
        return newList;
    }

    public static boolean domianTell(String fistDomain, String domain){//判断URL是否属于同一个顶级域名
        fistDomain = fistDomain.replace(fistDomain.substring(0, fistDomain.indexOf("//")+2), "");
        domain = domain.replace(domain.substring(0, domain.indexOf("//")+2), "");
        System.out.println(fistDomain);
        System.out.println(domain);
        if (fistDomain.contains("/") && fistDomain.length() <= domain.length())
            return Objects.equals(domain.substring(0, fistDomain.indexOf("/", 0)),
                    fistDomain.substring(0, fistDomain.indexOf("/", 0)));
        else if (fistDomain.length() > domain.length())
            return false;
        else
            return domain.contains(fistDomain);
    }

    public static List<WebInfo> arrangeWebInfoList(List<WebInfo> list){
        Set<String> set = new HashSet<>();
        for (WebInfo aNode : list) {
            if (!set.add(aNode.URL)) {
                list.remove(aNode);
            }
        }
        return list;
    }

}