# 从0到1手写Sharding框架

## 引言

分库分表技术成为了解决这一问题的有效手段之一。本文档将详细介绍一个从0到1手写Sharding框架的过程，包括项目的总体设计思路、核心功能实现细节、高级特性的探索与优化等。


## 总体设计

<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/shading%2F551722752293_.pic.jpg" alt="image-20240324222246295"  />

本项目旨在提供一种灵活、可扩展的分库分表解决方案，支持多种分片策略，并能够无缝集成到现有的基于MyBatis的应用中。总体设计包括以下几个关键点：

- **多数据源管理**：支持配置多个数据源，每个数据源对应不同的物理数据库。
- **分片算法定义**：提供配置化的分片算法，支持哈希分片等常见策略。
- **SQL解析**：利用Druid等工具解析SQL语句，提取出关键字（如INSERT、UPDATE、DELETE、SELECT）及相关的表名、字段等信息。
- **动态数据源切换**：根据解析后的SQL信息，通过分片算法动态选择合适的数据源。
- **MyBatis集成**：通过AOP机制拦截MyBatis的Mapper调用，动态修改SQL语句以适应分片后的数据库结构。

## 核心功能实现

### 多数据源管理

- **配置文件**：在配置文件中定义多个数据源的信息，包括数据库连接字符串、用户名、密码等。
- **数据源池**：使用Druid等成熟的数据库连接池技术管理这些数据源，确保连接的高效复用。

### 分片算法定义

- **哈希分片**：根据某个字段（如用户ID）的哈希值进行分片，确保数据分布均匀。

### SQL解析

- **Druid SQL解析器**：利用Druid提供的SQL解析能力，提取SQL语句的关键信息。
- **SQL类型识别**：根据SQL解析起识别SQL类型,如:INSERT、UPDATE、DELETE、SELECT等，进而决定后续处理逻辑。

### 动态数据源切换

- **分片规则匹配**：根据SQL语句中的表名和字段信息，结合配置的分片规则，计算出对应的分片键。
- **数据源选择**：依据分片键和分片算法，确定具体的数据源。

### MyBatis集成

- **Mapper拦截器**：通过Spring AOP技术，实现对MyBatis Mapper方法的拦截。
- **SQL动态修改**：根据分片信息动态修改SQL语句中的表名等信息，使其符合分片后的数据库结构。


## 总结

本项目通过手写Sharding框架的方式，深入理解了分库分表的核心原理和技术细节。不仅实现了基本的分片功能，还探索了一些高级特性，如事务支持、性能优化等。通过这种方式，不仅提升了个人的技术能力，也为实际项目提供了有力的支持。未来还可以继续探索更多高级特性，如分布式事务、数据迁移工具等，进一步完善整个框架的功能和性能。
