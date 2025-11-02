package com.example.groupbuying.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.model.activity.CouponInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.vo.activity.CouponRuleVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */
public interface CouponInfoService extends IService<CouponInfo> {

    //获取分页列表
    IPage<CouponInfo> selectPageCouponInfo(Page<CouponInfo> pageParam);

    //根据id获取优惠券
    CouponInfo getCouponInfo(String id);

    //根据优惠卷id获取优惠券规则列表
    Map<String, Object> findCouponRuleList(Long couponId);

    //新增优惠券规则
    void saveCouponRule(CouponRuleVo couponRuleVo);

    //根据关键字获取sku列表，活动使用
    List<CouponInfo> findCouponByKeyword(String keyword);

    List<CouponInfo> findCouponInfo(Long skuId, Long userId);
}
