package cn.ipman.sharding.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 配置分片属性类，用于集中管理分片相关的配置信息。
 * 该类通过@ConfigurationProperties注解与配置文件中的spring.sharding前缀绑定。
 *
 * @Author IpMan
 * @Date 2024/7/27 17:49
 */
@Data
@ConfigurationProperties(prefix = "spring.sharding")
public class ShardingProperties {

    /**
     * 数据源配置映射，键为数据源名称，值为对应的数据源配置Properties对象。
     * 用于配置多个数据源的信息，支持动态添加和删除数据源。
     */
    private Map<String, Properties> dataSources = new LinkedHashMap<>();

    /**
     * 分表分库配置映射，键为表名，值为对应表的分库分表配置TableProperties对象。
     * 用于配置每个表的分库分表策略，支持动态添加和删除表配置。
     */
    private Map<String, TableProperties> tables = new LinkedHashMap<>();

    @Data
    public static class TableProperties {
        /**
         * 实际的数据节点列表，每个数据节点由数据库名和表名组成。
         * 用于配置表的实际物理存储位置，支持多数据节点配置。
         */
        private List<String> actualDataNodes;

        /**
         * 数据源策略配置，用于配置表级别的数据源选择策略。
         * 通过Properties对象存储策略配置信息，键值对形式表示不同的策略配置项。
         */
        private Properties datasourceStrategy;

        /**
         * 表策略配置，用于配置表的分库分表策略。
         * 通过Properties对象存储策略配置信息，键值对形式表示不同的策略配置项。
         */
        private Properties tableStrategy;
    }
}


