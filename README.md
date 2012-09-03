MavenPlugin
===========

> This project was forked from http://code.google.com/p/robotframework-maven-plugin
> in order to be able to make backwards incompatible changes to configuration. 



Maven plugin for using the Robot Framework. Goal of this plugin is to be able to use Robot Framework in a Maven project
without the need to install anything extra (e.g. Robot Framework, Jython, etc). In short, it's a non-invasive way of 
introducing acceptance test driven development to your existing projects quickly.

Maven Goals
-----------

The plugin currently has two goals:

* run - behaves like invoking the "jybot" Robot Framework command for executing test cases
* libdoc - invokes the "libdoc.py" Robot Framework command for generating keyword documentation for test libraries and resource files

Quick Start
-----------

Add the plugin to your build:

```xml
<project>

  <build>
    ..
    ..
    <plugins>
      ..
      ..

      <plugin>
        <groupId>com.googlecode.robotframework-maven-plugin</groupId>
        <artifactId>robotframework-maven-plugin</artifactId>
        <version>1.1.2</version>
        <executions>
          <execution>
            <goals>
              <goal>run</goal>
            </goals>
          </execution>
        </executions>        
      </plugin>

      ..
      ..
    </plugins>
  </build>

</project>
```

By default, you can add your test cases to ${project.basedir}/src/test/resources/robotframework/tests

Third party libraries (e.g. Selenium Library) can be added to ${project.basedir}/src/test/resources/robotframework/libraries

During mvn install invocation, run command will be invoked during the integration-test phase.