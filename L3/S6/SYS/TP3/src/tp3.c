#include <stdio.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>
#include <strings.h>
#include <sys/sem.h>

#define TAILLE 1024


void ecrire_tableau(int *compteur, char *tableau, int semaphore) {
	char message[64], *msg=message;
	snprintf(message, 64, "Je suis le processus %d!\n", getpid());

	struct sembuf up = {0, 1, 0};
	struct sembuf down = {0, -1, 0};


	// On entre dans la section critique
	semop(semaphore, &down, 1);

	while ((*compteur<TAILLE)&&(*msg)) {
		tableau[*compteur] = *msg;
		msg++;
		usleep(100000);
		(*compteur)++;
	}

	semop(semaphore, &up, 1);
	// On sort de la section critique
	
}

int main() {
	struct sembuf up = {0, 1, 0};
	struct sembuf down = {0, -1, 0};

	int id, *compteur;
	char *tableau;

	int semaphore = 1;

	semaphore = semget((key_t)1235, 1, 0600);
	if(semaphore == -1){
		semaphore = semget((key_t)1235, 1, 0600|IPC_CREAT);
		semop(semaphore, &up, 1);

		if(semaphore == -1){
			perror("Erreur lors de la création du sémaphore !");
			exit(EXIT_FAILURE);
		}
	}

	int semaphoreValue = semctl(semaphore, 0, GETVAL);
	printf("%d\n", semaphoreValue);

	id = shmget((key_t)1234,TAILLE+sizeof(int),0600|IPC_CREAT); // Création d'un segement de mémoire partagée de 1028 octets dont l'id est 1234
	printf("L'id du segment de mémoire partagée est : %d\n", id);
	if (id<0) { perror("Error shmget"); exit(1); }

	compteur = (int*) shmat(id,0,0);                            // Attache à l'espace d'adressage du processus courant le segment de mémoire partagée
	if (compteur==NULL) { perror("Error shmat"); exit(1); }

	tableau = (char *)(compteur + 1);                           // Les variables tableau et compteur pointent vers un une adresse présente dans le segement de mémoire partagée

	ecrire_tableau(compteur, tableau, semaphore);
	printf("%s\n", tableau);

	if (shmdt(compteur)<0) { perror("Error shmdt"); exit(1); }  // Détache le segment de mémoire partagée (sans la détruire)
	return 0;
}

