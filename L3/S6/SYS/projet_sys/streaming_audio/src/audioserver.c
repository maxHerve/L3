#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <string.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include <dirent.h>
#include <regex.h>
#include "audio.h"
#include "trame.h"
#include "network.h"
#include "folder.h"

#define DEFAULT_PATH "audio_file/"

// TODO Utiliser ntohs ou htons pour convertir tous ce qui est plus grand qu'un otcet
#define CLIENT_NUMBER 4

bool is_valid_audio_filename(char *file);
void traiterClient(int socket_server, struct sockaddr_in clientAddress, socklen_t flen);
int send_file_paquet(int socket, struct PaquetFile *paquetFile, struct sockaddr *addr, socklen_t len);
int send_control_paquet(int socket, struct PaquetControl *paquetControl, struct sockaddr *addr, socklen_t len);

int main(){
	int domain = AF_INET;
	int family = AF_INET;
	int type = SOCK_DGRAM;
	int protocol = 0;

	int socket_server = socket(domain, type, protocol);

	if(socket_server == -1){
		perror("Error while creating server socket");
		exit(EXIT_FAILURE);
	}

	struct sockaddr_in addr;

	addr.sin_family = family;
	addr.sin_port = htons(PORT);
	addr.sin_addr.s_addr = htonl(INADDR_ANY);


	if(bind(socket_server, (struct sockaddr *) &addr, sizeof(struct sockaddr_in)) == -1){
		perror("Error while binding socket");
	}

	struct sockaddr_in *activeClientAddress;
	activeClientAddress = malloc(CLIENT_NUMBER * sizeof(struct sockaddr_in*));
	socklen_t flen;
	struct sockaddr_in clientAddress;
	flen = sizeof(struct sockaddr_in);

	char buffer[BUFFER_SIZE];

	int isChild = false;
	do{
		int size = recvfrom(socket_server, buffer, sizeof(buffer), 0, (struct sockaddr *)&clientAddress, &flen);
		bool forking = false;

		if(size == CONTROL_PAQUET_SIZE_SENT){
			struct PaquetControl *control;
			control = buffer_to_control_paquet(buffer);

			if(control->type == P_CONNECTION_START){
				// Si la connection n'a pas déjà été établie -> fork
				for(int i=0 ; i<CLIENT_NUMBER ; i++){
					struct sockaddr_in *currentAddress = activeClientAddress + i;
					forking = forking || ( currentAddress->sin_family == clientAddress.sin_family && currentAddress->sin_port == clientAddress.sin_port && currentAddress->sin_addr.s_addr == clientAddress.sin_addr.s_addr);
				}
				pid_t processId;

				if((processId = fork()) == (pid_t)-1){
					perror("Error while forking");
					exit(EXIT_FAILURE);
				}
				else if(processId == 0){
					isChild = true;
				}
				else{
					isChild = false;
				}
				printf("BOUH !\n");

				free(control);
			}
		}

	}while(!isChild);
	printf("BAAM !\n");

	struct PaquetControl startAck;
	startAck.type = P_ACK_SERV;
	startAck.number = 0;
	startAck.ack = 0;

	if(send_control_paquet(socket_server, &startAck, (struct sockaddr *)&clientAddress, flen) == -1){
		perror("Error while sending not sync paquet");
		exit(EXIT_FAILURE);
	}

	traiterClient(socket_server, clientAddress, flen);

	if(close(socket_server) == -1){
		perror("Error while closing server socket");
		exit(EXIT_FAILURE);
	}

	return EXIT_SUCCESS;
}

void traiterClient(int socket_server, struct sockaddr_in clientAddress, socklen_t flen){
	char *buffer[BUFFER_SIZE];
	ssize_t receivedMessageSize;

	bool quit = false;

	while(!quit){
		printf("GAAM !\n");
		receivedMessageSize = recvfrom(socket_server, buffer, sizeof(buffer), 0, (struct sockaddr *)&clientAddress, &flen);
		if(receivedMessageSize == -1){
			fprintf(stderr, "Error while receiving message\n");
			exit(EXIT_FAILURE);
		}
		printf("DAAM !\n");

		struct PaquetFile *paquet = (struct PaquetFile*) buffer;

		if(paquet->control.type == P_AUDIO){
			printf("Paquet is audio\n");
		}
		else if(paquet->control.type == P_FILE_REQ){
			printf("Paquet is file request\n");
			int fileDescriptor = -1;
			int sample_rate = 0;
			int sample_size = 0;
			int channel = 0;
			int filenameLength = strlen(DEFAULT_PATH) + strlen(paquet->file) + 1;
			char *filename = malloc(sizeof(char) * (filenameLength));
			memset(filename, '\0', sizeof(filenameLength));

			filename = strcat(filename, DEFAULT_PATH);
			filename = strcat(filename, paquet->file);

			printf("The requested file is : %s\n", filename);

			if((fileDescriptor = aud_readinit(filename, &sample_rate, &sample_size, &channel)) < 0){
				fprintf(stderr, "File '%s' not found on the server !\n", filename);

				// TODO print files

				struct PaquetControl notSyncPaquet;
				notSyncPaquet.type = P_FILENAME;
				notSyncPaquet.number = 0;
				notSyncPaquet.ack = 0;

				if(send_control_paquet(socket_server, &notSyncPaquet, (struct sockaddr *)&clientAddress, flen) == -1){
					perror("Error while sending not sync paquet");
					exit(EXIT_FAILURE);
				}

				int fileNumber = 0;

				char **listFiles = get_filename_from_folder(DEFAULT_PATH, &is_valid_audio_filename, &fileNumber, FILE_SIZE);

				for(int i=0 ; i<fileNumber ; i++){
					struct PaquetFile filePaquet;
					filePaquet.control.type = P_FILENAME;
					filePaquet.control.number = 0;
					filePaquet.control.ack = 0;
					memset(filePaquet.file, '\0', FILE_SIZE);
					strcpy(filePaquet.file, listFiles[i]);

					printf("%s\n", filePaquet.file);

					if(send_file_paquet(socket_server, &filePaquet, (struct sockaddr *)&clientAddress, flen) == -1){
						perror("Error while sending filename paquet");
						exit(EXIT_FAILURE);
					}

					struct PaquetControl paquetAckCli;

					int error = receive_paquet(socket_server, P_ACK_CLI, CONTROL_PAQUET_SIZE_SENT, &paquetAckCli, &clientAddress, &flen);
					err_mng_receive_paquet(error);
				}

				for(int i=0 ; i<fileNumber ; i++){
					free(*(listFiles + i));
				}
				free(listFiles);

				struct PaquetControl endPaquet;
				endPaquet.type = P_FILENAME_END;
				endPaquet.number = 0;
				endPaquet.ack = 0;

				if(send_control_paquet(socket_server, &endPaquet, (struct sockaddr *)&clientAddress, flen) == -1){
					perror("Error while sending filename paquet");
					exit(EXIT_FAILURE);
				}

				free(filename);
				exit(EXIT_FAILURE);
			}


			struct PaquetSyncClient paquetFichierInfo;
			memset(&paquetFichierInfo, '\0', sizeof(struct PaquetSyncClient));

			paquetFichierInfo.control.type = P_SYNC;
			paquetFichierInfo.control.number = 0;
			paquetFichierInfo.control.ack = 0;
			strcat(paquetFichierInfo.file, paquet->file);
			paquetFichierInfo.sample_rate = sample_rate;
			paquetFichierInfo.sample_size = sample_size;
			paquetFichierInfo.channel_number = channel;

			memcpy(buffer, &paquetFichierInfo, sizeof(paquetFichierInfo));

			if(sendto(socket_server, buffer, sizeof(buffer), 0, (struct sockaddr *)&clientAddress, flen) == -1){
				perror("Error while sending informations");
				exit(EXIT_FAILURE);
			}

			bool quit2 = false;
			while(!quit2){
				struct PaquetAudio audioPaquet;
				memset(&audioPaquet, '\0', sizeof(struct PaquetAudio));
				init_paquet_control(&audioPaquet.control);
				audioPaquet.control.type = P_AUDIO;
				audioPaquet.size = sample_size;
				audioPaquet.sample = malloc(sample_size);

				if(audioPaquet.sample == NULL){
					fprintf(stderr, "Error !!!");
					exit(EXIT_FAILURE);
				}

				quit2 = (read(fileDescriptor, audioPaquet.sample, sample_size) <= 0);
				if(!quit2){

					char *buffer1 = audio_paquet_to_buffer(&audioPaquet);
					int buffer2Size = sizeof(uint8_t) + 2 * sizeof(uint32_t) + sizeof(uint16_t) + sample_size;
					//int buffer2Size = sizeof(struct PaquetAudio) - sizeof(char) + sample_size;
					int bytesSent = 0;
					if((bytesSent = sendto(socket_server, buffer1, buffer2Size, 0, (struct sockaddr *) &clientAddress, flen)) == -1){
						perror("Error while sending informations");
						exit(EXIT_FAILURE);
					}

					if(bytesSent != buffer2Size){
						perror("Too few bytes sent");
						exit(EXIT_FAILURE);
					}

					free(buffer1);


					struct PaquetControl paquetAck;

					int error = receive_paquet(socket_server, P_ACK_CLI, CONTROL_PAQUET_SIZE_SENT, &paquetAck, &clientAddress, &flen);
					err_mng_receive_paquet(error);
					/*
					   char buffer3Size = sizeof(sizeof(uint8_t) + sizeof(uint32_t) + sizeof(uint32_t));
					   char *buffer3 = malloc(buffer3Size);

					   receivedMessageSize = recvfrom(socket_server, buffer3, buffer3Size, 0, (struct sockaddr *)&clientAddress, &flen);
					   if(receivedMessageSize == -1){
					   perror("Error while receiving message");
					   exit(EXIT_FAILURE);
					   }

					   if(receivedMessageSize != buffer3Size){
					   fprintf(stderr, "Tooo few bytes sent !");
					   exit(EXIT_FAILURE);
					   }

					   memcpy(&paquetAck, buffer3, sizeof(buffer3Size));
					   free(buffer3);
					 */
				}

				free(audioPaquet.sample);
			}

			printf("Fin de la lecture !\n");

			free(filename);

			struct PaquetControl paquetQuit;
			paquetQuit.type = P_AUDIO_END;
			paquetQuit.number = 0;
			paquetQuit.ack = 0;

			char *bufferAudioQuit = control_paquet_to_buffer(&paquetQuit);
			int sizeSendTo = 0;
			if((sizeSendTo = sendto(socket_server, bufferAudioQuit, sizeof(uint8_t) + sizeof(uint32_t) + sizeof(uint32_t), 0, (struct sockaddr *)&clientAddress, flen)) == -1){
				perror("Error while sending audio stop");
				exit(EXIT_FAILURE);
			}

			if(sizeSendTo != sizeof(uint8_t) + sizeof(uint32_t) + sizeof(uint32_t)){
				perror("Too few bytes sent !");
				exit(EXIT_FAILURE);
			}

			free(bufferAudioQuit);

			quit = true;
		}
		else if(paquet->control.type == P_LIST_FILE){
			printf("Paquet is a list file request\n");
		}
		else{
			printf("Unknown paquet type !\n");
		}
	}
}

bool is_valid_audio_filename(char *file){
	bool result = false;
	regex_t regex;
	int regexMatches = 0;
	char msgBuf[100];

	if(regcomp(&regex, "^\\([[:alnum:]]\\|_\\|-\\)\\{1,90\\}\\.[[:alnum:]]\\{1,8\\}$", 0)){
		fprintf(stderr, "Cannot compile regex !\n");
		exit(EXIT_FAILURE);
	}

	regexMatches = regexec(&regex, file, 0, NULL, 0);

	if(!regexMatches){
		//printf("Filename '%s' match regex !\n", file);
	}
	else if(regexMatches == REG_NOMATCH){
		//printf("Filename '%s' doesn't match regex !\n", file);
		result = true;
	}
	else{
		regerror(regexMatches, &regex, msgBuf, sizeof(msgBuf));
		fprintf(stderr, "Regex match failed: %s\n", msgBuf);
		exit(1);
	}

	regfree(&regex);
	return result;
}

int send_control_paquet(int socket, struct PaquetControl *paquetControl, struct sockaddr *addr, socklen_t len){
	char *buffer = control_paquet_to_buffer(paquetControl);
	int bytesSent = 0;

	if((bytesSent = sendto(socket, buffer, CONTROL_PAQUET_SIZE_SENT, 0, addr, len)) == -1){
		perror("Error while sending informations");
		exit(EXIT_FAILURE);
	}

	free(buffer);

	return bytesSent == CONTROL_PAQUET_SIZE_SENT ? 0 : -1;
}

/**
 * 0 -> Ok
 * -1 -> Invalid number of bytes sent
 */
int send_file_paquet(int socket, struct PaquetFile *paquetFile, struct sockaddr *addr, socklen_t len){
	char *buffer = file_paquet_to_buffer(paquetFile);
	int bytesSent = 0;

	if((bytesSent = sendto(socket, buffer, FILE_PAQUET_SIZE_SENT, 0, addr, len)) == -1){
		perror("Error while sending informations");
		exit(EXIT_FAILURE);
	}

	free(buffer);

	return bytesSent == FILE_PAQUET_SIZE_SENT ? 0 : -1;
}
