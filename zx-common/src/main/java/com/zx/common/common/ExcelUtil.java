package com.zx.common.common;

import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: law-ibeas->ExcelUtil
 * @description: excel相关工具方法
 * @author: 黄智强
 * @create: 2019-11-26 12:00
 **/
public class ExcelUtil {
    static  Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }

        return wb;
    }

    /**
     * 发送响应流方法
     *
     * @param response
     * @param fileName
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=utf-8");
            //要保存的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //要保存的文件名
            response.setHeader("fileName", fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * EXCEL文件导入
     * @param file
     * @param type
     * @return
     */
    public static ResponseBean excelAnalysis(MultipartFile file, String type) {
        logger.info("excel文件内容解析=====================start==============");
        long startTime = System.currentTimeMillis();
        if (file == null) { //校验文件是否为空
            return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.FILE_IS_EMPTY_ERROR.getValue());
        }
        try {
            String name = file.getOriginalFilename();
            long size = file.getSize();
            logger.info("文件名(fileName):" + name);
            logger.info("文件大小(size):" + size);
            //进一步校验文件
            if (name == null || ("").equals(name) || size == 0) {
                return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.FILE_IS_EMPTY_ERROR.getValue());
            }
            //获取文件流
            InputStream is = file.getInputStream();
            //文件流转换
            Workbook workbook = null;
            if (name.endsWith(CommonConstants.FILE_EXCEL_XLS.getCode())) {
                workbook = new HSSFWorkbook(is);
            } else if (name.endsWith(CommonConstants.FILE_EXCEL_XLSX.getCode())) {
                workbook = new XSSFWorkbook(is);
            } else {
                return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.FILE_IS_GS_ERROR.getValue());
            }

            //获取excel解析数据
            List<String> excelConfigList = getExcelCellConfig(type);
            //初始化存放excel内容参数
            List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
            //获取第一个子sheet
            Sheet sheet = workbook.getSheetAt(0);
            //校验sheet
            if (sheet == null) {
                return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.FILE_IS_EMPTY_ERROR.getValue());
            }
            logger.info("获取sheet成功");
            //读取sheet中的每一行
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {//如果该行为空，继续下一行
                    continue;
                }
                Map<String, String> map = new HashMap<>();
                map.put("INDEX_EXCEL", (rowNum + 1) + "");
                //获取每列值
                for (int columnNum = 0; columnNum < excelConfigList.size(); columnNum++) {
                    Cell cell = row.getCell(columnNum);
                    //cell全部转换成字符串
                    if (!StringUtils.isEmpty(cell)) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        map.put(excelConfigList.get(columnNum), cell.getStringCellValue());
                    }else{
                        map.put(excelConfigList.get(columnNum),"");
                    }
                }
                ls.add(map);
            }
            logger.info("excel文件内容解析(正常)=====================end================");
            return new ResponseBean<List<Map<String, String>>>(ls);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("excel文件内容解析(异常)=====================end================");
            return new ResponseBean<>(CommonConstants.FAIL.getCode(), e.getMessage());
        }
    }


    /**
     * 获取excel数据解析配置
     * @return
     */
    public static List<String> getExcelCellConfig(String configData) {
        List<String> ls = new ArrayList<String>();
        String[] l = configData.split(",");
        for (String e : l) {
            ls.add(e);
        }
        return ls;
    }
}
