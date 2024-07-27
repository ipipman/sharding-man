package cn.ipman.shading.engine;

import cn.ipman.shading.config.ShardingProperties;
import cn.ipman.shading.demo.model.User;
import cn.ipman.shading.strategy.HashShardingStrategy;
import cn.ipman.shading.strategy.ShadingStrategy;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
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
    final Map<String, ShadingStrategy> datasourceStrategys = new HashMap<>();

    // 分表策略
    final Map<String, ShadingStrategy> tableStrategys = new HashMap<>();


    public StandardShardingEngine(ShardingProperties properties) {
        // 遍历所有的逻辑表名, 初始化逻辑表和实际库、表对应关系,以及sharding的列和算法策略
        properties.getTables().forEach((table, tableProperties) -> {
            // 实际的库、表名字
            tableProperties.getActualDataNodes().forEach(actualDataNode -> {
                String[] split = actualDataNode.split("\\.");
                String databaseName = split[0], tableName = split[1];
                actualDatabaseNames.add(databaseName, tableName);
                actualTableNames.add(tableName, tableName);
            });
            // 分库、分表策略
            datasourceStrategys.put(table, new HashShardingStrategy(tableProperties.getDatasourceStrategy()));
            tableStrategys.put(table, new HashShardingStrategy(tableProperties.getTableStrategy()));
        });
    }


    @Override
    public ShardingResult sharding(String sql, Object[] args) {

        // 解析SQL
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        // 如果是insert语句
        if (sqlStatement instanceof SQLInsertStatement sqlInsertStatement) {
            // 获取表名
            String table = sqlInsertStatement.getTableName().getSimpleName();
            Map<String, Object> shardingColumnsMap = new HashMap<>();

            // 匹配参数列、值的对应关系, 最终形成一个Map
            List<SQLExpr> cols = sqlInsertStatement.getColumns();
            for (int i = 0; i < cols.size(); i++) {
                SQLExpr column = cols.get(i);
                SQLIdentifierExpr columnName = (SQLIdentifierExpr) column;
                String columnNameStr = columnName.getSimpleName();
                shardingColumnsMap.put(columnNameStr, args[i]);
            }

            // 通过sharding选择database
            ShadingStrategy databaseStrategy = datasourceStrategys.get(table);
            // 通过用户sql种的表名,知道实际对应的库名列表
            List<String> actualDatabases = actualDatabaseNames.get(table);
            String targetDatabase = databaseStrategy.doSharding(actualDatabases, table, shardingColumnsMap);

            // 通过sharding选择table
            ShadingStrategy tableStrategy = tableStrategys.get(table);
            // 通过用户sql种的表名,知道实际对应的表名列表
            List<String> actualTables = actualTableNames.get(table);
            String targetTable = tableStrategy.doSharding(actualTables, table, shardingColumnsMap);
            System.out.println(" ====>>>> target db.table = " + targetDatabase + "." + targetTable);


        } else {
            // select/update/insert
        }

        // todo: 以下测试

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
