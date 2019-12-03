# login-sefaz  
Desafio Sefaz para desenvolvedores  
  
# Versão das Libs utilizadas:  
JSP-API - 2.2  
IDE - Eclipse oxygen.3.7  
JAVA - 1.8  
Apache Tomcat - 8.5  
JSTL - 1.2  
Javax Servlet - 3.0  
mysql-connector-java - 8.0.13  
  
# Funcionalidades:  
Login;  
Listagem de usuários;  
Cadastro de usuários;  
  
# Configuração do banco de dados;  
create DATABASE mysql_database;  
use mysql_database;  
CREATE TABLE `user` (  
  `email` varchar(45) NOT NULL,  
  `password` varchar(45) NOT NULL,  
  `user_name` varchar(45) NOT NULL,  
  PRIMARY KEY (`email`)  
);  
  
CREATE TABLE `user_phone` (  
	`user_email` varchar(45) NOT NULL,  
	`phone_number` varchar(45) NOT NULL,  
  	`ddd` INT NULL,  
  	`tp_phone` varchar(45) DEFAULT NULL,  
  	PRIMARY KEY (`phone_number`)  
);  
