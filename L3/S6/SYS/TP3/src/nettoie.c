#include <stdio.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>
#include <strings.h>
#include <sys/sem.h>

#define TAILLE 1024

int main(){
	int id = 0;
	int semaphore = 0;

	id = shmget((key_t)1234,TAILLE+sizeof(int),0600|IPC_CREAT);
	if(id < 0){
		perror("Error shmget");
		exit(EXIT_FAILURE);
	}

	if(shmctl(id, IPC_RMID, NULL) == -1){
		perror("Erreur lors de la suppression du segment de mémoire partagée\n");
	}
	else
	{
		printf("Le segment de mémoire partagée ayant l'id %d a été détruit avec succès !\n", id);
	}

	semaphore = semget((key_t)1235, 1, 0600|IPC_CREAT);

	if(semaphore == -1){
		perror("Erreur lors de la création du sémaphore !");
		exit(EXIT_FAILURE);
	}

	if(semctl(semaphore, 0, IPC_RMID) == -1){
		perror("Erreur lors de la suppression du sémaphore !");
		exit(EXIT_FAILURE);
	}
}
