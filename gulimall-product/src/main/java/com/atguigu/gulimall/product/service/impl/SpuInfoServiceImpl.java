package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.dao.SpuInfoDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import com.atguigu.gulimall.product.entity.SpuInfoEntity;
import com.atguigu.gulimall.product.service.*;
import com.atguigu.gulimall.product.vo.BaseAttrs;
import com.atguigu.gulimall.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    /**
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Query<SpuInfoEntity> query = new Query<>();
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        IPage<SpuInfoEntity> page = this.page(query.getPage(params),queryWrapper);
        return new PageUtils(page);
    }

    /**
     * 1、新增商品
     * 页面获取所有数据 vo，分别保存对应的表中
     * @param vo ：页面获取所有数据
     */
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {

        // 1、保存spu基本信息 pms_spu_info
        // 创建要保存的实体类，获取对应信息封装
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        // 2、保存Spu的描述图片 pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);

        // 3、保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(infoEntity.getId(),images);

        // 4、保存spu的规格参数;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());

            AttrEntity id = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(id.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            return valueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);
        // 5、保存当前spu对应的所有sku信息；
        //5.1）、sku的基本信息；pms_sku_info
        //5.2）、sku的图片信息；pms_sku_image
        //5.3）、sku的销售属性信息：pms_sku_sale_attr_value
        // //5.4）、sku的优惠、满减等信息；
        // gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price

        // 6、保存spu的积分信息；gulimall_sms->sms_spu_bounds
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

}