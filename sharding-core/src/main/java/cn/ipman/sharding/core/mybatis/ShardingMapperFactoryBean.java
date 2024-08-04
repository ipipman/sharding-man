package cn.ipman.sharding.core.mybatis;

import cn.ipman.sharding.core.engine.ShardingContext;
import cn.ipman.sharding.core.engine.ShardingEngine;
import cn.ipman.sharding.core.engine.ShardingResult;
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
 * 为分库分表的MyBatis映射器提供工厂bean。
 * 该类继承自{@link MapperFactoryBean}，并添加了分库分表的逻辑。
 *
 * @param <T> 映射器接口的类型。
 */
@Setter
public class ShardingMapperFactoryBean<T> extends MapperFactoryBean<T> {

    /**
     * 分库分表引擎。
     * 用于根据业务逻辑将SQL路由到正确的数据库和表。
     * 由Spring自动注入Bean
     */
    ShardingEngine engine;

    public ShardingMapperFactoryBean() {
    }

    /**
     * 构造函数，指定映射器接口类。
     *
     * @param mapperInterface 映射器接口类。
     */
    public ShardingMapperFactoryBean(Class<T> mapperInterface) {
        super(mapperInterface);
    }

    /**
     * 获取映射器实例。
     * 该方法重写了父类方法，用于创建带有分库分表逻辑的映射器代理。
     *
     * @return 映射器代理实例。
     * @throws Exception 如果创建映射器代理出现异常。
     */
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

            // 选择库、选择表, 执行sql前, 通过sharding代理进行处理
            Object[] params = getParams(boundSql, args);
            ShardingResult result = engine.sharding(boundSql.getSql(), params);
            // 知道库和表后, 动态切换库的数据源
            ShardingContext.set(result);

            return method.invoke(proxy, args);
        });

    }

    /**
     * 获取参数数组。
     * 如果参数是复杂对象，该方法会将其属性值提取出来作为SQL的参数。
     * 这样做是为了支持根据对象的属性进行分库分表。
     *
     * @param boundSql SQL的绑定对象，包含SQL信息和参数信息。
     * @param args 方法的参数数组。
     * @return 处理后的参数数组。
     * @throws IllegalArgumentException 如果找不到字段对应的反射方法。
     */
    @SneakyThrows
    private static Object[] getParams(BoundSql boundSql, Object[] args) {
        Object[] params = args;
        // 如果不是基础类或包装类
        if (args.length == 1 && !ClassUtils.isPrimitiveOrWrapper(args[0].getClass())) {
            Object arg = args[0];

            // 从sql里拿到cols, 先获取param名字
            List<String> cols = boundSql.getParameterMappings()
                    .stream().map(ParameterMapping::getProperty).toList();

            Object[] values = new Object[cols.size()];
            for (int i = 0; i < cols.size(); i++) {
                // 通过param从对象反射拿到具体的参数值, 最终转成一个数组
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
