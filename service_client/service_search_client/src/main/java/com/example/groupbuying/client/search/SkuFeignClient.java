package com.example.groupbuying.client.search;

import com.example.groupbuying.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "service-search")
public interface SkuFeignClient {
    // 获取爆品商品
    @GetMapping("/api/search/sku/inner/findHotSkuList")
    List<SkuEs> findHotSkuList();
}
