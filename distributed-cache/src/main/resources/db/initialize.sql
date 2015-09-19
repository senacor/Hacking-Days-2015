create table if not exists stuff (
  id integer identity primary key,
  name varchar(255) not null
);

create table if not exists profile (
  uuid varchar(255) primary key,
  name varchar(255) not null,
  gender integer not null,
  age integer not null,
  location_state varchar(255) not null,
  location_city varchar(255) not null,
  location_zip varchar(255) not null,
  relationship integer not null,
  smoker boolean not null,
  seeking_gender integer not null,
  seeking_age_min integer not null,
  seeking_age_max integer not null,
  activity_logincount integer not null default 0,
  activity_lastlogin bigint not null
);

--insert into profile values
--  ('ea453b5b-c3f3-4685-b203-4f19b1ab7b64', 'Hans Dampf', 0, 25, 'NY', 'New York', '12345', 0, false, 0, 25, 30, 0, 123412341234);
