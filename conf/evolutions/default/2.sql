# --- Sample dataset

# --- !Ups

insert into users(id, username, password, token) values (1, 'ali', 'strongePass', 'someToken');
insert into users(id, username, password, token) values (2, 'ali', 'stROngePASS', 'anOther');
insert into users(id, username, password, token) values (3, 'ali', 'STROngePASs', 'anOtherAgain');
insert into users(id, username, password, token) values (4, 'ali', 'verySTRONG',  'andAnOtherAgain');

# --- !Downs

delete from users;
