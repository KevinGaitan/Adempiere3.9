@Title Install ADempiere Server
@Echo off


@if not "%JAVA_HOME%" == "" goto JAVA_HOME_OK
@Set JAVA=java
@Echo JAVA_HOME is not set.  
@Echo You may not be able to start the required Setup window !!
@Echo Set JAVA_HOME to the directory of your local 1.5 JDK.
@Echo If you experience problems, run utils/WinEnv.js
@Echo Example: cscript utils\WinEnv.js C:\Adempiere "C:\Program Files\Java\jdk1.5.0_04"
goto START

:JAVA_HOME_OK
@Set JAVA=%JAVA_HOME%\bin\java

:START
@"%JAVA%" --add-opens java.base/sun.security.tools.keytool=ALL-UNNAMED -classpath lib\CInstall.jar; -DADEMPIERE_HOME=%ADEMPIERE_HOME% -Dant.home="." org.apache.tools.ant.launch.Launcher update
@Rem Sleep 10
@CHOICE /C YN /T 10 /D N > NUL