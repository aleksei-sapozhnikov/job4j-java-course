CREATE TABLE IF NOT EXISTS items (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(200) NOT NULL,
  description TEXT,
  create_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS comments (
  id      SERIAL PRIMARY KEY,
  item_id INTEGER REFERENCES items (id),
  text    TEXT
);