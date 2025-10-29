package com.example.groupbuying.activity.service.impl;

import com.example.groupbuying.activity.mapper.ActivityRuleMapper;
import com.example.groupbuying.activity.mapper.ActivitySkuMapper;
import com.example.groupbuying.client.product.ProductFeignClient;
import com.example.groupbuying.enums.ActivityType;
import com.example.groupbuying.model.activity.ActivityInfo;
import com.example.groupbuying.activity.mapper.ActivityInfoMapper;
import com.example.groupbuying.activity.service.ActivityInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.groupbuying.model.activity.ActivityRule;
import com.example.groupbuying.model.activity.ActivitySku;
import com.example.groupbuying.model.product.SkuInfo;
import com.example.groupbuying.vo.activity.ActivityRuleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */

@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Autowired
    private ActivityRuleMapper activityRuleMapper;

    @Autowired
    private ActivitySkuMapper activitySkuMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    //优惠活动列表方法
    @Override
    public IPage<ActivityInfo> selectPageActivityInfo(Page<ActivityInfo> pageParam) {
        QueryWrapper<ActivityInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        IPage<ActivityInfo> page = activityInfoMapper.selectPage(pageParam, queryWrapper);
        //page.toString()
        page.getRecords().stream().forEach(item -> {
            item.setActivityTypeString(item.getActivityType().getComment());
        });
        return page;
    }

    //1.根据活动id获取活动规则数据
    @Override
    public Map<String, Object> findActivityRuleList(Long activityId) {
        Map<String, Object> result = new HashMap<>();
        // 1.根据活动id查询，查询规则列表activity_rule表
        QueryWrapper queryWrapper = new QueryWrapper<ActivityRule>();
        queryWrapper.eq("activity_id",activityId);
        List<ActivityRule> activityRuleList = activityRuleMapper.selectList(queryWrapper);
        result.put("activityRuleList", activityRuleList);
        // 2.根据活动id查询，查询规则商品列表activity_sku表
        QueryWrapper activitySkuQueryWrapper = new QueryWrapper<ActivitySku>();
        activitySkuQueryWrapper.eq("activity_id",activityId);
        List<ActivitySku> activitySkuList = activitySkuMapper.selectList(activitySkuQueryWrapper);
        List<Long> skuIdList = activitySkuList.stream().map(ActivitySku::getSkuId).collect(Collectors.toList());
        //通过远程调用service-product模块接口，根据skuId列表得到商品信息
        List<SkuInfo> skuInfoList = new ArrayList<>();
        if (skuIdList.size() > 0) {
            skuInfoList = productFeignClient.findSkuInfoList(skuIdList);
        }
        result.put("skuInfoList", skuInfoList);
        return result;
    }

    //2.在活动里面添加规则数据
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveActivityRule(ActivityRuleVo activityRuleVo) {
        // 第一步 根据活动id删除之前规则数据
        activityRuleMapper.delete(new QueryWrapper<ActivityRule>().eq("activity_id",activityRuleVo.getActivityId()));
        activitySkuMapper.delete(new QueryWrapper<ActivitySku>().eq("activity_id",activityRuleVo.getActivityId()));
        // 第二步 获取规则列表数组
        List<ActivityRule> activityRuleList = activityRuleVo.getActivityRuleList();
        ActivityInfo activityInfo = baseMapper.selectById(activityRuleVo.getActivityId());
        for(ActivityRule activityRule : activityRuleList) {
            activityRule.setActivityId(activityRuleVo.getActivityId());
            activityRule.setActivityType(activityInfo.getActivityType());
            activityRuleMapper.insert(activityRule);
        }
        // 第三部 获取规则范围数据
        List<ActivitySku> activitySkuList = activityRuleVo.getActivitySkuList();
        for(ActivitySku activitySku : activitySkuList) {
            activitySku.setActivityId(activityRuleVo.getActivityId());
            activitySkuMapper.insert(activitySku);
        }
    }

    //3.根据关键字查询匹配sku信息
    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        // 第一步 根据关键字查询sku匹配内容列表
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoByKeyword(keyword);
        // 判断：根据关键字查找不到集合，直接返回空
        if (skuInfoList.size() == 0) {
            return skuInfoList;
        }
        List<Long> skuIdList = skuInfoList.stream().map(SkuInfo::getId).collect(Collectors.toList());
        // 第二步 判断添加商品之前是否参加过活动，如果之前参加过，活动正在进行中，排除商品
        List<SkuInfo> notExistSkuInfoList = new ArrayList<>();
        // 查询两张表activity_info和activity_sku判断，编写sql语句实现
        List<Long> existSkuIdList = activityInfoMapper.selectExistSkuIdList(skuIdList);
        String existSkuIdString = "," + StringUtils.join(existSkuIdList.toArray(), ",") + ",";
        for(SkuInfo skuInfo : skuInfoList) {
            if(existSkuIdString.indexOf(","+skuInfo.getId()+",") == -1) {
                notExistSkuInfoList.add(skuInfo);
            }
        }
        return notExistSkuInfoList;
    }

    //查询商品获取规则数据
    @Override
    public List<ActivityRule> findActivityRule(Long skuId) {
        List<ActivityRule> activityRuleList = activityInfoMapper.selectActivityRuleList(skuId);
        if(!CollectionUtils.isEmpty(activityRuleList)) {
            for(ActivityRule activityRule : activityRuleList) {
                activityRule.setRuleDesc(this.getRuleDesc(activityRule));
            }
        }
        return activityRuleList;
    }


    /**
     * 根据skuId列表获取促销信息
     *
     * @param skuIdList skuId列表
     * @return 取促销信息
     */
    @Override
    public Map<Long, List<String>> findActivity(List<Long> skuIdList) {
        Map<Long, List<String>> result = new HashMap<>();
        //skuIdList遍历，得到每个skuId
        skuIdList.forEach(skuId -> {
            //根据skuId进行查询，查询sku对应活动里面规则列表
            List<ActivityRule> activityRuleList = baseMapper.findActivityRule(skuId);
            //数据封装，规则名称
            if(!CollectionUtils.isEmpty(activityRuleList)) {
                List<String> ruleList = new ArrayList<>();
                //把规则名称处理
                for (ActivityRule activityRule:activityRuleList) {
                    ruleList.add(this.getRuleDesc(activityRule));
                }
                result.put(skuId,ruleList);
            }
        });
        return result;
    }

    //构造规则名称的方法
    private String getRuleDesc(ActivityRule activityRule) {
        ActivityType activityType = activityRule.getActivityType();
        StringBuilder ruleDesc = new StringBuilder();
        if (activityType == ActivityType.FULL_REDUCTION) {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionAmount())
                    .append("元减")
                    .append(activityRule.getBenefitAmount())
                    .append("元");
        } else {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionNum())
                    .append("元打")
                    .append(activityRule.getBenefitDiscount())
                    .append("折");
        }
        return ruleDesc.toString();
    }
}
