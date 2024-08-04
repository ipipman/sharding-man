package cn.ipman.sharding.core.datasource;


import cn.ipman.sharding.core.config.ShardingProperties;
import cn.ipman.sharding.core.engine.ShardingContext;
import cn.ipman.sharding.core.engine.ShardingResult;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分片数据源类，继承自AbstractRoutingDataSource，实现数据源的路由选择。
 * 通过ShardingProperties配置的多个数据源进行动态路由，选择合适的数据源进行数据库操作。
 *
 * @Author IpMan
 * @Date 2024/7/27 17:52
 */
public class ShardingDataSource extends AbstractRoutingDataSource {

    /**
     * 构造函数，初始化分片数据源。
     * 根据ShardingProperties配置的数据源信息，创建并配置多个Druid数据源。
     *
     * @param properties 分片配置属性，包含多个数据源的配置信息。
     */
    public ShardingDataSource(ShardingProperties properties) {
        Map<Object, Object> dataSourcesMap = new LinkedHashMap<>();
        properties.getDataSources().forEach((k, v) -> {
            // k = ds0, ds1 等，v 是对应的数据源配置
            try {
                dataSourcesMap.put(k, DruidDataSourceFactory.createDataSource(v));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // 设置目标数据源映射
        setTargetDataSources(dataSourcesMap);
        // 设置默认数据源，当无法确定路由时使用
        setDefaultTargetDataSource(dataSourcesMap.values().iterator().next());
    }

    /**
     * 确定当前的路由关键字，用于选择具体的数据源。
     * 通过ShardingContext获取当前的分片结果，进而得到应该路由到的具体数据源名称。
     *
     * @return 当前的路由关键字，即具体的数据源名称。
     */
    @Override
    protected Object determineCurrentLookupKey() {
        ShardingResult shardingResult = ShardingContext.get();
        Object key = shardingResult == null ? null : shardingResult.getTargetDataSourceName();
        System.out.println(" ===>> determineCurrentLookupKey: " + key);
        return key;
    }
}
