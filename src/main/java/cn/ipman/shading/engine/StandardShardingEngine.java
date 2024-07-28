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
 * 标准分片引擎实现类，负责根据分片策略对数据库和表进行分片。
 *
 * @Author IpMan
 * @Date 2024/7/27 20:35
 */
public class StandardShardingEngine implements ShardingEngine {

    /**
     * 存储实际的数据库名称，用于分库。
     */
    final MultiValueMap<String, String> actualDatabaseNames = new LinkedMultiValueMap<>();

    /**
     * 存储实际的表名称，用于分表。
     */
    final MultiValueMap<String, String> actualTableNames = new LinkedMultiValueMap<>();

    /**
     * 存储分库策略实例，每个数据源对应一个策略。
     */
    final Map<String, ShadingStrategy> datasourceStrategys = new HashMap<>();

    /**
     * 存储分表策略实例，每个表对应一个策略。
     */
    final Map<String, ShadingStrategy> tableStrategys = new HashMap<>();


    /**
     * 构造函数，初始化分片引擎。
     *
     * @param properties 分片配置属性，包含表和对应的分片策略。
     */
    public StandardShardingEngine(ShardingProperties properties) {
        // 遍历配置的表信息，初始化实际的数据库和表名称，以及对应的分库分表策略
        properties.getTables().forEach((table, tableProperties) -> {
            // 解析实际的数据库和表名称。
            tableProperties.getActualDataNodes().forEach(actualDataNode -> {
                String[] split = actualDataNode.split("\\.");
                String databaseName = split[0], tableName = split[1];
                actualDatabaseNames.add(databaseName, tableName);
                actualTableNames.add(tableName, tableName);
            });
            // 初始化分库和分表策略为哈希分片策略。
            datasourceStrategys.put(table, new HashShardingStrategy(tableProperties.getDatasourceStrategy()));
            tableStrategys.put(table, new HashShardingStrategy(tableProperties.getTableStrategy()));
        });
    }


    /**
     * 对给定的SQL语句进行分片处理。
     *
     * @param sql 待处理的SQL语句。
     * @param args SQL语句中的参数。
     * @return 分片结果，包含目标数据库和表的名称。
     */
    @Override
    public ShardingResult sharding(String sql, Object[] args) {
        // 解析SQL语句
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        // 当前仅处理插入语句
        if (sqlStatement instanceof SQLInsertStatement sqlInsertStatement) {
            // 获取目标表名
            String table = sqlInsertStatement.getTableName().getSimpleName();
            Map<String, Object> shardingColumnsMap = new HashMap<>();

            // 提取插入语句中的列名和对应的参数值
            List<SQLExpr> cols = sqlInsertStatement.getColumns();
            for (int i = 0; i < cols.size(); i++) {
                SQLExpr column = cols.get(i);
                SQLIdentifierExpr columnName = (SQLIdentifierExpr) column;
                String columnNameStr = columnName.getSimpleName();
                shardingColumnsMap.put(columnNameStr, args[i]);
            }

            // 根据分库策略选择目标数据库
            ShadingStrategy databaseStrategy = datasourceStrategys.get(table);
            // 通过用户sql种的表名,知道实际对应的库名列表
            List<String> actualDatabases = actualDatabaseNames.get(table);
            String targetDatabase = databaseStrategy.doSharding(actualDatabases, table, shardingColumnsMap);

            // 根据分表策略选择目标表
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
