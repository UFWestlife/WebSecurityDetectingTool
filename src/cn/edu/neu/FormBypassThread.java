package cn.edu.neu.HJ.Threads;

import cn.edu.neu.LY.FormBypass.Formbypass;
import cn.edu.neu.HJ.ProcedureOrientedJAVA.TheParam;

import java.io.IOException;

/**
 * Created by AzureHJ on 2015/8/9.
 */
public class FormBypassThread implements Runnable{
    private Thread formBypassThread = new Thread(this);

    @Override
    public void run() {
        Formbypass fp=new Formbypass(TheParam.getParams().fromBypassURL);
        try {
            TheParam.getParams().formBypassResult = fp.detect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTheFormBypassThread(){
        formBypassThread.start();
    }
    public boolean isTheFormBypassThreadAlive(){
        return formBypassThread.isAlive();
    }
    public void stopTheFormBypassThread(){
        if (!formBypassThread.isInterrupted()){
            formBypassThread.interrupt();
        }
    }
}
