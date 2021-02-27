r4 in integer
r5 in Ident
freed
freed
r4 in integer
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in funcCall
freed
r4 in Ident
freed
r4 in Ident
freed
", "
r4 in String
freed
r4 in integer
r5 in Ident
freed
freed
r4 in bool
freed
r4 in funcCall
freed
"The first 20 fibonacci numbers are:"
r4 in String
freed
"0, "
r4 in String
freed
r4 in integer
freed
r4 in bool
freed
r4 in funcCall
freed
r4 in Ident
freed
"..."
r4 in String
freed
	.data


	msg_2:
		.word 9
		.ascii "%.*s\0"
	msg_4:
		.word 5
		.ascii "\0"
	msg_0:
		.word 7
		.ascii "%d\0"
	msg_1:
		.word 5
		.ascii ", "
	msg_6:
		.word 6
		.ascii "..."
	msg_3:
		.word 38
		.ascii "The first 20 fibonacci numbers are:"
	msg_5:
		.word 6
		.ascii "0, "
	.text


	.global main
		SUB sp, sp, #8
		LDR r4, =1
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVLE r5, #1
		B backend.instructions.Label@35851384
		B backend.instructions.Label@649d209a
	L0:
	L1:
		LDR r4, =1
		LDR r5, [sp, #0]
		SUB r5, r5, r4
		STR null, 
		LDRB r4, [sp, #4]
		STR null, 
		BL f_fibonacci
		ADD sp, sp, #5
		MOV r0, r4
		STR r4, [sp, #0]
		LDRB r4, [sp, #4]
		B backend.instructions.Label@5a01ccaa
		B backend.instructions.Label@71c7db30
	L2:
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L4
		MOV r0, r4
		BL p_print_string
	L3:
		LDR r4, =2
		LDR r5, [sp, #0]
		SUB r5, r5, r4
		STR null, 
		MOV r4, #0
		STR null, 
		BL f_fibonacci
		ADD sp, sp, #5
		MOV r0, r4
		STR r4, [sp, #4]
		ADD sp, sp, #8
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =L6
		MOV r0, r4
		BL p_print_string
		LDR r4, =19
		STR null, 
		MOV r4, #1
		STR null, 
		BL f_fibonacci
		ADD sp, sp, #5
		MOV r0, r4
		STR r4, [sp, #8]
		LDR r4, [sp, #8]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L7
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}
	p_print_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_string:
		PUSH {lr}
		LDR r1, r0
		ADD r2, r0, #4
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

