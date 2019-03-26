#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include <string.h>
#include "audio.h"
#include "trame.h"
#include "network.h"

#define DEFAULT_FILE "test.wav"

int main(int argc, char *argv[]){
	char file[FILE_SIZE];
	memset(file, '\0', FILE_SIZE);

	if(argc == 1){
		printf("Utilisation du fichier par défaut '%s'\n", DEFAULT_FILE);
		printf("Vous pouvez spécifier un autre fichier grâce à l'argument 1 !");
		memcpy(file, DEFAULT_FILE, strlen(DEFAULT_FILE));
	}
	else if(argc == 2){
		// TODO Vérifier que le fichier passé en ligne de commande ne contient aucun caractère bizzare
		memcpy(file, argv[1], strlen(argv[1]));
		printf("Utilisation du fichier '%s'\n", file);
	}
	else{
		fprintf(stderr, "Trop de paramètres !\n");
		fprintf(stderr, "Utilisation :\n");
		fprintf(stderr, "\t%s [FICHIER]\n", argv[0]);
		exit(EXIT_FAILURE);
	}

	int domain = AF_INET;
	int family = AF_INET;
	int type = SOCK_DGRAM;
	int protocol = 0;
	socklen_t flen;

	flen = sizeof(struct sockaddr_in);
	int socket_server = socket(domain, type, protocol);

	if(socket_server == -1){
		perror("Error while creating server socket");
		exit(EXIT_FAILURE);
	}


	struct sockaddr_in addr;
	char *serverAddress = "127.0.0.1";

	addr.sin_family = family;
	addr.sin_port = htons(PORT);
	addr.sin_addr.s_addr = inet_addr(serverAddress);

	int byteSent = 0;	

	struct PaquetControl paquetStart;
	paquetStart.type = P_CONNECTION_START;
	paquetStart.number = 0;
	paquetStart.ack = 0;
	if((byteSent = sendto(socket_server, &paquetStart, CONTROL_PAQUET_SIZE_SENT, 0, (struct sockaddr *)&addr, flen)) == -1){
		perror("Error while sending informations");
		exit(EXIT_FAILURE);
	}

	if(byteSent != CONTROL_PAQUET_SIZE_SENT){
		fprintf(stderr, "Too few bytes sent !");
		exit(EXIT_FAILURE);
	}

	printf("START\n");
	struct PaquetControl paquetStartAck;
	int error = receive_paquet(socket_server, P_ACK_SERV, CONTROL_PAQUET_SIZE_SENT, &paquetStartAck, &addr, &flen);

	printf("ACK\n");

	err_mng_receive_paquet(error);

	struct PaquetFile paquetFileRequest;
	init_paquet_file(&paquetFileRequest);
	paquetFileRequest.control.type = P_FILE_REQ;
	paquetFileRequest.control.number = 0;
	paquetFileRequest.control.ack = 0;
	strcpy(paquetFileRequest.file, file);

	printf("FILE\n");

	if((byteSent = sendto(socket_server, &paquetFileRequest, sizeof(paquetFileRequest), 0, (struct sockaddr *)&addr, flen)) == -1){
		perror("Error while sending informations");
		exit(EXIT_FAILURE);
	}

	if(byteSent != sizeof(paquetFileRequest)){
		fprintf(stderr, "Too few bytes sent !");
		exit(EXIT_FAILURE);
	}
	else{
		struct PaquetSyncClient paquet;
		int error = receive_paquet(socket_server, P_SYNC, sizeof(struct PaquetSyncClient), &paquet, &addr, &flen);
			
		bool quit = false;

		if(paquet.control.type == P_FILENAME){
			fprintf(stderr, "Le fichier '%s' n'est pas disponible sur le serveur !\n", file);
			fprintf(stderr, "Les fichiers audio suivants sont disponibles :\n");

			while(!quit){
				struct PaquetFile filePaquet;
				error = receive_paquet(socket_server, P_FILENAME, FILE_PAQUET_SIZE_SENT, &filePaquet, &addr, &flen);

				if(error == -1){
					if(filePaquet.control.type == P_FILENAME_END){
						quit = true;
					}
					else{
						err_mng_receive_paquet(error);
					}
				}
				else{
					err_mng_receive_paquet(error);
				}

				if(!quit){

					printf("\t* %s\n", filePaquet.file);

					struct PaquetControl paquetAckFile;
					paquetAckFile.type = P_ACK_CLI;
					paquetAckFile.number = 0;
					paquetAckFile.ack = 0;

					char *bufferAckList = NULL;
					bufferAckList = control_paquet_to_buffer(&paquetAckFile);
					if((sendto(socket_server, bufferAckList, CONTROL_PAQUET_SIZE_SENT, 0, (struct sockaddr *)&addr, flen)) == -1){
						fprintf(stderr, "Error while sending ack");
					}

					free(bufferAckList);
				}
			}

			quit = true;
		}
		else{
			err_mng_receive_paquet(error);
		}


		if(!quit){
			int speakerFileDescriptor = 0;

			if((speakerFileDescriptor = aud_writeinit(paquet.sample_rate, paquet.sample_size, paquet.channel_number)) < 0){
				fprintf(stderr, "Error while setting speakerFileDescriptor parameters !\n");
				exit(EXIT_FAILURE);
			}

			while(!quit){

				struct PaquetAudio paquetAudio;

				int paquetAudioSize = sizeof(uint8_t) + 2 * sizeof(uint32_t) + sizeof(uint16_t) + paquet.sample_size;
				error = receive_paquet(socket_server, P_AUDIO, paquetAudioSize, &paquetAudio, &addr, &flen);

				if(error == -1){
					if(paquetAudio.control.type == P_AUDIO_END){
						quit = true;
					}
				}

				if(!quit){
					err_mng_receive_paquet(error);

					write(speakerFileDescriptor, paquetAudio.sample, paquetAudio.size);
					struct PaquetControl paquetAck;
					/*
					   paquetAck.type = P_ACK_CLI;
					   paquetAck.number = paquetAudio.control.number;
					   paquetAck.ack = paquetAudio.control.ack;
					 */

					paquetAck.type = P_ACK_CLI;
					paquetAck.number = 0;
					paquetAck.ack = 0;

					int byteSent1 = 0;
					char *buffer = control_paquet_to_buffer(&paquetAck);
					int size = sizeof(uint8_t) + sizeof(uint32_t) + sizeof(uint32_t);

					if((byteSent1 = sendto(socket_server, buffer, size, 0, (struct sockaddr *)&addr, flen)) == -1){
						perror("Error while paquet ack");
						exit(EXIT_FAILURE);
					}

					if(byteSent != sizeof(paquetFileRequest)){
						fprintf(stderr, "Too few bytes sent !");
						exit(EXIT_FAILURE);
					}

					free(buffer);
				}
				if(error == 0){
					free(paquetAudio.sample);
				}
			}

			if(close(speakerFileDescriptor) == -1){
				perror("Error while closing speak file descriptor");
				exit(EXIT_FAILURE);
			}
		}
	}



	if(close(socket_server) == -1){
		perror("Error while closing server socket");
		exit(EXIT_FAILURE);
	}

	return EXIT_SUCCESS;
}
