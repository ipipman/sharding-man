package cn.ipman.shading.mybatis;

import cn.ipman.shading.engine.ShardingContext;
import cn.ipman.shading.engine.ShardingEngine;
import cn.ipman.shading.engine.ShardingResult;
import cn.ipman.shading.demo.model.User;
import lombok.Setter;
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

    @Setter
    ShardingEngine engine;

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

            // 选择库
            ShardingResult result = engine.sharding(boundSql.getSql(), args);
            ShardingContext.set(result);

            return method.invoke(proxy, args);
        });

    }
}
