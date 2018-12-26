insert into account(_name, _phone)
values (?, ?)
on conflict do nothing;