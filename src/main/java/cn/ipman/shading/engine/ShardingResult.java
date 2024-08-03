package cn.ipman.shading.engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表示分片操作的结果。
 * 此类用于封装分片后目标数据源和SQL语句的信息。
 *
 * @Author IpMan
 * @Date 2024/7/27 18:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShardingResult {

    /**
     * 目标数据源名称。
     */
    private String targetDataSourceName;

    /**
     * 目标SQL语句。
     */
    private String targetSqlStatement;

    /**
     * SQL参数
     */
    private Object[] parameters;

}
