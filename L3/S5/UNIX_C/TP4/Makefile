CFLAGS=-g -ggdb -g3 -Wall
INCLUDE_DIR=include/
CC=gcc
#
# Link
#

bin/test_list: obj/list.o obj/test_list.o
	$(CC) $(CFLAGS) -O0 -o $@ obj/*

#
# Objets of TP Lists
#

obj/list.o: src/list.c
	$(CC) $(CFLAGS) -I $(INCLUDE_DIR) -O0 -c -o $@ $<

obj/test_list.o: src/test_list.c
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
	clear ; clear ; clear ; clear ; cat command.txt | valgrind ./bin/test_list
