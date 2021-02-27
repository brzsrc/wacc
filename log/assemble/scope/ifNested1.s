r4 in integer
freed
r4 in integer
r5 in Ident
freed
freed
"incorrect"
r4 in String
freed
r4 in integer
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


	msg_2:
		.word 5
		.ascii "\0"
	msg_4:
		.word 10
		.ascii "correct"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_3:
		.word 12
		.ascii "incorrect"
	msg_0:
		.word 12
		.ascii "incorrect"
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
		B backend.instructions.Label@7907ec20
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		B backend.instructions.Label@23223dd8
	L0:
		LDR r4, =5
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVGT r5, #1
		B backend.instructions.Label@4ec6a292
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		B backend.instructions.Label@1b40d5f0
	L3:
		LDR r4, =L6
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
	L4:
	L1:
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
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

