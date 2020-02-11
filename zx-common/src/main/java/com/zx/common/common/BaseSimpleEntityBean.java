package com.zx.common.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: law-ibeas->EntityEntityBean
 * @description: 实体类基础公共方法 基础
 * @author: 黄智强
 * @create: 2019-11-12 12:10
 **/
public class BaseSimpleEntityBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @Setter
    @Getter
    @TableId(type = IdType.UUID)
    private String id;
}
