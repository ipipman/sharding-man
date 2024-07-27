package cn.ipman.shading.engine;

/**
 * Core engine.
 *
 * @Author IpMan
 * @Date 2024/7/27 18:12
 */
public interface ShardingEngine {

    ShardingResult sharding(String sql, Object[] args);

}
