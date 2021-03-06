package com.zx.rts.service;


import com.zx.rts.config.ExportExcel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface ExportExcelService {
    /**
     * excel单表导出接口
     *
     * @param response
     * @param exportExcel
     */
    void export(HttpServletResponse response, ExportExcel exportExcel);

    /**
     * excel多表导出接口
     *
     * @param response
     * @param exportExcel
     */
    void export(HttpServletResponse response, List<ExportExcel> exportExcel);

}
