/**
 * Authors :
 * 	- Maxime Hervé
 * 	- Donatien Sattler
 */

#include <ctype.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#include "types.h"
#include "functions.h" // extern functions declarations

// ------------------------------------------------------------------------
// inner functions declarations
// ------------------------------------------------------------------------

void resetWord(char* word, int size);
int addFilename(char* filename, listfile_entry* fileList);
void strtolower(char* str);
void initialiseFileList(listfile_entry *fileList);
void initialiseFileEntry(listfile_entry *fileEntry);
word_entry* searchWordEntry(char* word, word_list* wordList, int fileNumber);
void updateWordEntryTimes(int delta, word_entry* wordEntry);
void remove_file_of_wordList(word_list* wordList, int fileNumber);
int addWordEntry(char* word, int fileNumber, word_list* wordList);
int getFilenameNumber(char filename[], listfile_entry* filelist);
bool readNextWord(FILE *file, char* word, int size);

//------------------------------------------------------------------------
// global functions definitions
//------------------------------------------------------------------------

/**
  Create and initialize file table of capacity maxfiles

parameters :
maxfiles : capacity of file table

returns : pointer to table or NULL in case of error
*/
listfile_entry * create_filelist(int maxfiles)
{
	listfile_entry* fileList = (listfile_entry*) malloc(MAX_FILES * sizeof(listfile_entry));
	initialiseFileList(fileList);

	return fileList; // TODO
}

/**
  add words from file to table
  - checks if the file has already been loaded
  - updates the file table (if file not already loaded)
  - reads every word in the file (idem)
  - updates the hash table (idem)

parameters :
filename   : name of file :)
filelist   : pointer to table of files
htable_ptr : pointer to hash table

returns :
1 if file already present in table
2 if no space left in filelist
-1 if file doesn't exist or can't be read
-2 if allocation error
0 if everything ok
*/
int add_file(char filename[], listfile_entry * filelist, hash_table * htable_ptr)
{
	int res = 0;

	int i=0;
	for(i=0 ; i<MAX_FILES && (res == 0) ; i++){
		if((strcmp((filelist+i)->filename, filename) == 0) && ((filelist+i)->loaded == 1)){
			res = 1;
		}
	}

	if(res != 1){
		FILE* file = fopen(filename, "r");

		if(file == NULL){
			res = -1;
		}
		else{
			int fileNumber = addFilename(filename,filelist);
			if(fileNumber >= 0){
				char word[MAX_LENGTH] = "";
				while(readNextWord(file, word, MAX_LENGTH)){
					if(strlen(word) > 0){
						strtolower(word);
						int index = hashcode(word, MAX_LENGTH);
						word_list* wordList = (htable_ptr->htable + index);
						
						word_entry* wordEntry = searchWordEntry(word, wordList, fileNumber);
						if(wordEntry == NULL){
							// TODO search fileNumber
							addWordEntry(word, fileNumber, wordList);
						}
						else{
							updateWordEntryTimes(1, wordEntry);
						}
					}
				}
			}
			else{
				res = 2;
			}

			fclose(file);
		}

	}
	return res;
}


/**
  remove file from file table

parameters :
filename   : name of file to remove
filelist   : pointer to table of files
htable_ptr : pointer to hash table

returns :
-1 if file not in table
0 if file removed
*/
int remove_file(char filename[], listfile_entry * filelist, hash_table * htable_ptr)
{
	int fileNumber = getFilenameNumber(filename, filelist);

	if(fileNumber != -1){
		(filelist+fileNumber)->loaded = 0;

		for(int i=0 ; i<=MAX_ENTRIES ; i++){
			remove_file_of_wordList(htable_ptr->htable+i, fileNumber);
		}
		return 0;
	}

	return -1;
}

/*
   print file table (only loaded files)

parameters :
filelist : pointer to table of files
*/
void print_list(listfile_entry * filelist)
{
	int i=0;

	for(i=0 ; i<MAX_FILES ; i++){
		if((filelist+i)->loaded){
			printf("\t* %s\n", (filelist+i)->filename);
		}
	}
}

/**
  free file table

parameters :
filelist   : pointer to table of files
*/
void free_filelist(listfile_entry * filelist)
{
	free(filelist);
}

// ************************************************************************
// inner functions
// ************************************************************************

/**
 * Get the index of the first loaded filename in the filelist
 * @param filename a filename
 * @param filelist the list of file
 * @return -1 if not found, the index of the filename in the filelist if found
 */
int getFilenameNumber(char filename[], listfile_entry* filelist){
	int index = 0;
	bool found = false;

	while(!((index >= MAX_FILES) || found)){
		found = (strcmp(filename, (filelist+index)->filename) == 0) && ((filelist+index)->loaded);
		index++;
	}

	if(!found){
		index = -1;
	}
	else{
		index--;
	}

	return index;
}

/**
 * @pre strlen(word) < MAX_LENGTH
 * @pre strlen(filename) < MAX_LENGTH
 */
int addWordEntry(char* word, int fileNumber, word_list* wordList){
	word_entry *newWordEntry = (word_entry *) malloc(sizeof(word_entry));

	strcpy(newWordEntry->word, word);
	newWordEntry->in_file = fileNumber;
	newWordEntry->times = 1;

	if(wordList->first_word == NULL){
		wordList->first_word = newWordEntry;
		wordList->last_word = newWordEntry;
		wordList->first_word->next = NULL;
		//printf("%s\n", wordList->first_word->word);
		// TODO
		//printf("%d\n", wordList->first_word->in_file);
	}
	else{
		word_entry *oldFirstWord = wordList->first_word;
		wordList->first_word = newWordEntry;
		wordList->first_word->next = oldFirstWord;
	}
	return 0;
}

void remove_file_of_wordList(word_list* wordList, int fileNumber){
	word_entry* oldEntry = NULL;
	word_entry* currentEntry = wordList->first_word;

	while(currentEntry != NULL){
		word_entry* nextEntry = currentEntry->next;

		if(currentEntry->in_file == fileNumber){
			if(oldEntry != NULL){
				oldEntry->next = nextEntry;
			}
			else{
				wordList->first_word = nextEntry;
			}
			free(currentEntry);
			currentEntry = nextEntry;
		}
		else{
			oldEntry = currentEntry;
			currentEntry = nextEntry;
		}
	}
}

/**
 * Add delta to field time to the word entry
 * @param delta the number to sum
 * @param wordEntry a word entry
 */
void updateWordEntryTimes(int delta, word_entry* wordEntry){
	wordEntry->times += delta;
}

/**
 * Search the word entry whose word field is equal to word in word list
 * @word the searched word
 * @wordList a word list
 * @return the corresponding word entry if found, NULL otherwise
 */
word_entry* searchWordEntry(char* word, word_list* wordList, int fileNumber){
	word_entry* currentWordEntry = wordList->first_word;

	while(!(currentWordEntry == NULL || (strcmp(currentWordEntry->word, word) == 0 && currentWordEntry->in_file == fileNumber))){
		currentWordEntry = currentWordEntry->next;
	}
	
	return currentWordEntry;
}

void initialiseFileEntry(listfile_entry *fileEntry){
	int i = 0;

	for(i = 0; i < MAX_LENGTH ; i++){
		fileEntry->filename[i] = '\0';
	}

	fileEntry->loaded = false;
}

void initialiseFileList(listfile_entry *fileList){
	int i=0;

	for(i=0 ; i<MAX_FILES ; i++){
		initialiseFileEntry(fileList+i);
	}
}

/**
 * Convert all upper case characters of a string to lower case
 * @param str a string
 */
void strtolower(char* str){
	int i=0;
	for(i=0 ; i<strlen(str) ; i++){
		str[i] = tolower(str[i]);
	}
}

/**
 * Add filename to the filelist and put loaded to true in the corresponding entry
 * @param filename a filename with strlen(filename < MAX_LENGTH)
 * @param fileList the fileList
 * @return the index where the filename was added if it was successfully added, -1 if strlen(filename) >= MAX_LENGTH, -2 otherwise
 * @pre all filename initialised in fileList have a length of MAX_LENGTH
 */
int addFilename(char* filename, listfile_entry* fileList){
	int res = -1;
	if(strlen(filename) < MAX_LENGTH){
		res = -2;

		int fileNumber = 0;
		bool fileEntryFound = false;
		bool fileEntryNonAvailable = false;

		while(!(fileEntryFound || fileEntryNonAvailable)){
			if(fileNumber >= MAX_FILES){
				fileEntryNonAvailable = true;
			}
			else if(!(fileList+fileNumber)->loaded){
				fileEntryFound = true;
				strcpy((fileList+fileNumber)->filename, filename);
				(fileList+fileNumber)->loaded = 1;
				res = fileNumber;
			}
			else{
				fileNumber++;
			}
		}
	}
	return res;
}

/**
 * Emtpy a word
 * @param word the word to empty
 * @param size the maximum length of the word
 */
void resetWord(char* word, int size){
	int i=0;
	for(i=0 ; i<size ; i++){
		word[i] = '\0';
	}
}

/**
 * Read the next word in file and put it into word. If the word read is too long, put the empty string into word.
 * @param file a file
 * @param word the word that will contain the result of this method
 * @param size the maximum length of the word
 * @return false if and only if the current character of file is EOF
 * @pre A word is only constituted of alphabetic characters
 */
bool readNextWord(FILE *file, char* word, int size){
	char currentCharacter = ' ';
	int wordSize = 0;

	while(isalpha(currentCharacter = fgetc(file))){
		if(wordSize < size){
			word[wordSize] = (char)currentCharacter;
		}
		wordSize++;
	}

	if(wordSize > size){
		resetWord(word, size);
	}
	else{
		word[wordSize] = '\0';
	}

	return (currentCharacter != EOF);
}
