CREATE DATABASE admin;
USE admin;
show databases;
CREATE TABLE userAccount(
idUserAccount int unsigned not null auto_increment,
firstName varchar(200) not null,
lastName varchar(200) not null,
userName varchar(200) not null,
password varchar(200) not null,
primary key(idUserAccount));

INSERT INTO userAccount(firstName,lastName,username,password)
VALUES ('Ideja','Ferati','admin','ideja123');


CREATE TABLE  addBook(
Bookid varchar(200) not null,
title varchar(200) not null,
author varchar(200) not null,
publisher varchar(200) not null,
quantity int not null,
isAvail boolean default true,
primary key(Bookid));

CREATE TABLE  addMember(
Memberid varchar(200) not null,
name varchar(200) not null,
email varchar(200) not null ,
phone varchar(200)not null ,
gender enum('female','male') not null,
primary key(Memberid));

CREATE TABLE  issuedBooks(
bookID varchar(200) not null,
memberID varchar(200) not null,
issueTime timestamp default CURRENT_TIMESTAMP,
renew_count integer default 0,
primary key(bookID,memberID),
foreign key(bookID) references addBook(Bookid),
foreign key(Memberid) references addMember(Memberid));




insert into issuedBooks(bookId,memberID)
		values("b1","1");
        insert into issuedBooks(bookId,memberID)
		values("b2","2");
 
select * from issuedbooks ;
select * from addbook where boOkID = "b1";
select * from addmember;

SELECT memberID,bookID from issuedBooks where memberID='1' 
                    and bookID="b1";
                    
-- Testing Querries --

SELECT *
FROM issuedbooks,addmemeber
inner JOIN addbook
ON addbook.bookid = 'b1' and addmember.memberid='1'; 

select * from issuedbooks
inner join addmember
inner join addbook on addmember.memberid="2" and addbook.bookid ="b2" and issuedbooks.bookid = addbook.bookid ;

UPDATE issuedBooks SET issueTime = CURRENT_TIMESTAMP,renew_count = renew_count+1 WHERE bookID ="b1";

SELECT * FROM issuedBooks WHERE bookID ="b" and memberID = "2";
SELECT * FROM issuedBooks WHERE bookID = 'b1' and memberID='2';
