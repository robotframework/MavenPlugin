*** Test cases ***
Dependencies should be in classpath
     Should contain             %{java.class.path}       httprequestlibrary
Dependencies should not be in classpath
     Should not contain         %{java.class.path}       httprequestlibrary
     Should contain             %{java.class.path}       this-should-be-seen-by-external-process.jar