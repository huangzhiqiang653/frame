package com.zx.auth.generator;

import com.zx.common.common.ExcelUtil;
import com.zx.common.common.FreemarkerUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
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

        Map<String, Object> map = new HashMap<>();
        // 基本信息赋值
        map.put("tableName", tableName);
        map.put("tableComment", tableComment.substring(0, tableComment.length() - 1) + "列表");
        map.put("fileDesc", tableDb + "." + tableFullName + "（" + tableComment + "）-table");
        // 字段信息赋值
        map.put("fields", fields);
        // 操作信息赋值
        map.put("tableHandleList", tableHandle);
        // 单条数据操作
        map.put("infoHandleList", infoHandle);
        String cmtTpl = FreemarkerUtils.getTemplate("table.ftl", map);
        try {
            ExcelUtil.setResponseHeader(response, tableName + "Table" + ".vue");
            OutputStream os = response.getOutputStream();
            os.write(cmtTpl.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Map<String, Object> map = new HashMap<>();
        // map.put("article",article);
        // map.put("comment",comment);
        // map.put("replyLabel","回复");
        String cmtTpl = FreemarkerUtils.getTemplate("suvdasPage.ftl", map);
        // String cmtTpl = FreemarkerUtils.getTemplate("suvdasDialog.ftl", map);
        try {
            ExcelUtil.setResponseHeader(response, tableName + "OperatePage" + ".vue");
            // ExcelUtil.setResponseHeader(response, tableName + "OperateDialog" + ".vue");
            OutputStream os = response.getOutputStream();
            os.write(cmtTpl.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
