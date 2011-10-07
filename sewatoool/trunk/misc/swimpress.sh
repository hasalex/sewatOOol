#!/bin/sh
SCRIPT_DIR=`dirname $0`
cd $SCRIPT_DIR
OFFICE_HOME="/usr/lib/openoffice/program/" 
CLASSPATH="../lib/sewatoool.jar:../conf/:$OFFICE_HOME/classes/juh.jar:$OFFICE_HOME/classes/jurt.jar:$OFFICE_HOME/classes/ridl.jar:$OFFICE_HOME/classes/unoil.jar:$OFFICE_HOME":../lib/commons-logging-1.1.1.jar:../lib/log4j-1.2.15.jar
java -cp $CLASSPATH fr.sewatech.sewatoool.impress.Main $*

