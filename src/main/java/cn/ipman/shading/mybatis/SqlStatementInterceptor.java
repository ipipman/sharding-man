package cn.ipman.shading.mybatis;

import cn.ipman.shading.engine.ShardingContext;
import cn.ipman.shading.engine.ShardingResult;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;
import org.springframework.stereotype.Component;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * SqlStatementInterceptor 类实现了 MyBatis 的 Interceptor 接口，用于拦截 StatementHandler 的 prepare 方法。
 * 主要功能是打印 SQL 语句及其参数，以及提供了一个 TODO 注释提示未来可能的 SQL 修改功能。
 *
 * @Author IpMan
 * @Date 2024/7/27 17:49
 */
@Component
@Intercepts(
        @org.apache.ibatis.plugin.Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {java.sql.Connection.class, Integer.class}
        )
)
public class SqlStatementInterceptor implements Interceptor {

    /**
     * 当拦截器被调用时，此方法执行。它获取即将执行的 SQL 语句及其参数，并进行打印。
     *
     * @param invocation Invocation 对象，包含当前的调用信息。
     * @return 返回 Invocation 对象的 proceed 方法调用结果，即执行原始方法的返回值。
     * @throws Throwable 如果执行过程中出现异常，则抛出。
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ShardingResult result = ShardingContext.get();
        if (result != null) {
            StatementHandler handler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = handler.getBoundSql();
            System.out.println(" ===> sql statement: " + boundSql.getSql());

            String originalSql = boundSql.getSql();
            String targetSql = result.getTargetSqlStatement();
            if (!originalSql.equals(targetSql)) {
                // 替换SQL
                replaceSql(boundSql, result.getTargetSqlStatement());
                System.out.println(" ===> sql replaced: " + boundSql.getSql());
            }
        }
        return invocation.proceed();
    }

    private static void replaceSql(BoundSql boundSql, String sql) throws NoSuchFieldException {
        // 通过Unsafe改BoundSQL对象中的sql属性值，因为这个属性被final修饰了，所以暂时没找到其他方法
        Field field = boundSql.getClass().getDeclaredField("sql");
        Unsafe unsafe = UnsafeUtils.getUnsafe();
        long fieldOffset = unsafe.objectFieldOffset(field); // 获取偏移量
        unsafe.putObject(boundSql, fieldOffset, sql);
    }
}

