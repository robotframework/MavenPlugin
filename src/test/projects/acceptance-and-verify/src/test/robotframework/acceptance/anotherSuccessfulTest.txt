*** Settings ***
Default Tags    success

*** Test Cases ***
Successful Test Negatives
  Should Be Equal As Numbers  -1  -1
  Should Be Equal As Numbers  -4  -4