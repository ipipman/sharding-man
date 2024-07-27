package cn.ipman.shading.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.stereotype.Component;

/**
 * intercept sql.
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
