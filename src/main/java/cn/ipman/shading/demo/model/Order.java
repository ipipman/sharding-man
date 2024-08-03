package cn.ipman.shading.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order Entity
 *
 * @Author IpMan
 * @Date 2024/8/3 16:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private int id;
    private int uid;
    private double price;

}
