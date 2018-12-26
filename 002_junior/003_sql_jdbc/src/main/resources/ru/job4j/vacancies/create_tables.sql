CREATE TABLE IF NOT EXISTS vacancy (
  id      SERIAL PRIMARY KEY,
  title   TEXT,
  url     TEXT,
  updated TIMESTAMP,
  UNIQUE (title, url)
);


