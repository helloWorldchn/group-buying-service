package com.example.groupbuying.activity.mapper;

import com.example.groupbuying.model.activity.ActivityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.groupbuying.model.activity.ActivityRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    List<Long> selectExistSkuIdList(@Param("skuIdList")List<Long> skuIdList);

    List<ActivityRule> selectActivityRuleList(@Param("skuId")Long skuId);

    List<ActivityRule> findActivityRule(Long skuId);
}
