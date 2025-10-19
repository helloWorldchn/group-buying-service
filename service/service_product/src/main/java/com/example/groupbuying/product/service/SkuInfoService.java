package com.example.groupbuying.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.vo.product.SkuInfoQueryVo;
import com.example.groupbuying.vo.product.SkuInfoVo;

import java.util.List;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
public interface SkuInfoService extends IService<SkuInfo> {

    // 获取sku分页列表
    IPage<SkuInfo> selectPageSkuInfo(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo);

    //添加商品
    void saveSkuInfo(SkuInfoVo skuInfoVo);

    //根据sku的id获取商品sku信息
    SkuInfoVo getSkuInfoVo(Long id);

    //修改商品sku信息
    void updateSkuInfo(SkuInfoVo skuInfoVo);

    // 商品审核
    void check(Long skuId, Integer status);

    // 商品上下架
    void publish(Long skuId, Integer status);

    //新人专享
    void isNewPerson(Long skuId, Integer status);

    //批量获取sku信息
    List<SkuInfo> findSkuInfoList(List<Long> skuIdList);

    //根据关键字获取sku列表
    /**
     * 根据关键字查询sku列表
     * @param keyword 关键字
     * @return sku列表
     */
    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    /**
     * 获取新人专享商品列表
     * @return 新人专享商品列表
     */
    List<SkuInfo> findNewPersonSkuInfoList();
}
