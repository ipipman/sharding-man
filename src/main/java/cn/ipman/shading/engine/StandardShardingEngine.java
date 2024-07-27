package cn.ipman.shading.engine;

import cn.ipman.shading.config.ShardingProperties;
import cn.ipman.shading.demo.model.User;

/**
 * Description for this class
 *
 * @Author IpMan
 * @Date 2024/7/27 20:35
 */
public class StandardShardingEngine implements ShardingEngine {

    public StandardShardingEngine(ShardingProperties properties) {

    }

    @Override
    public ShardingResult sharding(String sql, Object[] args) {
        Object parameterObject = args[0];
        int id = 0;
        if (parameterObject instanceof User user) {
            id = user.getId();
        } else if (parameterObject instanceof Integer uid) {
            id = uid;
        }
        return new ShardingResult(id % 2 == 0 ? "ds0" : "ds1", sql);
    }
}
