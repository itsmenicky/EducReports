CREATE TABLE `user` (`id_user` int not null PRIMARY KEY auto_increment, 
`username` varchar(30) not null, 
`email` varchar(50) default null, 
`login` varchar(15) not null UNIQUE KEY, 
`password` varchar(15) not null, 
`hierarchy` varchar(20) not null);

CREATE TABLE `tb_child` (`RA` int not null PRIMARY KEY, 
`child_name` varchar(50) not null, 
`birth` date not null, 
`class` varchar(20) not null, 
`child_photo` longblob not null, 
`child_phone` varchar(10) not null, 
`responsible` varchar(50) not null, 
`address` varchar(100) not null, 
`teacher_name` varchar(50) not null, 
`teacher_id` int not null, 
foreign key(teacher_id) references tb_teacher(id_teacher));

CREATE TABLE `tb_reports` (`ID Rel` int not null auto_increment PRIMARY KEY, 
`child_RA` int not null, 
`birth` date not null, 
`class` varchar(20) not null, 
`report` varchar(5000) not null,
`emission_date` timestamp not null default current_timestamp,
`child_name` varchar(50) not null,
`teacher_name` varchar(50) not null,
foreign key(child_RA) references tb_child(RA));