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

1. update the version number from pom.xml (also src/test/project/acceptance-and-verify/pom.xml, src/site/apt/index.apt, src/site/apt/examples/javalibraries.apt)
2. update release notes
3. mvn verify
4. mvn site:site (check that site docs are alright from target/site/index.html)
5. git commit -am "version VERSION"
6. git tag VERSION
7. git push
8. git push --tags
9. mvn site (see Generate site docs below)
10. mvn deploy (see Deploy project to Sonatype below)
11. do close and release from Staging Repository at https://oss.sonatype.org/index.html#welcome

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

As a prerequisite, you need account at Sonatype Jira, gpg keys, and Sonatype servers configured to your settings.xml.
See https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide

> mvn deploy

Then do close and release from Staging Repository at https://oss.sonatype.org/index.html#welcome