sky-take-out 是一个父项目，统一管理依赖版本，聚合其他子项目

sky-common 子模块 存放公共类 例如工具类 常量类 异常类  

sky-pojo 子模块 存放实体类 vo dto 等

sky-server 子模块 后端服务 存放配置文件 controller service mapper等等


entity 实体，通常和数据库中的表对应 

dto 数据传输对象 通常用于程序中各层之间传递数据

vo 视图对象 为前端展示数据的对象

pojo 普通的java对象 只有属性和对应的getter 和setter 