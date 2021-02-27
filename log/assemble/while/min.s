r4 in integer
freed
r4 in integer
freed
r4 in integer
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
r5 in integer
r6 in Ident
freed
freed
freed
"min value = "
r4 in String
freed
r4 in Ident
freed
	.data


	msg_3:
		.word 5
		.ascii "\0"
	msg_2:
		.word 7
		.ascii "%d\0"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_0:
		.word 15
		.ascii "min value = "
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #12
		LDR r4, =0
		STR r4, [sp, #0]
		LDR r4, =10
		STR r4, [sp, #4]
		LDR r4, =17
		STR r4, [sp, #8]
		B backend.instructions.Label@6833ce2c
	L1:
		LDR r4, =1
		LDR r5, [sp, #4]
		SUB r5, r5, r4
		LDR r5, [sp, #4]
		STR r4, [r5, ]
		LDR r4, =1
		LDR r5, [sp, #8]
		SUB r5, r5, r4
		LDR r5, [sp, #8]
		STR r4, [r5, ]
		LDR r4, =1
		LDR r5, [sp, #0]
		ADD r5, r5, r4
		LDR r5, [sp, #0]
		STR r4, [r5, ]
	L0:
		LDR r4, =0
		LDR r5, [sp, #4]
		CMP r5, r5
		MOV r5, #0
		MOVGT r5, #1
		LDR r5, =0
		LDR r6, [sp, #8]
		CMP r6, r6
		MOV r6, #0
		MOVGT r6, #1
		AND r5, r5, r4
		B backend.instructions.Label@357246de
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		ADD sp, sp, #12
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
	p_print_int:
		PUSH {lr}
		MOV r1, r0
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

