package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author lzx
 */
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 重新定义分页查询
     * @param params：前端传入的 map
     * @param catelogId：三级分类id
     * @return 封装的工具类 PageUtils
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {

        String key = (String) params.get("key");
        // select * from pms_attr_group where catelog_id=? and attr_group_id=key or attr_group_name like %key%
        // 构造查询条件
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        // 判断 key 是否为空
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        // 前端规定，如果没有三级分类，则 id 传0查询所有，有则传指定的值
        if(catelogId == 0){
            // 调用 common 封装的类，返回分页信息对象，里面封装了定义的信息和条件
            IPage<AttrGroupEntity> page = new Query<AttrGroupEntity>().getPage(params);
            // 封装的查询条件
            IPage<AttrGroupEntity> iPage = this.page(page, wrapper);
            return new PageUtils(iPage);
        }else {
            // 当有分类 id 时,添加查询条件
            wrapper.eq("catelog_id",catelogId);
            IPage<AttrGroupEntity> iPage = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(iPage);
        }
    }


}