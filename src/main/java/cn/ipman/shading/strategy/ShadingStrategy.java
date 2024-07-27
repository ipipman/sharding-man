package cn.ipman.shading.strategy;

import java.util.List;
import java.util.Map;

/**
 * strategy for sharidng
 *
 * @Author IpMan
 * @Date 2024/7/27 20:55
 */
public interface ShadingStrategy {

    List<String> getShardingColumns();

    String doSharding(List<String> availableTargetNames, String logicTableName, Map<String, Object> shardingParams);


}
