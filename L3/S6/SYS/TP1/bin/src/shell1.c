#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include <stdbool.h>

#define SIZE_COMMAND 50

int main(){
	char command[SIZE_COMMAND] = "";


	while(1){
		printf("$ ");
		fgets(command, SIZE_COMMAND, stdin);
		strtok(command, "\n");

		if(strcmp(command, "exit") == 0){
			exit(EXIT_SUCCESS);
		}

		pid_t forkValue = fork();

		if(forkValue == -1){
			perror("Erreur lors du fork");
			exit(EXIT_FAILURE);
		}
		else if(forkValue == 0){
			// Processus fils
			
			execlp(command, command, NULL);
			perror("Erreur lors de la commande exécutée !");
			exit(EXIT_FAILURE);
		}
		else{
			// Processus père
			int filsStatus = 0;
			
			pid_t retourProcessus = waitpid(-1, &filsStatus, 0);

			if(retourProcessus == -1 || filsStatus == EXIT_FAILURE){
				printf("La commande %s ne s'est pas terminée correctement !\n", command);
				exit(EXIT_FAILURE);
			}
		}
	}
	
	return EXIT_SUCCESS;
}
