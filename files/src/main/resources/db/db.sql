CREATE TABLE t_user(
	id INT(8) PRIMARY KEY,
	username VARCHAR(60),
	PASSWORD VARCHAR(60)
);

CREATE TABLE t_files(
	id INT(8) PRIMARY KEY,
	oldFileName VARCHAR(200),
	newFileName VARCHAR(300),
	ext VARCHAR(20) ,
	path VARCHAR(300),
	size VARCHAR(200),
	TYPE VARCHAR(120),
	isimg VARCHAR(8),
	downcounts INT(6),
	uploadTime DATE,
	userId int(8)

);