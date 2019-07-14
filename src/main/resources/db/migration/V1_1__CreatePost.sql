create table post (
    id int(11) not null primary key auto_increment,
    title varchar(255) not null ,
    content varchar(1000),
    user_id int(11),
    foreign key (user_id) references user(id)
) engine=InnoDB default charset=utf8;