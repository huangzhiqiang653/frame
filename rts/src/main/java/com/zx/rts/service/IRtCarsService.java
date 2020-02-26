package com.zx.rts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.rts.entity.RtCars;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 车辆表 服务类
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
public interface IRtCarsService extends IService<RtCars> {

    /**
     * 公共基础方法
     *
     * @param requestBean
     * @return
     */
    public ResponseBean base(RequestBean requestBean);

    /**
     * 车辆信息导出
     * @param response
     */
    public void exportRtCar(HttpServletResponse response);

    /**
     * 车辆信息导出
     * @param file
     * @return
     */
    public ResponseBean importRtCar(MultipartFile file);
}
