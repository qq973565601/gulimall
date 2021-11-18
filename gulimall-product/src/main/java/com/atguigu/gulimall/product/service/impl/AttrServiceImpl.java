package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author lzx
 */
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 1、规格参数中 新增属性
     *
     * @param attr：前端页面获取的数据封装的实体类
     */
    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        // 使用Spring工具类，将实体类 attr 复制给 attrEntity
        BeanUtils.copyProperties(attr,attrEntity);
        // 1、保存基本数据
        this.save(attrEntity);
        // 2、保存关联关系(pms_attr_attrgroup_relation)
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());
        // 使用 dao 保存关联关系进数据库
        relationDao.insert(relationEntity);
    }

   /**
   * @param params
   * @param catelogId
   * @return
   */
    @Override
    public PageUtils queryBasePage(Map<String, Object> params, Long catelogId) {
        // 创建复杂查询条件
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        if(catelogId != 0) {
            queryWrapper.eq("catelog_id",catelogId);
        }
        // 获取查询携带条件 key
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)) {
            // 进行模糊查询
            queryWrapper.and((wrapper)->{
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> iPage = new Query<AttrEntity>().getPage(params);
        IPage<AttrEntity> page = this.page(iPage, queryWrapper);
        PageUtils pageUtils = new PageUtils(page);

        // 添加完整数据
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((AttrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(AttrEntity, attrRespVo);

            // 设置分组和分类的名字
            AttrAttrgroupRelationEntity attr_id = relationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", AttrEntity.getAttrId()));
            if (attr_id != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attr_id.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }

            CategoryEntity categoryEntity = categoryDao.selectById(AttrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;

    }

}