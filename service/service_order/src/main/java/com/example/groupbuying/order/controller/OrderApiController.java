package com.example.groupbuying.order.controller;

import com.example.groupbuying.common.auth.AuthContextHolder;
import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.model.order.OrderInfo;
import com.example.groupbuying.order.service.OrderInfoService;
import com.example.groupbuying.vo.order.OrderConfirmVo;
import com.example.groupbuying.vo.order.OrderSubmitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(value = "Order管理", tags = "Order管理")
@RestController
@RequestMapping(value="/api/order")
public class OrderApiController {
	
	@Resource
	private OrderInfoService orderService;

	@ApiOperation("确认订单")
	@GetMapping("auth/confirmOrder")
	public Result<OrderConfirmVo> confirm() {
		OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
		return Result.ok(orderConfirmVo);
	}

	@ApiOperation("生成订单")
	@PostMapping("auth/submitOrder")
	public Result<Long> submitOrder(@RequestBody OrderSubmitVo orderParamVo, HttpServletRequest request) {
		// 获取到用户Id
		Long userId = AuthContextHolder.getUserId();
		Long orderId = orderService.submitOrder(orderParamVo);
		return Result.ok(orderId);
	}

	@ApiOperation("获取订单详情")
	@GetMapping("auth/getOrderInfoById/{orderId}")
	public Result<OrderInfo> getOrderInfoById(@PathVariable("orderId") Long orderId){
		OrderInfo orderInfoById = orderService.getOrderInfoById(orderId);
		return Result.ok(orderInfoById);
	}
}