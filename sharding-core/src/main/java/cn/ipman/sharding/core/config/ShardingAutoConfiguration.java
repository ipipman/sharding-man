package cn.ipman.sharding.core.config;


import cn.ipman.sharding.core.datasource.ShardingDataSource;
import cn.ipman.sharding.core.engine.ShardingEngine;
import cn.ipman.sharding.core.engine.StandardShardingEngine;
import cn.ipman.sharding.core.mybatis.SqlStatementInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置类，自动配置分片相关的组件。
 * 该类利用Spring Boot的自动配置特性，根据配置文件中的属性自动创建和配置分片数据源和分片引擎。
 *
 * @Author IpMan
 * @Date 2024/7/27 18:20
 */
@Configuration
@EnableConfigurationProperties(ShardingProperties.class)
public class ShardingAutoConfiguration {

    /**
     * 创建并配置分片数据源。
     * 根据ShardingProperties中的配置信息，初始化并返回一个ShardingDataSource实例。
     * 这个数据源会自动被Spring Boot纳入管理，并用于数据库分片操作。
     *
     * @param properties 分片配置属性，用于配置分片数据源。
     * @return 初始化后的分片数据源。
     */
    @Bean
    public ShardingDataSource shardingDataSource(ShardingProperties properties) {
        return new ShardingDataSource(properties);
    }

    /**
     * 创建并配置分片引擎。
     * 使用StandardShardingEngine实现，并根据ShardingProperties中的配置进行初始化。
     * 分片引擎负责实际的数据库分片逻辑处理。
     *
     * @param properties 分片配置属性，用于配置分片引擎。
     * @return 初始化后的分片引擎。
     */
    @Bean
    public ShardingEngine shardingEngine(ShardingProperties properties) {
        return new StandardShardingEngine(properties);
    }


    /**
     * 创建并配置SQL语句拦截器。
     * 该拦截器用于在SQL执行前、执行后或出现异常时进行一些额外的操作，比如统计SQL执行时间、记录执行日志等。
     *
     * @return 初始化后的SQL语句拦截器。
     */
    @Bean
    public SqlStatementInterceptor sqlStatementInterceptor() {
        return new SqlStatementInterceptor();
    }

}
