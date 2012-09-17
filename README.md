MavenPlugin
===========

Maven plugin for using the Robot Framework. Goal of this plugin is to be able to use Robot Framework in a Maven project
without the need to install anything extra (e.g. Robot Framework, Jython, etc). In short, it's a non-invasive way of 
introducing acceptance test driven development to your existing projects quickly.

Plugin documentation is available at http://robotframework.github.com/MavenPlugin/

> This project was forked from http://code.google.com/p/robotframework-maven-plugin
> in order to be able to make backwards incompatible changes to configuration.

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
        <groupId>org.robotframework</groupId>
        <artifactId>robotframework-maven-plugin</artifactId>
        <version>1.0.2</version>
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

By default, you can add your test cases to ${project.basedir}/src/test/robotframework/acceptance

Third party libraries (e.g. Selenium Library) can be added to ${project.basedir}/src/test/resources/robotframework/libraries

During mvn install invocation, run command will be invoked during the integration-test phase.

For more detailed documentation please see http://robotframework.github.com/MavenPlugin/