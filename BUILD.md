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
1. VERSION=1.0.2
2. update the version number from pom.xml (also src/test/project/acceptance-and-verify/pom.xml, src/site/apt/index.apt, src/site/apt/examples/javalibraries.apt)
3. update version numbers from documentation

> osx find ./ -type f \( -name \*.apt -o -name \*.md \) -exec sed -i '' 's/1.0.1/$VERSION/g' '{}' \;
> linux find ./ -type f \( -name \*.apt -o -name \*.md \) -exec sed -i 's/1.0.1/$VERSION/g' '{}' \;

4. update [release notes](https://github.com/robotframework/MavenPlugin/wiki/ReleaseNotes "release notes")
5. mvn verify
6. mvn site:site (check that site docs are alright from target/site/index.html)
7. git commit -am "version $VERSION"
8. git tag $VERSION
9. git push
10. git push --tags
11. mvn site (see Generate site docs below)
12. mvn deploy (see Deploy project to Sonatype below)
13. do close and release from Staging Repository at https://oss.sonatype.org/index.html#welcome

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