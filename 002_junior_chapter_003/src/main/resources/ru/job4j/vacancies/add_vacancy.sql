INSERT INTO vacancy (theme, url, updated)
VALUES ('%s', '%s', '%s')
RETURNING id;