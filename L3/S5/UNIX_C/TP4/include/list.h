/**************************************************************************
 * L3Informatique						C/Unix
 * 			TP linked lists
 *
 * Group  : 1.2
 * Name 1 : Maxime Hervé
 * Name 2 : Donatien Sattler
 *
 **************************************************************************/

#ifndef LIST_H
#define LIST_H

/* Structure of an element of a linked list */
typedef struct s_list {
	int value;
	struct s_list* next;
} list_elem_t;

/* Prototypes */
int insert_head(list_elem_t** l, int value);
int insert_tail(list_elem_t** l, int value);
int remove_element(list_elem_t** ppl, int value);
list_elem_t* get_tail(list_elem_t* l);
void reverse_list(list_elem_t** l);
list_elem_t* find_element(list_elem_t* l, int index);
int list_size(list_elem_t* l);
void freeAll(list_elem_t **l);

#endif // LIST_H
