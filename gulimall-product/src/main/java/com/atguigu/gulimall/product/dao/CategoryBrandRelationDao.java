package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌分类关联
 * 
 * @author linzongxing
 * @email 973565601@qq.com
 * @date 2021-10-08 22:28:15
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    /**
     * 用 Param 为参数起一个名字
     * @param catId
     * @param name
     */
    void updateCategory(@Param("catId") Long catId, @Param("name") String name);
}
