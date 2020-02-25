package com.zx.rts.service.impl;

import com.zx.rts.config.ExportExcel;
import com.zx.rts.service.ExportExcelService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/**
 * 王志成
 * 导出表格方法封装
 * 2020/2/24
 */
@Service
public class ExportExcelServiceImpl implements ExportExcelService {
    Logger logger = LoggerFactory.getLogger(ExportExcelServiceImpl.class);


    /**
     * Excel单表导出方法
     *
     * @param response
     * @param exportExcel
     */
    @Override
    public void export(HttpServletResponse response, ExportExcel exportExcel) {

        try {
            // 创建工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 创建工作表
            HSSFSheet sheet = workbook.createSheet(exportExcel.getTitle());
            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTiltle = rowm.createCell(0);
            //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
            //获取列头样式对象
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
            //单元格样式对象
            HSSFCellStyle style = this.getStyle(workbook);
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (exportExcel.getRowName().length - 1)));
            cellTiltle.setCellStyle(columnTopStyle);
            cellTiltle.setCellValue(exportExcel.getTitle());
            // 定义所需列数
            int columnNum = exportExcel.getRowName().length;
            // 在索引2的位置创建行(最顶端的行开始的第二行)
            HSSFRow rowRowName = sheet.createRow(2);
            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                //创建列头对应个数的单元格
                HSSFCell cellRowName = rowRowName.createCell(n);
                //设置列头单元格的数据类型
                cellRowName.setCellValue(HSSFCell.CELL_TYPE_STRING);
                HSSFRichTextString text = new HSSFRichTextString(exportExcel.getRowName()[n]);
                //设置列头单元格的值
                cellRowName.setCellValue(text);
                //设置列头单元格样式
                cellRowName.setCellStyle(columnTopStyle);
            }
            //将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < exportExcel.getDataList().size(); i++) {
                //遍历每个对象
                Map<String, Object> map = exportExcel.getDataList().get(i);
                //创建所需的行数
                HSSFRow row = sheet.createRow(i + 3);
                for (int j = 0; j < exportExcel.getRowName().length; j++) {
                    //设置单元格的数据类型
                    HSSFCell cell = null;
                    if (j == 0) {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(i + 1);
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        Object obj = map.get(exportExcel.getRowName()[j]);
                        if (obj != null) {
                            cell.setCellValue(String.valueOf(obj));
                        } else {
                            cell.setCellValue("");
                        }
                    } else {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        Object obj = map.get(exportExcel.getRowName()[j]);
                        if (obj != null) {
                            cell.setCellValue(String.valueOf(obj));
                        } else {
                            cell.setCellValue("");
                        }
                    }
                    //设置单元格样式
                    cell.setCellStyle(style);
                }
            }
            if (workbook != null) {
                try {
                    String fileName = exportExcel.getTitle() + ".xls";
                    //中文名称
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Excel多表导出方法
     *
     * @param response
     * @param list
     */
    @Override
    public void export(HttpServletResponse response, List<ExportExcel> list) {

        try {
            // 创建工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            String fileName = "";
            // 创建工作表
            for (ExportExcel exportExcel : list) {
                if (StringUtils.isEmpty(fileName)) {
                    fileName = exportExcel.getTitle() + ".xls";
                }
                HSSFSheet sheet = workbook.createSheet(exportExcel.getTitle());
                // 产生表格标题行
                HSSFRow rowm = sheet.createRow(0);
                HSSFCell cellTiltle = rowm.createCell(0);
                //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
                //获取列头样式对象
                HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
                //单元格样式对象
                HSSFCellStyle style = this.getStyle(workbook);
                sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (exportExcel.getRowName().length - 1)));
                cellTiltle.setCellStyle(columnTopStyle);
                cellTiltle.setCellValue(exportExcel.getTitle());
                // 定义所需列数
                int columnNum = exportExcel.getRowName().length;
                // 在索引2的位置创建行(最顶端的行开始的第二行)
                HSSFRow rowRowName = sheet.createRow(2);
                // 将列头设置到sheet的单元格中
                for (int n = 0; n < columnNum; n++) {
                    //创建列头对应个数的单元格
                    HSSFCell cellRowName = rowRowName.createCell(n);
                    //设置列头单元格的数据类型
                    cellRowName.setCellValue(HSSFCell.CELL_TYPE_STRING);
                    HSSFRichTextString text = new HSSFRichTextString(exportExcel.getRowName()[n]);
                    //设置列头单元格的值
                    cellRowName.setCellValue(text);
                    //设置列头单元格样式
                    cellRowName.setCellStyle(columnTopStyle);
                }
                //将查询出的数据设置到sheet对应的单元格中
                for (int i = 0; i < exportExcel.getDataList().size(); i++) {
                    //遍历每个对象
                    Map<String, Object> map = exportExcel.getDataList().get(i);
                    //创建所需的行数
                    HSSFRow row = sheet.createRow(i + 3);


                    for (int j = 0; j < exportExcel.getRowName().length; j++) {
                        //设置单元格的数据类型
                        HSSFCell cell = null;
                        if (j == 0) {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(i + 1);
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                            Object obj = map.get(exportExcel.getRowName()[j]);
                            if (obj != null) {
                                cell.setCellValue(String.valueOf(obj));
                            } else {
                                cell.setCellValue("");
                            }
                        } else {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                            Object obj = map.get(exportExcel.getRowName()[j]);
                            if (obj != null) {
                                cell.setCellValue(String.valueOf(obj));
                            } else {
                                cell.setCellValue("");
                            }
                        }
                        //设置单元格样式
                        cell.setCellStyle(style);
                    }
                }
            }
            if (workbook != null) {
                try {
                    //中文名称
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列表单元格样式
     *
     * @param workbook
     * @return
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        //表格基本常规样式
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        font.setFontName("Courier New");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(style.getBorderRightEnum());
        //HSSFColor.BLACK.index（过时，替换成(short)8）下面都一样
        style.setBottomBorderColor((short) 8);
        style.setBorderLeft(style.getBorderRightEnum());
        style.setLeftBorderColor((short) 8);
        style.setBorderRight(style.getBorderRightEnum());
        style.setRightBorderColor((short) 8);
        style.setBorderTop(style.getBorderRightEnum());
        style.setTopBorderColor((short) 8);
        style.setFont(font);
        style.setWrapText(false);
        style.setAlignment(style.getAlignmentEnum());
        style.setVerticalAlignment(style.getVerticalAlignmentEnum());
        return style;

    }

    /**
     * 列数据信息单元格样式
     *
     * @param workbook
     * @return
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        //表格基本常规样式
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("Courier New");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(style.getBorderRightEnum());
        //HSSFColor.BLACK.index（过时，替换成(short)8）
        style.setBottomBorderColor((short) 8);
        style.setBorderLeft(style.getBorderRightEnum());
        //HSSFColor.BLACK.index（过时，替换成(short)8）
        style.setLeftBorderColor((short) 8);
        style.setBorderRight(style.getBorderRightEnum());
        style.setRightBorderColor((short) 8);
        style.setBorderTop(style.getBorderRightEnum());
        style.setTopBorderColor((short) 8);
        style.setFont(font);
        style.setWrapText(false);
        style.setAlignment(style.getAlignmentEnum());
        style.setVerticalAlignment(style.getVerticalAlignmentEnum());

        return style;

    }

}

