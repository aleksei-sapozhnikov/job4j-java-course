DELETE FROM items;
DELETE FROM comments;

INSERT INTO items (name, description, create_time) VALUES
  ('item_first', 'first item created', '1968-09-12 18:33:12');

INSERT INTO comments (item_id, text) VALUES
  ((SELECT items.id FROM items WHERE items.name = 'item_first'), 'comment to first item');