package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author lzx
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 1、查出所有分类，并以树形解构输出
     * @return 分类列表
     */
    @Override
    public List<CategoryEntity> getListTree() {
        // 1、查出所有分类
        List<CategoryEntity> categoryEntityList = baseMapper.selectList(null);
        // 2、组装成树状结构
        // 2.1、查出一级分类
        List<CategoryEntity> levelOneMenus = categoryEntityList.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == 0;
        }).map((Menus)->{
            Menus.setChildren(getChildren(Menus, categoryEntityList));
            return Menus;
        }).sorted((MenusBefore,MenusAfter)->{
            return (MenusBefore.getSort()==null?0:MenusBefore.getSort()) - (MenusAfter.getSort()==null?0:MenusAfter.getSort());
        }).collect(Collectors.toList());
        return levelOneMenus;
    }

    /**
     * 递归方法：获取某个菜单下的子菜单
     * @param root 当前菜单
     * @param all 从所有菜单中获取
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter((categoryEntity) -> {
            // 当前菜单父ID等于所有菜单中的分类ID
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map((categoryEntity)->{
            // 递归查找子菜单
            categoryEntity.setChildren(getChildren(categoryEntity, all));
            return categoryEntity;
        }).sorted((MenusBefore,MenusAfter)->{
            //排序规则；解决空指针异常，判断是否为空，是返回0，不是返回自己的值
            return (MenusBefore.getSort()==null?0:MenusBefore.getSort()) - (MenusAfter.getSort()==null?0:MenusAfter.getSort());
        }).collect(Collectors.toList());
        return children;
    }

    /**
     * 2、删除功能
     * @param asList
     */
    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 查出被关联的菜单

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * 3、属性分组模块：回显三级分类的所有路径 id
     * 查询回调的属性 id 的完整路径
     * @param cateLogId 当前id
     * @return id路径
     */
    @Override
    public Long[] findCateLogPath(Long cateLogId) {
        List<Long> paths = new ArrayList<>();
        CategoryEntity byId = this.getById(cateLogId);
        List<Long> parentPath = findParentPath(cateLogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 递归查询父 id
     * @param cateLogId
     * @param paths
     * @return
     */
    private List<Long> findParentPath(Long cateLogId, List<Long> paths) {
        paths.add(cateLogId);
        CategoryEntity byId = this.getById(cateLogId);
        if(byId.getParentCid() != 0 ){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    /**
     * 4、级联更新所有关联的数据
     * 同步更新其他关联表数据
     *
     * @param category
     */
    @Transactional
    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);

        if(!StringUtils.isEmpty(category.getName())){
            categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
        }

        // TODO 其他关联关系表
    }

}