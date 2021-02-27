r4 in integer
freed
r4 in integer
freed
r4 in integer
freed
r4 in integer
freed
"The first 20 fibonacci numbers are:"
r4 in String
freed
r4 in Ident
freed
", "
r4 in String
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
r5 in Ident
freed
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
"..."
r4 in String
freed
	.data


	msg_4:
		.word 5
		.ascii ", "
	msg_3:
		.word 7
		.ascii "%d\0"
	msg_2:
		.word 5
		.ascii "\0"
	msg_5:
		.word 6
		.ascii "..."
	msg_0:
		.word 38
		.ascii "The first 20 fibonacci numbers are:"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #16
		LDR r4, =0
		STR r4, [sp, #0]
		LDR r4, =0
		STR r4, [sp, #4]
		LDR r4, =1
		STR r4, [sp, #8]
		LDR r4, =0
		STR r4, [sp, #12]
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		B backend.instructions.Label@5a01ccaa
	L2:
		LDR r4, [sp, #4]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #4]
		LDR r5, [sp, #12]
		STR r4, [r5, ]
		LDR r4, [sp, #8]
		LDR r5, [sp, #4]
		STR r4, [r5, ]
		LDR r4, [sp, #8]
		LDR r5, [sp, #12]
		ADD r5, r5, r4
		LDR r5, [sp, #8]
		STR r4, [r5, ]
		LDR r4, =1
		LDR r5, [sp, #0]
		ADD r5, r5, r4
		LDR r5, [sp, #0]
		STR r4, [r5, ]
	L1:
		LDR r4, =20
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		B backend.instructions.Label@5e025e70
		LDR r4, =L4
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #16
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

