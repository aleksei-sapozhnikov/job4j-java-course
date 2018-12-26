select a._name, a._phone, p._amount, p._comment
from payment p
       join account a on p._from_id = a._id;