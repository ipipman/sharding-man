
#### 初始化SQL

```sql
CREATE DATABASE db0;

CREATE TABLE `user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL,
    `age` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    constraint user_name_uindex
    unique (name)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
```