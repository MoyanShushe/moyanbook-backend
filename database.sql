drop database if exists item_sell_db;

create database item_sell_db;

use item_sell_db;

create table address_part2
(
    id             int auto_increment comment 'id'
        primary key,
    name           varchar(127) not null comment '区名',
    parent_address int          null comment '隶属于'
)
    comment '区';

create table address_part1
(
    id             int auto_increment comment '主键'
        primary key,
    name           varchar(127) not null comment '地址名',
    parent_address int          not null comment '隶属于的地址',
    constraint address_part1_address_part2_id_fk
        foreign key (parent_address) references address_part2 (id)
)
    comment '园栋';

create table address
(
    id               int auto_increment
        primary key,
    address          varchar(255) default 'cqu' not null comment '详细地址',
    address_part1    int          default 0     not null comment '地址段1',
    address_part2    int          default 0     not null comment '地址段2',
    is_deleted       int          default 0     not null comment '逻辑删除',
    create_person_id int                        not null comment '创建人id',
    update_person_id int                        not null comment '更新人id',
    constraint ADDRESS_pk
        unique (address),
    constraint address_address_part1_id_fk
        foreign key (address_part1) references address_part1 (id),
    constraint address_address_part2_id_fk
        foreign key (address_part2) references address_part2 (id)
)
    comment '地址表';

create index ADDRESS_id_index
    on address (id);

create table admin
(
    id                  int auto_increment comment '管理员id'
        primary key,
    name                varchar(63)                          not null comment '管理员名称',
    gender              tinyint    default 0                 not null comment '性别',
    status              tinyint    default 1                 not null comment '状态',
    profile_url         varchar(255)                         not null comment '头像网址',
    address_id          int                                  null comment '地址信息',
    is_deleted          tinyint(1) default 0                 not null comment '逻辑删除字段',
    area_responsibility int        default 1                 null comment '负责区域',
    phone               char(11)                             not null comment '手机号',
    email               varchar(63)                          not null comment '邮箱',
    password            char(64)                             not null comment '密码',
    create_person_id    int        default 0                 not null comment '创建人',
    update_person_id    int                                  not null comment '更新人',
    create_time         timestamp  default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time         timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint admin_address_id_fk
        foreign key (address_id) references address (id)
)
    comment '管理员表';

create index admin_name_index
    on admin (name);

create table admin_address_responsibility_mapping
(
    id               int auto_increment comment '主键'
        primary key,
    admin_id         int           not null comment '管理员id',
    address_id       int           not null comment '管理地区id',
    is_deleted       int default 1 not null comment '逻辑删除',
    create_person_id int default 1 not null comment '创建人员id',
    update_person_id int default 1 not null comment '更新人id',
    constraint admin_responsibility_address_mapping_address_id_fk
        foreign key (address_id) references address (id),
    constraint admin_responsibility_address_mapping_admin_id_fk
        foreign key (admin_id) references admin (id)
)
    comment '管理员管理地区映射表';

create table admin_confirm
(
    id           int auto_increment comment '主键',
    confirm_code char(32)      not null comment '确定码',
    status       int           not null comment '状态',
    operation    int           not null comment '操作，接收或者送出',
    admin_id     int           not null comment '对应管理员id',
    is_deleted   int default 1 not null comment '逻辑删除',
    update_time  timestamp     not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint admin_confirm_confirm_code_uindex
        unique (confirm_code),
    constraint admin_item_confirm_pk
        unique (id),
    constraint admin_confirm_admin_id_fk
        foreign key (admin_id) references admin (id)
);

create table item_code
(
    id         int auto_increment
        primary key,
    code_part1 int not null comment '地址',
    code_part2 int not null comment '编码',
    item_id    int null comment '对应的物品id'
)
    comment '物件码';

create table label
(
    id   int auto_increment comment '标签id，主键'
        primary key,
    name varchar(60) default '' not null comment '标签名'
);

create table user
(
    id               int auto_increment comment '用户id，主键'
        primary key,
    name             varchar(20) default ''          not null comment '用户名',
    age              int         default 18          null comment '用户年龄',
    gender           tinyint                         null comment '用户性别',
    email            varchar(30) default 'null'      null comment '用户邮箱',
    phone            varchar(20)                     null comment '用户手机号',
    password         varchar(64)                     not null comment '用户密码',
    status           tinyint                         null comment '用户状态  0: 正常，1：冻结，2：过期',
    create_time      date        default (curdate()) not null comment '用户创建时间',
    update_time      date        default (curdate()) not null comment '用户更新时间',
    profile_url      varchar(128)                    null comment '头像网址',
    last_login_time  date        default (curdate()) not null comment '上一次登录时间',
    address_id       int         default 0           null comment '用户地址对应的id',
    is_deleted       tinyint(1)  default 0           not null comment '账户是否已经删除',
    update_person_id int                             not null comment '更新人id',
    constraint user_address_id_fk
        foreign key (address_id) references address (id)
);

create table coupon
(
    id              int auto_increment comment '优惠券id，主键'
        primary key,
    name            varchar(20)  default ''                                 not null comment '优惠券名',
    price           decimal(10, 2)                                          not null comment '优惠券价格',
    description     varchar(128) default '这是一个优惠券'                   not null comment '优惠券描述',
    status          tinyint      default 1                                  not null comment '优惠券状态  0: 正常，1：下架',
    create_time     datetime     default (curtime())                        not null comment '优惠券创建时间',
    expiration_time datetime     default ((`create_time` + interval 7 day)) not null comment '优惠券过期时间',
    user_id         int                                                     not null comment '优惠券所属用户id',
    constraint coupon_ibfk_1
        foreign key (user_id) references user (id)
);

create table item
(
    id               int auto_increment comment '书籍id，主键'
        primary key,
    name             varchar(20) default ''          not null comment '书籍名',
    price            decimal(10, 2)                  not null comment '书籍价格',
    description      varchar(128)                    null comment '书籍描述',
    status           tinyint     default 1           not null comment '书籍状态  0: 正常，1：下架',
    create_time      date        default (curdate()) not null comment '书籍创建时间',
    update_time      timestamp   default (curdate()) not null on update CURRENT_TIMESTAMP comment '书籍更新时间',
    profile_url      varchar(128)                    null comment '书籍图片网址',
    user_id          int                             not null comment '书籍所属用户id',
    update_person_id int         default (`user_id`) not null comment '更新人',
    constraint item_ibfk_1
        foreign key (user_id) references user (id)
);

create table admin_confirm_item_mapping
(
    id           int auto_increment comment 'id'
        primary key,
    confirm_code char(32) not null comment '确认码',
    item_id      int      not null comment '对应的物件id',
    constraint admin_confirm_item_mapping_admin_confirm_confirm_code_fk
        foreign key (confirm_code) references admin_confirm (confirm_code),
    constraint admin_confirm_item_mapping_item_id_fk
        foreign key (item_id) references item (id)
);

create index user_id
    on item (user_id);

create table item_image
(
    id        int auto_increment comment '书籍图片id，主键'
        primary key,
    item_id   int          not null comment '书籍id',
    image_url varchar(255) not null comment '图片网址',
    constraint item_image_item_id_fk
        foreign key (item_id) references item (id)
)
    comment '书籍图片';

create table item_label_mapping
(
    id       int auto_increment comment '书籍标签映射id，主键'
        primary key,
    item_id  int not null comment '书籍id',
    label_id int not null comment '标签id',
    constraint item_label_mapping_ibfk_1
        foreign key (item_id) references item (id),
    constraint item_label_mapping_ibfk_2
        foreign key (label_id) references label (id)
);

create index item_id
    on item_label_mapping (item_id);

create index label_id
    on item_label_mapping (label_id);

create table order_detail
(
    order_id         char(36)                            not null comment 'id',
    status           tinyint   default 0                 not null comment '订单状态',
    create_time      datetime  default CURRENT_TIMESTAMP not null comment '创建时间',
    user_id          int                                 not null comment '订单对应用户id',
    update_time      timestamp default (now())           not null on update CURRENT_TIMESTAMP,
    update_person_id int                                 not null,
    is_deleted       tinyint   default 0                 not null comment '逻辑删除',
    id               int auto_increment comment '主键'
        primary key,
    constraint order_detail_order_id_uindex
        unique (order_id),
    constraint shopping_cart_user_id_fk
        foreign key (user_id) references user (id)
)
    comment '订单';

create table order_item_mapping
(
    id          int auto_increment comment '订单映射id，主键'
        primary key,
    order_id    char(36)      not null comment '订单id',
    item_id     int           not null comment '物品id',
    item_number int default 1 not null comment '物品数量',
    constraint order_item_mapping_ibfk_2
        foreign key (item_id) references item (id),
    constraint order_item_mapping_order_detail_order_id_fk
        foreign key (order_id) references order_detail (order_id)
);

create index id_index
    on user (id);



INSERT INTO address_part2 (name)
VALUES ('渝北区'),
       ('南岸区'),
       ('沙坪坝区'),
       ('九龙坡区'),
       ('江北区');

INSERT INTO address_part1 (name, parent_address)
VALUES ('大学城', 1),
       ('解放碑', 2),
       ('磁器口', 3),
       ('石桥铺', 4),
       ('观音桥', 5);

INSERT INTO address (address, address_part1, address_part2, create_person_id, update_person_id)
VALUES ('沙坪坝大学城南路1号', 1, 1, 1, 1),
       ('南岸区南滨路1号', 2, 2, 1, 1),
       ('渝北区新牌坊转盘', 3, 1, 1, 1),
       ('九龙坡区杨家坪步行街', 4, 4, 1, 1),
       ('江北区观音桥步行街', 5, 5, 1, 1);

INSERT INTO admin (name, gender, status, profile_url, phone, email, password, create_person_id, update_person_id)
VALUES ('Napbad', 1, 1, 'http://adminA.com/avatar.jpg', '13800000001', 'adminA@example.com', 'hashed_pwd_A', 1, 1),
       ('管理员B', 0, 1, 'http://adminB.com/avatar.jpg', '13800000002', 'adminB@example.com', 'hashed_pwd_B', 1, 2),
       ('管理员C', 1, 1, 'http://adminC.com/avatar.jpg', '13800000003', 'adminC@example.com', 'hashed_pwd_C', 1, 3),
       ('管理员D', 0, 1, 'http://adminD.com/avatar.jpg', '13800000004', 'adminD@example.com', 'hashed_pwd_D', 1, 4),
       ('管理员E', 1, 1, 'http://adminE.com/avatar.jpg', '13800000005', 'adminE@example.com', 'hashed_pwd_E', 1, 5);


INSERT INTO user (name, age, gender, email, password, update_person_id, address_id)
VALUES ('Napbad', 18, 1, 'userA@example.com', 'hashed_pwd_userA', 1, 1),
       ('用户乙', 28, 0, 'userB@example.com', 'hashed_pwd_userB', 2, 3),
       ('用户丙', 25, 1, 'userC@example.com', 'hashed_pwd_userC', 3, 2),
       ('用户丁', 30, 0, 'userD@example.com', 'hashed_pwd_userD', 4, 5),
       ('用户戊', 26, 1, 'userE@example.com', 'hashed_pwd_userE', 5, 4);

INSERT INTO coupon (name, price, description, status, user_id)
VALUES ('迎新优惠券', 10.00, '欢迎新用户，满100元可用', 0, 1),
       ('会员专享', 5.00, '会员特惠，全场通用', 0, 2),
       ('周末特卖', 20.00, '本周末购物满200元减20元', 0, 3),
       ('节日红包', 3.50, '节日限定，无门槛使用', 0, 4),
       ('生日礼券', 15.00, '生日当月专享，全场八折', 0, 5);
-- 第一条记录
INSERT INTO label (name)
VALUES ('科幻'),
       ('历史'),
       ('编程'),
       ('心理学'),
       ('文学');

INSERT INTO item (name, price, description, status, create_time, update_time, profile_url, user_id, update_person_id)
VALUES ('编程珠玑', 88.50, '探索编程之美与高效算法实践。', 0, CURDATE(), NOW(),
        'http://example.com/programming-pearls.jpg', 1, 1)
     , ('深入浅出MySQL', 55.00, '全面理解MySQL核心原理与优化技巧。', 0, CURDATE(), NOW(),
        'http://example.com/mysql-inside-out.jpg', 1, 1)
     , ('算法导论', 120.00, '算法领域的权威著作，涵盖基础到高级算法。', 0, CURDATE(), NOW(),
        'http://example.com/introduction-to-algorithms.jpg', 1, 1)
     , ('设计模式：可复用面向对象软件的基础', 79.99, '软件工程中经典的设计模式介绍。', 0, CURDATE(), NOW(),
        'http://example.com/design-patterns.jpg', 1, 1)
     , ('计算机程序的构造和解释', 99.00, '深入讲解编程语言原理的经典教材。', 0, CURDATE(), NOW(),
        'http://example.com/sicp.jpg', 1, 1);

-- 示例订单数据插入
INSERT INTO order_detail(order_id, status, create_time, user_id, update_time, update_person_id, is_deleted)
VALUES
    ('e5b9fc6b-d2f2-4a88-8a04-203efcd4d081', 00, NOW(), 1, NOW(), 1, 0),
    ('f762aa11-2d1f-4c42-a4bb-056dbae1c37b', 10, NOW() - INTERVAL 1 DAY, 3, NOW(), 1, 0),
    ('ca3f4f7c-93d6-4c5d-b47e-cfa2ee74df7a', 20, NOW() - INTERVAL 2 DAY, 2, NOW(), 1, 0),
    ('d4d6b23d-e9b6-41ad-85cb-746a30f40a2d', 10, NOW(), 1, NOW(), 3, 0),
    ('ab9c1def-77c8-4acb-a22c-f10bddd0f743', 10, NOW() - INTERVAL 3 HOUR, 4, NOW(), 2, 0),
    ('9e7b8a3c-2b9d-4d42-a551-7bddd766943b', 20, NOW() - INTERVAL 1 WEEK, 5, NOW(), 1, 0),
    ('cfebe1b3-7f57-4876-a7e5-2c1f4c1d294d', 10, NOW(), 1, NOW(), 3, 0),
    ('a8c5ebf6-c7a5-4da1-9436-40e2e9f4c389', 10, NOW() - INTERVAL 6 HOUR, 2, NOW(), 4, 0),
    ('b45d6a9d-955f-4264-a766-450e93e5d402', 20, NOW() - INTERVAL 3 DAY, 3, NOW(), 5, 0),
    ('e9f758e8-cc66-43ce-8d26-31f3e2730f70', 10, NOW(), 2, NOW(), 3, 0);

INSERT INTO order_item_mapping(order_id, item_id, item_number)
VALUES
    ('e5b9fc6b-d2f2-4a88-8a04-203efcd4d081', 1, 1), -- 订单1关联商品1
    ('e5b9fc6b-d2f2-4a88-8a04-203efcd4d081', 2, 1), -- 订单1关联商品2
    ('f762aa11-2d1f-4c42-a4bb-056dbae1c37b', 3, 1), -- 订单2关联商品3
    ('ca3f4f7c-93d6-4c5d-b47e-cfa2ee74df7a', 4, 1), -- 订单3关联商品4
    ('d4d6b23d-e9b6-41ad-85cb-746a30f40a2d', 5, 1), -- 订单4关联商品5
    ('ab9c1def-77c8-4acb-a22c-f10bddd0f743', 1, 1); -- 订单5关联商品1

-- 插入第一条记录
INSERT INTO item_code (code_part1, code_part2, item_id)
VALUES (1, 001, 1);

-- 插入第二条记录
INSERT INTO item_code (code_part1, code_part2, item_id)
VALUES (2, 002, 2);

