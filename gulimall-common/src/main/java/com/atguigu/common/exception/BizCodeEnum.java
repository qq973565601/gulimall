package com.atguigu.common.exception;


/**
 * 统一状态码处理
 * 使用枚举：其特点为私有构造器
 * @author lzx
 */
public enum BizCodeEnum {
    UNKNOWN_EXCPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数格式校验失败");

    /**
     * 封装状态码
     */
    private Integer code;
    private String msg;

    BizCodeEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
