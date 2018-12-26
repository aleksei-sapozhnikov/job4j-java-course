-- === CREATE DATABASE ===

CREATE DATABASE food;
\c food;

-- === CREATE TABLES ===

CREATE TABLE types (
  id   INTEGER PRIMARY KEY,
  name VARCHAR(200)
);

CREATE TABLE products (
  id          INTEGER PRIMARY KEY,
  name        VARCHAR(200),
  type_id     INTEGER REFERENCES types (id),
  expire_date TIMESTAMP,
  price       NUMERIC CONSTRAINT positive_price CHECK (price > 0),
  quantity    INTEGER CONSTRAINT positive_quantity CHECK (quantity > 0)
);

-- === FILL VALUES ===

INSERT INTO types (id, name) VALUES
  (1, 'Мясо'),
  (2, 'Молочные изделия'),
  (3, 'Хлебобулочные изделия'),
  (4, 'Сыр');

INSERT INTO products (id, name, type_id, expire_date, price, quantity) VALUES
  (1, 'Сыр Иваново', 4, '2018-10-19 10:11:32', 9.99, 3),
  (2, 'Сыр Хлебопечкино', 4, '2018-06-12 23:15:32', 5.99, 2),
  (3, 'Хлеб черный', 3, '2018-07-12 15:23:00', 1.99, 5),
  (4, 'Молоко замороженное', 2, '2019-01-06 05:13:17', 2.99, 8),
  (5, 'Сливочное мороженое', 2, '2018-08-12 22:14:33', 2.99, 3),
  (6, 'Мясо замороженное говядина', 1, '2018-06-06 23:11:32', 1243.99, 10),
  (7, 'Мясо замороженное свинина', 1, '2018-12-06 11:15:32', 958.12, 53);

-- === QUERIES ===

-- 1. Написать запрос получение всех продуктов с типом "СЫР"
SELECT * FROM products WHERE type_id = (
  SELECT id FROM types WHERE name = 'Сыр'
);

-- 2. Написать запрос получения всех продуктов, у кого в имени есть слово "мороженное"
SELECT * FROM products WHERE
  name SIMILAR TO '%мороженное%';

-- 3. Написать запрос, который выводит все продукты, срок годности которых заканчивается в следующем месяце.
SELECT * FROM products WHERE (
                               EXTRACT(MONTH FROM now()) < 12 -- сейчас не декабрь
                               AND
                               EXTRACT(YEAR FROM expire_date) = EXTRACT(YEAR FROM now())
                               AND
                               EXTRACT(MONTH FROM expire_date) = EXTRACT(MONTH FROM now()) + 1
                             )
                             OR (
                               EXTRACT(MONTH FROM now()) = 12 -- сейчас декабрь
                               AND
                               EXTRACT(YEAR FROM expire_date) = EXTRACT(YEAR FROM now()) + 1
                               AND
                               EXTRACT(MONTH FROM expire_date) = EXTRACT(MONTH FROM now()) - 1
                             );

-- Есть и более, на мой взгляд, простой, логичный и почти подходящий к условию задания вариант:
SELECT * FROM products WHERE (
  expire_date - now() > '0'
  AND
  expire_date - now() < '30 days'
);

-- 4. Написать запрос, который вывод самый дорогой продукт.
SELECT * FROM products WHERE price = (
  SELECT max(price) FROM products
);

-- 5. Написать запрос, который выводит количество всех продуктов определенного типа.
SELECT count(id) AS number_of_that_type FROM (
                                               SELECT id FROM products WHERE type_id = (
                                                 SELECT id FROM types WHERE name = 'Мясо'
                                               )
                                             ) AS of_that_type;

-- 6. Написать запрос получение всех продуктов с типом "СЫР" и "МОЛОКО"
SELECT * FROM products WHERE
  type_id = (SELECT id FROM types WHERE name = 'Сыр')
  OR
  type_id = (SELECT id FROM types WHERE name = 'Молочные изделия');

-- 7. Написать запрос, который выводит тип продуктов, которых осталось меньше 10 штук.
SELECT DISTINCT types.id, types.name FROM products, types WHERE products.quantity < 10
ORDER BY types.id;

-- 8. Вывести все продукты и их тип.
SELECT id, name, (SELECT name FROM types WHERE products.type_id = types.id) FROM products;

-- Сложить все количества (quantity) продуктов этого типа и вывести тех, чего в сумме < 10
--
SELECT type_id, types.name, SUM(quantity) AS sum_quantity FROM products
  INNER JOIN types ON types.id = products.type_id
GROUP BY type_id, types.name
HAVING SUM(quantity) < 10
ORDER BY type_id;

-- Посчитать, сколько "марок" продуктов имеют такой тип и вывести типы, где "марок" < 10.
--
SELECT type_id, types.name AS type_name, COUNT(type_id) AS products_of_that_type FROM products
  INNER JOIN types ON types.id = products.type_id
GROUP BY type_id, types.name
HAVING COUNT(type_id) < 10
ORDER BY type_id;

