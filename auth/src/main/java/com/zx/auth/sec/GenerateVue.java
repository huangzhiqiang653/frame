package com.zx.auth.sec;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @program: frame->GenerateVue
 * @description: 生成vue文件
 * @author: 黄智强
 * @create: 2019-12-10 15:02
 **/
@Component
public class GenerateVue {
    protected Log log = LogFactory.getLog(this.getClass());

    public void forTable(Map configMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取表信息
        String[] tableDetail = ((String) configMap.get("tableDetail")).split(",");
        String tableName = tableDetail[0], tableFullName = tableDetail[1], tableComment = tableDetail[2], tableDb = tableDetail[3];
        // 获取列表操作信息
        List<String> tableHandle = (List<String>) configMap.get("tableHandle");
        // 获取单条数据操作信息
        List<String> infoHandle = (List<String>) configMap.get("infoHandle");
        // 获取展示字段数据
        List<Map> fields = (List<Map>) configMap.get("showFields");
        // TODO 相应的vue文件生成
        log.trace("forTable");
    }

    public void forForm(Map configMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取表信息
        String[] tableDetail = ((String) configMap.get("tableDetail")).split(",");
        String tableName = tableDetail[0], tableFullName = tableDetail[1], tableComment = tableDetail[2], tableDb = tableDetail[3];
        // 展现形式
        String formShowType = (String) configMap.get("formShowType");
        // 获取操作数据
        List<String> handleList = (List<String>) configMap.get("handleList");
        // 获取表单字段数据
        List<Map> fields = (List<Map>) configMap.get("mainSelectFields");
        // TODO 相应的vue文件生成
        log.trace("forForm");
    }

}
