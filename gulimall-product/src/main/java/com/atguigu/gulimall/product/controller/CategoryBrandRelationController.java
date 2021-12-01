package com.atguigu.gulimall.product.controller;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.BrandVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 品牌分类关联
 * 对应页面：品牌管理
 * 表：pms_category_brand_relation
 *
 * @author linzongxing
 *
 * @email 973565601@qq.com
 * @date 2021-10-08 22:28:15
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 1、获取当前品牌关联的所有分类列表
     */
    @RequestMapping(value = "/catelog/list",method = RequestMethod.GET)
    public R catelogList(@RequestParam("brandId") Long brandId){
        // 构建查询条件
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<CategoryBrandRelationEntity>()
                .eq("brand_id", brandId);

        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(wrapper);

        return R.ok().put("data", data);
    }

    /**
     * 2、保存详细细节,对应品牌管理里的关联分类
     * 进行冗余存储，减少关联查询
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){

		categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

  /**
   * 3、在发布中，获取分类关联的品牌
   * /product/categorybrandrelation/brands/list
   *    1)、Controller：处理请求，接受和校验数据
   *    2)、Service接受controller传来的数据，进行业务处理
   *    3)、Controller接受Service处理完的数据，封装页面指定的vo
   */
  @GetMapping("/brands/list")
  public R getRelationBrandList(@RequestParam(value = "catId", required = true) Long catId) {

        // 获取 BrandEntity ，用于复用
        List<BrandEntity> vos = categoryBrandRelationService.getBrandsByCatId(catId);
        // 获取其中需要的
        List<BrandVo> brandVos = vos.stream().map((item) -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data",brandVos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
