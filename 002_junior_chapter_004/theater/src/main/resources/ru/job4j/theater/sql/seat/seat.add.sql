insert into seat(_row, _column, _price, _owner_id)
values (?,
        ?,
        ?,
        greatest(null, (select _id from account where _name = ? and _phone = ?)));