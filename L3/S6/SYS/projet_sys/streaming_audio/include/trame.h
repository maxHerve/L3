#ifndef TRAME_H
#define TRAME_H

#include <stdint.h>
#include <netinet/in.h>

/* FONCTIONS : 
   	CLIENT  : acquittement (client), lister fichiers, requete fichier
	SERVEUR : acquittement (serveur), synchronisation client, envoie audio, fin audio
 */

/*
 TYPE (1 octet) :
 	0  -> acquittement (client)        : PaquetControl
	1  -> lister fichiers (client)     : PaquetControl
	2  -> requete fichiers (client)    : PaquetFile
	3  -> acquittement (serveur)       : PaquetControl
	4  -> synchronisation (serveur)    : PaquetSyncClient
	5  -> envoie audio (serveur)       : PaquetAudio
	6  -> envoie fichier (serveur)     : PaquetFile
	7  -> fin audio (serveur)          : PaquetControl
	8  -> fin envoie fichier (serveur) : PaquetControl
	9  -> début communication (client) : PaquetControl
	10 -> fin communication (serveur)  : PaquetControl
 */

#define FILE_SIZE 100

enum StructPaquet { STRUCT_P_CONTROL, STRUCT_P_FILE, STRUCT_P_SYNC, STRUCT_P_AUDIO };
enum TypePaquet { P_ACK_CLI = 0, P_LIST_FILE = 1, P_FILE_REQ = 2, P_ACK_SERV = 3, P_SYNC = 4, P_AUDIO = 5, P_FILENAME = 6, P_AUDIO_END = 7, P_FILENAME_END = 8, P_CONNECTION_START = 9, P_CONNECTION_STOP = 10 };

struct PaquetControl *create_paquet_control();
struct PaquetFile *create_paquet_file();
struct PaquetSyncClient *create_paquet_sync_client();
struct PaquetAudio *create_paquet_audio(unsigned int sample_byte_size);

void free_paquet_audio(struct PaquetAudio *paquet);

enum TypePaquet get_type_of(void *paquet);
enum StructPaquet get_struct_of(enum TypePaquet type);
void set_type_of(struct PaquetControl *paquet, enum TypePaquet type);
void print_paquet_type(void *paquet);
char *audio_paquet_to_buffer(struct PaquetAudio *paquet);
char *control_paquet_to_buffer(struct PaquetControl *paquet);
struct PaquetAudio *buffer_to_audio_paquet(char *buffer);
char *file_paquet_to_buffer(struct PaquetFile *paquet);
struct PaquetControl *buffer_to_control_paquet(char *buffer);
struct PaquetFile *buffer_to_file_paquet(char *buffer);

void init_paquet_control(struct PaquetControl *paquet);
void init_paquet_file(struct PaquetFile *paquet);
void init_paquet_sync_client(struct PaquetSyncClient *paquet);
void init_paquet_audio(struct PaquetAudio *paquet, unsigned int sample_byte_size);

int receive_paquet(int socket, enum TypePaquet type, int messageSize, void *paquetDest, struct sockaddr_in *addr, socklen_t *len);
void err_mng_receive_paquet(int error);

#define CONTROL_PAQUET_SIZE_SENT (sizeof(uint8_t) + sizeof(uint32_t) + sizeof(uint32_t))

struct PaquetControl{
	uint8_t type;
	uint32_t number;
	uint32_t ack;
};

#define FILE_PAQUET_SIZE_SENT (CONTROL_PAQUET_SIZE_SENT + FILE_SIZE) 

struct PaquetFile{
	struct PaquetControl control;
	char file[FILE_SIZE];
};

struct PaquetSyncClient{
	struct  PaquetControl control;
	char file[FILE_SIZE];
	uint16_t sample_rate;
	uint16_t sample_size;
	uint16_t channel_number;
};

struct PaquetAudio{
	struct PaquetControl control;
	uint16_t size;
	char *sample;
};

#endif // TRAME_H
