package cn.ipman.shading.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.stereotype.Component;

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
     * @param invocation Invocation 对象，包含当前的调用信息。
     * @return 返回 Invocation 对象的 proceed 方法调用结果，即执行原始方法的返回值。
     * @throws Throwable 如果执行过程中出现异常，则抛出。
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = handler.getBoundSql();
        System.out.println(" ===> sql statement: " + boundSql.getSql());

        Object parameterObject = boundSql.getParameterObject();
        System.out.println(" ===> sql parameters Type: " + boundSql.getParameterObject().getClass());
        System.out.println(" ===> sql parameters: " + parameterObject);
        // todo 修改sql， user -> user1
        return invocation.proceed();
    }
}
