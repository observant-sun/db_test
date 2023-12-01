DROP TABLE IF EXISTS TABLE_LIST;
CREATE TABLE TABLE_LIST
(
    TABLE_NAME VARCHAR(32) COMMENT 'имя таблицы',
    PK         VARCHAR(256) COMMENT 'поля первичного ключа, разделитель - запятая'
);
DROP TABLE IF EXISTS TABLE_COLS;
CREATE TABLE TABLE_COLS
(
    TABLE_NAME  VARCHAR(32) COMMENT 'имя таблицы',
    COLUMN_NAME VARCHAR(32) COMMENT 'имя поля',
    COLUMN_TYPE VARCHAR(32) COMMENT 'тип данных поля - INT или VARCHAR'
);
INSERT INTO TABLE_LIST (TABLE_NAME, PK)
VALUES ('users', 'ID'),
       ('accounts', 'account, account_id');
INSERT INTO TABLE_COLS (TABLE_NAME, COLUMN_NAME, COLUMN_TYPE)
VALUES ('users', 'first_name', 'VARCHAR(32)'),
       ('users', 'second_name', 'VARCHAR(32)'),
       ('users', 'id', 'INT'),
       ('accounts', 'register_date', 'TIMESTAMP'),
       ('accounts', 'ACCOUNT', 'VARCHAR(32)'),
       ('accounts', 'ACCOUNT_ID', 'INT'),
       ('accounts', 'CARD_NUMBER', 'INT');
