package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.dao.BrandDao;
import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author lzx
 */
@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

   /**
    * 1、根据前端传入的品牌 id，和分类 id
    * @param categoryBrandRelation 包含品牌 id 和分类 id
    *
    */
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        // 查询详细名字
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        this.save(categoryBrandRelation);
    }

    /**
     * 2、更新品牌时，同步更新品牌分类关联表
     * 使用 QueryWrapper 的方法
     *
     * @param brandId 品牌 id
     * @param name 品牌名称
     */
    @Override
    public void updateBrand(Long brandId, String name) {
        // 创建一个实体类用于封装
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandId(brandId);
        relationEntity.setBrandName(name);
        // 创建更新条件
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId);
        this.update(relationEntity,wrapper);
    }

    /**
     * 3、更新三级分类时，同步更新品牌分类关联表
     * 使用 sql 语句的方法
     *
     * @param catId
     * @param name
     */
    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId,name);
    }

}