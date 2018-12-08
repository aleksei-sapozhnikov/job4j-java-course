insert into payment(_amount, _from_id, _comment)
values (?,
        (select _id from account where _name = ? and _phone = ?),
        ?);