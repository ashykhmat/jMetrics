## jMetrics
jMetrics is an application to calculate different code quality metrics for Java based project.
The main feature is ability to calculate **Maintainability Index**.
Supported metrics:

Metric | Description |
------------ | ------------- |
Maintainability Index | Is a software metric which measures how maintainable (easy to support and change) the source code is. |
Cyclomatic Complexity | Is a software metric (measurement), used to indicate the complexity of a program. It is a quantitative measure of the number of linearly independent paths through a program's source code. |
Halstead Volume | Describes the size of the implementation of an algorithm. |
Lines of Code | Describes total lines of code in the project. |


## Building from Source
jMetrics uses a [Maven][]-based build system. In the instructions below, `mvn` is invoked from the root of the source tree and servces as a cross-patform, self-contained bootstrap mechanism for the build.

### Prerequisites
[Git][] and [JDK 8 update 20 or later][JDK8 build]

Be sure that your `JAVA_HOME` environment variable points to the `jdk1.8.0` folder
extracted from the JDK download.

### Check out sources
`git clone git@github.com:ashykhmat/jMetrics.git`

### Import sources into your IDE
In IDE import project as existing Maven project. IDE will load automatically all required dependencies with their sources.

### Compile and test; build all jars, distribution zips, and docs
`mvn clean install`

... and discover more commands with `mvn --help`.

## Run Console application

To run jMetrics as a console application, after it was built, run the following command
`java -jar jmetrics-console/target/jmetrics-console-1.0-SNAPSHOT.jar -projectPath PATH_TO_PROJECT -reportPath PATH_TO_REPORT`

Parameter | Description | Example Value |
------------ | ------------- | ------------- |
`projectPath` | specifies path to the folder, that contains java application to analyze | C:\workspace\my_application |
`reportPath` | specifies path to the folder, that will be used to store generated Excel report | C:\report |
`help` | command to see application help information | 

**Note that all these parameters are required to run application correctly.**

Sample command:

`java -jar jmetrics-console/target/jmetrics-console-1.0-SNAPSHOT.jar -projectPath C:\\workspace\\my_application -reportPath C:\\report`

## Run SonarQube Plug-in

To run jMetrics as a [SonarQube][] plug-in, copy jmetrics-sonar-plugin/target/jmetrics-sonar-plugin-1.0-SNAPSHOT.jar into SonarQube/extensions/plugins folder.
Start SonarQube and execute metrics collection on your project. This can be done executing following Maven command in your project root folder:

`mvn sonar:sonar`

To view results, configure SonarQube dashboard for your project. Add custom widget called **jMetrics**

## Run Maven Plug-in

To run jMetrics as a [Maven][] plug-in, update your project pom.xml file and add following reporting configuration:

```xml
<plugin>
	<groupId>com.shykhmat</groupId>
	<artifactId>jmetrics-maven-plugin</artifactId>
	<version>1.0-SNAPSHOT</version>
</plugin>
```

To execute metrics collection on your project and report generation, execute following Maven command in your project root folder:

`mvn site`

To view results, navigate into target/site folder. Report will have name YOUR_PROJECT_NAME.xsls


## Update Java version

Currently Java 9 is supported. To add support of newer Java version, you should use Anrlr to generate new Java parser, lexer and listeners.
Current [Antlr4 Java Grammar][] that is used, provided by Antlr.
To generate new Java parser, lexer and listeners you could create simple maven project and use antlr4-maven-plugin:

```xml
<plugin>
    <groupId>org.antlr</groupId>
    <artifactId>antlr4-maven-plugin</artifactId>
    <version>4.7</version>
    <executions>
        <execution>
            <goals>
               <goal>antlr4</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
Antlr plugin gets grammar description from src/main/antlr4/\<package\>/*.g4 files, generates the code and places it into target/generated-sources/antlr4/<package>
Use command `mvn generate-sources` to generate Java classes from grammar file.

[Maven]: https://maven.apache.org/
[Git]: http://help.github.com/set-up-git-redirect
[JDK8 build]: http://www.oracle.com/technetwork/java/javase/downloads
[SonarQube]: https://www.sonarqube.org/
[Antlr4 Java Grammar]: https://github.com/antlr/grammars-v4/blob/master/java9/Java9.g4
