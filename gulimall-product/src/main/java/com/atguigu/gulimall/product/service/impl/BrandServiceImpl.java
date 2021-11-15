package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.dao.BrandDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;


/**
 * 品牌管理
 * @author lzx
 */
@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    /**
     * @param params 查询条件
     * @return 分页信息
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        /**
         * 添加条件，增加模糊查询
         * 从 params 获取页面传入的 key 值
         */
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(key)){
            QueryWrapper<BrandEntity> wrapper = queryWrapper.eq("brand_id", key).or().like("name",key );
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
              queryWrapper
        );
        return new PageUtils(page);
    }

    /**
     * 开启事务
     * @param brand
     */
    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {
        // 先更新品牌表
        this.updateById(brand);
        // 判断是否更改品牌名
        if (!StringUtils.isEmpty(brand.getName())){
            // 同步更新品牌分类关联中的数据中的 name 值
            categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());

            // TODO 其他关联
        }

    }

}