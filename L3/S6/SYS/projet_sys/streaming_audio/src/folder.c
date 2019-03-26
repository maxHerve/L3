#include <stdlib.h>
#include <stdio.h>
#include <dirent.h>
#include <stdbool.h>
#include <string.h>

char **get_filename_from_folder(char *folder, bool (*excludeFile)(char *), int *fileNumber, int fileLengthMax){
	char **result = NULL;
	(*fileNumber) = 0;
	DIR *d;
	struct dirent *dir;
	d = opendir(folder);
	if (d) {
		while ((dir = readdir(d)) != NULL) {
			if(!(*excludeFile)(dir->d_name)){
				(*fileNumber)++;
				result = realloc(result, (*fileNumber) * sizeof(char *));
				result[(*fileNumber) - 1] = malloc(sizeof(char) * (int)fileLengthMax);
				memset(result[(*fileNumber) - 1], '\0', sizeof(char) * fileLengthMax);
				if(strlen(dir->d_name) >= (unsigned int)fileLengthMax){
					fprintf(stderr, "Filename '%s' is too long !\n", dir->d_name);
					exit(EXIT_FAILURE);
				}
				else{
					strcpy(result[(*fileNumber) - 1], dir->d_name);
					printf("%s:%s\n", result[(*fileNumber) - 1], dir->d_name);
				}
			}
		}
		closedir(d);
	}
	return result;
}
