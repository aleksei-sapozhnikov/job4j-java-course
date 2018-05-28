CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  login VARCHAR(200) UNIQUE NOT NULL
);

INSERT INTO users (login) VALUES
  ('Vincent'),
  ('Peter'),
  ('James'),
  ('Arthur')
ON CONFLICT (login) DO NOTHING;