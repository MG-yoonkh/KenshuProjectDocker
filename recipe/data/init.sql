USE Recipe;

SET FOREIGN_KEY_CHECKS = 0;

LOAD DATA LOCAL INFILE '/csv_files/ingredient.csv'
INTO TABLE ingredient
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/csv_files/ingredient_category.csv'
INTO TABLE ingredient_category
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/csv_files/measurement_unit.csv'
INTO TABLE measurement_unit
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/csv_files/site_user.csv'
INTO TABLE site_user
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

SET FOREIGN_KEY_CHECKS = 1;