
#### 初始化SQL

```sql
CREATE DATABASE ds0;
       
CREATE DATABASE ds1;

-- 分别创建如下:       
       
CREATE TABLE `user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL,
    `age` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    constraint user_name_uindex
    unique (name)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `t_user_0` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL,
    `age` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    constraint user_name_uindex
    unique (name)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `t_user_1` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL,
    `age` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    constraint user_name_uindex
    unique (name)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

```