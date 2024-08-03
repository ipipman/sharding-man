
#### 初始化SQL

```sql
-- ####
-- 创建库1如下:
CREATE DATABASE ds0;

-- 创建user表如下: 
CREATE TABLE `user0` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL,
    `age` int(11) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `user1` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL,
     `age` int(11) NOT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `user2` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL,
     `age` int(11) NOT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


-- 创建order表如下: 
CREATE TABLE `t_order0` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `uid` int(11) NOT NULL,
    `price` double(16,2) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `t_order1` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `uid` int(11) NOT NULL,
    `price` double(16,2) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


-- ####
-- 创建库2如下:

CREATE DATABASE ds1;

-- 创建user表如下: 
CREATE TABLE `user0` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL,
     `age` int(11) NOT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `user1` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL,
     `age` int(11) NOT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `user2` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL,
     `age` int(11) NOT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;



-- 创建order表如下: 
CREATE TABLE `t_order0` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `uid` int(11) NOT NULL,
    `price` double(16,2) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `t_order1` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `uid` int(11) NOT NULL,
    `price` double(16,2) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 查看所有user表数
select * from (
  select * from ds0.user0 union select * from ds0.user1 union select * from ds0.user2
  union
  select * from ds1.user0 union select * from ds1.user1 union select * from ds1.user2
) as user_all
order by  user_all.id asc;


-- 查看所有order表数
select * from (
  select * from ds0.t_order0 union select * from ds0.t_order1 
  union
  select * from ds1.t_order0 union select * from ds1.t_order1
) as order_all
order by  order_all.id asc;

```