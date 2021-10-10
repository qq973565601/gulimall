package com.atguigu.gulimall.member.feign;


import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务调用类
 * 调用coupon优惠券服务所在的接口
 * 流程：先从gulimall-coupon服务中寻找，再从coupon/coupon/member/list寻找调用方法
 * @author lzx
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @RequestMapping("coupon/coupon/member/list")
    public R testMemberCoupon();

}
