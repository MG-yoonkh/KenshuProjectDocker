CREATE DATABASE Recipe;

create user 'root'@'%' identified by '1111';

grant all privileges on Recipe.* to root@'%' with grant option;

USE Recipe;

SET FOREIGN_KEY_CHECKS = 0;

LOAD DATA INFILE '/csv_files/ingredient.csv'
INTO TABLE ingredient
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA INFILE '/csv_files/ingredient_category.csv'
INTO TABLE ingredient_category
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA INFILE '/csv_files/measurement_unit.csv'
INTO TABLE measurement_unit
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA INFILE '/csv_files/site_user.csv'
INTO TABLE site_user
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

SET FOREIGN_KEY_CHECKS = 1;

FLUSH PRIVILEGES;