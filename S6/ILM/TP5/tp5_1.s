      .equ KEYBOARD, 0x8000FF00
      .equ CONSOLE, 0x80007040

	    .data
mess:	.asciz "Hello world from NIOS II processor\n"
	    .align	4
	    .set noat

	    .text
	    .align	4
	    .global	main

main:
	addi    r4,zero,mess
	call    print_string
	beq     zero,zero,main

print_string:
	subi sp,sp,16
	stw r4,0(sp)
	stw r16,4(sp)
	stw r17,8(sp)
	stw ra,12(sp)

	add 	  r17,zero,r4
loop:
	ldb	r16,(r17)
	addi r4,r16,0
	call putc
	beq     r16,zero, eos
	addi    r17,r17,1
	beq     zero,zero,	loop
eos:

	ldw r4,0(sp)
	ldw r16,4(sp)
	ldw r17,8(sp)
	ldw ra,12(sp)
	addi sp,sp,16

	ret

putc:
	subi sp,sp,12
	stw r4,0(sp)
	stw r16,4(sp)
	stw ra,8(sp)

	movia 	r16,CONSOLE
	stbio   r4,0(r16)

	ldw r4,0(sp)
	ldw r16,4(sp)
	ldw ra,8(sp)
	addi sp,sp,12

	ret


getc:
	movia	  r4,KEYBOARD
polling:
	ldwio	r5,4(r4)
	beq     r5,zero,polling
	ldwio	r5,0(r4)
	stwio   r5,0(r4)
	ret

