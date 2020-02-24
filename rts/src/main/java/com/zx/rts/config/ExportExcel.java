package com.zx.rts.config;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ExportExcel {
    //显示的导出表的标题
    private String title;
    //导出表的列名
    private String[] rowName ;

    private List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();

    HttpServletResponse response;

    //构造方法，传入要导出的数据
    public ExportExcel(String title, String[] rowName, List<Map<String,Object>>  dataList){
        this.dataList = dataList;
        this.rowName = rowName;
        this.title = title;
    }
    public ExportExcel(){}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getRowName() {
        return rowName;
    }

    public void setRowName(String[] rowName) {
        this.rowName = rowName;
    }

    public List<Map<String,Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String,Object>> dataList) {
        this.dataList = dataList;
    }
}
