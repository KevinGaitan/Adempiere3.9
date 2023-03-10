@Title Build Adempiere Incremental + Install
@Rem $Header: /cvsroot/adempiere/utils_dev/RUN_buildIncremental.bat,v 1.14 2005/09/08 21:56:11 jjanke Exp $

@Rem Check java home
@IF NOT EXIST "%JAVA_HOME%\bin" ECHO "** JAVA_HOME NOT found"
@SET PATH=%JAVA_HOME%\bin;%PATH%

@Rem Set ant classpath
@SET ANT_CLASSPATH=%CLASSPATH%;..\tools\lib\ant.jar;..\tools\lib\ant-launcher.jar;..\tools\lib\ant-swing.jar;..\tools\lib\ant-commons-net.jar;..\tools\lib\commons-net-1.4.0.jar
@SET ANT_CLASSPATH=%ANT_CLASSPATH%;"%JAVA_HOME%\lib\tools.jar"

@SET ANT_OPTS=-Xms128m -Xmx512m

@echo Building ...
@"%JAVA_HOME%\bin\java" %ANT_OPTS% -classpath %ANT_CLASSPATH% -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main update
@Echo ErrorLevel = %ERRORLEVEL%

@IF NOT ERRORLEVEL 0 GOTO BUILDOK
@Pause
@Exit

:BUILDOK

@Rem Echo	Cleaning up ...
@Rem erase /q /s %TMP%

@Pause
@Exit
