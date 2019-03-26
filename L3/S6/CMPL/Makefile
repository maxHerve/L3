JAVAC=javac

PATH_ANTLR=antlr/
PATH_SOURCE=src/
PATH_LIBCLASS=libclass/
PATH_CLASS=class/
PATH_GRAMMAR=grammaire/

projet:
	./g2java.sh $(PATH_GRAMMAR)projet.g
	mv grammaire/*.java $(PATH_SOURCE)
	$(JAVAC) -d $(PATH_CLASS) -cp $(PATH_ANTLR)antlr-3.5.2-complete.jar:libclass/ $(PATH_SOURCE)*.java

clean:
	rm -rf $(PATH_CLASS)*

run:
	java Mapile
