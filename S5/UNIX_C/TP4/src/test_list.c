/**************************************************************************
 * L3Informatique						C/Unix
 * 			TP linked lists
 *
 * Group  : 1.2
 * Name 1 : Maxime Hervé
 * Name 2 : Donatien Sattler
 *
 **************************************************************************/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <termios.h>
#include <unistd.h>
#include "list.h"

/* Compute the number of elements of the list */
	int
list_size(list_elem_t * p_list)
{
	int nb = 0;
	while (p_list != NULL) {
		nb += 1;
		p_list = p_list->next;
	}
	return nb;
}

/* Print the elements of the list */
void
print_list(list_elem_t* p_list) {
	list_elem_t * pl = p_list;
	printf("The list contains %d element(s)\n", list_size(p_list));
	while(pl != NULL) {
		printf("[%d]",pl->value);
		pl = pl->next;
		if (pl != NULL) {
			printf("->");
		}
	}
}

/* Compute the number of memory allocations */

	int
main(int argc, char **argv)
{
	list_elem_t * la_liste = NULL;	// The pointer to the head of the list
	char menu[] =
		"\n Program of chained list \n"\
		"  'h/t' : Insert an element to the head/tail of the list\n"\
		"  'f'   : search of a list element\n"\
		"  's'   : suppression of a list element\n"\
		"  'r'   : reverse the order of the list elements\n"\
		"  'x'   : exit the program\n"\
		" What is your choice ? ";
	int choice=0;				// choice from the menu
	int value=0;				// inserted value

	printf("%s",menu);
	fflush(stdout);

	while(1) {
		fflush(stdin);
		choice = getchar();
		printf("\n");

		switch(choice) {
			case 'H' :
			case 'h' :
				printf("Value of the new element ? ");
				scanf("%d",&value);
				printf("\n%d\n",value);
				if (insert_head(&la_liste,value)!=0) {
					printf("Error : impossible to add the element %d\n",value);
				};
				break;

			case 'T' :
			case 't' :
				printf("Value of the new element ? ");
				scanf("%d",&value);
				printf("\n%d\n",value);
				if (insert_tail(&la_liste,value)!=0) {
					printf("Error : impossible to add the element %d to the end of the list\n",value);
				};

				break;


			case 'F' :
			case 'f' :
				printf("Index of the element to search ? ");
				scanf("%d",&value);
				printf("\n%d\n",value);
				list_elem_t * elem = find_element(la_liste,value);
				if (elem != NULL) {
					printf("The element %d is in the list at the index %d.\n", elem->value, value);
				}
				else{
					printf("No element found !!!\n");
				}

				break;


			case 's' :
			case 'S' :
				printf("Value of the element to remove :");
				scanf("%d",&value);
				printf("\n%d\n",value);
				
				if(remove_element(&la_liste,value) == 0){
					printf("The element was deleted successfully.\n");
				}
				else{
					printf("No element to remove.\n");
				}

				break;
			case 'r' :
			case 'R' :
				printf("Current list is :\n");
				print_list(la_liste);
				printf("\nReversed list is :\n");
				reverse_list(&la_liste);
				break;


			case 'x' :
			case 'X' :
				printf("L'adresse de la liste est : %p\n",la_liste);
				freeAll(&la_liste);
				printf("L'adresse de la liste est : %p\n",la_liste);
				return 0;

			default:
				break;
		}
		print_list(la_liste);

		getchar(); // to consume a return character and avoid double display of the menu
		printf("%s\n",menu);
	}
	freeAll(&la_liste);
	return 0;
}
