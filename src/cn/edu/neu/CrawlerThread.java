package cn.edu.neu.HJ.Threads;

import cn.edu.neu.HJ.ProcedureOrientedJAVA.TheParam;
import cn.edu.neu.HJ.Scrawler.WebCrawler;
import cn.edu.neu.HJ.Scrawler.WebInfo;

import java.util.ArrayList;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;

/**
 * Created by AzureHJ on 2015/8/4.
 */
public class CrawlerThread implements Runnable{
    //private CrawlerThread crawlerThread = new CrawlerThread();
    private Thread theCrawlerThread = new Thread(this);

    @Override
    public void run() {

            WebCrawler crawler = new WebCrawler();
            TheParam.getParams().crawlerResult = crawler.Crawler(TheParam.getParams().crawlerURL, TheParam.getParams().crawlerLayers, TheParam.getParams().crawlerOption);

            for (WebInfo aURL : TheParam.getParams().crawlerResult) {
                String URLs = null;
                for (String aNode : aURL.links) {
                    if (aNode != null) {
                        URLs += (aNode + "\n");
                    }
                    if (theCrawlerThread.isInterrupted()) {
                        return;
                    }
                }
                mf.printResult("\u722c\u866b\u7ed3\u679c\uff1a\n" + "URL:" + aURL.URL + "\n" + "\u5305\u542b\u7684URL:\n" + URLs + "\u7f51\u9875\u6e90\u7801\u5df2\u4fdd\u5b58\n");
            }
            mf.makeTree((ArrayList<WebInfo>) TheParam.getParams().crawlerResult, TheParam.getParams().crawlerOption);

    }

    public void startCrawlerThread(){
        theCrawlerThread.start();
    }
    public void stopCrawlerThread() {
        if (!theCrawlerThread.isInterrupted()) {
            theCrawlerThread.interrupt();
        }
    }
    public boolean isCrawlerThreadAlive(){
        return theCrawlerThread.isAlive();
    }
    public void notifyCrawlerThread(){theCrawlerThread.notify();}
}
