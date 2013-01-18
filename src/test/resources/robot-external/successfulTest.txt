*** Settings ***
Default Tags    success

*** Test Cases ***
Verify environment variable
  Should Be Equal    %{FOO}     bar
  Should Be Equal    %{FAA}     Something else