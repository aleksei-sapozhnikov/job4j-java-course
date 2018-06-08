INSERT INTO vacancy (title, url, updated)
VALUES ('%s', '%s', '%s')
RETURNING id;