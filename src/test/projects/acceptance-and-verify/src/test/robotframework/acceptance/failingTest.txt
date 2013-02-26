*** Variables ***
${VariableThatShouldNotBeOverridden}        This defined in pom_with_test_configured.xml
${VariableThatShouldBeOverridden}           This defined in pom_with_test_configured.xml but should be overridden on prompt
${extra variable from prompt}              This also should be overridden from prompt

*** Test Cases ***
Failing Test Case
  Should Be Equal As Numbers  1  2
  Should Be Equal As Numbers  2  3
Failing based on variable
  Should Be Equal   ${VariableThatShouldNotBeOverridden}   ${VariableThatShouldBeOverridden}
  Should Be Equal   ${extra variable from prompt}         overridden
