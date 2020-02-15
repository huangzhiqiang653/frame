package com.zx.rts.generator;

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
        String fileName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
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
        // 文件名
        map.put("fileName", fileName + "Table");
        String cmtTpl = FreemarkerUtils.getTemplate("table.ftl", map);
        try {
            ExcelUtil.setResponseHeader(response, fileName + "Table" + ".vue");
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
        String fileName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
        // 展现形式
        String formShowType = (String) configMap.get("formShowType");
        // 获取操作数据
        List<String> handleList = (List<String>) configMap.get("handleList");
        // 获取表单字段数据
        List<Map> fields = (List<Map>) configMap.get("mainSelectFields");
        Map<String, Object> map = new HashMap<>();
        // 基本信息赋值
        map.put("tableName", tableName);
        map.put("tableComment", tableComment.substring(0, tableComment.length() - 1) + "列表");
        if (formShowType.equals("dialog")) {
            map.put("fileDesc", tableDb + "." + tableFullName + "（" + tableComment + "）-dialog");
        } else if (formShowType.equals("page")) {
            map.put("fileDesc", tableDb + "." + tableFullName + "（" + tableComment + "）-page");
        }

        // 字段信息赋值
        map.put("fields", fields);
        // 操作信息赋值
        map.put("handleList", handleList);
        if (handleList != null && handleList.size() > 0) {
            for (String handleType : handleList) {
                switch (handleType) {
                    case "add":
                        map.put("saveFlag", true);
                        break;
                    case "update":
                        map.put("updateFlag", true);
                        break;
                    case "view":
                        map.put("viewFlag", true);
                        break;
                    case "approval":
                        map.put("approvalFlag", true);
                        break;
                    case "submit":
                        map.put("submitFlag", true);
                        break;
                    case "enclosure":
                        map.put("enclosureFlag", true);
                        break;
                    default:

                }
            }
        }
        String cmtTpl = "";
        if (formShowType.equals("dialog")) {
            cmtTpl = FreemarkerUtils.getTemplate("suvdasDialog.ftl", map);
        } else if (formShowType.equals("page")) {
            cmtTpl = FreemarkerUtils.getTemplate("suvdasPage.ftl", map);
        }
        try {
            String fileTypeName = "";
            if (formShowType.equals("dialog")) {
                fileTypeName = "OperateDialog";
            } else if (formShowType.equals("page")) {
                fileTypeName = "OperatePage";
            }
            // 文件名
            map.put("fileName", fileName + fileTypeName);
            ExcelUtil.setResponseHeader(response, fileName + fileTypeName + ".vue");
            OutputStream os = response.getOutputStream();
            os.write(cmtTpl.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
