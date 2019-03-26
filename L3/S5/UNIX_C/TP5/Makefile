CFLAGS=-g -ggdb -g3 -Wall
INCLUDE_DIR=include/
CC=gcc
#
# Link
#

bin/word_search_engine: obj/file.o obj/hash.o obj/main.o
	$(CC) $(CFLAGS) -O0 -o $@ obj/*

#
# Objets of TP Lists
#

obj/file.o: src/file.c
	$(CC) $(CFLAGS) -I $(INCLUDE_DIR) -O0 -c -o $@ $<

obj/hash.o: src/hash.c
	$(CC) $(CFLAGS) -I $(INCLUDE_DIR) -O0 -c -o $@ $<

obj/main.o: src/main.c
	$(CC) $(CFLAGS) -I $(INCLUDE_DIR) -O0 -c -o $@ $<

#
# Listing creation in pdf
#

listing:

#
# Remove files
#

clean :
	/bin/rm -f obj/*
	/bin/rm -f bin/*

#
# Test the functionality 
#

test :
# To be completed
