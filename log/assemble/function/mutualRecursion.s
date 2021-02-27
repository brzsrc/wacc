"r2: received "
r4 in String
freed
r4 in Ident
freed
r4 in integer
r5 in Ident
freed
freed
r4 in funcCall
freed
r4 in integer
r5 in Ident
freed
freed
"r1: sending "
r4 in String
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in funcCall
freed
r4 in integer
freed
r4 in integer
freed
r4 in funcCall
r5 in Ident
freed
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
	msg_4:
		.word 15
		.ascii "r1: sending "
	msg_0:
		.word 16
		.ascii "r2: received "
	.text


	.global main
		SUB sp, sp, #4
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, =1
		LDR r5, [sp, #0]
		SUB r5, r5, r4
		STR null, 
		BL f_r1
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #4]
		ADD sp, sp, #4
		LDR r4, =0
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@5e025e70
		SUB sp, sp, #4
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #4]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, [sp, #4]
		STR null, 
		BL f_r2
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #0]
		ADD sp, sp, #4
		B backend.instructions.Label@1fbc7afb
	L1:
	L2:
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =0
		STR r4, [sp, #8]
		LDR r4, =8
		STR null, 
		BL f_r1
		ADD sp, sp, #4
		MOV r0, r4
		LDR r5, [sp, #8]
		STR r4, [r5, ]
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

