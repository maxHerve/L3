JAVAC=javac

PATH_SOURCE=./src/
PATH_CLASS=./class/

compileAll:
	$(JAVAC) -d $(PATH_CLASS) `find ${PATH_SOURCE} -name "*.java"`

AfficheInterfaces:
	java -classpath $(PATH_CLASS) istic/pr/socket/address/AfficheInterfaces

EchoServeurTCP:
	java -classpath $(PATH_CLASS) istic.pr.socket.tcp.echo/ServeurTCP

ClientTCP:
	java -classpath $(PATH_CLASS) istic.pr.socket.tcp.echo/ClientTCP
	
EchoClientTCP:
	java -classpath $(PATH_CLASS) istic.pr.socket.tcp.echo/ClientTCP

ServeurTCP:
	java -classpath $(PATH_CLASS) istic.pr.socket.tcp.nom/ServeurTCP
	
ClientTCP:
	java -classpath $(PATH_CLASS) istic.pr.socket.tcp.nom/ClientTCP ${ARGS}

clean:
	rm -r $(PATH_CLASS)*
