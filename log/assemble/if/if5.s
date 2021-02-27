r4 in bool
freed
r4 in bool
freed
r4 in Ident
r5 in Ident
freed
freed
"incorrect"
r4 in String
freed
"correct"
r4 in String
freed
	.data


	msg_0:
		.word 12
		.ascii "incorrect"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_2:
		.word 5
		.ascii "\0"
	msg_3:
		.word 10
		.ascii "correct"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #2
		MOV r4, #1
		STRB r4, [sp, #0]
		MOV r4, #0
		STRB r4, [sp, #1]
		LDRB r4, [sp, #1]
		LDRB r5, [sp, #0]
		ORR r5, r5, r4
		B backend.instructions.Label@17d99928
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		B backend.instructions.Label@1888ff2c
	L0:
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
	L1:
		ADD sp, sp, #2
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
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

