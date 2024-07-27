package cn.ipman.shading.config;

import cn.ipman.shading.datasource.ShardingDataSource;
import cn.ipman.shading.engine.ShardingEngine;
import cn.ipman.shading.engine.StandardShardingEngine;
import cn.ipman.shading.mybatis.SqlStatementInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sharding Configuration
 *
 * @Author IpMan
 * @Date 2024/7/27 18:20
 */
@Configuration
@EnableConfigurationProperties(ShardingProperties.class)
public class ShardingAutoConfiguration {

    @Bean
    public ShardingDataSource shardingDataSource(ShardingProperties properties) {
        return new ShardingDataSource(properties);
    }

    @Bean
    public ShardingEngine shardingEngine(ShardingProperties properties) {
        return new StandardShardingEngine(properties);
    }

    @Bean
    public SqlStatementInterceptor sqlStatementInterceptor() {
        return new SqlStatementInterceptor();
    }

}
