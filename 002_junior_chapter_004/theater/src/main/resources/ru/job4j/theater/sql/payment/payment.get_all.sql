select
  p._amount as payment_amount,
  a._name as from_name,
  a._phone as from_phone,
  p._comment as payment_comment
from payment p
       join account a on p._from_id = a._id;