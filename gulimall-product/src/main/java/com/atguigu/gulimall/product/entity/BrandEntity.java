package com.atguigu.gulimall.product.entity;

import com.atguigu.common.vaild.AddVaild;
import com.atguigu.common.vaild.UpdateStatus;
import com.atguigu.common.vaild.UpdateVaild;
import com.atguigu.common.vaild.ValueList;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 * 
 * @author linzongxing
 * @email 973565601@qq.com
 * @date 2021-10-08 22:28:15
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@Null(message = "新增不能指定id",groups = {AddVaild.class})
    @NotNull(message = "修改必须指定品牌id",groups = {UpdateVaild.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
     * 自定义 message ，否则使用默认
	 */
	@NotBlank(message = "品牌名必须提交",groups = {AddVaild.class,UpdateVaild.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(groups = {AddVaild.class})
	@URL(message = "logo必须是一个合法的url地址",groups = {AddVaild.class,UpdateVaild.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
     * 使用自定义校验注解
	 */
	@ValueList(vals = {0,1},groups = {AddVaild.class, UpdateStatus.class})
	private Integer showStatus;
	/**
	 * 检索首字母
     * 自定义规则: 使用正则表达式
	 */
	@NotEmpty(groups = {AddVaild.class})
	@Pattern(regexp = "^[a-zA-z]$",message = "检索首字母必须是一个字母",groups = {AddVaild.class,UpdateVaild.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(groups = {AddVaild.class})
	@Min(value = 0,message = "排序必须大于等于0",groups = {AddVaild.class,UpdateVaild.class})
	private Integer sort;

}
