do $$
declare
brow record;
begin
for brow in (select format(
'DROP TABLE %I CASCADE', tablename)
as table_name
from pg_tables
where schemaname = 'public')
loop
execute brow.table_name;
end loop;
end; $$