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

    final String shardingColumn;
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
    public String doSharding(List<String> availableTargetNames, String logicTableName, Map<String, Object> shardingParams) {
        // 先转换下表达式,避免错误,再进行解析
        String expression = InlineExpressionParser.handlePlaceHolder(algorithmExpression);
        InlineExpressionParser parser = new InlineExpressionParser(expression);

        // 根据params的分片参数和分片表达式, 计算Hash值
        Closure<?> closure = parser.evaluateClosure();
        closure.setProperty(this.shardingColumn, shardingParams.get(this.shardingColumn));
        return closure.call().toString();
    }
}
