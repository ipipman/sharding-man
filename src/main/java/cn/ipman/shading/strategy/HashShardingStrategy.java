package cn.ipman.shading.strategy;

import groovy.lang.Closure;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * hash strategy for sharding.
 *
 * @Author IpMan
 * @Date 2024/7/27 20:57
 */
public class HashShardingStrategy implements ShadingStrategy {

    // 需要Sharding的列
    final String shardingColumn;

    // Sharding的Hash算法表达式
    final String algorithmExpression;

    public HashShardingStrategy(Properties properties) {
        this.shardingColumn = properties.getProperty("shardingColumn");
        this.algorithmExpression = properties.getProperty("algorithmExpression");
    }

    @Override
    public List<String> getShardingColumns() {
        return List.of(this.shardingColumn);
    }

    @Override
    public String doSharding(List<String> availableTargetNames,
                             String logicTableName, Map<String, Object> shardingParams) {

        // 先纠正Hash算法表达式,避免错误
        String expression = InlineExpressionParser.handlePlaceHolder(algorithmExpression);
        InlineExpressionParser parser = new InlineExpressionParser(expression);

        // 根据params的分片key和分片表达式进行计算Hash值
        Closure<?> closure = parser.evaluateClosure();
        closure.setProperty(this.shardingColumn, shardingParams.get(this.shardingColumn));
        return closure.call().toString();
    }
}
