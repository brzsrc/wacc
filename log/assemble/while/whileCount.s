r4 in integer
freed
"Can you count to 10?"
r4 in String
freed
r4 in Ident
freed
r4 in integer
r5 in Ident
freed
r5 in Ident
freed
freed
r4 in integer
r5 in Ident
freed
freed
	.data


	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_2:
		.word 5
		.ascii "\0"
	msg_0:
		.word 23
		.ascii "Can you count to 10?"
	msg_3:
		.word 7
		.ascii "%d\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =1
		STR r4, [sp, #0]
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		B backend.instructions.Label@3c5a99da
	L2:
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, =1
		LDR r5, [sp, #0]
		ADD r5, r5, r4
		LDR r5, [sp, #0]
		STR r4, [r5, ]
	L1:
		LDR r4, =10
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVLE r5, #1
		B backend.instructions.Label@4563e9ab
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
	p_print_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}

