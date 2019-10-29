## jMetrics
jMetrics is an application to calculate different code quality metrics for Java based project.
The main feature is ability to calculate **Maintainability Index**.
Supported metrics:

Metric | Description |
------------ | ------------- |
Maintainability Index | Is a software metric which measures how maintainable (easy to support and change) the source code is. Microsoft formula is used for calculation. A **high value means better maintainability**. Color coded ratings can be used to quickly identify trouble spots in your code. A **green rating is between 20 and 100** and indicates that the code has good maintainability. A **yellow rating is between 10 and 19** and indicates that the code is moderately maintainable. A **red rating is a rating lower than 9** and indicates low maintainability. More details about formula can be found on [Microsoft Official Website][]|
Cyclomatic Complexity | Is a software metric (measurement), used to indicate the complexity of a program. It is a quantitative measure of the number of linearly independent paths through a program's source code. |
Halstead Volume | Describes the size of the implementation of an algorithm. |
Lines of Code | Describes total lines of code in the project. |


## Building from Source
jMetrics uses a [Maven][]-based build system. In the instructions below, `mvn` is invoked from the root of the source tree and servces as a cross-patform, self-contained bootstrap mechanism for the build.

### Prerequisites
[Git][] and [JDK 8 update 20 or later][JDK8 build]

Be sure that your `JAVA_HOME` environment variable points to the `jdk1.8.20` folder
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
`java -jar jmetrics-console/target/jmetrics-console-2.0-SNAPSHOT.jar -projectPath PATH_TO_PROJECT -reportPath PATH_TO_REPORT`

Parameter | Description | Example Value |
------------ | ------------- | ------------- |
`projectPath` | specifies path to the folder, that contains java application to analyze | C:\workspace\my_application |
`reportPath` | specifies path to the folder, that will be used to store generated Excel report | C:\report |
`help` | command to see application help information | 

**Note that all these parameters are required to run application correctly.**

Sample command:

`java -jar jmetrics-console/target/jmetrics-console-2.0-SNAPSHOT.jar -projectPath C:\\workspace\\my_application -reportPath C:\\report`

## Run SonarQube Plug-in

To run jMetrics as a [SonarQube][] plug-in, copy jmetrics-sonar-plugin/target/jmetrics-sonar-plugin-2.0-SNAPSHOT.jar into SonarQube/extensions/plugins folder. 
Start SonarQube and execute metrics collection on your project. This can be done executing following Maven command in your project root folder:

`mvn sonar:sonar`

To view results, navigate to project page in SonarQube, click "More" and open page called **jMetrics Page**

## Run Maven Plug-in

To run jMetrics as a [Maven][] plug-in, update your project pom.xml file and add following reporting configuration:

```xml
<plugin>
	<groupId>com.shykhmat</groupId>
	<artifactId>jmetrics-maven-plugin</artifactId>
	<version>2.0-SNAPSHOT</version>
</plugin>
```

To execute metrics collection on your project and report generation, execute following Maven command in your project root folder:

`mvn site`

To view results, navigate into target/site folder. Report will have name YOUR_PROJECT_NAME.xsls

[Maven]: https://maven.apache.org/
[Git]: http://help.github.com/set-up-git-redirect
[JDK8 build]: http://www.oracle.com/technetwork/java/javase/downloads
[SonarQube]: https://www.sonarqube.org/
[Microsoft Official Website]: https://blogs.msdn.microsoft.com/zainnab/2011/05/26/code-metrics-maintainability-index