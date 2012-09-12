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

* update the version numbers

> osx grep -rl '<version>1.0.2</version>' ./ | xargs sed -i '' 's|<version>1.0.2</version>|<version>1.0.3</version>|g'
> linux grep -rl '<version>1.0.2</version>' ./ | xargs sed -i 's|<version>1.0.2</version>|<version>1.0.3</version>|g'

* Sanity check the changes

> git di

* Build project

> mvn clean verify

* Generate site docs and check that site docs are alright from target/site/index.html

> mvn site:site

* Commit to git

> git commit -am "version 1.0.3"
> git tag 1.0.3
> git push
> git push --tags

* update [release notes](https://github.com/robotframework/MavenPlugin/wiki/ReleaseNotes "release notes")

* Push site to github (See generate site docs below for prerequirements)

> mvn site

* Send the material to Sonatype (see Deploy project to Sonatype below)

> mvn deploy

* do close and release from Staging Repository at https://oss.sonatype.org/index.html#welcome

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