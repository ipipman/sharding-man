package cn.ipman.shading.engine;

/**
 * Sharding Context
 *
 * @Author IpMan
 * @Date 2024/7/27 18:12
 */
public class ShardingContext {

    private static final ThreadLocal<ShardingResult> LOCAL = new ThreadLocal<>();

    public static ShardingResult get(){
        return LOCAL.get();
    }

    public static void set(ShardingResult shardingResult){
        LOCAL.set(shardingResult);
    }

}
