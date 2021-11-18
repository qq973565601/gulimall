package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.AttrEntity;
import lombok.Data;

/**
 * 前端响应的数据封装的实体类
 *
 * @author lzx
 */
@Data
public class AttrRespVo extends AttrEntity {

    private String catelogName;
    private String groupName;
}
