CC=gcc
CFLAGS=-g

PATH_BINARY=./bin/
PATH_OBJECT=./obj/
PATH_SOURCE=./src/
PATH_INCLUDE=./include/

INCLUDES=-I $(PATH_INCLUDE)

all: $(PATH_BINARY)tp3 $(PATH_BINARY)nettoie

$(PATH_BINARY)tp3: $(PATH_SOURCE)tp3.c
	$(CC) $(CFLAGS) $(INCLUDES) $< -o $@


$(PATH_BINARY)nettoie: $(PATH_SOURCE)nettoie.c
	$(CC) $(CFLAGS) $(INCLUDES) $< -o $@
