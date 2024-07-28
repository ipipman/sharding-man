package cn.ipman.shading.engine;

/**
 * 分片上下文类，用于存储和管理分片操作的上下文信息。
 * 本类提供了获取和设置线程局部的分片结果信息的方法，利用ThreadLocal实现。
 *
 * @Author IpMan
 * @Date 2024/7/27 18:12
 */
public class ShardingContext {

    // 使用ThreadLocal来存储当前线程的分片结果
    private static final ThreadLocal<ShardingResult> LOCAL = new ThreadLocal<>();

    /**
     * 获取当前线程的分片结果
     *
     * @return 当前线程的ShardingResult对象
     */
    public static ShardingResult get(){
        return LOCAL.get();
    }

    /**
     * 设置当前线程的分片结果
     *
     * @param shardingResult 需要设置的ShardingResult对象
     */
    public static void set(ShardingResult shardingResult){
        LOCAL.set(shardingResult);
    }

}
