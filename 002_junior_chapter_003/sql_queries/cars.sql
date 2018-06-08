-- === CREATE DATABASE ===

CREATE DATABASE cars;
\c cars;

-- === CREATE TABLES ===

CREATE TABLE chassis (
  id   INTEGER PRIMARY KEY,
  name VARCHAR(200)
);

CREATE TABLE engine (
  id   INTEGER PRIMARY KEY,
  name VARCHAR(200)
);

CREATE TABLE transmission (
  id   INTEGER PRIMARY KEY,
  name VARCHAR(200)
);

CREATE TABLE car (
  id              INTEGER PRIMARY KEY,
  name            VARCHAR(200)                         NOT NULL,
  chassis_id      INTEGER REFERENCES chassis (id)      NOT NULL,
  engine_id       INTEGER REFERENCES engine (id)       NOT NULL,
  transmission_id INTEGER REFERENCES transmission (id) NOT NULL
);

-- === ADD VALUES ===

INSERT INTO chassis (id, name) VALUES
  (1, 'chassis_very small'),
  (2, 'chassis_small'),
  (3, 'chassis_middle-size'),
  (4, 'chassis_big'),
  (5, 'chassis_huge'),
  (6, 'chassis_gigantic');

INSERT INTO engine (id, name) VALUES
  (1, 'engine_very weak'),
  (2, 'engine_weak'),
  (3, 'engine_not so weak'),
  (4, 'engine_not so powerful'),
  (5, 'engine_powerful'),
  (6, 'engine_very powerful');

INSERT INTO transmission (id, name) VALUES
  (1, 'transmission_very bad'),
  (2, 'transmission_bad'),
  (3, 'transmission_ok'),
  (4, 'transmission_good'),
  (5, 'transmission_very good'),
  (6, 'transmission_excellent');

INSERT INTO car VALUES
  (1, 'car_bad_1', 1, 1, 1),
  (2, 'car_different_1', 2, 6, 3),
  (3, 'car_excellent', 6, 6, 6),
  (4, 'car_bad_2', 1, 1, 1),
  (5, 'car_different_2', 2, 6, 3);

-- === QUERIES ===

-- Вывести все машины.
SELECT car.id            AS car_id, car.name AS car_name, chassis.name AS chassis, engine.name AS engine,
       transmission.name AS transmission FROM car
  INNER JOIN chassis ON car.chassis_id = chassis.id
  INNER JOIN engine ON car.engine_id = engine.id
  INNER JOIN transmission ON car.transmission_id = transmission.id
ORDER BY car_id;

-- Вывести все неиспользуемые детали
SELECT chassis.id AS unused_id, chassis.name AS unused_name FROM chassis
  LEFT OUTER JOIN car ON chassis.id = car.chassis_id
WHERE car.id IS NULL
UNION ALL
SELECT engine.id, engine.name FROM engine
  LEFT OUTER JOIN car ON engine.id = car.engine_id
WHERE car.id IS NULL
UNION ALL
SELECT transmission.id, transmission.name FROM transmission
  LEFT OUTER JOIN car ON transmission.id = car.transmission_id
WHERE car.id IS NULL;