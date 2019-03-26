CC=gcc
CFLAGS=--std=c11 -g -Wall

PATH_BINARY=./bin/
PATH_OBJECT=./obj/
PATH_SOURCE=./src/
PATH_INCLUDE=./include/

INCLUDES=-I $(PATH_INCLUDE)

all: $(PATH_BINARY)shell1 $(PATH_BINARY)shell2 $(PATH_BINARY)shell3

$(PATH_BINARY)shell1: $(PATH_SOURCE)shell1.c
	$(CC) $(CFLAGS) $(INCLUDES) $< -o $@


$(PATH_BINARY)shell2: $(PATH_SOURCE)shell2.c
	$(CC) $(CFLAGS) $(INCLUDES) $< -o $@


$(PATH_BINARY)shell3: $(PATH_SOURCE)shell3.c
	$(CC) $(CFLAGS) $(INCLUDES) $< -o $@
