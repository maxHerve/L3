/**
 * Authors :
 * 	- Maxime Hervé
 * 	- Donatien Sattler
 */

#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "functions.h" // global functions declarations

// ------------------------------------------------------------------------
// inner functions declarations
// ------------------------------------------------------------------------

void free_word_list(word_list* wordList);
void free_word_list_array(word_list* wordListArray);

//------------------------------------------------------------------------
// global functions definitions
//------------------------------------------------------------------------

/**
 * create and initialize hash table
 * returns : pointer to table or NULL if creation error
 */
hash_table * create_table(){
	hash_table * table = malloc(sizeof(hash_table));

	(table->hsize) = 0;
	table->htable = malloc((MAX_ENTRIES+1) * sizeof(word_list));

	int i=0;
	for(i=0 ; i<=MAX_ENTRIES ; i++){
		(table->htable+i)->first_word = NULL;
		(table->htable+i)->last_word = NULL;
	}

	return table;
}

/**
 *   search a word in table ; print word if found, with number of occurrences
 *     and file where word is found
 *
 *     parameters :
 *     word : the word to look for
 *     filelist   : pointer to table of files
 *     htable_ptr : pointer to hash table
 *
 *     returns : true if found, false otherwise
 *     */
int search_word(char words[], listfile_entry * filelist, hash_table * htable_ptr)
{
	int hash = hashcode(words, strlen(words));
	bool found = false;
	printf("\nHash of the word \'%s\' is : %d\n",words, hash);

	word_list *wl = htable_ptr->htable + hash;

	word_entry *current = wl -> first_word;

	while(current != NULL)
	{
		if(strcmp(current -> word, words) == 0)
		{
			int nbOccur = current -> times;
			listfile_entry *le = filelist + (current -> in_file);
			printf("\t%d times in %s\n", nbOccur, le->filename );

			found = true;
		}
		current = current->next;
	}

	if(found){
		return 1;
	}

	return 0;
}

/**
 *   print table contents
 *
 *   parameters :
 *   htable_ptr : pointer to hash table
 *   filelist   : pointer to table of files
 *   */
void print_table(hash_table * htable_ptr, listfile_entry * filelist)
{
	for(int i=0 ; i<MAX_ENTRIES ; i++){
		word_list *wordList = htable_ptr->htable+i;
		if(wordList != NULL){
			if(wordList->first_word != NULL){
				printf("%3d : { ", i);
				word_entry *currentWordEntry = wordList->first_word;
				bool first = true;
				while(currentWordEntry != NULL){
					if(!first){
						printf(", ");
					}
					printf("(%s, %d, %d)", currentWordEntry->word, currentWordEntry->in_file, currentWordEntry->times);
					currentWordEntry = currentWordEntry->next;
					first = !first;
				}
				printf(" }\n");
			}
		}
	}
}

void free_table(hash_table *htable_ptr){
	free_word_list_array(htable_ptr->htable);
	
	free(htable_ptr->htable);
	htable_ptr->htable = NULL;
	free(htable_ptr);
}

// ------------------------------------------------------------------------
// inner functions definitions
// ------------------------------------------------------------------------

void free_word_list(word_list* wordList){
	word_entry* currentEntry = wordList->first_word;

	while(currentEntry != NULL){
		word_entry* oldEntry = currentEntry;
		currentEntry = currentEntry->next;
		free(oldEntry);
	}

	wordList->first_word = NULL;
	wordList->last_word = NULL;
}

void free_word_list_array(word_list* wordListArray){
	int i=0;
	for(i=0 ; i<=MAX_ENTRIES ; i++){
		free_word_list(wordListArray+i);
	}
}
