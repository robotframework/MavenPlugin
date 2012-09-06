Robot Framework Maven Plugin
============================

This is the documentation for `Robot Framework Maven Plugin`__. You can explore the plugin site documentation `here`__.

__ https://github.com/robotframework/MavenPlugin
__ _static/site/index.html


Introduction
------------

Goal of this plugin is to be able to use Robot Framework in a Maven project
without the need to install anything extra (e.g. Robot Framework, Jython, etc). In short, it's a non-invasive way of
introducing acceptance test driven development to your existing projects quickly.


Maven Goals
-----------

The plugin currently has two goals:

* `run <_static/site/run-mojo.html>`_  - behaves like invoking the "jybot" Robot Framework command for executing test cases
* `libdoc <_static/site/libdoc-mojo.html>`_ - invokes the "libdoc.py" Robot Framework command for generating keyword documentation for test libraries and resource files


Quick Start
-----------

Add the plugin to your build::

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
            <version>1.0.0</version>
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


By default, you can add your test cases to ${project.basedir}/src/test/resources/robotframework/tests

Third party libraries (e.g. Selenium Library) can be added to ${project.basedir}/src/test/resources/robotframework/libraries

During mvn install invocation, run command will be invoked during the integration-test phase.


