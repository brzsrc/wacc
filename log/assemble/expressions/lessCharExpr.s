r4 in charNode
freed
r4 in charNode
freed
r4 in charNode
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
	.data


	msg_0:
		.word 9
		.ascii "true\0"
	msg_1:
		.word 10
		.ascii "false\0"
	msg_2:
		.word 5
		.ascii "\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #3
		LDR r4, =39
		STRB r4, [sp, #0]
		LDR r4, =39
		STRB r4, [sp, #1]
		LDR r4, =39
		STRB r4, [sp, #2]
		LDRB r4, [sp, #1]
		LDRB r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		LDRB r4, [sp, #2]
		LDRB r5, [sp, #1]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		ADD sp, sp, #3
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

