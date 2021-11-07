package com.atguigu.common.vaild;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lzx
 * 自定义校验器
 */
public class ListValueConstraintValidator implements ConstraintValidator<ValueList,Integer> {

    private Set<Integer> set = new HashSet<>();

    /**
     * 初始化方法
     */
    @Override
    public void initialize(ValueList constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            // 遍历放入set中
            set.add(val);
        }
    }

    /**
     * @param value：需要校验的值
     * @param context：整个校验的上下文环境信息
     * @return true or false
     * 判断校验
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // 判断是否包含传入的值
        return set.contains(value);
    }
}
