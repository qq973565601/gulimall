package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * 前端响应的数据封装的实体类
 *
 * @author lzx
 */
@Data
public class AttrRespVo extends AttrVo {

    private String catelogName;
    private String groupName;

    private Long[] catelogPath;
}
