package com.zx.rts.service.impl;

import com.zx.rts.config.ExportExcel;
import com.zx.rts.service.ExportExcelService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



@Service
public class ExportExcelServiceImpl  implements ExportExcelService {
    Logger logger = LoggerFactory.getLogger(ExportExcelServiceImpl.class);

    //Excel导出方法
    @Override
    public void export(HttpServletResponse response, ExportExcel exportExcel){

        try{
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
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (exportExcel.getRowName().length-1)));
            cellTiltle.setCellStyle(columnTopStyle);
            cellTiltle.setCellValue(exportExcel.getTitle());
            // 定义所需列数
            int columnNum = exportExcel.getRowName().length;
            // 在索引2的位置创建行(最顶端的行开始的第二行)
            HSSFRow rowRowName = sheet.createRow(2);
            // 将列头设置到sheet的单元格中
            for(int n=0;n<columnNum;n++){
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
            for(int i=0;i<exportExcel.getDataList().size();i++){
                //遍历每个对象
                Map<String,Object> map = exportExcel.getDataList().get(i);
                //创建所需的行数
                HSSFRow row = sheet.createRow(i+3);


                for(int j=0; j < exportExcel.getRowName().length ; j ++){
                    //设置单元格的数据类型
                    HSSFCell cell = null;
                    if(j == 0){
                        cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(i+1);
                        cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                        Object obj = map.get(exportExcel.getRowName()[j]);
                        if(obj != null) {
                            cell.setCellValue(String.valueOf(obj));
                        }else{
                            cell.setCellValue("");
                        }
                    }else{
                        cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                        Object obj = map.get(exportExcel.getRowName()[j]);
                        if(obj != null) {
                            cell.setCellValue(String.valueOf(obj));
                        }else{
                            cell.setCellValue("");
                        }
                    }
                    //设置单元格样式
                    cell.setCellStyle(style);
                }
            }
            if(workbook !=null){
                try
                {
                    String fileName = exportExcel.getTitle()+".xls";
                    //中文名称
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void export(HttpServletResponse response, List<ExportExcel> list){

        try{
            // 创建工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            String fileName = "";
            // 创建工作表
            for(ExportExcel exportExcel:list){
                if(StringUtils.isEmpty(fileName)) {
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
                sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (exportExcel.getRowName().length-1)));
                cellTiltle.setCellStyle(columnTopStyle);
                cellTiltle.setCellValue(exportExcel.getTitle());
                // 定义所需列数
                int columnNum = exportExcel.getRowName().length;
                    // 在索引2的位置创建行(最顶端的行开始的第二行)
                    HSSFRow rowRowName = sheet.createRow(2);
                    // 将列头设置到sheet的单元格中
                    for(int n=0;n<columnNum;n++){
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
                    for(int i=0;i<exportExcel.getDataList().size();i++){
                        //遍历每个对象
                        Map<String,Object> map = exportExcel.getDataList().get(i);
                        //创建所需的行数
                        HSSFRow row = sheet.createRow(i+3);


                    for(int j=0; j < exportExcel.getRowName().length ; j ++){
                        //设置单元格的数据类型
                        HSSFCell cell = null;
                        if(j == 0){
                            cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(i+1);
                            cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                            Object obj = map.get(exportExcel.getRowName()[j]);
                            if(obj != null) {
                                cell.setCellValue(String.valueOf(obj));
                            }else{
                                cell.setCellValue("");
                            }
                        }else{
                            cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                            Object obj = map.get(exportExcel.getRowName()[j]);
                            if(obj != null) {
                                cell.setCellValue(String.valueOf(obj));
                            }else{
                                cell.setCellValue("");
                            }
                        }
                        //设置单元格样式
                        cell.setCellStyle(style);
                    }
                }
            }
            if(workbook !=null){
                try
                {
                    //中文名称
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
     * 列头单元格样式
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)12);
        //字体加粗
        font.setBold(true);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(style.getBorderRightEnum());
        //设置底边框颜色;
        style.setBottomBorderColor((short)8);//HSSFColor.BLACK.index（过时，替换成(short)8）下面都一样
        //设置左边框;
        style.setBorderLeft(style.getBorderRightEnum());
        //设置左边框颜色;
        style.setLeftBorderColor((short)8);
        //设置右边框;
        style.setBorderRight(style.getBorderRightEnum());
        //设置右边框颜色;
        style.setRightBorderColor((short)8);
        //设置顶边框;
        style.setBorderTop(style.getBorderRightEnum());
        //设置顶边框颜色;
        style.setTopBorderColor((short)8);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(style.getAlignmentEnum());
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(style.getVerticalAlignmentEnum());

        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)11);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(style.getBorderRightEnum());
        //设置底边框颜色;
        style.setBottomBorderColor((short)8);//HSSFColor.BLACK.index（过时，替换成(short)8）
        //设置左边框;
        style.setBorderLeft(style.getBorderRightEnum());
        //设置左边框颜色;
        style.setLeftBorderColor((short)8);//HSSFColor.BLACK.index（过时，替换成(short)8）
        //设置右边框;
        style.setBorderRight(style.getBorderRightEnum());
        //设置右边框颜色;
        style.setRightBorderColor((short)8);
        //设置顶边框;
        style.setBorderTop(style.getBorderRightEnum());
        //设置顶边框颜色;
        style.setTopBorderColor((short)8);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(style.getAlignmentEnum());
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(style.getVerticalAlignmentEnum());

        return style;

    }

}

