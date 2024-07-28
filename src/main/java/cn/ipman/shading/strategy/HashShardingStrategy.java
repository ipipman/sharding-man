package cn.ipman.shading.strategy;

import groovy.lang.Closure;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 哈希分片策略实现类。
 * 该类用于根据哈希算法对数据进行分片，实现数据的分布式存储。
 *
 * @Author IpMan
 * @Date 2024/7/27 20:57
 */
public class HashShardingStrategy implements ShadingStrategy {

    // 需要Sharding的列名
    final String shardingColumn;

    // 分片算法表达式, Sharding的Hash算法表达式
    final String algorithmExpression;

    /**
     * 构造函数，通过Properties对象初始化分片列名和算法表达式。
     *
     * @param properties 包含分片列名和算法表达式的Properties对象。
     */
    public HashShardingStrategy(Properties properties) {
        this.shardingColumn = properties.getProperty("shardingColumn");
        this.algorithmExpression = properties.getProperty("algorithmExpression");
    }

    /**
     * 获取分片列名列表。
     *
     * @return 分片列名的List。
     */
    @Override
    public List<String> getShardingColumns() {
        return List.of(this.shardingColumn);
    }

    /**
     * 根据提供的逻辑表名和分片参数，计算物理表名。
     *
     * @param availableTargetNames 可用的目标表名列表。
     * @param logicTableName       逻辑表名。
     * @param shardingParams       分片参数Map，包含用于分片计算的键值对。
     * @return 计算后的物理表名。
     */
    @Override
    public String doSharding(List<String> availableTargetNames,
                             String logicTableName, Map<String, Object> shardingParams) {

        // 处理算法表达式中的占位符, 先纠正Hash算法表达式,避免错误
        String expression = InlineExpressionParser.handlePlaceHolder(algorithmExpression);
        InlineExpressionParser parser = new InlineExpressionParser(expression);

        // 创建并执行分片算法闭包, 根据params的分片key和分片表达式进行计算Hash值
        Closure<?> closure = parser.evaluateClosure();
        closure.setProperty(this.shardingColumn, shardingParams.get(this.shardingColumn));
        return closure.call().toString();
    }
}
