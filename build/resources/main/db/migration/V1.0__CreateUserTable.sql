CREATE TABLE user (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL,
  age INT NOT NULL,
  gender VARCHAR(20) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT pk_username UNIQUE (username)
);

