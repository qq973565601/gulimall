package com.atguigu.gulimall.product.controller;

import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;
import com.atguigu.common.vaild.AddVaild;
import com.atguigu.common.vaild.UpdateStatus;
import com.atguigu.common.vaild.UpdateVaild;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 品牌管理
 * 对应页面：品牌管理
 * 表：pms_brand
 *
 * @author linzongxing
 * @email 973565601@qq.com
 * @date 2021-10-08 22:28:15
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId) {
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     * 开启校验注解，校验返回的数据 BrandEntity
     * BindingResult ,封装校验的结果
     * @Validated：Spring提供用于分组校验
     */
    @RequestMapping("/save")
    public R save(/*@Valid*/@Validated({AddVaild.class}) @RequestBody BrandEntity brand /*, BindingResult result*/) {

    /**
     * if(result.hasErrors()){ Map<String,String> map = new HashMap<>(); //1、获取校验的错误结果
     * result.getFieldErrors().forEach((item)->{ //FieldError 获取到错误提示 String message =
     * item.getDefaultMessage(); //获取错误的属性的名字 String field = item.getField();
     * map.put(field,message); });
     * <p>return R.error(400,"提交的数据不合法").put("data",map); }else {
     * <p>}
     */
    brandService.save(brand);
        return R.ok();
    }

    /**
     * 当品牌表发生变化时，因为关联关系表定义了name，所以要同步更新
     * 冗余存储，同步更新数据
     */
    @RequestMapping("/update")
    public R update(@Validated(UpdateVaild.class) @RequestBody BrandEntity brand) {
		//brandService.updateById(brand);
        brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 修改状态
     */
    @RequestMapping("/update/status")
    public R updateStatus(@Validated(UpdateStatus.class) @RequestBody BrandEntity brand) {
		brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
