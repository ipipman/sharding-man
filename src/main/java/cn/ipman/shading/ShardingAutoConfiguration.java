package cn.ipman.shading;

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

}
