CREATE table user (
    id int(11) not null primary key auto_increment,
    username varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(100) not null,
    age int(11),
    gender varchar(100)
) engine=InnoDB default charset=utf8;
