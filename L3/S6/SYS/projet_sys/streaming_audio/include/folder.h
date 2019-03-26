#ifndef FOLDER_H
#define FOLDER_H

char **get_filename_from_folder(char *folder, bool (*excludeFile)(char *), int *fileNumber, int fileLengthMax);

#endif // FOLDER_H
