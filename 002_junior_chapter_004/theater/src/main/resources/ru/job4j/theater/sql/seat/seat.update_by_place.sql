update seat
set
        _row      = ?,
        _column   = ?,
        _price    = ?,
        _owner_id = greatest(null, (select _id from account where _name = ? and _phone = ?))
where
        _row = ? and _column = ?;