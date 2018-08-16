DO $$ DECLARE brow RECORD;
BEGIN FOR brow IN (SELECT format('DROP TABLE %I CASCADE', tablename) AS table_name
                   FROM pg_tables
                   WHERE schemaname = 'public') LOOP EXECUTE brow.table_name;
  END LOOP;
END; $$;

CREATE TABLE IF NOT EXISTS roles (
  id   SERIAL PRIMARY KEY,
  name TEXT UNIQUE
);

CREATE TABLE IF NOT EXISTS addresses (
  id   SERIAL PRIMARY KEY,
  name TEXT
);

CREATE TABLE IF NOT EXISTS music (
  id    SERIAL PRIMARY KEY,
  genre TEXT UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
  id         SERIAL PRIMARY KEY,
  login      TEXT UNIQUE,
  password   TEXT,
  role_id    INTEGER REFERENCES roles (id),
  address_id INTEGER UNIQUE REFERENCES addresses (id)
);

CREATE TABLE IF NOT EXISTS users_music (
  id       SERIAL PRIMARY KEY,
  user_id  INTEGER REFERENCES users (id),
  music_id INTEGER REFERENCES music (id)
);

WITH set_login AS (SELECT ?),
     set_password AS (SELECT ?),
     set_role AS (SELECT ?),
     set_address AS (SELECT ?),
     role_got_id AS (INSERT INTO roles (name) VALUES ((SELECT * FROM set_role))
ON CONFLICT (name)
  DO UPDATE SET name = roles.name
RETURNING id),
     address_got_id AS (INSERT INTO addresses (name) VALUES ((SELECT * FROM set_address))
RETURNING id)
INSERT INTO users(login, password, role_id, address_id)
VALUES ((SELECT * FROM set_login),
        (SELECT * FROM set_password),
        (SELECT id FROM role_got_id),
        (SELECT id FROM address_got_id));


WITH set_user AS (SELECT ?),
     set_genres AS (SELECT ARRAY [1,2,3])
INSERT INTO users_music(user_id, music_id)
VALUES ((SELECT * FROM set_user), (SELECT unnest(set_genres)))

DELETE
FROM users_music
WHERE user_id = ?;

SELECT music_id
FROM users_music
WHERE user_id = ?;


SELECT *
FROM users
       JOIN roles ON users.role_id = roles.id
       JOIN addresses ON users.address_id = addresses.id;