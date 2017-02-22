package cn.edu.neu.HJ.ProcedureOrientedJAVA;

import cn.edu.neu.HJ.GUI.ErrorDialog;
import cn.edu.neu.HJ.Threads.*;

/**
 * Created by AzureHJ on 2015/8/2.
 */
public class TheFunction {
    private static TheFunction functions = new TheFunction();

    public static TheFunction getInstance() {
        return functions;
    }


    private TheFunction() {
    }


    public static void checkOptions() {
        //判断是否至少选择了一个模块
        if (TheParam.getParams().SelectedModule[0] == 0 && TheParam.getParams().SelectedModule[1] == 0 && TheParam.getParams().SelectedModule[2] == 0
                && TheParam.getParams().SelectedModule[3] == 0 && TheParam.getParams().SelectedModule[4] == 0){
            ErrorDialog modualErrorDialog = new ErrorDialog("\u6a21\u5757\u9519\u8bef\uff1a\u81f3\u5c11\u9009\u62e9\u4e00\u4e2a\u529f\u80fd\u6a21\u5757");
            modualErrorDialog.pack();
            modualErrorDialog.setVisible(true);
        }
    }






}