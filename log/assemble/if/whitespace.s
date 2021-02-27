r4 in integer
freed
r4 in integer
r5 in Ident
freed
freed
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
	.data


	msg_0:
		.word 7
		.ascii "%d\0"
	msg_1:
		.word 5
		.ascii "\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =13
		STR r4, [sp, #0]
		LDR r4, =13
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@1ae369b7
		LDR r4, =0
		LDR r5, [sp, #0]
		STR r4, [r5, ]
		B backend.instructions.Label@6fffcba5
	L0:
		LDR r4, =1
		LDR r5, [sp, #0]
		STR r4, [r5, ]
	L1:
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
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
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

