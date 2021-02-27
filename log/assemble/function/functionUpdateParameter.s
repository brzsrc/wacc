"x is "
r4 in String
freed
r4 in Ident
freed
r4 in integer
r5 in Ident
freed
freed
"x is now "
r4 in String
freed
r4 in Ident
freed
r4 in integer
freed
"y is "
r4 in String
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in funcCall
freed
"y is still "
r4 in String
freed
r4 in Ident
freed
	.data


	msg_3:
		.word 5
		.ascii "\0"
	msg_5:
		.word 8
		.ascii "y is "
	msg_6:
		.word 14
		.ascii "y is still "
	msg_2:
		.word 7
		.ascii "%d\0"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_0:
		.word 8
		.ascii "x is "
	msg_4:
		.word 12
		.ascii "x is now "
	.text


	.global main
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, =5
		LDR r5, [sp, #0]
		STR r4, [r5, ]
		LDR r4, =L1
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
	main:
		PUSH {lr}
		SUB sp, sp, #8
		LDR r4, =1
		STR r4, [sp, #0]
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, [sp, #0]
		STR null, 
		BL f_f
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #4]
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		ADD sp, sp, #8
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

