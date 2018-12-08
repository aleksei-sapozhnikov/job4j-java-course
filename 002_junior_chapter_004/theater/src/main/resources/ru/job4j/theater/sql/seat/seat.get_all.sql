select
  s._row,
  s._column,
  s._price,
  nullif(s._owner_id, null) as _owner_id,
  coalesce(a._name, null) as _name,
  coalesce(a._phone, null) as _phone
from seat s
  left outer join account a on s._owner_id = a._id;