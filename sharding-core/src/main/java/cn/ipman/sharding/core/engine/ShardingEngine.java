package cn.ipman.sharding.core.engine;

/**
 * 此接口定义了分片引擎的核心功能。
 * 分片引擎负责执行数据分片的逻辑，提供统一的外部调用接口。
 * 它使用统一的SQL接口处理不同的数据库操作，支持参数化查询以增强代码的复用性和安全性。
 *
 * @Author IpMan
 * @Date 2024/7/27 18:12
 */
public interface ShardingEngine {

    /**
     * 对指定的SQL和参数执行分片操作。
     * 此方法是分片引擎的核心功能，负责解析SQL、确定目标数据源并执行查询。
     * 它支持传入可变参数以满足不同SQL语句的需求，增强了引擎的灵活性。
     *
     * @param sql   SQL语句
     * @param args  SQL语句中的参数数组
     * @return      分片结果对象，包含分片后的执行计划和数据
     */
    ShardingResult sharding(String sql, Object[] args);

}
