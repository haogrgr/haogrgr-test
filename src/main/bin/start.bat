@echo off
SetLocal EnableDelayedExpansion

SET MAIN_CLASS=${main.class}

SET JAVA_OPTS=-Xms2048m -Xmx2048m -XX:NewSize=1024m -XX:PermSize=512m -server -XX:+DisableExplicitGC -verbose:gc -XX:+PrintGCDateStamps -XX:+PrintGCDetails

SET JAVA_PROPS=-D${project.parent.groupId}.${project.parent.artifactId}

SET CLASSPATH=.;.\conf
FOR %%i IN ("./lib/*.jar") DO SET CLASSPATH=!CLASSPATH!;.\lib\%%i

ECHO java %JAVA_OPTS% %JAVA_PROPS% -cp %CLASSPATH% %MAIN_CLASS%

java %JAVA_OPTS% %JAVA_PROPS% -cp %CLASSPATH% %MAIN_CLASS%

EndLocal