#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include "audio.h"

/*
Q1 :
Si on passe une fausse fréquence d'échantillonage, la musique se joue plus rapidement ou plus lentement et la musique est respectivement plus aigu, et plus grave.
Q2 :
Si on passe en mono au lieu de stéréo, les deux enceintes renvoient exactement le même son sur un casque.
Q3 : 
Si on déclare une mauvaise taille d'échantillon, on obtient du bruit au lieu de la musique.
*/

int main(int argc, char* argv[]){
	if(argc == 2){
		char *filename = argv[1];
		printf("Ouverture du fichier audio : %s\n", filename);
		int sample_rate = 0;
		int sample_size = 0;
		int channels = 0;

		int audioFileDescriptor = 0;
		int speakerFileDescriptor = 0;

		if((audioFileDescriptor = aud_readinit(filename, &sample_rate, &sample_size, &channels)) < 0){
			fprintf(stderr, "Error while trying to read audio file descriptor !\n");
			exit(EXIT_FAILURE);
		}

		if((speakerFileDescriptor = aud_writeinit(sample_rate, sample_size, channels)) < 0){
			fprintf(stderr, "Error while setting speakerFileDescriptor parameters !\n");
			exit(EXIT_FAILURE);
		}

		char *buffer = malloc(sample_size);
		int buffer_size = sample_size;

		while(1){
			read(audioFileDescriptor, buffer, buffer_size);
			write(speakerFileDescriptor, buffer, buffer_size);
		}

		free(buffer);
	}
	else{
		fprintf(stderr, "Le nombre de paramètre est invalide !\n");
	}


	return EXIT_SUCCESS;
}
