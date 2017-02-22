package cn.edu.neu.HJ.Threads;

import cn.edu.neu.HJ.GUI.ErrorDialog;
import cn.edu.neu.HJ.ProcedureOrientedJAVA.TheParam;
import cn.edu.neu.LY.WebTro.Trojan;

/**
 * Created by AzureHJ on 2015/8/10.
 */
public class WebTroThread implements Runnable {
    private Thread theWebTroThread = new Thread(this);

    @Override
    public void run() {
        Trojan T = new Trojan(TheParam.getParams().webTroURL);
        if (T.test()){
            ErrorDialog WebTroResult = new ErrorDialog("该网站存在木马!");
            WebTroResult.setVisible(true);
        }
        else {
            ErrorDialog WebTroResult = new ErrorDialog("该网站不存在木马~~~");
            WebTroResult.setVisible(true);
        }
    }

    public void runTheWebTroThread(){theWebTroThread.start();}
    public void stopTheWebTroThread(){
        if (!theWebTroThread.isInterrupted()){
            theWebTroThread.interrupt();
        }
    }
    public boolean isWenTroThradAlive(){return theWebTroThread.isAlive();}
}
