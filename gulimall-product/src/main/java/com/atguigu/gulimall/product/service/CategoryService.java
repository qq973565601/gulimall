package com.atguigu.gulimall.product.service;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author linzongxing
 * @email 973565601@qq.com
 * @date 2021-10-08 22:28:15
 */
public interface CategoryService extends IService<CategoryEntity> {

    /**
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * @return
     * */
    List<CategoryEntity> getListTree();

    /**
     * @param asList
     */
    void removeMenuByIds(List<Long> asList);

    /**
     * 找到 cateLogId 的完整路径
     * 路径格式 id：[父，当前、子]
     * @param cateLogId 当前id
     * @return 数组存储完整路径
     */
    Long[] findCateLogPath(Long cateLogId);
}

