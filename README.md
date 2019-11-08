## jMetrics
jMetrics is an application to calculate different code quality metrics for Java based project.
The main feature is ability to calculate **Maintainability Index**.
Supported metrics:

Metric | Description |
------------ | ------------- |
Maintainability Index (MI) | Is a software metric which measures how maintainable (easy to support and change) the source code is. Microsoft formula is used for calculation. A **high value means better maintainability**. Color coded ratings can be used to quickly identify trouble spots in your code. A **green rating is between 20 and 100** and indicates that the code has good maintainability. A **yellow rating is between 10 and 19** and indicates that the code is moderately maintainable. A **red rating is a rating lower than 9** and indicates low maintainability. More details about formula can be found on [Microsoft Official Website][]|
Cyclomatic Complexity | Is a software metric (measurement), used to indicate the complexity of a program. It is a quantitative measure of the number of linearly independent paths through a program's source code. |
Distinct Operators (n1)| Number of distinct operators. |
Distinct Operands (n2)| Number of distinct operands. |
Occurences of Operators (N1)| Total number of occurrences of operators. |
Occurences of Operands (N2)| Total number of occurrences of operands.|
Program Length (N) | The total number of operator occurrences and the total number of operand occurrences. N = N1 + N2 |
Halstead Vocabulary (n)| The total number of unique operator and unique operand occurrences. n = n1 + n2|
Estimated Length (N^) | Estimated program length. N^ = n1log2n1 + n2log2n2 |
Purity Ratio (PR) | Ratio of estimated length and program length. PR = N^ / N |
Program Difficulty (D) | This parameter shows how difficult to handle the program is. D = (n1 / 2) * (N2 / n2) |
Programming Effort (E) | Measures the amount of mental activity needed to translate the existing algorithm into implementation in the specified program language. E = D * V |
Programming Time (T) |  Shows time (in seconds) needed to translate the existing algorithm into implementation in the specified program language.|
Halstead Volume (V) | Describes the size of the implementation of an algorithm. Proportional to program size, represents the size, in bits, of space necessary for storing the program. This parameter is dependent on specific algorithm implementation. The properties V, N, and the number of lines in the code are shown to be linearly connected and equally valid for measuring relative program size. V = Size * (log2 vocabulary) = N * log2(n) |
Lines of Code | Describes total lines of code in the project. |
Efferent Coupling (Ce) | The number of classes in other packages/classes that depend upon classes within the package/class is an indicator of the package/class's responsibility. Afferent couplings signal inward. |
Afferent Coupling  (Ca)| The number of classes in other packages/classes that the classes in a package/class depend upon is an indicator of the package/class's dependence on externalities. Efferent couplings signal outward.|
Instability | The ratio of efferent coupling (Ce) to total coupling (Ce + Ca) such that I = Ce / (Ce + Ca). This metric is an indicator of the package/class's resilience to change. The range for this metric is 0 to 1, with I=0 indicating a completely stable package and I=1 indicating a completely unstable package. |
Number of abstract classes and interfaces |  The number of abstract classes (and interfaces) in the package is an indicator of the extensibility of the package.|
Number of non-abstract classes |  The number of concrete classes in the package is an indicator of the extensibility of the package.|
Abstractness | The ratio of the number of abstract classes (and interfaces) in the analyzed package to the total number of classes in the analyzed package. The range for this metric is 0 to 1, with A=0 indicating a completely concrete package and A=1 indicating a completely abstract package.|
Distance | The perpendicular distance of a package from the idealized line A + I = 1. D is calculated as D = \| A + I - 1 \|. This metric is an indicator of the package's balance between abstractness and stability. A package squarely on the main sequence is optimally balanced with respect to its abstractness and stability. Ideal packages are either completely abstract and stable (I=0, A=1) or completely concrete and unstable (I=1, A=0). The range for this metric is 0 to 1, with D=0 indicating a package that is coincident with the main sequence and D=1 indicating a package that is as far from the main sequence as possible. |
Circular Dependencies | A relation between two or more modules which either directly or indirectly depend on each other to function properly. Such modules are also known as mutually recursive. |

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