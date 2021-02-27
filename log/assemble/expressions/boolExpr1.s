r4 in bool
r5 in bool
freed
r5 in bool
r6 in bool
freed
freed
freed
r4 in bool
r5 in Ident
freed
freed
"Wrong"
r4 in String
freed
"Correct"
r4 in String
freed
	.data


	msg_0:
		.word 8
		.ascii "Wrong"
	msg_2:
		.word 5
		.ascii "\0"
	msg_3:
		.word 10
		.ascii "Correct"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #1
		MOV r4, #0
		MOV r5, #1
		AND r5, r5, r4
		MOV r5, #0
		MOV r6, #1
		AND r6, r6, r5
		ORR r5, r5, r4
		EOR r4, r4, #1
		STRB r4, [sp, #0]
		MOV r4, #1
		LDRB r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@3ab39c39
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		B backend.instructions.Label@2a18f23c
	L0:
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
	L1:
		ADD sp, sp, #1
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

