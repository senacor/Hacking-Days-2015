create table if not exists $PROFILETABLE$ (
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
