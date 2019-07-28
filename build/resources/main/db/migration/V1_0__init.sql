CREATE table users (
    id int(11) not null primary key auto_increment,
    username varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(100) not null,
    age int(11),
    gender varchar(100)
) engine=InnoDB default charset=utf8;

create table posts (
    id int(11) not null primary key auto_increment,
    title varchar(255) not null ,
    content varchar(1000),
    timestamp varchar(100),
    users_id int(11),
    foreign key (users_id) references users(id)
) engine=InnoDB default charset=utf8;

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