r4 in bool
freed
r4 in bool
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in bool
r5 in Ident
freed
freed
r4 in bool
r5 in Ident
freed
freed
	.data


	msg_2:
		.word 5
		.ascii "\0"
	msg_1:
		.word 10
		.ascii "false\0"
	msg_0:
		.word 9
		.ascii "true\0"
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
		AND r5, r5, r4
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		MOV r4, #1
		LDRB r5, [sp, #0]
		AND r5, r5, r4
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		MOV r4, #0
		LDRB r5, [sp, #1]
		AND r5, r5, r4
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		ADD sp, sp, #2
		LDR r0, =0
		POP {pc}
	p_print_bool:
		PUSH {lr}
		CMP r0, #0
		LDRNE r0, =msg_0
		LDREQ r0, =msg_1
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

