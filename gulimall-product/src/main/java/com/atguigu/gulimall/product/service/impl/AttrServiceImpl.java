package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.constant.ProductConstant;
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
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
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

    @Autowired
    CategoryService categoryService;

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
     *  保存关联关系
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
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId()!=null) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            // 使用 dao 保存关联关系进数据库
            relationDao.insert(relationEntity);
        }
    }

   /**
    * 2、分页查询，并列表显示
   * @param params ：页面封装数据
   * @param catelogId ：
   * @param type
    * @return 实体类 respVos，封装到分页数据里
   */
    @Override
    public PageUtils queryBasePage(Map<String, Object> params, Long catelogId, String type) {
        // 创建复杂查询条件
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
                        .eq("attr_type","base"
                        .equalsIgnoreCase(type)?ProductConstant
                        .AttrEnum.ATTR_TYPE_BASE
                        .getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
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
            if ("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity attrId = relationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", AttrEntity.getAttrId()));
                if (attrId != null && attrId.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
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

    /**
     * 3、信息回显
     *
     * @param attrId
     * @return
     */
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,attrRespVo);

        // 通过关联关系表设置分组信息
        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
            QueryWrapper<AttrAttrgroupRelationEntity> attr_id = queryWrapper.eq("attr_id", attrId);
            AttrAttrgroupRelationEntity attrGroupRelation = relationDao.selectOne(attr_id);
            if (attrGroupRelation != null){
                attrRespVo.setAttrGroupId(attrGroupRelation.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupRelation.getAttrGroupId());
                if (attrGroupEntity != null){
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        // 设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] cateLogPath = categoryService.findCateLogPath(catelogId);
        attrRespVo.setCatelogPath(cateLogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null){
            attrRespVo.setCatelogName(categoryEntity.getName());
        }

        return attrRespVo;
    }

    /**
     * 4、更新数据
     *
     * @param attr: web date
     */
    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        // 修改关联分组
        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attr.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());

            Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if (count>0){
                relationDao.update(relationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
            }else {
                relationDao.insert(relationEntity);
            }
        }
    }

    /**
     * 5、根据分组 id，获取组内关联的所有属性
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",attrgroupId);
        List<AttrAttrgroupRelationEntity> entities = relationDao.selectList(wrapper);

        List<Long> attrIds = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        if (attrIds == null || attrIds.size() == 0){
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    /**
     * 6、删除分组与规格的关联关系
     * 在 AttrGroupController
     * @param vos：封装的实体类
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {

        List<AttrAttrgroupRelationEntity> collect = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        relationDao.deleteBatchRelation(collect);
    }

    /**
     * 7、获取当前分组没有关联的分组属性
     * @param attrgroupId：获取路径变量
     * @param params：收集页面带来的分页参数
     * @return 分页信息
     */
    @Override
    public PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params) {
        // 1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        // 2、当前分组只能关联别的分组没有引用的属性
        //2.1)、当前分类下的其他分组
        QueryWrapper<AttrGroupEntity> wrapper1 = new QueryWrapper<AttrGroupEntity>().eq("attr_group_id", attrgroupId);
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(wrapper1);
        List<Long> collect = attrGroupEntities.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        //2.2)、这些分组关联的属性
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper2 = new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrgroupId);
        List<AttrAttrgroupRelationEntity> attrgroupRelationEntities = relationDao.selectList(wrapper2);
        List<Long> attrIds = attrgroupRelationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        //2.3)、从当前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> wrapper3 = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        //List<AttrEntity> attrEntities = this.baseMapper.selectList(wrapper3);
        if(attrIds!=null && attrIds.size()>0){
            wrapper3.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if (StringUtils.isEmpty(key)){
            wrapper3.and((w)->{
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper3);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

}