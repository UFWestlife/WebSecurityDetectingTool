package cn.edu.neu.HJ.Threads;

import cn.edu.neu.HJ.ProcedureOrientedJAVA.TheParam;
import cn.edu.neu.WZ.XSS.XssDetection;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;

/**
 * Created by AzureHJ on 2015/8/5.
 */
public class XSSThread implements Runnable {
    private Thread theXSSThread = new Thread(this);

    @Override
    public void run() {
        mf.setDetectedURL(TheParam.getParams().XSSURL);
        mf.setXSSProcessText("\u6b63\u5728\u5bf9" + TheParam.getParams().XSSURL + "\u8fdb\u884cXSS\u6f0f\u6d1e\u68c0\u6d4b...\n");
        XssDetection XSSD = new XssDetection(TheParam.getParams().XSSURL);
        mf.setXSSResultText("\u5bf9\u7f51\u7ad9:" + TheParam.getParams().XSSURL + "\u8fdb\u884cXSS\u6f0f\u6d1e\u626b\u63cf\uff0c\u7ed3\u679c\u4e3a\uff1a");
        if (XSSD.isXss()) {
            TheParam.getParams().XSSResult = true;
            mf.setXSSResultText("\u5b58\u5728XSS\u653b\u51fb\u6f0f\u6d1e\n");
        } else {
            TheParam.getParams().XSSResult = false;
            mf.setXSSResultText("\u4e0d\u5b58\u5728XSS\u653b\u51fb\u6f0f\u6d1e\n");
        }
        mf.setXSSProcessText("cn.edu.neu.WZ.XSS\u6f0f\u6d1e\u68c0\u6d4b\u5b8c\u6bd5\uff01\n");
    }

    public void startTheXSSThread(){
        theXSSThread.start();
    }
    public void stopTheXSSThread(){
        if (!theXSSThread.isInterrupted()){
           theXSSThread.interrupt();
        }
    }
    public boolean isTheXSSThreadAlive(){
        return theXSSThread.isAlive();
    }
}
