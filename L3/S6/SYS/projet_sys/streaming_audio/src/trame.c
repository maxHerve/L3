#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include "trame.h"

#define UNUSED(x) (void)(x)
/*
   int main(){
   struct PaquetControl    *paquetControl    = create_paquet_control();
   struct PaquetFile       *paquetFile       = create_paquet_file();
   struct PaquetSyncClient *paquetSyncClient = create_paquet_sync_client();
   struct PaquetAudio      *paquetAudio      = create_paquet_audio(12);

   set_type_of(paquetControl, P_ACK_CLI);
   print_paquet_type(paquetControl);
   set_type_of(paquetControl, P_LIST_FILE);
   print_paquet_type(paquetControl);
   set_type_of(paquetControl, P_FILE_REQ);
   print_paquet_type(paquetControl);
   set_type_of(paquetControl, P_ACK_SERV);
   print_paquet_type(paquetControl);
   set_type_of(paquetControl, P_SYNC);
   print_paquet_type(paquetControl);
   set_type_of(paquetControl, P_AUDIO);
   print_paquet_type(paquetControl);
   set_type_of(paquetControl, P_FILENAME);
   print_paquet_type(paquetControl);

   free(paquetControl);
   free(paquetFile);
   free(paquetSyncClient);
   free_paquet_audio(paquetAudio);
   return 0;
   }
 */

void init_paquet_control(struct PaquetControl *paquet){
	memset(paquet, '\0', sizeof(struct PaquetControl));
}

void init_paquet_file(struct PaquetFile *paquet){
	memset(paquet, '\0', sizeof(struct PaquetFile));
}

void init_paquet_sync_client(struct PaquetSyncClient *paquet){
	memset(paquet, '\0', sizeof(struct PaquetSyncClient));
}

void init_paquet_audio(struct PaquetAudio *paquet, unsigned int sample_byte_size){
	printf("%ld\n", (sizeof(char) * sample_byte_size));
	memset(paquet, '\0', sizeof(struct PaquetAudio));
	paquet->size = sample_byte_size;
	paquet->sample = malloc(sizeof(char) * sample_byte_size);

	if(paquet->sample == NULL){
		fprintf(stderr, "Error allocating sample of audio paquet !");
	}
}

struct PaquetControl *create_paquet_control(){
	struct PaquetControl* paquet = (struct PaquetControl *) malloc(sizeof(struct PaquetControl));
	if(paquet == NULL){
		perror("Error while initialising paquet control");
	}
	init_paquet_control(paquet);
	return paquet;
}

struct PaquetFile *create_paquet_file(){
	struct PaquetFile* paquet = (struct PaquetFile *) malloc(sizeof(struct PaquetFile));
	if(paquet == NULL){
		perror("Error while initialising paquet control");
	}
	init_paquet_file(paquet);
	return paquet;
}

struct PaquetSyncClient *create_paquet_sync_client(){
	struct PaquetSyncClient* paquet = (struct PaquetSyncClient *) malloc(sizeof(struct PaquetSyncClient));
	if(paquet == NULL){
		perror("Error while initialising paquet control");
	}
	init_paquet_sync_client(paquet);
	return paquet;
}

struct PaquetAudio *create_paquet_audio(unsigned int sample_byte_size){
	struct PaquetAudio* paquet = (struct PaquetAudio *) malloc(sizeof(struct PaquetAudio));
	if(paquet == NULL){
		perror("Error while initialising paquet control");
		exit(EXIT_FAILURE);
	}
	init_paquet_audio(paquet, sample_byte_size);

	if(paquet->sample == NULL){
		perror("Error while initializing paquet sample size");
		exit(EXIT_FAILURE);
	}
	return paquet;
}

void free_paquet_audio(struct PaquetAudio *paquet){
	free(paquet->sample);
	free(paquet);
}

enum TypePaquet get_type_of(void *paquet){
	uint8_t type = *(uint8_t *)paquet;
	return (enum TypePaquet)type;
}

enum StructPaquet get_struct_of(enum TypePaquet type){
	enum StructPaquet result;
	switch(type){
		case P_ACK_CLI:
			result = STRUCT_P_CONTROL;
			break;
		case P_LIST_FILE:
			result = STRUCT_P_CONTROL;
			break;
		case P_FILE_REQ:
			result = STRUCT_P_FILE;
			break;
		case P_ACK_SERV:
			result = STRUCT_P_CONTROL;
			break;
		case P_SYNC:
			result = STRUCT_P_SYNC;
			break;
		case P_AUDIO:
			result = STRUCT_P_AUDIO;
			break;
		case P_FILENAME:
			result = STRUCT_P_FILE;
			break;
		case P_AUDIO_END:
			result = STRUCT_P_CONTROL;
			break;
		case P_FILENAME_END:
			result = STRUCT_P_CONTROL;
			break;
		case P_CONNECTION_START:
			result = STRUCT_P_CONTROL;
			break;
		case P_CONNECTION_STOP:
			result = STRUCT_P_CONTROL;
			break;
	}

	return result;
}

void set_type_of(struct PaquetControl *paquet, enum TypePaquet type){
	paquet->type = (enum TypePaquet) type;
}

void print_paquet_type(void *paquet){
	enum TypePaquet type = get_type_of(paquet);
	switch(type){
		case P_ACK_CLI:
			printf("Le paquet est une trame d'acquittement(client)\n");
			break;
		case P_LIST_FILE:
			printf("Le paquet est une demande de liste de noms de fichiers (client)\n");
			break;
		case P_FILE_REQ:
			printf("Le paquet est une requête de fichier (client)\n");
			break;
		case P_ACK_SERV:
			printf("Le paquet est une trame d'acquittement (serveur)\n");
			break;
		case P_SYNC:
			printf("Le paquet est une trame qui sert à synchroniser l'audio entre le client et le serveur (serveur)\n");
			break;
		case P_AUDIO:
			printf("Le paquet est une trame audio (serveur)\n");
			break;
		case P_FILENAME:
			printf("Le paquet est une trame contenant le nom d'un fichier (serveur)\n");
			break;
		case P_AUDIO_END:
			printf("Le paquet est une trame indiquant la fin de la piste audio\n");
			break;
		case P_FILENAME_END:
			printf("Le paquet est une fin d'envoie de nom de fichiers\n");
			break;
		case P_CONNECTION_START:
			printf("Le paquet est une demande de début de connection\n");
			break;
		case P_CONNECTION_STOP:
			printf("Le paquet est une demande de fin de connection\n");
			break;
		default:
			fprintf(stderr, "Type de paquet inconnu !\n");
			exit(EXIT_FAILURE);
			break;
	}
}

char *audio_paquet_to_buffer(struct PaquetAudio *paquet){
	int size = sizeof(uint8_t) + 2 * sizeof(uint32_t) + sizeof(uint16_t) + paquet->size;
	char *buffer = malloc(size);
	memset(buffer, '\0', size);

	int currentSize = 0;
	memcpy(buffer + currentSize, &paquet->control.type, sizeof(uint8_t));
	//printf("Champ 1 : OK : %d\n", currentSize);
	currentSize += sizeof(uint8_t);
	memcpy(buffer + currentSize, &paquet->control.number, sizeof(uint32_t));
	//printf("Champ 2 : OK : %d\n", currentSize);
	currentSize += sizeof(uint32_t);
	memcpy(buffer + currentSize, &paquet->control.ack, sizeof(uint32_t));
	//printf("Champ 3 : OK : %d\n", currentSize);
	currentSize += sizeof(uint32_t);
	memcpy(buffer + currentSize, &paquet->size, sizeof(uint16_t));
	//printf("Champ 4 : OK : %d\n", currentSize);
	currentSize += sizeof(uint16_t);
	memcpy(buffer + currentSize, paquet->sample, paquet->size);
	//printf("Champ 5 : OK : %d\n", currentSize);
	currentSize += paquet->size;
	//printf("Total   : OK : %d\n", currentSize);

	//memcpy(buffer, paquet, sizeof(struct PaquetAudio));
	//memcpy(buffer + sizeof(struct PaquetAudio) - sizeof(char *), paquet->sample, paquet->size);

	return buffer;
}

char *control_paquet_to_buffer(struct PaquetControl *paquet){
	char *buffer = malloc(CONTROL_PAQUET_SIZE_SENT);

	int currentSize = 0;
	memcpy(buffer + currentSize, &paquet->type, sizeof(uint8_t));
	//printf("Champ 1 : OK : %d\n", currentSize);
	currentSize += sizeof(uint8_t);
	memcpy(buffer + currentSize, &paquet->number, sizeof(uint32_t));
	//printf("Champ 2 : OK : %d\n", currentSize);
	currentSize += sizeof(uint32_t);
	memcpy(buffer + currentSize, &paquet->ack, sizeof(uint32_t));
	//printf("Champ 3 : OK : %d\n", currentSize);
	currentSize += sizeof(uint32_t);
	//printf("Total   : OK : %d\n", currentSize);

	return buffer;
}

struct PaquetControl *buffer_to_control_paquet(char *buffer){
	struct PaquetControl *paquet = malloc(sizeof(struct PaquetControl));
	memset(paquet, '\0', sizeof(struct PaquetControl));
	int currentSize = 0;

	memcpy(&paquet->type, buffer + currentSize, sizeof(uint8_t));
	//printf("Champ 1 : OK : %d\n", currentSize);
	currentSize += sizeof(uint8_t);
	memcpy(&paquet->number, buffer + currentSize, sizeof(uint32_t));
	//printf("Champ 2 : OK : %d\n", currentSize);
	currentSize += sizeof(uint32_t);
	memcpy(&paquet->ack, buffer + currentSize, sizeof(uint32_t));
	//printf("Champ 3 : OK : %d\n", currentSize);
	currentSize += sizeof(uint32_t);
	//printf("Total   : OK : %d\n", currentSize);

	return paquet;
}

struct PaquetFile *buffer_to_file_paquet(char *buffer){
	struct PaquetFile* paquet = malloc(sizeof(struct PaquetFile));

	int currentSize = 0;
	struct PaquetControl *controlPaquet = buffer_to_control_paquet(buffer);
	memcpy(&paquet->control, controlPaquet, sizeof(struct PaquetControl));
	free(controlPaquet);
	controlPaquet = NULL;

	currentSize += CONTROL_PAQUET_SIZE_SENT;
	if(strlen(buffer + currentSize) < FILE_SIZE){
		strcpy(paquet->file, buffer + currentSize);
		currentSize += FILE_SIZE;
	}
	else{
		fprintf(stderr, "Filename is too long \n");
		exit(EXIT_FAILURE);
	}

	return paquet;
}

char *file_paquet_to_buffer(struct PaquetFile *paquet){
	char *buffer = malloc(FILE_PAQUET_SIZE_SENT);

	char *controlBuffer = control_paquet_to_buffer(&paquet->control);

	int currentSize = 0;
	memcpy(buffer + currentSize, controlBuffer, CONTROL_PAQUET_SIZE_SENT);
	free(controlBuffer);
	controlBuffer = NULL;

	currentSize += CONTROL_PAQUET_SIZE_SENT;
	memcpy(buffer + currentSize, paquet->file, FILE_SIZE);
	currentSize += FILE_SIZE;
	//printf("Total   : OK : %d\n", currentSize);

	return buffer;
}

struct PaquetAudio *buffer_to_audio_paquet(char *buffer){
	struct PaquetAudio *paquet;
	paquet = malloc(sizeof(struct PaquetAudio));

	int currentSize = 0;
	memcpy(&paquet->control.type, buffer + currentSize, sizeof(uint8_t));
	//printf("Champ 1 : OK : %d\n", currentSize);
	currentSize += sizeof(uint8_t);
	memcpy(&paquet->control.number, buffer + currentSize, sizeof(uint32_t));
	//printf("Champ 2 : OK : %d\n", currentSize);
	currentSize += sizeof(uint32_t);
	memcpy(&paquet->control.ack, buffer + currentSize, sizeof(uint32_t));
	//printf("Champ 3 : OK : %d\n", currentSize);
	currentSize += sizeof(uint32_t);


	if(paquet->control.type == P_AUDIO){
		memcpy(&paquet->size, buffer + currentSize, sizeof(uint16_t));
		//printf("Champ 4 : OK : %d\n", currentSize);
		currentSize += sizeof(uint16_t);

		paquet->sample = malloc(paquet->size);

		memcpy(paquet->sample, buffer + currentSize, paquet->size);
		//printf("Champ 5 : OK : %d\n", currentSize);
		currentSize += paquet->size;
		//printf("Total   : OK : %d\n", currentSize);

		/*
		   memcpy(paquet, buffer, sizeof(struct PaquetAudio));
		   int relativeSampleBeginAddr = sizeof(struct PaquetAudio) - sizeof(char *);
		   paquet->sample = malloc(paquet->size);
		   memcpy(paquet->sample, buffer + relativeSampleBeginAddr, paquet->size);
		 */
	}

	return paquet;
}

/**
 * Receives a paquet
 * @param socket the server socket from witch to receive
 * @param type the type of the paquet we expect
 * @param messageSize the size of the paquet we expect
 * @param paquetDest the destination paquet
 * @param addr the address from witch to receive
 * @param len the address length
 * @return 0  -> Everything worked correctly
 1 -> Connection reset by peer
 -1 -> The paquet type didn't match
 -2 -> The paquet size didn't match
 -3 -> Message's length received was less than PaquetControl's size
 -4 -> Parameter message size is less than PaquetControl's size
 */
int receive_paquet(int socket, enum TypePaquet type, int messageSize, void *paquetDest, struct sockaddr_in *addr, socklen_t *len){
	struct sockaddr_in oldAddr = *addr;
	int result = 0;
	if(messageSize < (int)(sizeof(uint8_t) + sizeof(uint32_t) + sizeof(uint32_t))){
		result = -4;
	}
	else{
		char *buffer = malloc(messageSize);

		if(buffer == NULL){
			perror("Error while allocating memory for buffer");
			exit(EXIT_FAILURE);
		}

		//printf("1. Le port du destinataire est : %d\n", ntohs(addr->sin_port));
		//getsockname(socket, (struct sockaddr *) addr, len);
		int sizeReceived = 0;
		do{
			sizeReceived = recvfrom(socket, buffer, messageSize, 0, (struct sockaddr *)addr, len);
		}while(!(oldAddr.sin_family == addr->sin_family && oldAddr.sin_port == addr->sin_port && (oldAddr.sin_addr.s_addr == addr->sin_addr.s_addr) ));
		//printf("2. Le port du destinataire est : %d\n", ntohs(addr->sin_port));

		if(sizeReceived == -1){
			perror("Error while receiving socket");
			exit(EXIT_FAILURE);
		}

		if(sizeReceived == 0){
			result = 1;
		}
		else if(sizeReceived < (int)sizeof(sizeof(uint8_t) + sizeof(uint32_t) + sizeof(uint32_t))){
			result = -3;
		}
		else{
			struct PaquetControl control;
			if(memcpy(&control, buffer, sizeof(struct PaquetControl)) != &control){
				perror("Error while copying buffer to paquet control");
				exit(EXIT_FAILURE);
			}

			if(get_type_of(&control) != type){
				result = -1;
				memcpy(&((struct PaquetControl *)paquetDest)->type, &control.type, sizeof(control.type)); 
			}
			else if(messageSize != sizeReceived){
				result = -2;
			}
		}

		if(result == 0){
			enum StructPaquet structPaquet = get_struct_of(type);
			struct PaquetAudio *audioPaquet = NULL;
			struct PaquetControl *controlPaquet = NULL;
			struct PaquetFile *filePaquet = NULL;
			switch(structPaquet){
				case STRUCT_P_CONTROL:
					controlPaquet = buffer_to_control_paquet(buffer);
					*((struct PaquetControl *)paquetDest) = *controlPaquet;
					free(controlPaquet);
					break;
				case STRUCT_P_FILE:
					filePaquet = buffer_to_file_paquet(buffer);
					*((struct PaquetFile *)paquetDest) = *filePaquet;
					free(filePaquet);
					break;
				case STRUCT_P_SYNC:
					memcpy(paquetDest, buffer, messageSize);
					break;
				case STRUCT_P_AUDIO:
					audioPaquet = buffer_to_audio_paquet(buffer);
					*((struct PaquetAudio *)paquetDest) = *audioPaquet;
					break;
				default:
					fprintf(stderr, "Undefined error occured !\n");
					exit(EXIT_FAILURE);
					break;
			}
			free(audioPaquet);
		}
		free(buffer);
	}
	return result;
}

void err_mng_receive_paquet(int error){
	switch(error){
		case 0:
			break;
		case -1:
			fprintf(stderr, "Paquet type didn't match !");
			exit(EXIT_FAILURE);
			break;
		case -2:
			fprintf(stderr, "Paquet size didn't match !");
			exit(EXIT_FAILURE);
			break;
		case -3:
			fprintf(stderr, "Message was less than minimum size of a paquet !");
			exit(EXIT_FAILURE);
			break;
		case -4:
			fprintf(stderr, "Parameter message size was less than the PaquetControl's minimum size");
			break;
		default:
			fprintf(stderr, "Unexpected error occured !");
			exit(EXIT_FAILURE);
			break;
	}
}
