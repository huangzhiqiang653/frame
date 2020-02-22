package com.zx.rts.service;


import com.zx.rts.config.ExportExcel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface ExportExcelService {

    void export(HttpServletResponse response, ExportExcel exportExcel);

    void export(HttpServletResponse response, List<ExportExcel> exportExcel);

}
