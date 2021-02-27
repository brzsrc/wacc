r4 in integer
r5 in Ident
freed
freed
r4 in Ident
freed
"-"
r4 in String
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
""
r4 in String
freed
r4 in integer
r5 in Ident
freed
freed
r4 in funcCall
freed
r4 in integer
freed
r4 in funcCall
freed
	.data


	msg_3:
		.word 5
		.ascii "\0"
	msg_0:
		.word 4
		.ascii "-"
	msg_2:
		.word 3
		.ascii ""
	msg_1:
		.word 9
		.ascii "%.*s\0"
	.text


	.global main
		LDR r4, =0
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@2a18f23c
		SUB sp, sp, #8
		LDR r4, [sp, #8]
		STR r4, [sp, #0]
		B backend.instructions.Label@4ec6a292
	L3:
		LDR r4, =L4
		MOV r0, r4
		BL p_print_string
		LDR r4, =1
		LDR r5, [sp, #0]
		SUB r5, r5, r4
		LDR r5, [sp, #0]
		STR r4, [r5, ]
	L2:
		LDR r4, =0
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVGT r5, #1
		B backend.instructions.Label@7dc36524
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =1
		LDR r5, [sp, #8]
		SUB r5, r5, r4
		STR null, 
		BL f_f
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #4]
		ADD sp, sp, #8
		B backend.instructions.Label@2c8d66b2
	L0:
	L1:
	main:
		PUSH {lr}
		LDR r4, =8
		STR null, 
		BL f_f
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #8]
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

