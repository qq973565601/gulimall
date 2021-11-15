package com.atguigu.gulimall.product.controller;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;



/**
 * 商品三级分类
 * 对应表：pms_category
 *
 * @author linzongxing
 * @email 973565601@qq.com
 * @date 2021-10-08 22:28:15
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 1、查出所有商品，并以父类子类形式列出，以树形结构输出
     */
    @RequestMapping("/list/tree")
    public R list(){

        List<CategoryEntity> list = categoryService.getListTree();

        return R.ok().put("data", list);
    }


    /**
     * 5、回显信息，考虑并发请求
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 3、保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 4、修改
     * 同步更有关联关系的表
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		//categoryService.updateById(category);
		categoryService.updateDetail(category);

        return R.ok();
    }

    /**
     * 2、删除
     * RequestBody：获取到请求体里的内容（只有POST请求才有请求体）
     * SpringMVC会将请求体的数据（json）转为对应的对象
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
		//categoryService.removeByIds(Arrays.asList(catIds));

		categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
