package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author linzongxing
 * @email 973565601@qq.com
 * @date 2021-10-08 22:28:15
 */
public interface BrandService extends IService<BrandEntity> {

  /**
   * 进行分页显示
   * @param params 参数
   * @return 分页数据
   */
  PageUtils queryPage(Map<String, Object> params);

  /**
   * 更新品牌时，同步更新关联表
   * @param brand 品牌更新
   */
  void updateDetail(BrandEntity brand);
}

