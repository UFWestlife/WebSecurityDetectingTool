package cn.edu.neu.HJ.Threads;

import cn.edu.neu.HJ.GUI.ErrorDialog;
import cn.edu.neu.HJ.ProcedureOrientedJAVA.TheParam;
import cn.edu.neu.MJ.SQLInject.GetJudge;
import cn.edu.neu.MJ.SQLInject.PostJudge;
import cn.edu.neu.MJ.SQLInject.ViolenceFrame;

import static cn.edu.neu.HJ.GUI.GUIStart.mf;

/**
 * Created by AzureHJ on 2015/8/5.
 */
public class SQLInjectThread implements Runnable {
    private Thread theSQLInjectThread = new Thread(this);
    @Override
    public void run() {
        mf.setSQLInjectURL(TheParam.getParams().SQLURL);
        GetJudge gj = new GetJudge();
        if (gj.Judge(TheParam.getParams().SQLURL)){
            System.out.println("\u5b58\u5728get\u578b\u6ce8\u5165\n");

            String sqltype=gj.getType();
            ViolenceFrame violence = new ViolenceFrame(TheParam.getParams().SQLURL,sqltype);
        }
        else{
            System.out.println("\u4e0d\u5b58\u5728get\u578b\u6ce8\u5165\n");
            mf.printResult("\u4e0d\u5b58\u5728get\u578b\u6ce8\u5165\n");
        }
        //post
        PostJudge pj = new PostJudge(TheParam.getParams().SQLURL);

        System.out.println(TheParam.getParams().sqlInjectInfo);
        ErrorDialog endTip = new ErrorDialog("SQL注入检测完毕！");
        endTip.setVisible(true);
    }

    public void startTheSQLInjectThread(){
        theSQLInjectThread.start();
    }
    public void stopTheSQLInjectThread(){
        if (!theSQLInjectThread.isInterrupted()){
            theSQLInjectThread.interrupt();
        }
    }
    public boolean isTheSQLInjectThreadAlive(){
        return theSQLInjectThread.isAlive();
    }
}
