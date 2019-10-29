DEL %SONAR_HOME%\logs\*.* /s /q
DEL %SONAR_HOME%\extensions\plugins\jmetrics-sonar-plugin-2.0-SNAPSHOT.jar /s /q
CALL mvn clean install
COPY /Y target\jmetrics-sonar-plugin-2.0-SNAPSHOT.jar %SONAR_HOME%\extensions\plugins\jmetrics-sonar-plugin-2.0-SNAPSHOT.jar