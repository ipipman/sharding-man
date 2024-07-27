package cn.ipman.shading;

import cn.ipman.shading.demo.User;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.mapper.MapperFactoryBean;

import java.lang.reflect.Proxy;

/**
 * Factory bean for mapper.
 *
 * @Author IpMan
 * @Date 2024/7/27 18:53
 */
public class ShardingMapperFactoryBean<T> extends MapperFactoryBean<T> {

    public ShardingMapperFactoryBean() {
    }

    public ShardingMapperFactoryBean(Class<T> mapperInterface) {
        super(mapperInterface);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        Object proxy = super.getObject();
        SqlSession session = getSqlSession();
        Configuration configuration = session.getConfiguration();
        Class<?> clazz = getMapperInterface();
        // 创建 mapper 代理
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (p, method, args) -> {
            String mapperId = clazz.getName() + "." + method.getName();
            MappedStatement statement = configuration.getMappedStatement(mapperId);
            BoundSql boundSql = statement.getBoundSql(args);
            System.out.println(" ===> sql statement: " + boundSql);

            Object parameterObject = args[0];
            if (parameterObject instanceof User user) {
                ShardingContext.set(new ShardingResult(user.getId() % 2 == 0 ? "ds0" : "ds1"));
            } else if (parameterObject instanceof Integer id) {
                ShardingContext.set(new ShardingResult(id % 2 == 0 ? "ds0" : "ds1"));
            }
            return method.invoke(proxy, args);
        });

    }
}
