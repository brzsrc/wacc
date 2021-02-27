r4 in charNode
freed
r4 in charNode
r5 in Ident
freed
freed
"Change c"
r4 in String
freed
r4 in charNode
r5 in Ident
freed
freed
"Should print \"Change c\" once before."
r4 in String
freed
	.data


	msg_3:
		.word 41
		.ascii "Should print \"Change c\" once before."
	msg_2:
		.word 5
		.ascii "\0"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_0:
		.word 11
		.ascii "Change c"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #1
		LDR r4, =39
		STRB r4, [sp, #0]
		B backend.instructions.Label@3834d63f
	L1:
		LDR r4, =39
		LDRB r5, [sp, #0]
		STR r4, [r5, ]
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
	L0:
		LDR r4, =39
		LDRB r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@28f67ac7
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
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

