package cn.ipman.shading.engine;

import cn.ipman.shading.config.ShardingProperties;
import cn.ipman.shading.demo.model.User;
import cn.ipman.shading.strategy.HashShardingStrategy;
import cn.ipman.shading.strategy.ShadingStrategy;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Description for this class
 *
 * @Author IpMan
 * @Date 2024/7/27 20:35
 */
public class StandardShardingEngine implements ShardingEngine {

    // 实际Database的名字
    final MultiValueMap<String, String> actualDatabaseNames = new LinkedMultiValueMap<>();

    // 实际Table的名字
    final MultiValueMap<String, String> actualTableNames = new LinkedMultiValueMap<>();

    // 分库策略
    final Map<String, ShadingStrategy> datasourceStrategy = new HashMap<>();

    // 分表策略
    final Map<String, ShadingStrategy> tableStrategy = new HashMap<>();


    public StandardShardingEngine(ShardingProperties properties) {
        properties.getTables().forEach((table, tableProperties) -> {
            // 实际的库、表名字
            tableProperties.getActualDataNodes().forEach(actualDataNode -> {
                String[] split = actualDataNode.split("\\.");
                String databaseName = split[0], tableName = split[1];
                actualDatabaseNames.add(databaseName, tableName);
                actualTableNames.add(tableName, tableName);
            });
            // 分库、分表策略
            datasourceStrategy.put(table, new HashShardingStrategy(tableProperties.getDatasourceStrategy()));
            tableStrategy.put(table, new HashShardingStrategy(tableProperties.getTableStrategy()));
        });
    }


    @Override
    public ShardingResult sharding(String sql, Object[] args) {
        Object parameterObject = args[0];
        int id = 0;
        if (parameterObject instanceof User user) {
            id = user.getId();
        } else if (parameterObject instanceof Integer uid) {
            id = uid;
        }
        return new ShardingResult(id % 2 == 0 ? "ds0" : "ds1", sql);
    }
}
