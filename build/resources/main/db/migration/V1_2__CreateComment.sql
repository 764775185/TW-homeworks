create table comments (
   id int(11) not null primary key auto_increment,
   title varchar(255) not null ,
   content varchar(1000),
   timestamp varchar(100),
   users_id int(11),
   posts_id int(11),
   foreign key (users_id) references users(id),
   foreign key (posts_id) references posts(id)
) engine=InnoDB default charset=utf8;