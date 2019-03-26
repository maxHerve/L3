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
#include "types.h"
#include "functions.h"

//------------------------------------------------------------------------

void emptyStdin();

int main()
{
	// create hash table
	hash_table* hashTable = NULL;
	hashTable = create_table(hashTable);

	// create filelist array
	listfile_entry* fileList = NULL;
	fileList = create_filelist(MAX_FILES);
	// TO BE COMPLETED

	// display menu
	while (1) {
		int nbchoices = 0;
		printf("\nChoisir une action\n");
		printf("%d. Load a file in dictionary\n", ++nbchoices);
		printf("%d. Search a word in dictionary\n", ++nbchoices);
		printf("%d. Remove file from dictionary\n", ++nbchoices);
		printf("\n");
		printf("%d. Print dictionary\n", ++nbchoices);
		printf("%d. Print file list\n", ++nbchoices);
		printf("\n0. Quit\n");
		int choice;
		while (1) {
			printf("Your choice ? ");
			scanf("%d", & choice);
			if (choice >= 0 && choice <= nbchoices) { break; }
			printf("\nError %d is an incorrect choice\n", choice);
		}
		if (choice == 0) { break; }

		printf("-------------------------------------------------\n");

		// TO BE COMPLETED

		int fileStatus = 0;
		char searchFilename[MAX_LENGTH] = "";
		char filename[MAX_LENGTH] = "";

		switch (choice) {

			// Load a file in dictionary
			case 1:
				printf("Wich filename do you want to add ?\n");
				emptyStdin();
				fgets(filename, MAX_LENGTH, stdin);
				filename[strcspn(filename, "\n")] = 0;
				fileStatus = add_file(filename, fileList, hashTable);
				switch(fileStatus){
					case 1:
						printf("The file is already present in the file list\n");
					break;
					case 2:
						printf("No left space in file list\n");
					break;
					case -1:
						fprintf(stderr, "File doesn't exists or can't be read\n");
					break;
					case -2:
						fprintf(stderr, "Allocation error\n");
					break;
					case 0:
						printf("File added successfully\n");
					break;
					default :
						fprintf(stderr, "Weird error happened\n");
					break;
				}


				break;

				// Search a word in dictionary
			case 2:
				printf("Which word do you want to search for ?\n");
				emptyStdin();
				fgets(searchFilename, MAX_LENGTH, stdin);
				searchFilename[strcspn(searchFilename, "\n")] = 0;
				if(search_word(searchFilename, fileList, hashTable) == 0){
					printf("Word not found !\n");
				}
				break;

				// Remove file from dictionary
			case 3:
				printf("Wich filename do you want to remove ?\n");
				emptyStdin();
				fgets(filename, MAX_LENGTH, stdin);
				filename[strcspn(filename, "\n")] = 0;
				remove_file(filename, fileList, hashTable);
				break;

				// Print dictionary
			case 4:
				printf("Dictionary is :\n");
				
				print_table(hashTable,fileList);

				break;

				// Print file list
			case 5:
				printf("List of files present in file list :\n");
				print_list(fileList);
				break;
		}
		printf("-------------------------------------------------\n");

	}

	// the end : free allocated memory
	
	free_table(hashTable);
	free_filelist(fileList);
	
	return 0;
}

// compute hash value for word
// returns : N ; 0 <= N < size
int hashcode(char word[], int size)
{
	int hashCode = 0;
	int i = 0;

	for(i = 0; i < strlen(word) ; i++)
	{
		hashCode += (int)word[i];
	}
	
	return hashCode % 1023;
}

void emptyStdin(){
	int c;
	do{
	    c = getchar();
	}while(c != EOF && c != '\n');
}
