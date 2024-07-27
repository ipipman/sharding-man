package cn.ipman.shading.mybatis;

import cn.ipman.shading.engine.ShardingContext;
import cn.ipman.shading.engine.ShardingEngine;
import cn.ipman.shading.engine.ShardingResult;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;

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

            // 选择库,经过sharding后重新生成SQL
            Object[] params = getParams(boundSql, args);
            ShardingResult result = engine.sharding(boundSql.getSql(), params);
            ShardingContext.set(result);

            return method.invoke(proxy, args);
        });

    }

    @SneakyThrows
    private static Object[] getParams(BoundSql boundSql, Object[] args) {
        Object[] params = args;
        // 如果不是基础类或包装类
        if (args.length == 1 && !ClassUtils.isPrimitiveOrWrapper(args[0].getClass())) {
            Object arg = args[0];

            List<String> cols = boundSql.getParameterMappings()
                    .stream().map(ParameterMapping::getProperty).toList();
            Object[] values = new Object[cols.size()];
            for (int i = 0; i < cols.size(); i++) {
                Field field = ReflectionUtils.findField(arg.getClass(), cols.get(i));
                if (field == null) throw new IllegalArgumentException("can not find field " + cols.get(i));
                field.setAccessible(true);
                values[i] = field.get(arg);
            }
            params = values;
        }

        return params;
    }
}
