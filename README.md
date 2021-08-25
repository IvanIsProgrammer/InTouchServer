# InTouchServer
InTouch Server - REST API Server, исполняющий роль чата.
Сервер расположен по адресу: http://195.58.37.50:8500
Документация по API: http://195.58.37.50:8500/api/info

#How to start
1) настройки сервера и БД находятся в properties.configuration
2) создать БД MySQL с названием "intouch"
3) создать таблицу Accounts:
  CREATE TABLE `intouch`.`Accounts` (
  `A_ID` INT(11) NOT NULL DEFAULT '0',
  `Login` VARCHAR(20) NOT NULL,
  `Password` VARCHAR(20) NOT NULL,
  `Token` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`A_ID`),
  UNIQUE INDEX `idnew_table_UNIQUE` (`A_ID` ASC),
  UNIQUE INDEX `new_tablecol_UNIQUE` (`Login` ASC),
  UNIQUE INDEX `new_tablecol2_UNIQUE` (`Token` ASC));
4) создать таблицу Messages:
  CREATE TABLE `test`.`Messages` (
  `M_ID` INT(11) NOT NULL DEFAULT '0',
  `Sender` VARCHAR(20) NOT NULL,
  `Content` TEXT NOT NULL,
  PRIMARY KEY (`M_ID`),
  UNIQUE INDEX `idnew_table_UNIQUE` (`M_ID` ASC));
5)Запустить сервер.
