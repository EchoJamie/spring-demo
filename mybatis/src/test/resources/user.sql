create table if not exists user
(
    id   varchar(100) not null comment '主键ID',
    name varchar(100) null comment '用户名',
    age  int          null comment '年龄',
    primary key (id)
);
insert into user (id, name, age)
values  ('100001', 'Jamie', 18),
        ('100002', '李四', 23),
        ('100003', '张三', 16);
