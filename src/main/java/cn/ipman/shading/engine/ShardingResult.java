package cn.ipman.shading.engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sharding Result.
 *
 * @Author IpMan
 * @Date 2024/7/27 18:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShardingResult {

    private String targetDataSourceName;

}
