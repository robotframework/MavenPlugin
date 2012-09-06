Building
========

To run the unit tests, execute goal:

> mvn test

To run all tests, including the integration tests:

> mvn verify

To install the plugin locally:

> mvn install

To generate the documentation:

> mvn site:site

Releasing
=========

1. update the version number from pom.xml
2. commit the changes to github

Generate site docs
------------------

To generate the documentation and upload it to github, first configure your settings.xml

```xml
<servers>
  <server>
    <id>github</id>
    <username>GitHubLogin</username>
    <password>GitHubPassw0rd</password>
  </server>
</servers>
```

then:

> mvn site

Deploy project to Sonatype
--------------------------

> mvn deploy