Branches
========

`master` = "Release" branch, contains always version that's release to Maven Central

`develop` = Developent time branch. All PRs should be targetted (and usually also started) from here.

`gh-pages` = Github internal branch containing

Building
========

To run the unit tests, execute goal:
```bash
mvn test
```
To run all tests, including the integration tests:
```bash
mvn verify
```
To install the plugin locally:
```bash
mvn install
```
To generate the documentation:
```bash
mvn site:site
```

Releasing
=========

* update the version numbers, first the pom, then from other files and docs.

osx:
```bash
grep -rl 'sion>1.7.2</ver' ./ | xargs sed -i '' 's|sion>1.7.2</ver|sion>1.8.0</ver|g'
```
linux:
```bash
grep -rl 'sion>1.7.2</ver' ./ | xargs sed -i 's|sion>1.7.2</ver|sion>1.8.0</ver|g'
```
Last you should update those rows above.

* Sanity check the changes

```bash
git di
```
* Build project

```bash
mvn clean verify
```
* Generate site docs and check that site docs are alright from target/site/index.html

```bash
mvn site:site
```
* Commit to git

```bash
git commit -am "version 1.6"
git tag -a 1.6
git push
git push --tags
```
* update [release notes](https://github.com/robotframework/MavenPlugin/wiki/ReleaseNotes "release notes")

* Push site to github (See generate site docs below for prerequirements)

```bash
mvn site
```
* Send the material to Sonatype (see Deploy project to Sonatype below)

```bash
mvn deploy -Pdeploy
```
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

```bash
mvn site
```
Deploy project to Sonatype
--------------------------

As a prerequisite, you need account at Sonatype Jira, gpg keys, and Sonatype servers configured to your settings.xml.
See https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide

```bash
mvn deploy -Pdeploy
```
Then do close and release from Staging Repository at https://oss.sonatype.org/index.html#welcome
