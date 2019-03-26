      .equ KEYBOARD, 0x8000FF00

	    .data
mess:	.asciz "Hello world from NIOS II processor\n"
	    .align	4
	    .set noat

	    .text
	    .align	4
	    .global	main

main:
	// &mess[0] dans r4
	addi    r4,zero,mess
	call    print_string
	beq     zero,zero,main

print_string:
	// &mess[0] dans r2
	add 	  r2,zero,r4
loop:
	// mess[i] dans r4
	ldb	    r3,(r2)
	// AFFICHE mess[i] à l'écran ???
	stbio   r3,(r2)
	// On quitte lorsque le caractère lu est \0
	beq     r3,zero, eos
	// i++
	addi    r2,r2,1
	// On boucle
	beq     zero,zero,	loop
eos:
	ret

getc:
	movia	  r4,KEYBOARD
polling:
	ldwio	  r5,4(r4)
	beq     r5,zero,polling
	ldwio	  r5,0(r4)
	stwio   r5,0(r4)
	ret

