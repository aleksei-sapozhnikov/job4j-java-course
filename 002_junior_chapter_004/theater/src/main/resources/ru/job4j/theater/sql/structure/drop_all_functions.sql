do $$
declare
brow record;
begin
for brow in (select format(
'DROP FUNCTION %I.%I(%s);',
nspname, proname, oidvectortypes(proargtypes))
as func_name
from pg_proc
inner join pg_namespace ns
on (pg_proc.pronamespace = ns.oid)
where ns.nspname = 'public'
order by proname)
loop
execute brow.func_name;
end loop;
end; $$