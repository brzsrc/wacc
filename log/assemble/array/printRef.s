"Printing an array variable gives an address, such as "
r4 in String
freed
r4 in array1
r5 in array2
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
freed
freed
r4 in Ident
freed
	.data


	msg_3:
		.word 5
		.ascii "\0"
	msg_2:
		.word 7
		.ascii "%p\0"
	msg_0:
		.word 56
		.ascii "Printing an array variable gives an address, such as "
	msg_1:
		.word 9
		.ascii "%.*s\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		LDR r0, =16
		BL malloc
		MOV r4, r0
		LDR r6, =1
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =2
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r6, =3
		LDR r5, [r6, ]
		STR r4, [r5, #12]
		LDR r5, =16
		STR r5, [r4, ]
		STR r4, [sp, #0]
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_reference
		BL p_print_ln
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}
	p_print_string:
		PUSH {lr}
		LDR r1, r0
		ADD r2, r0, #4
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_reference:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

