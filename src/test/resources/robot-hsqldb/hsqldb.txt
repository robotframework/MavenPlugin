*** Settings ***
Library         OperatingSystem
Library         DatabaseLibrary

*** Test Cases ***
Connect
    Connect To Dbapi2  jaydebeapi  org.hsqldb.jdbcDriver  jdbc:hsqldb:mem:robot  sa  ${EMPTY}
    Execute SQL  create table testtable (myid integer not null primary key, name varchar(25))
    Execute SQL  insert into testtable values (1, 'myname')
    Execute SQL  insert into testtable values (2, 'yourname')
    @{result}=  Execute Sql  Select * from testtable
    Log Many  @{result}
    Check If Exists In Database  select * from testtable where myid=2
    Execute SQL  drop table testtable
    Disconnect From Database

