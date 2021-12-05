package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.dao.SpuImagesDao;
import com.atguigu.gulimall.product.entity.SpuImagesEntity;
import com.atguigu.gulimall.product.service.SpuImagesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author lzx
 */
@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 1、保存发布时图片信息
     * @param id
     * @param images
     */
    @Override
    public void saveImages(Long id, List<String> images) {
        if (images == null || images.size() == 0){

        }else {
            List<SpuImagesEntity> collect = images.stream().map((item) -> {
                SpuImagesEntity imagesEntity = new SpuImagesEntity();
                imagesEntity.setSpuId(id);
                imagesEntity.setImgUrl(item);
                return imagesEntity;
            }).collect(Collectors.toList());
            this.saveBatch(collect);
        }
    }

}