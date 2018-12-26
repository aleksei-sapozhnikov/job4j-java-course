-- 					-- С О З Д А Н И Е   Т А Б Л И Ц
-- 
-- CREATE TABLE company
-- (
-- id integer NOT NULL,
-- name character varying,
-- CONSTRAINT company_pkey PRIMARY KEY (id)
-- );
-- 
-- CREATE TABLE person
-- (
-- id integer NOT NULL,
-- name character varying,
-- company_id integer,
-- CONSTRAINT person_pkey PRIMARY KEY (id)
-- );
-- 
-- 
-- 					-- З А П О Л Н Е Н И Е
-- 
-- INSERT INTO company VALUES
-- (1, 'COMPANY_1'),
-- (2, 'COMPANY_2'),
-- (3, 'COMPANY_3'),
-- (4, 'COMPANY_4'),
-- (5, 'COMPANY_5'),
-- (6, 'COMPANY_6'),
-- (7, 'COMPANY_7');
-- 
-- INSERT INTO person VALUES
-- (1, 'PERSON_1', 1),
-- (2, 'PERSON_2', 1),
-- (3, 'PERSON_3', 1),
-- (4, 'PERSON_4', 1),
-- (5, 'PERSON_5', 1),
-- (6, 'PERSON_6', 1),
-- (7, 'PERSON_7', 2),
-- (8, 'PERSON_8', 2),
-- (9, 'PERSON_9', 3),
-- (10, 'PERSON_10', 3),
-- (11, 'PERSON_11', 3),
-- (12, 'PERSON_12', 4),
-- (13, 'PERSON_13', 4),
-- (14, 'PERSON_14', 4),
-- (15, 'PERSON_15', 4),
-- (16, 'PERSON_16', 5),
-- (17, 'PERSON_17', 5),
-- (18, 'PERSON_18', 5),
-- (19, 'PERSON_19', 6),
-- (20, 'PERSON_20', 7),
-- (21, 'PERSON_21', 7),
-- (22, 'PERSON_22', 7);

-- З А П Р О С Ы

-- // 1) Retrieve in a single query:
-- // - names of all persons that are NOT in the company with id = 5
-- // - company name for each person
--
SELECT person.id, person.name, company.name
FROM person
  INNER JOIN company ON person.company_id = company.id
WHERE person.company_id != 5;

-- // 2) Select the name of the company with the maximum number of persons + number of persons in this company
--
SELECT company.id, company.name, COUNT(company.id) AS workers
FROM company
  INNER JOIN person ON person.company_id = company.id
GROUP BY company.id
HAVING COUNT(company.id) =
       (
         SELECT DISTINCT MAX(COUNT(company_id))
         OVER ()
         FROM person
         GROUP BY company_id
       );