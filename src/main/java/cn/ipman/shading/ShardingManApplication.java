package cn.ipman.shading;

import cn.ipman.shading.demo.User;
import cn.ipman.shading.demo.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ShardingAutoConfiguration.class)
@MapperScan(value = "cn.ipman.shading.demo", factoryBean = ShardingMapperFactoryBean.class)
public class ShardingManApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingManApplication.class, args);
    }

    @Autowired
    UserMapper userMapper;

    @Bean
    ApplicationRunner applicationRunner() {
        return x -> {

            int id = 2;

            System.out.println(" ====> 1. test insert ...");
            int inserted = userMapper.insert(new User(id, "ipman", 18));
            System.out.println(" ====> 1. test insert = " + inserted);

            System.out.println(" ====> 2. test find ....");
            User user = userMapper.findById(id);
            System.out.println(" ====> find = " + user);

            System.out.println(" ====> 3. test update ...");
            user.setName("IP-MAN");
            int updated = userMapper.update(user);
            System.out.println(" ====> updated = " + updated);

            System.out.println(" ====> 4. test find ....");
            User user2 = userMapper.findById(id);
            System.out.println(" ====> find = " + user2);

            System.out.println(" ====> 4. test delete ....");
            int deleted = userMapper.delete(id);
            System.out.println(" ====> deleted = " + deleted);

        };
    }

}
