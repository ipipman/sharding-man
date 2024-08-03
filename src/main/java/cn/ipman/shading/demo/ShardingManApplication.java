package cn.ipman.shading.demo;

import cn.ipman.shading.config.ShardingAutoConfiguration;
import cn.ipman.shading.mybatis.ShardingMapperFactoryBean;
import cn.ipman.shading.demo.mapper.UserMapper;
import cn.ipman.shading.demo.model.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;


/**
 * 分片管理应用程序的入口类。
 * 使用Spring Boot自动配置和MyBatis分片插件。
 */
@SpringBootApplication
@Import(ShardingAutoConfiguration.class)
@MapperScan(value = "cn.ipman.shading.demo.mapper", factoryBean = ShardingMapperFactoryBean.class)
public class ShardingManApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingManApplication.class, args);
    }


    @Autowired
    UserMapper userMapper;

    /**
     * 定义应用启动后执行的逻辑。
     * 使用ApplicationRunner接口，在应用启动后插入、查询、更新和删除用户信息进行测试。
     *
     * @return ApplicationRunner 实例，用于应用启动时执行特定逻辑。
     */
    @Bean
    ApplicationRunner applicationRunner() {
        return x -> {
            for (int i = 1; i <= 1; i++) {
                test(i);
            }
        };
    }


    /**
     * 测试用户数据操作：插入、查询、更新和删除。
     * 该方法模拟了对单个用户进行CRUD操作的流程。
     *
     * @param id 用户ID，用于操作特定用户。
     */
    private void test(int id) {

        System.out.println("=============================");
        System.out.println("=========== ID " + id + " ============");
        System.out.println("=============================");

        // 测试插入用户
        System.out.println(" ====> 1. test insert ...");
        int inserted = userMapper.insert(new User(id, "ipman", 18));
        System.out.println(" ====> 1. test insert = " + inserted);

        // 测试查询用户
        System.out.println(" ====> 2. test find ....");
        User user = userMapper.findById(id);
        System.out.println(" ====> find = " + user);

        // 测试更新用户
        System.out.println(" ====> 3. test update ...");
        user.setName("IP-MAN");
        int updated = userMapper.update(user);
        System.out.println(" ====> updated = " + updated);

        // 再次查询用户，验证更新结果
        System.out.println(" ====> 4. test find ....");
        User user2 = userMapper.findById(id);
        System.out.println(" ====> find = " + user2);

        // 测试删除用户
        System.out.println(" ====> 4. test delete ....");
        int deleted = userMapper.delete(id);
        System.out.println(" ====> deleted = " + deleted);
    }

}
