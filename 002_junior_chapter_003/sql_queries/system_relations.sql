-- ======================= CREATE DATABASE =======================

CREATE DATABASE users_items;
\c users_items postgres

-- ======================= CREATE TABLES =======================

CREATE TABLE roles (-- possible roles of users
  role_id   INTEGER PRIMARY KEY,
  role_name VARCHAR(10) UNIQUE NOT NULL
);
CREATE TABLE users (-- users of the system
  user_id    INTEGER PRIMARY KEY,
  user_login VARCHAR(10) UNIQUE NOT NULL,
  role_id    INTEGER REFERENCES roles (role_id)    -- many users - one role
);
CREATE TABLE rules (-- possible rules applicated to users
  rule_id   INTEGER PRIMARY KEY,
  rule_name VARCHAR(10) UNIQUE NOT NULL
);
CREATE TABLE roles_rules (-- links rules and roles		-- many roles - many rules
  PRIMARY KEY (role_id, rule_id),
  role_id INTEGER REFERENCES roles (role_id),
  rule_id INTEGER REFERENCES rules (rule_id)
);
CREATE TABLE categories (-- categories of items
  categ_id   INTEGER PRIMARY KEY,
  categ_name VARCHAR(10) UNIQUE NOT NULL
);
CREATE TABLE states (-- states of items
  state_id   INTEGER PRIMARY KEY,
  state_name VARCHAR(10) UNIQUE NOT NULL
);
CREATE TABLE items (-- items created by users
  user_id       INTEGER PRIMARY KEY REFERENCES users (user_id), -- one item - one user
  item_category INTEGER REFERENCES categories (categ_id), -- many items - one category
  item_state    INTEGER REFERENCES states (state_id)    -- many items - one state
);
CREATE TABLE comments (-- comments on items
  comment_id   INTEGER PRIMARY KEY,
  item_id      INTEGER REFERENCES items (user_id), -- many comments - one item
  comment_text VARCHAR(200)
);
CREATE TABLE attachments (
  attach_id   INTEGER PRIMARY KEY,
  item_id     INTEGER REFERENCES items (user_id), -- many attachmets - one item
  attach_path VARCHAR(200)
);

-- ======================= FILL INITIAL VALUES =======================

INSERT INTO rules (rule_id, rule_name) VALUES (1, 'rule_1');
INSERT INTO rules (rule_id, rule_name) VALUES (2, 'rule_2');
INSERT INTO rules (rule_id, rule_name) VALUES (3, 'rule_3');

INSERT INTO roles (role_id, role_name) VALUES (1, 'admin');
INSERT INTO roles (role_id, role_name) VALUES (2, 'power_user');
INSERT INTO roles (role_id, role_name) VALUES (3, 'user');

INSERT INTO users (user_id, user_login, role_id) VALUES (1, 'vasya', 1);
INSERT INTO users (user_id, user_login, role_id) VALUES (2, 'masha', 1);
INSERT INTO users (user_id, user_login, role_id) VALUES (3, 'jenya', 2);
INSERT INTO users (user_id, user_login, role_id) VALUES (4, 'petya', 3);
INSERT INTO users (user_id, user_login, role_id) VALUES (5, 'alexa', 3);
INSERT INTO users (user_id, user_login, role_id) VALUES (6, 'misha', 2);

INSERT INTO roles_rules (role_id, rule_id) VALUES (1, 1);
INSERT INTO roles_rules (role_id, rule_id) VALUES (1, 2);
INSERT INTO roles_rules (role_id, rule_id) VALUES (1, 3);
INSERT INTO roles_rules (role_id, rule_id) VALUES (2, 2);
INSERT INTO roles_rules (role_id, rule_id) VALUES (2, 3);
INSERT INTO roles_rules (role_id, rule_id) VALUES (3, 3);

INSERT INTO categories (categ_id, categ_name) VALUES (1, 'fatal');
INSERT INTO categories (categ_id, categ_name) VALUES (2, 'severe');
INSERT INTO categories (categ_id, categ_name) VALUES (3, 'problem');
INSERT INTO categories (categ_id, categ_name) VALUES (4, 'feature');

INSERT INTO states (state_id, state_name) VALUES (1, 'new');
INSERT INTO states (state_id, state_name) VALUES (2, 'working');
INSERT INTO states (state_id, state_name) VALUES (3, 'closed');

INSERT INTO items (user_id, item_category, item_state) VALUES (1, 1, 1);
INSERT INTO items (user_id, item_category, item_state) VALUES (2, 1, 1);
INSERT INTO items (user_id, item_category, item_state) VALUES (3, 4, 2);
INSERT INTO items (user_id, item_category, item_state) VALUES (4, 4, 2);
INSERT INTO items (user_id, item_category, item_state) VALUES (5, 2, 3);
INSERT INTO items (user_id, item_category, item_state) VALUES (6, 1, 3);

INSERT INTO comments (comment_id, item_id, comment_text) VALUES (1, 1, 'ha-ha');
INSERT INTO comments (comment_id, item_id, comment_text) VALUES (2, 3, 'he-he');
INSERT INTO comments (comment_id, item_id, comment_text) VALUES (3, 3, 'ho-ho');

INSERT INTO attachments (attach_id, item_id, attach_path) VALUES (1, 1, 'file1.txt');
INSERT INTO attachments (attach_id, item_id, attach_path) VALUES (2, 1, 'file2.txt');
INSERT INTO attachments (attach_id, item_id, attach_path) VALUES (3, 1, 'file3.txt');
INSERT INTO attachments (attach_id, item_id, attach_path) VALUES (4, 5, 'file1.txt');
