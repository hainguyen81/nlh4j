@echo off

call "./setEnv.bat" /wait

%JAVA_HOME%\bin\java -jar .\LicenseTool.jar -f .\licenseIn.lic -ks .\.keystore.jks -t JKS -p 01101981 -s Serial.Key -a org.nlh4j
pause