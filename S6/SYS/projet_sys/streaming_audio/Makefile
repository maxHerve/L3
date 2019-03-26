CC=gcc

PATH_SOURCE=./src/
PATH_OBJECT=./obj/
PATH_BINARY=./bin/
PATH_INCLUDE=./include/
PATH_LIBAUDIO=./sysprog-audio-1.5/

CFLAGS=-g -Wall -Wpedantic -Wextra
LDFLAGS=-I $(PATH_INCLUDE) -I $(PATH_LIBAUDIO)

all: $(PATH_BINARY)lecteur $(PATH_BINARY)audioserver $(PATH_BINARY)audioclient

$(PATH_BINARY)lecteur: $(PATH_SOURCE)lecteur.c $(PATH_LIBAUDIO)audio.c
	$(CC) $(CFLAGS) $(LDFLAGS) $^ -o $@


$(PATH_BINARY)audioserver: $(PATH_SOURCE)audioserver.c $(PATH_SOURCE)trame.c $(PATH_LIBAUDIO)audio.c $(PATH_SOURCE)folder.c
	$(CC) $(CFLAGS) $(LDFLAGS) $^ -o $@


$(PATH_BINARY)audioclient: $(PATH_SOURCE)audioclient.c $(PATH_SOURCE)trame.c $(PATH_LIBAUDIO)audio.c $(PATH_SOURCE)folder.c
	$(CC) $(CFLAGS) $(LDFLAGS) $^ -o $@

lecteur:
	padsp $(PATH_BINARY)lecteur ${ARGS}

server:
	padsp $(PATH_BINARY)audioserver ${ARGS}


client:
	padsp $(PATH_BINARY)audioclient ${ARGS}

valClient:
#padsp valgrind --track-origins=yes --leak-check=full --show-leak-kinds=all $(PATH_BINARY)audioclient
	padsp valgrind --leak-check=full $(PATH_BINARY)audioclient ${ARGS}

valServer:
	padsp valgrind --track-origins=yes --leak-check=full --show-leak-kinds=all $(PATH_BINARY)audioserver
#padsp valgrind --leak-check=full $(PATH_BINARY)audioserver ${ARGS}


clean:
	rm -rf $(PATH_BINARY)*
