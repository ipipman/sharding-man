package cn.ipman.shading;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Sharding datasource
 *
 * @Author IpMan
 * @Date 2024/7/27 17:52
 */
public class ShardingDataSource extends AbstractRoutingDataSource {

    public ShardingDataSource(ShardingProperties properties) {
        Map<Object, Object> dataSourcesMap = new LinkedHashMap<>();
        properties.getDataSources().forEach((k, v) -> {
            // k = ds0, ds1
            try {
                dataSourcesMap.put(k, DruidDataSourceFactory.createDataSource(v));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // 设置目标数据源
        setTargetDataSources(dataSourcesMap);
        setDefaultTargetDataSource(dataSourcesMap.values().iterator().next());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        ShardingResult shardingResult = ShardingContext.get();
        Object key = shardingResult == null ? null : shardingResult.getTargetDataSourceName();
        System.out.println(" ===>> determineCurrentLookupKey: " + key);
        return key;
    }
}
