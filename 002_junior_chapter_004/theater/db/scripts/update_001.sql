create table if not exists account (
  _id    serial primary key,
  _name  text not null,
  _phone text not null,
  unique (_name, _phone)
  );

create table if not exists seat (
  _id       serial primary key,
  _row      int not null,
  _column   int not null,
  _price    int not null,
  _owner_id int references account(_id) default null,
  unique (_row, _column)
  );

create table if not exists payment (
  _id      serial primary key,
  _amount  int  not null,
  _from_id int  not null references account(_id),
  _comment text not null
  );