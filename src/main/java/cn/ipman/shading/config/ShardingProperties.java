package cn.ipman.shading.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuration for sharding
 *
 * @Author IpMan
 * @Date 2024/7/27 17:49
 */
@Data
@ConfigurationProperties(prefix = "spring.sharding")
public class ShardingProperties {

    private Map<String, Properties> dataSources = new LinkedHashMap<>();

}
