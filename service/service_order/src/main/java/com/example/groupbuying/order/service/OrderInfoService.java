package com.example.groupbuying.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.model.order.OrderInfo;
import com.example.groupbuying.vo.order.OrderConfirmVo;
import com.example.groupbuying.vo.order.OrderSubmitVo;

public interface OrderInfoService extends IService<OrderInfo> {
    /**
     * 确认订单
     *
     * @return 订单确认信息
     */
    OrderConfirmVo confirmOrder();

    /**
     * 生成订单
     *
     * @param orderParamVo 订单参数
     * @return 订单id
     */
    Long submitOrder(OrderSubmitVo orderParamVo);

    /**
     * 获取订单详情
     *
     * @param orderId 订单id
     * @return 订单详情
     */
    OrderInfo getOrderInfoById(Long orderId);
}