*** Settings ***
Library    HttpRequestLibrary
Default Tags    success

*** Test Cases ***
Successful Test
  Should Be Equal As Numbers  1  1
  Should Be Equal As Numbers  4  4
  Pretty Print Json    { "testJson": { "key": "value" }}
