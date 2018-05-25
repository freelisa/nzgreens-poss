@echo on
cd %~dp0
java  -jar H:\workspace-hlz\xinxilan\nzgreens-parent\nzgreens-dal\mybatisgenerator\mybatis-generator-core-1.3.2.jar -configfile generatorConfig.xml -overwrite
pause