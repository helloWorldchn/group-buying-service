package com.example.groupbuying.search.service;

import com.example.groupbuying.model.search.SkuEs;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface SkuService {

    /**
     * 上架商品列表
     * @param skuId 商品id
     */
    void upperSku(Long skuId);

    /**
     * 下架商品列表
     * @param skuId 商品id
     */
    void lowerSku(Long skuId);
    /**
     * 获取爆品商品
     * @return 爆品商品列表
     */
    List<SkuEs> findHotSkuList();
}