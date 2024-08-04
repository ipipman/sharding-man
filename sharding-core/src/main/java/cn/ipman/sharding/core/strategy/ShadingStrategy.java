package cn.ipman.sharding.core.strategy;

import java.util.List;
import java.util.Map;

/**
 * 分片策略接口
 *
 * @Author IpMan
 * @Date 2024/7/27 20:55
 */
public interface ShadingStrategy {

    /**
     * 获取分片列
     * 该方法用于返回当前分片策略所使用的分片列名称列表。
     *
     * @return 分片列名称的列表
     */
    List<String> getShardingColumns();

    /**
     * 执行分片操作
     * 根据可用的目标名称列表、逻辑表名和分片参数，计算出具体的物理表名。
     *
     * @param availableTargetNames 可用的目标名称列表，通常是指数据库或表的物理名称列表
     * @param logicTableName       逻辑表名，即需要进行分片的表的逻辑名称
     * @param shardingParams       分片参数，包含用于分片计算的键值对，键为分片列名，值为分片列的值
     * @return 计算后的物理表名
     */
    String doSharding(List<String> availableTargetNames, String logicTableName, Map<String, Object> shardingParams);


}
