package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 返回一个实体类
 * @author lzx
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
