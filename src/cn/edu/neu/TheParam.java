package cn.edu.neu.HJ.ProcedureOrientedJAVA;

import cn.edu.neu.MJ.SQLInject.SqlInjectInfo;
import cn.edu.neu.HJ.Scrawler.WebInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AzureHJ on 2015/8/1.
 */
public class TheParam {
    private static TheParam params = new TheParam();

    public static TheParam getParams() {
        return params;
    }

    private TheParam() {}

    public String treeClickedURL;

    //模块选择与初始化的变量
    public int[] SelectedModule = {0, 0, 0, 0, 0};
    public int[] initModule = {0, 0, 0, 0, 0};

    //右键菜单的变量
    public String goalURL;

    //爬虫模块的变量
    public String crawlerURL;
    public int crawlerLayers;
    public int crawlerOption;
    public List<WebInfo> crawlerResult;

    //SQL注入模块的变量
    public String SQLURL;
    public boolean isSQL = false;
    public String sqlDatabaseTpye;
    public List<String> sqlInjectType = new ArrayList<>();
    //public List<String> sqlInjectTable = new ArrayList<>();
    //public List<String> sqlInjectWord = new ArrayList<>();
    public SqlInjectInfo sqlInjectInfo = new SqlInjectInfo();

    //XSS模块的变量
    public String XSSURL;
    public boolean XSSResult;

    //表单绕过模块的变量
    public String fromBypassURL;
    public boolean formBypassResult;

    //网页木马模块变量
    public String webTroURL;
    public boolean webTroResult;

}
