@echo off
SET JAVA_HOME=F:\Java.Working\1.tools\jdk\1.8.0.73.x64
%JAVA_HOME%\bin\java -jar .\LicenseTool.jar -f .\licenseIn.lic -ks .\.keystore.jks -t JKS -p systemexe -s Serial.Key -a org.nlh4j
pause